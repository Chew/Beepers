package com.mcprohosting.beepers.listeners;

import com.mcprohosting.beepers.objects.MCProChannel;
import com.mcprohosting.beepers.objects.TicketCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import static com.mcprohosting.beepers.util.QueryMember.hasPower;
import static com.mcprohosting.beepers.util.QueryMember.isStaff;
import static com.mcprohosting.beepers.util.SwearHandler.handleMessage;

public class MessageEvent extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getMember() != null && event.getMember().getUser().isBot())
            return;
        // Iterate through each ticket category, if it's not in this category we don't care.
        for (TicketCategory ticketCategory : TicketCategory.values()) {
            if (event.getChannel().getParent() != null && ticketCategory.getId().equals(event.getChannel().getParent().getId())) {
                // Dictate outcome based on type of category this ticket is in
                switch (ticketCategory) {
                    // If it's "Open"
                    case OPEN -> {
                        if (event.getMember() != null && isStaff(event.getMember())) {
                            // Staff Response
                            TicketCategory.ANSWERED.moveHere(event.getGuild(), event.getChannel());
                        } else {
                            // Customer Response
                            TicketCategory.CUSTOMER_REPLY.moveHere(event.getGuild(), event.getChannel());
                        }
                    }
                    // If a customer responded, it does not matter if they respond again since it's already in this category
                    case CUSTOMER_REPLY -> {
                        // Staff response means move it to "Answered"
                        if (event.getMember() != null && isStaff(event.getMember())) {
                            TicketCategory.ANSWERED.moveHere(event.getGuild(), event.getChannel());
                        }
                    }
                    // Opposite of above, if a staff responded, don't move it
                    case ANSWERED -> {
                        // If a customer respond, mark it as Customer Reply
                        if (event.getMember() != null && !isStaff(event.getMember())) {
                            TicketCategory.CUSTOMER_REPLY.moveHere(event.getGuild(), event.getChannel());
                        }
                    }
                }
            }
        }
        handleEvent(event.getMember(), event.getMessage(), event.getAuthor());
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        handleEvent(event.getMember(), event.getMessage(), event.getAuthor());
    }

    private void handleEvent(Member member, Message message, User author) {
        if(member != null && hasPower(member)) {
            return;
        }
        TextChannel channel = MCProChannel.AUTO_MOD_LOG.getAsChannel();
        String word = handleMessage(message.getContentRaw());
        if (word == null) {
            return;
        }
        LoggerFactory.getLogger(this.getClass()).debug("Message '" + message.getContentRaw() + "' swear found: " + word);
        message.delete().queue();
        author.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Hey! You said a bad word, please refrain from swearing! Your message: ```" + message.getContentRaw() + "```").queue());
        EmbedBuilder oof = new EmbedBuilder();
        oof.setTitle("Message deleted");
        oof.setDescription("Offender: " + author.getAsTag() + " " + author.getAsMention() + "\n" +
                "Reason: Automatic action carried out for using a blacklisted word (" + word + ")." + "\n" +
                "Message: " + message.getContentRaw() + "\n" +
                "Responsible moderator: me lol");
        channel.sendMessage(oof.build()).queue();
    }
}

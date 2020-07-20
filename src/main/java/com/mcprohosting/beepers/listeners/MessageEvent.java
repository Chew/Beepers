package com.mcprohosting.beepers.listeners;

import com.mcprohosting.beepers.Main;
import com.mcprohosting.beepers.util.QueryMember;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import static com.mcprohosting.beepers.util.SwearHandler.handleMessage;

public class MessageEvent extends ListenerAdapter {
    public static final String[] swears = Main.getProp().getProperty("swears").split(",");

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        handleEvent(event.getMember(), event.getMessage(), event.getAuthor(), event.getGuild());
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        handleEvent(event.getMember(), event.getMessage(), event.getAuthor(), event.getGuild());
    }

    private void handleEvent(Member member, Message message, User author, Guild guild) {
        if(member != null && QueryMember.isStaff(member)) {
            return;
        }
        String word = handleMessage(message.getContentRaw());
        if (word != null) {
            LoggerFactory.getLogger(this.getClass()).debug("Message '" + message.getContentRaw() + "' swear found: " + word);
            message.delete().queue();
            author.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Hey! You said a bad word, please refrain from swearing! Your message: ```" + message.getContentRaw() + "```").queue());
            EmbedBuilder oof = new EmbedBuilder();
            oof.setTitle("Message deleted");
            oof.setDescription("Offender: " + author.getAsTag() + " " + author.getAsMention() + "\n" +
                    "Reason: Automatic action carried out for using a blacklisted word (" + word + ")." + "\n" +
                    "Message: " + message.getContentRaw() + "\n" +
                    "Responsible moderator: me lol");
            ((TextChannel) guild.getGuildChannelById("716712150722412664")).sendMessage(oof.build()).queue();
        }
    }
}

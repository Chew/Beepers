package com.mcprohosting.beepers.listeners;

import com.mcprohosting.beepers.util.QueryMember;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

public class MessageEvent extends ListenerAdapter {
    /*
    The actual swear is left private (obviously). You get this instead. It's commented out so my IDE doesn't yell at me.
    public static final String[] swears = new String[];
    */

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getMember() != null && QueryMember.isStaff(event.getMember())) {
            return;
        }
        String message = event.getMessage().getContentRaw().toLowerCase().replace(" ", "").replaceAll("[^a-zA-Z0-9]", "");
        String word = getSwear(message);
        if (word != null) {
            LoggerFactory.getLogger(this.getClass()).debug("Message '" + message + "' swear found: " + word);
            event.getMessage().delete().queue();
            event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Hey! You said a bad word, please refrain from swearing! Your message: ```" + event.getMessage().getContentRaw() + "```").queue());
            EmbedBuilder oof = new EmbedBuilder();
            oof.setTitle("Message deleted");
            oof.setDescription("Offender: " + event.getMember().getUser().getAsTag() + " " + event.getMember().getAsMention() + "\n" +
                    "Reason: Automatic action carried out for using a blacklisted word (" + word + ")." + "\n" +
                    "Message: " + event.getMessage().getContentRaw() + "\n" +
                    "Responsible moderator: me lol");
            ((TextChannel)event.getGuild().getGuildChannelById("716712150722412664")).sendMessage(oof.build()).queue();
        }
    }

    public String getSwear(String message) {
        for (String swear : swears) {
            if (message.contains(swear)) {
                return swear;
            }
        }
        return null;
    }
}

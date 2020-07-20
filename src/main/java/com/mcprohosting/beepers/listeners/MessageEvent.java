package com.mcprohosting.beepers.listeners;

import com.mcprohosting.beepers.Main;
import com.mcprohosting.beepers.util.QueryMember;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageEvent extends ListenerAdapter {
    public static final String[] swears = Main.getProp().getProperty("swears").split(",");

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getMember() != null && QueryMember.isStaff(event.getMember())) {
            return;
        }
        List<CharSequence> words = Arrays.asList(event.getMessage().getContentRaw().toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "").split(" "));
        List<String> safeWords = new ArrayList<>();
        for(CharSequence word : words) {
            if(!checkForWord(word)) {
                safeWords.add((String) word);
            }
        }
        String word = getSwear(String.join("", safeWords));
        if (word != null) {
            LoggerFactory.getLogger(this.getClass()).debug("Message '" + event.getMessage().getContentRaw() + "' swear found: " + word);
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

    public static boolean checkForWord(CharSequence word) {
        // System.out.println(word);
        for (String swear : swears) {
            if (word.toString().contains(swear)) {
                return false;
            }
        }
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    "words_alpha.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.contains(word)) {
                    return true;
                }
            }
            in.close();
        } catch (IOException ignored) {
        }

        return false;
    }
}

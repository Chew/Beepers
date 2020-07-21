package com.mcprohosting.beepers.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FAQCommand extends Command {

    public FAQCommand() {
        this.name = "faq";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        TextChannel faq = commandEvent.getGuild().getTextChannelById("715393203095994380");
        if(faq == null) {
            LoggerFactory.getLogger(this.getClass()).error("FAQ Channel is null. This is not good.");
            return;
        }
        faq.getHistoryAfter("715404677277286410", 50).queue((messages -> {
            String args = commandEvent.getArgs().toLowerCase();
            List<Message> potential = new ArrayList<>();
            for(Message message : messages.getRetrievedHistory()) {
                String content = message.getContentRaw().toLowerCase();
                if(content.contains(args)) {
                    potential.add(message);
                }
            }
            if(potential.isEmpty()) {
                commandEvent.reply("No FAQ found for the input. Try being less specific.");
                return;
            }
            EmbedBuilder embed = new EmbedBuilder();
            Message question = potential.get(0);
            String content = question.getContentRaw();
            embed.setTitle("FAQ Found for Input");
            for(String word : content.replace("\n", " ").split(" ")) {
                if(word.contains("cdn.discordapp.com")) {
                    embed.setImage(word);
                    content = content.replace(word, "");
                }
            }
            content = content.trim();
            embed.setDescription(content);
            embed.setAuthor(question.getAuthor().getAsTag(), null, question.getAuthor().getAvatarUrl());
            commandEvent.reply(embed.build());
        }));
    }
}

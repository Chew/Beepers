package com.mcprohosting.beepers.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
        faq.getHistory().retrievePast(25).queue((messages -> {
            String args = commandEvent.getArgs().toLowerCase();
            List<Message> potential = new ArrayList<>();
            for(Message message : messages) {
                String content = message.getContentRaw().toLowerCase();
                if(content.contains(args)) {
                    potential.add(message);
                }
            }
            if(potential.size() == 0) {
                commandEvent.reply("No FAQ found for the input. Try being less specific.");
                return;
            }
            if(potential.size() > 1) {
                commandEvent.reply("Multiple potential FAQs were found, try being a bit more specific. Showing the first result.");
            }
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("FAQ Found for Input");
            embed.setDescription(potential.get(0).getContentRaw());
            embed.setAuthor(potential.get(0).getAuthor().getAsTag(), null, potential.get(0).getAuthor().getAvatarUrl());
            embed.addField("Jump to message", potential.get(0).getJumpUrl(), true);
            commandEvent.reply(embed.build());
        }));
    }
}

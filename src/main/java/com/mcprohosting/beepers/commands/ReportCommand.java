package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.LoggerFactory;

public class ReportCommand extends Command {

    public ReportCommand() {
        this.name = "report";
        this.guildOnly = false;
        this.cooldown = 300;
        this.cooldownScope = CooldownScope.USER;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if(commandEvent.getChannelType() != ChannelType.PRIVATE)
            commandEvent.getMessage().delete().queue();

        TextChannel channel = commandEvent.getJDA().getTextChannelById("715322163850117172");
        if(channel == null) {
            LoggerFactory.getLogger(this.getClass()).error("Mod channel is missing, this is not good.");
            return;
        }

        channel.sendMessage(new EmbedBuilder()
                .setTitle("New Report from " + commandEvent.getAuthor().getAsTag())
                .setDescription(commandEvent.getArgs())
                .build()
        ).queue();
        commandEvent.reply("Report sent!");
    }
}

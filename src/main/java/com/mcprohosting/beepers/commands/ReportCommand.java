package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;

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

        commandEvent.getJDA().getGuildById("584169742156169236").getTextChannelById("715322163850117172").sendMessage(new EmbedBuilder()
                .setTitle("New Report from " + commandEvent.getAuthor().getAsTag())
                .setDescription(commandEvent.getArgs())
                .build()
        ).queue();
        commandEvent.reply("Report sent!");
    }
}

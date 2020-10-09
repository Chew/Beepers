package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

public class MultistickCommand extends Command {
    public MultistickCommand() {
        this.name = "multistick";
        this.cooldown = 10;
        this.cooldownScope = CooldownScope.CHANNEL;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        EmbedBuilder bob = new EmbedBuilder();
        bob.setDescription("Sadly the multistick command had to be retired on October 1st, 2020. It will not be forgotten... :pensive:");
        commandEvent.reply(bob.build());
    }
}

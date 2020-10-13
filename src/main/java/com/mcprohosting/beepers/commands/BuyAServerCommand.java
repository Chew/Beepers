package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class BuyAServerCommand extends Command {

    public BuyAServerCommand() {
        this.name = "buyaserver";
        this.aliases = new String[]{"order", "buy"};
        this.cooldownScope = CooldownScope.CHANNEL;
        this.cooldown = 30;
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Hey, " + event.getAuthor().getAsMention() + "! Beepers here with that handy link to our order page for you to check out!\n" +
            "<https://mcprohosting.com/order>");
    }
}

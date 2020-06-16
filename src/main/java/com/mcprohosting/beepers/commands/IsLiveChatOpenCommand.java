package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.time.Instant;
import java.time.LocalDateTime;

public class IsLiveChatOpenCommand extends Command {
    public IsLiveChatOpenCommand() {
        this.name = "islivechatopen";
        this.cooldown = 30;
        this.cooldownScope = CooldownScope.CHANNEL;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if(LocalDateTime.now().getHour() >= 8) {
            commandEvent.reply("It should be. It might not be if it's a holiday or for other reasons no one told me about. Try it and see!");
        } else {
            commandEvent.reply("nah mate, try again later. Livechat will be open soon i promise. 9 AM EST");
        }
    }
}

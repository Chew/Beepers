package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class LiveChatCommand extends Command {
    public LiveChatCommand() {
        this.name = "livechat";
        this.aliases = new String[]{"islivechatopen"};
        this.cooldown = 1;
        this.cooldownScope = CooldownScope.CHANNEL;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getMessage().delete().queue();
        String mention = commandEvent.getAuthor().getAsMention();
        if(commandEvent.getArgs().length() > 0 && commandEvent.getArgs().startsWith("<@!")) {
            mention = commandEvent.getArgs().split(" ")[0];
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCProHosting Live Chat", "https://mcph.info/Livechat");
        if(LocalDateTime.now().getHour() >= 8) {
            embed.setDescription("If you have a problem you need solved quickly, check out our Live Chat at [this link](https://mcph.info/Livechat).\n" +
                    "Live Chat is available every day from 9:00 AM - 1:00 AM EST with the exception of Holidays. If you do not see the Live Chat bubble check our social media for an announcement or check back in a few minutes as our team may be undergoing a shift change!\n" +
                    "It is currently within the hours of 9 AM and 1 AM, hop on! <:beepers:715601889391149125>");
        } else {
            embed.setDescription("If you have a problem you need solved quickly, check out our Live Chat at [this link](https://mcph.info/Livechat).\n" +
                    "Live Chat is available every day from 9:00 AM - 1:00 AM EST with the exception of Holidays.\n" +
                    "Live Chat is currently closed, see below for when it opens. <:beepers:715601889391149125>");
            LocalDateTime now = LocalDateTime.now();
            ZonedDateTime opens = LocalDateTime.of(now.toLocalDate(), LocalTime.of(8, 0)).atZone(TimeZone.getTimeZone("America/Chicago").toZoneId());
            embed.setFooter("Live Chat opens at");
            embed.setTimestamp(opens);
        }
        commandEvent.getChannel().sendMessage("Hey, " + mention + "!").embed(embed.build()).queue();
    }
}

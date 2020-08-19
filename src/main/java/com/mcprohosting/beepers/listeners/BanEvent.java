package com.mcprohosting.beepers.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.stream.Collectors;

public class BanEvent extends ListenerAdapter {
    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        LoggerFactory.getLogger(this.getClass()).info("Ban Event raised.");

        TextChannel channel = event.getJDA().getTextChannelById("715409019308736563");
        if(channel == null) {
            LoggerFactory.getLogger(this.getClass()).error("Could not find channel, this is not good.");
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Ban Detected");
        embed.addField("User", event.getUser().getAsTag() + "\n" + event.getUser().getAsMention(), true);

        AuditLogEntry entry = getRecentBan(event.getGuild(), event.getUser());
        String reason = entry.getReason();
        if(reason == null) {
            reason = "*No reason provided*";
        }

        if(entry.getUser() == null)
            embed.addField("Banned by", "Unknown", true);
        else
            embed.addField("Banned by", entry.getUser().getAsTag() + "\n" + entry.getUser().getAsMention(), true);

        embed.addField("Reason", reason + "\n\nAdditional details may be posted below.", false);

        embed.setFooter("Banned at");
        embed.setTimestamp(Instant.now());

        channel.sendMessage(embed.build()).queue();
    }

    public AuditLogEntry getRecentBan(Guild server, User user) {
        return server.retrieveAuditLogs().cache(false).stream()
                .filter(it -> it.getType() == ActionType.BAN)
                .filter(it -> it.getTargetId().equals(user.getId()))
                .collect(Collectors.toList()).get(0); // collects actions done by user
    }
}

package com.mcprohosting.beepers.listeners;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public class BanEvent extends ListenerAdapter {
    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        LoggerFactory.getLogger(this.getClass()).info("Ban Event raised.");

        TextChannel channel = event.getJDA().getTextChannelById("715409019308736563");
        if(channel == null) {
            LoggerFactory.getLogger(this.getClass()).error("Could not find channel, this is not good.");
            return;
        }

        channel.sendMessage(event.getUser().getAsTag() + "\n" + event.getUser().getAsMention() + " was banned!").queue();

        /*
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Ban Detected");

        event.getGuild().retrieveAuditLogs()
                .type(ActionType.BAN)
                .limit(1)
                .queue(list -> {
                    if (list.isEmpty()) return;
                    AuditLogEntry entry = list.get(0);

                    embed.addField("User", event.getUser().getAsTag() + "\n" + event.getUser().getAsMention(), true);

                    if(entry.getUser() == null)
                        embed.addField("Banned by", "Unknown", true);
                    else
                        embed.addField("Banned by", entry.getUser().getAsTag() + "\n" + entry.getUser().getAsMention(), true);

                    String reason = entry.getReason();
                    if(reason == null) {
                        reason = "*No reason provided*";
                    }
                    embed.addField("Reason", reason + "\n\nAdditional details may be posted below.", false);


                    embed.setFooter("Banned at");
                    embed.setTimestamp(Instant.now());

                    channel.sendMessage(embed.build()).queue();
                });

         */
    }
}

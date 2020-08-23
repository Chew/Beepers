package com.mcprohosting.beepers.listeners;

import com.mcprohosting.beepers.commands.staff.SyncSuggestionSiteCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class SuggestHandler extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    Instant lastSync = Instant.now();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getChannel() != event.getGuild().getTextChannelById("715650049744306318"))
            return;

        logger.debug("New Decision posted, syncing...");

        SyncSuggestionSiteCommand.gatherSuggestionData(event.getJDA(), event.getGuild());
    }

    @Override
    public void onGenericGuildMessageReaction(GenericGuildMessageReactionEvent event) {
        if(event.getChannel() != event.getGuild().getTextChannelById("715650008942116985"))
            return;

        if(lastSync.isBefore(Instant.now().minusSeconds(10))) {
            logger.debug("MessageReaction accepted, syncing...");
            SyncSuggestionSiteCommand.gatherSuggestionData(event.getJDA(), event.getGuild());
            lastSync = Instant.now();
        } else {
            logger.debug("MessageReaction ignored due to rate-limit");
        }
    }
}

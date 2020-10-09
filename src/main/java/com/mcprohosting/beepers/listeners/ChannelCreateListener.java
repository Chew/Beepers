package com.mcprohosting.beepers.listeners;

import com.mcprohosting.beepers.objects.TicketCategory;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChannelCreateListener extends ListenerAdapter {
    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        // If it's a new ticket
        if (event.getChannel().getParent() != null && event.getChannel().getParent().getId().equals("715434370705653850")) {
            TicketCategory.OPEN.moveHere(event.getGuild(), event.getChannel());
        }
    }
}

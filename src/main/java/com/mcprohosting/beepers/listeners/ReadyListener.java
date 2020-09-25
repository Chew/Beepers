package com.mcprohosting.beepers.listeners;

import com.mcprohosting.beepers.objects.MCProChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        MCProChannel.setMCProServer(event.getJDA().getGuildById("584169742156169236"));
    }
}

package com.mcprohosting.beepers.objects;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public enum MCProChannel {
    FAQ("715393203095994380"),
    RULES("715406878976507954"),
    SUGGESTIONS("715650008942116985"),
    DECISIONS("715650049744306318"),
    AUTO_MOD_LOG("716712150722412664"),
    MOD_LOG("715409019308736563");

    private final String channelId;
    public static Guild mcproServer;

    MCProChannel(String channelId) {
        this.channelId = channelId;
    }

    public static void setMCProServer(Guild server) {
        mcproServer = server;
    }

    @NotNull
    public TextChannel getAsChannel() {
        TextChannel textChannel = mcproServer.getTextChannelById(channelId);
        if (textChannel != null) {
            return textChannel;
        } else {
            LoggerFactory.getLogger(MCProChannel.class).error("Not good, Channel NOT found!");
            throw new RuntimeException("Channel no longer exists, not good. NOT GOOD!");
        }
    }
}

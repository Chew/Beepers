package com.mcprohosting.beepers.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.atomic.AtomicReference;

public class SendTemporaryMessage {
    public static void send(CommandEvent event, String message) {
        AtomicReference<Message> sent = new AtomicReference<>();
        event.getChannel().sendMessage(message).queue(sent::set);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        sent.get().delete().queue();
                    }
                },
                5000
        );
    }
}

package com.mcprohosting.beepers.objects;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public enum TicketCategory {
    OPEN("764192818130190407"),
    CUSTOMER_REPLY("764192862874632202"),
    ANSWERED("764192881560125441"),
    ON_HOLD("764192895548129280");

    public final String id;

    TicketCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Move a specified channel to this category
     */
    public void moveHere(Guild server, TextChannel channel) {
        channel.getManager().setParent(server.getCategoryById(id)).complete();
    }
}

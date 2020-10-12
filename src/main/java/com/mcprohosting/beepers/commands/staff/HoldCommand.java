package com.mcprohosting.beepers.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcprohosting.beepers.objects.TicketCategory;

import static com.mcprohosting.beepers.util.QueryMember.isStaff;

public class HoldCommand extends Command {

    public HoldCommand() {
        this.name = "onhold";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getMessage().delete().queue();
        for (TicketCategory ticketCategory : TicketCategory.values()) {
            if (event.getTextChannel().getParent() != null && ticketCategory.getId().equals(event.getTextChannel().getParent().getId())) {
                if (isStaff(event.getMember())) {
                    if (TicketCategory.ON_HOLD.getId().equals(event.getTextChannel().getParent().getId())) {
                        TicketCategory.ANSWERED.moveHere(event.getGuild(), event.getTextChannel());
                    } else {
                        TicketCategory.ON_HOLD.moveHere(event.getGuild(), event.getTextChannel());
                    }
                }
            }
        }
    }
}

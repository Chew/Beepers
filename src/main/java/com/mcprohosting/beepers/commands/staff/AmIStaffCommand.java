package com.mcprohosting.beepers.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.mcprohosting.beepers.util.QueryMember.hasPower;
import static com.mcprohosting.beepers.util.QueryMember.isStaff;

public class AmIStaffCommand extends Command {
    public AmIStaffCommand() {
        this.name = "amistaff";
        this.guildOnly = true;
        this.cooldown = 300;
        this.cooldownScope = CooldownScope.USER;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply(
            "Staff? " + isStaff(commandEvent.getMember()) + "\n" +
            "Has Power? " + hasPower(commandEvent.getMember())
        );
    }

}

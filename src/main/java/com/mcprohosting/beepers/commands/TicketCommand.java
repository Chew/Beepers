package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

public class TicketCommand extends Command {

    public TicketCommand() {
        this.name = "ticket";
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.cooldown = 60;
        this.cooldownScope = CooldownScope.CHANNEL;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(new EmbedBuilder()
            .setTitle("Make a Ticket")
            .setDescription("""
                Have a problem and need our support team to help? Get a ticket submitted to us!
                Use the links below to select what you need most!
                Ticket support is available 24/7! <:beepers:758342395929821214>
                
                Only see two departments? Make sure you **Sign In**!
                
                [Submit Ticket Page](https://clients.mcprohosting.com/submitticket.php)
                
                Specific Departments are listed below:
                [General Support](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=5) - For general/misc questions
                [Billing](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=4) - For questions regarding billing
                [Pre-Sales/Sales](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=8) - Any questions about our service before you buy
                [Plugins](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=6) - For help with plugins
                [Modpacks](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=12) - For help with modpacks
                [Password/Login Help](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=16) - For help with passwords or logging in
                [Plugin Setup](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=17) - Bought a Management addon? Submit your perk here!
                [BungeeCord](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=26) - For help with BungeeCord (or its forks)
                [Create-A-Modpack](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=29) - Make your own modpack!
                [Dedicated Servers](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=32) - If you have a problem with your dedicated server, go here
                [Restorations](https://clients.mcprohosting.com/submitticket.php?step=2&deptid=38) - Go here for backups, or to restore/get your old files
                """)
            .build());
    }
}

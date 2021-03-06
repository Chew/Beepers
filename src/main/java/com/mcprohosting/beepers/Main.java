package com.mcprohosting.beepers;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.mcprohosting.beepers.commands.LiveChatCommand;
import com.mcprohosting.beepers.commands.MultistickCommand;
import com.mcprohosting.beepers.commands.NodeStatusCommand;
import com.mcprohosting.beepers.commands.ReportCommand;
import com.mcprohosting.beepers.commands.TicketCommand;
import com.mcprohosting.beepers.commands.staff.AmIStaffCommand;
import com.mcprohosting.beepers.commands.staff.FAQCommand;
import com.mcprohosting.beepers.commands.staff.HoldCommand;
import com.mcprohosting.beepers.commands.staff.RuleCommand;
import com.mcprohosting.beepers.commands.staff.SyncSuggestionSiteCommand;
import com.mcprohosting.beepers.listeners.BanEvent;
import com.mcprohosting.beepers.listeners.ChannelCreateListener;
import com.mcprohosting.beepers.listeners.MessageEvent;
import com.mcprohosting.beepers.listeners.ReadyListener;
import com.mcprohosting.beepers.listeners.SuggestHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    static final Properties prop = new Properties();
    public static JDA jda;

    public static void main(String[] args) throws LoginException, IOException {
        prop.load(new FileInputStream("bot.properties"));

        EventWaiter waiter = new EventWaiter();

        CommandClientBuilder client = new CommandClientBuilder();

        client.setOwnerId(prop.getProperty("owner_id"));

        logger.info("Setting Prefix to " + prop.getProperty("prefix"));
        client.setPrefix(prop.getProperty("prefix"));

        client.useHelpBuilder(false);

        client.setActivity(Activity.watching("over the MCProHosting Community!"));

        // Register commands
        client.addCommands(
                // Staff Commands
                new AmIStaffCommand(),
                new FAQCommand(),
                new HoldCommand(),
                new RuleCommand(),
                new SyncSuggestionSiteCommand(),

                // GP Commands
                new LiveChatCommand(),
                new MultistickCommand(),
                new NodeStatusCommand(),
                new ReportCommand(),
                new TicketCommand()
        );

        // Register JDA
        jda = JDABuilder.createDefault(prop.getProperty("token"))
                .setStatus(OnlineStatus.ONLINE)
                .enableIntents(GatewayIntent.GUILD_BANS)
                .setActivity(Activity.playing("Booting..."))
                .addEventListeners(
                        waiter,
                        client.build(),
                        new MessageEvent(),
                        new ChannelCreateListener(),
                        new BanEvent(),
                        new SuggestHandler(),
                        new ReadyListener()
                )
                .build();
    }

    public static Properties getProp() {
        return prop;
    }
}

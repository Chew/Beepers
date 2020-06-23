package com.mcprohosting.beepers;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.mcprohosting.beepers.commands.*;
import com.mcprohosting.beepers.commands.staff.AmIStaffCommand;
import com.mcprohosting.beepers.commands.staff.FAQCommand;
import com.mcprohosting.beepers.commands.staff.RuleCommand;
import com.mcprohosting.beepers.commands.staff.SyncSuggestionSiteCommand;
import com.mcprohosting.beepers.listeners.MessageEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    static Properties prop = new Properties();
    public static JDA jda;

    public static void main(String[] args) throws LoginException, IOException {
        prop.load(new FileInputStream("bot.properties"));

        EventWaiter waiter = new EventWaiter();

        CommandClientBuilder client = new CommandClientBuilder();

        client.useDefaultGame();
        client.setOwnerId(prop.getProperty("owner_id"));

        // Set your bot's prefix
        logger.info("Setting Prefix to " + prop.getProperty("prefix"));
        client.setPrefix(prop.getProperty("prefix"));

        client.useHelpBuilder(false);

        client.setActivity(Activity.watching("over the MCProHosting Community!"));

        // Register commands
        client.addCommands(
                // Staff Commands
                new AmIStaffCommand(),
                new FAQCommand(),
                new RuleCommand(),
                new SyncSuggestionSiteCommand(),

                // GP Commands
                new LiveChatCommand(),
                new MultistickCommand(),
                new NodeStatusCommand(),
                new ReportCommand()
        );

        // Register JDA
        jda = JDABuilder.createDefault(prop.getProperty("token"))
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("Booting..."))
                .addEventListeners(waiter, client.build())
                .build();

        jda.addEventListener(new MessageEvent());
    }

    public JDA getJDA() {
        return jda;
    }

    public static Properties getProp() {
        return prop;
    }
}

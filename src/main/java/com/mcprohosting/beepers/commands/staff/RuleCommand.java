package com.mcprohosting.beepers.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class RuleCommand extends Command {

    public RuleCommand() {
        this.name = "rule";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendTyping().queue();
        int ruleId;
        String args = commandEvent.getArgs();
        if(args.length() == 0) {
            commandEvent.reply("Please specify a rule number!");
            return;
        }
        try {
            ruleId = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            commandEvent.reply("Invalid rule number!");
            return;
        }
        TextChannel welcomeChannel;
        welcomeChannel = commandEvent.getGuild().getTextChannelById("715406878976507954");
        if(welcomeChannel == null) {
            commandEvent.reply("Welcome channel could not be found, this is not good.");
            return;
        }
        welcomeChannel.retrieveMessageById("715603752639397931").queue((msg) -> {
            String[] rules = msg.getContentRaw().split("\n");
            List<String> actualRules = new ArrayList<>();
            for(String rule : rules) {
                try {
                    Integer.parseInt(rule.split("\\.")[0]);
                    actualRules.add(rule);
                } catch (NumberFormatException e) {
                    String old = actualRules.get(actualRules.size() - 1);
                    actualRules.set(actualRules.size() - 1, old + "\n" + rule);
                }
            }
            try {
                String rule = actualRules.get(ruleId - 1);
                commandEvent.reply(new EmbedBuilder()
                        .setTitle("Rule #" + ruleId)
                        .setDescription(rule)
                        .build()
                );
            } catch (IndexOutOfBoundsException e) {
                commandEvent.reply("This rule does not exist!");
            }

        }, (error) -> commandEvent.reply("Rules message could not be found, this is not good."));
    }
}

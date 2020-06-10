package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class MultistickCommand extends Command {
    public MultistickCommand() {
        this.name = "multistick";
        this.cooldown = 10;
        this.cooldownScope = CooldownScope.CHANNEL;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        EmbedBuilder bob = new EmbedBuilder();
        bob.setDescription(
                "<:multistick_0_0:715985103138062396><:multistick_1_0:715985102743666783><:multistick_2_0:715985103116959754><:multistick_3_0:715985103183937656><:multistick_4_0:715985103313961210>\n" +
                "<:multistick_0_1:715985102685077519><:multistick_1_1:715985102722826341><:multistick_2_1:715985103234269225><:multistick_3_1:715985103037137027><:multistick_4_1:715985103125479454>\n" +
                "<:multistick_0_2:715985102798192672><:multistick_1_2:715985102731214859><:multistick_2_2:715985103137800343><:multistick_3_2:715985102785478848><:multistick_4_2:715985103024554015>\n" +
                "<:multistick_0_3:715985102668300290><:multistick_1_3:715985103020621874><:multistick_2_3:715985103049982032><:multistick_3_3:715985102877753425><:multistick_4_3:715985103058370640>\n" +
                "<:multistick_0_4:715985103083405404><:multistick_1_4:715985103011971102><:multistick_2_4:715985102617706588><:multistick_3_4:715985102953381959><:multistick_4_4:715985103058370630>"
        );
        commandEvent.reply(bob.build());
    }
}

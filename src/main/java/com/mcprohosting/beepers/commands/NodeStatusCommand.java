package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcprohosting.MCProHostingAPI;
import com.mcprohosting.beepers.util.QueryMember;
import com.mcprohosting.beepers.util.SendTemporaryMessage;
import com.mcprohosting.objects.Node;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeStatusCommand extends Command {

    public NodeStatusCommand() {
        this.name = "nodestatus";
        this.aliases = new String[]{"node"};
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendTyping().queue();

        boolean replyInDm = true;
        if(commandEvent.getChannel().getId().equals("715322158041268254")) {
            replyInDm = false;
        }
        if(commandEvent.getChannelType() == ChannelType.PRIVATE) {
            replyInDm = false;
        }
        String args = commandEvent.getArgs();
        args = args.replace(" ", "");
        if(args.contains("--no-dm")) {
            args = args.replace("--no-dm", "");
            if(commandEvent.getMember() != null && QueryMember.isStaff(commandEvent.getMember()))
                replyInDm = false;
        }
        if(replyInDm) {
            commandEvent.getMessage().delete().queue();
            String finalArgs = args;
            commandEvent.getAuthor().openPrivateChannel().queue(privateChannel -> {
                if(finalArgs.length() == 0) {
                    privateChannel.sendMessage(allNodes().build()).queue(null, (exception) -> commandEvent.getChannel().sendMessage(commandEvent.getMember().getAsMention() + " Uh oh!! I can't message you privately. Please enable your DMs so I can message you!").queue());
                } else {
                    privateChannel.sendMessage(specificNode(finalArgs).build()).queue(null, (exception) -> commandEvent.getChannel().sendMessage(commandEvent.getMember().getAsMention() + " Uh oh!! I can't message you privately. Please enable your DMs so I can message you!").queue());
                }
                SendTemporaryMessage.send(commandEvent, "DM Sent!");
            });
        } else {
            if(args.length() == 0) {
                commandEvent.reply(allNodes().build());
            } else {
                commandEvent.reply(specificNode(args).build());
            }
        }

    }

    public EmbedBuilder allNodes() {
        List<Node> nodes;
        EmbedBuilder embed = new EmbedBuilder();
        try {
            nodes = new MCProHostingAPI().getNodeStatuses();
        } catch (Exception e) {
            e.printStackTrace();
            embed.setTitle("Error!");
            embed.setDescription("An error occurred retrieving stats. Please try again in a few seconds!");
            embed.setColor(Color.decode("#ff0000"));
            return embed;
        }
        embed.setTitle("MCProHosting Node Statuses", "https://panel.mcprohosting.com/status");
        embed.setDescription("Only showing status for locations with at least 1 down node.\n" +
                "Click the link above to view all statuses, or type `!node [your node]` to find more information about it!" +
                "\nNote: Just because a node is marked as down doesn't necessarily mean the node itself is down.");
        Map<String, List<CharSequence>> outages = new HashMap<>();
        for(Node node : nodes) {
            if(!node.isOnline()) {
                List<CharSequence> loc = outages.getOrDefault(node.getLocation(), new ArrayList<>());
                loc.add(String.valueOf(node.getId()));
                outages.put(node.getLocation(), loc);
            }
        }
        for(String outage : outages.keySet()) {
            embed.addField(outage, "Node Outages: " + String.join(", ", outages.get(outage)), true);
        }
        if(outages.size() == 0) {
            embed.setDescription("**All nodes are up and working!**\n\n" +
                    "Click the link above to view all statuses, or type `!node [your node]` to find more information about it!");
        }
        return embed;
    }

    public EmbedBuilder specificNode(String nodeId) {
        int id;
        try {
            id = Integer.parseInt(nodeId);
        } catch(NumberFormatException e) {
            return new EmbedBuilder().setTitle("Error occurred!").setDescription("Invalid input!").setColor(Color.decode("#ff0000"));
        }
        List<Node> nodes;
        EmbedBuilder embed = new EmbedBuilder();
        try {
            nodes = new MCProHostingAPI().getNodeStatuses();
        } catch (Exception e) {
            e.printStackTrace();
            embed.setTitle("Error!");
            embed.setDescription("An error occurred retrieving stats. Please try again in a few seconds!");
            embed.setColor(Color.decode("#ff0000"));
            return embed;
        }
        embed.setTitle("MCProHosting Status for Node " + nodeId, "https://panel.mcprohosting.com/status");
        Node requestedNode = null;
        for(Node node : nodes) {
            if(node.getId() == id)
                requestedNode = node;
        }
        if(requestedNode == null) {
            return new EmbedBuilder().setTitle("Error occurred!").setDescription("Invalid node!").setColor(Color.decode("#ff0000"));
        }
        embed.addField("Location", requestedNode.getLocation(), true);
        if(requestedNode.isOnline()) {
            embed.addField("Status", "Online", true);
            embed.setColor(Color.decode("#00ff00"));
        } else {
            embed.addField("Status", "Offline", true);
            embed.setColor(Color.decode("#ff0000"));
            embed.setDescription(requestedNode.getMessage());
        }
        embed.setFooter("Last Beep");
        embed.setTimestamp(requestedNode.getLastHeartbeat());
        return embed;
    }
}

package com.mcprohosting.beepers.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcprohosting.beepers.Main;
import com.mcprohosting.beepers.util.SendTemporaryMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;

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
        JSONArray data = retrieveNodeInformation();
        EmbedBuilder embed = new EmbedBuilder();
        if(data == null) {
            embed.setTitle("Error!");
            embed.setDescription("An error occurred retrieving stats. Please try again in a few seconds!");
            embed.setColor(Color.decode("#ff0000"));
            return embed;
        }
        embed.setTitle("MCProHosting Node Statuses", "https://panel.mcprohosting.com/status");
        embed.setDescription("Only showing status for locations with at least 1 down node.\n" +
                "Click the link above to view all statuses, or type `!node [your node]` to find more information about it!");
        for(int i = 0; i < data.length(); i++) {
            JSONObject loc = data.getJSONObject(i);
            String name = loc.getString("location");
            JSONArray nodes = loc.getJSONArray("nodes");
            int nodeCount = nodes.length();
            int downCount = 0;
            ArrayList<CharSequence> down = new ArrayList<>();
            for(int j = 0; j < nodes.length(); j++) {
                JSONObject node = nodes.getJSONObject(j);
                if(!node.getBoolean("online")) {
                    downCount++;
                    down.add(String.valueOf(node.getInt("id")));
                }
            }
            DecimalFormat df = new DecimalFormat("#.##");
            int upCount = nodeCount - downCount;
            if(upCount != nodeCount) {
                embed.addField(name, "Node Outages: " + String.join(", ", down), true);
            }
            if(embed.getFields().isEmpty()) {
                embed.setDescription("**All nodes are up and working!**\n\n" +
                        "Click the link above to view all statuses, or type `!node [your node]` to find more information about it!");
            }

        }
        return embed;
    }

    public EmbedBuilder specificNode(String nodeId) {
        try {
            int id = Integer.parseInt(nodeId);
        } catch(NumberFormatException e) {
            return new EmbedBuilder().setTitle("Error occurred!").setDescription("Invalid input!").setColor(Color.decode("#ff0000"));
        }
        JSONArray data = retrieveNodeInformation();
        EmbedBuilder embed = new EmbedBuilder();
        if(data == null) {
            embed.setTitle("Error!");
            embed.setDescription("An error occurred retrieving stats. Please try again in a few seconds!");
            embed.setColor(Color.decode("#ff0000"));
            return embed;
        }
        embed.setTitle("MCProHosting Status for Node " + nodeId, "https://panel.mcprohosting.com/status");
        JSONObject requestedNode = null;
        String location = null;
        for(int i = 0; i < data.length(); i++) {
            JSONObject loc = data.getJSONObject(i);
            String name = loc.getString("location");
            JSONArray nodes = loc.getJSONArray("nodes");
            ArrayList<CharSequence> down = new ArrayList<>();
            for(int j = 0; j < nodes.length(); j++) {
                JSONObject node = nodes.getJSONObject(j);
                if(node.getInt("id") == Integer.parseInt(nodeId)) {
                    requestedNode = node;
                    location = name;
                }
            }
        }
        if(requestedNode == null) {
            return new EmbedBuilder().setTitle("Error occurred!").setDescription("Invalid node!").setColor(Color.decode("#ff0000"));
        }
        embed.addField("Location", location, true);
        if(requestedNode.getBoolean("online")) {
            embed.addField("Status", "Online", true);
            embed.setColor(Color.decode("#00ff00"));
        } else {
            embed.addField("Status", "Offline", true);
            embed.setColor(Color.decode("#ff0000"));
            embed.setDescription(requestedNode.getString("message"));
        }
        embed.setFooter("Last Beep");
        embed.setTimestamp(Instant.ofEpochSecond(requestedNode.getLong("last_heartbeat_epoch_seconds")));
        return embed;
    }

    public JSONArray retrieveNodeInformation() {
        Request request = new Request.Builder()
                .url("https://chew.pw/mc/pro/status")
                .get()
                .build();

        try (Response response = Main.jda.getHttpClient().newCall(request).execute()) {
            String r = response.body().string();
            return new JSONArray(r);
        } catch (SocketTimeoutException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

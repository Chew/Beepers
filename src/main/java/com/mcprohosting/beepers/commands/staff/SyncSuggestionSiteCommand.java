package com.mcprohosting.beepers.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcprohosting.beepers.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

public class SyncSuggestionSiteCommand extends Command {
    public SyncSuggestionSiteCommand() {
        this.name = "syncsuggestions";
        this.ownerCommand = true;
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.debug("Gathering Suggestion History");
        TextChannel suggestions = commandEvent.getGuild().getTextChannelById("715650008942116985");
        if(suggestions == null) {
            logger.error("Suggestions Channel is null. This is not good.");
            return;
        }
        suggestions.getHistory().retrievePast(50).queue((messages) -> {
            JSONArray data = new JSONArray();
            for(Message message : messages) {
                List<MessageReaction> reactions = message.getReactions();
                String up = MessageReaction.ReactionEmote.fromUnicode("⬆", commandEvent.getJDA()).getName();
                String down  = MessageReaction.ReactionEmote.fromUnicode("⬇", commandEvent.getJDA()).getName();
                int upCount = 0;
                int downCount = 0;
                for(MessageReaction react : reactions) {
                    MessageReaction.ReactionEmote reactionEmote = react.getReactionEmote();
                    if (up.equals(reactionEmote.getName())) {
                        upCount = react.getCount() - 1;
                    } else if (down.equals(reactionEmote.getName())) {
                        downCount = react.getCount() - 1;
                    }
                }
                MessageEmbed embed = message.getEmbeds().get(0);
                String author = embed.getAuthor().getName();
                String suggestion = embed.getTitle();
                String description = embed.getDescription();
                String jumpUrl = message.getJumpUrl();
                long timestamp = message.getTimeCreated().toEpochSecond();
                JSONObject payload = new JSONObject()
                        .put("author", author)
                        .put("title", suggestion)
                        .put("description", description)
                        .put("epoch_second", timestamp)
                        .put("up", upCount)
                        .put("down", downCount)
                        .put("url", jumpUrl);
                JSONObject field = new JSONObject();
                if(embed.getFields().size() == 1) {
                    MessageEmbed.Field theField = embed.getFields().get(0);
                    field.put("name", theField.getName());
                    field.put("value", theField.getValue());
                    payload.put("status", field);
                }
                data.put(payload);
            }
            logger.debug("Sending to server: " + data.toString());
            String response = sendSuggestionData(data);
            logger.debug("Response: " + response);
            commandEvent.reply(response);
        });
    }

    public String sendSuggestionData(JSONArray data) {
        Request request = new Request.Builder()
                .url("https://chew.pw/mcpro/suggestions/update")
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), new JSONObject().put("data", data).toString()))
                .addHeader("Authorization", Main.getProp().getProperty("chewapi"))
                .build();

        try (Response response = Main.jda.getHttpClient().newCall(request).execute()) {
            return response.body().string();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "SocketTimeoutException Occurred. See Terminal!";
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException Occurred. See Terminal!";
        }
    }
}

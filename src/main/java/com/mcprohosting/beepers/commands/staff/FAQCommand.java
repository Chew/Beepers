package com.mcprohosting.beepers.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcprohosting.beepers.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class FAQCommand extends Command {

    public FAQCommand() {
        this.name = "faq";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getMessage().delete().queue();

        JSONArray response = getFAQ(commandEvent.getArgs());
        if (response.isEmpty()) {
            commandEvent.reply("No FAQ found for the input. Try being less specific.");
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        String content = response.getJSONObject(0).getString("data");
        String title = content.split("\n")[0];
        String description = content.replace(title + "\n", "");
        embed.setTitle(title);
        for(String word : description.replace("\n", " ").split(" ")) {
            if(word.contains("cdn.discordapp.com")) {
                embed.setImage(word);
                description = description.replace(word, "");
            }
        }
        description = description.trim();
        embed.setDescription(description);
        commandEvent.reply(embed.build());
    }

    private JSONArray getFAQ(String input) {
        Request request = new Request.Builder()
            .url("https://chew.pw/mcpro/faq")
            .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), new JSONObject().put("search", input).toString()))
            .addHeader("Authorization", Main.getProp().getProperty("chewapi"))
            .build();

        try (Response response = Main.jda.getHttpClient().newCall(request).execute()) {
            return new JSONArray(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}

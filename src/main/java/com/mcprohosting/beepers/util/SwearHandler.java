package com.mcprohosting.beepers.util;

import com.mcprohosting.beepers.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwearHandler {
    public static final String[] swears = Main.getProp().getProperty("swears").split(",");

    public static String handleMessage(String content) {
        CharSequence[] words = content.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "").split(" ");
        List<String> safeWords = new ArrayList<>();
        for(CharSequence word : words) {
            if(!checkForWord(word)) {
                safeWords.add((String) word);
            }
        }
        return getSwear(String.join("", safeWords));
    }

    public static String getSwear(String message) {
        for (String swear : swears) {
            if (message.contains(swear)) {
                return swear;
            }
        }
        return null;
    }

    public static boolean checkForWord(CharSequence word) {
        // System.out.println(word);
        for (String swear : swears) {
            if (word.toString().contains(swear)) {
                return false;
            }
        }
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    "words_alpha.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.contains(word)) {
                    return true;
                }
            }
            in.close();
        } catch (IOException ignored) {
        }

        return false;
    }
}

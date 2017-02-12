package com.example.juliethjaramillo.fitbuddies;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michael on 12.02.17.
 */

public class BuddyInformation {
    public int id1, id2;

    public static BuddyInformation get(int userid) throws Exception {
        URL url = new URL(config.baseUrl + "/workoutBuddyFor/" + userid);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        String message = sb.toString();

        Gson gson = new Gson();
        return gson.fromJson(message, BuddyInformation.class);
    }
}

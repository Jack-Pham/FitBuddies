package com.example.juliethjaramillo.fitbuddies;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by juliethjaramillo on 2/12/17.
 */

public class user {
    public int id;
    public Date dob;
    public String firstname;
    public String lastname;
    public String intensity;
    public String goal;
    public String city;
    public String countryIsoAlpha2;
    public String email;

    public static user get(int id) throws Exception {
        URL url = new URL(config.baseUrl + "/users/"+ id + "/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        String message = sb.toString();

        Gson gson = new Gson();
        return gson.fromJson(message, user.class);
    }

    /** public static user register(String email, String pass, Date dob, String name, String last, String intensity,String  goal) throws Exception {
        URL url = new URL(Config.baseUrl + "/createUser/" + email + "/" + pass + "/"+ dob + "/" + name + "/"+ last + "/" + intensity + "/"+ goal + "/" + country +  "/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        String message = sb.toString();

        Gson gson = new Gson();
        return gson.fromJson(message, User.class);
    }

    public static User login(String email, String pass) throws Exception {
        URL url = new URL(Config.baseUrl + "/login/" + email + "/" + pass + "/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        String message = sb.toString();

        Gson gson = new Gson();
        return gson.fromJson(message, User.class);
    } **/
}

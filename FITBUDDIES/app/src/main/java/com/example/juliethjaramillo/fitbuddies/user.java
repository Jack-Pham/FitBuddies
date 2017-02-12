package com.example.juliethjaramillo.fitbuddies;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by juliethjaramillo on 2/12/17.
 */

public class user {
    public int id;
    public long dob;
    public String firstname;
    public String lastname;
    public int intensity;
    public int goal;
    public String city;
    public String countryIsoAlpha2;
    public String email;

    public static user get(int id) throws Exception {
        URL url = new URL(config.baseUrl + "/users/"+ id);
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

    /* public static void register(String email, String pass, Date dob, String name, String last, String intensity,String  goal) throws Exception {
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
    } */

    public static user login(String email, String pass) throws Exception {
        URL url = new URL(config.baseUrl + "/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.connect();;
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        String data = "{\"email\" : \""+email+"\", \"password\" : \"" + pass + "\" }";


        wr.write(data);
        wr.flush();
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
}

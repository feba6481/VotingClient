package com.htlkaindorf.feba6481.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by feba6481 on 21.12.15.
 */
public class Data {
    private static int[] pointsSchul = null, pointsAbt = null;
    private static String username = "", password = "";

    public static void setPointsSchul(int[] points) {
        pointsSchul = points;
    }

    public static void setPointsAbt(int[] points) {
        pointsAbt = points;
    }

    public static String getPoints() {
        String out = "Schulsprecher: " + pointsSchul[0] + "";
        for (int i = 1; i < pointsSchul.length; i++) {
            out += " - " + pointsSchul[i];
        }

        out += "\nAbteilungssprecher: " + pointsAbt[0];
        for (int i = 1; i < pointsAbt.length; i++) {
            out += " - " + pointsAbt[i];
        }

        return out;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getCredentials() {
        return username + " - " + password;
    }

    public static void setCredentials(String[] credentials) {
        username = credentials[0];
        password = credentials[1];
    }

    public static String getJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("username", username);
            object.put("password", password);
            object.put("pointsSchul", new JSONArray(Arrays.asList(pointsSchul)));
            object.put("pointsAbt", new JSONArray(Arrays.asList(pointsAbt)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}

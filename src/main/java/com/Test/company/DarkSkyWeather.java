package com.Test.company;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DarkSkyWeather {
    String finalUrl = "https://api.darksky.net/forecast/9fcea4c0b66bc1251a7256a4294024ba/59.939095,30.315868";
    DarkSkyWeather() {
    }

    public int[] coordinates() {
        int[] temps = new int[2];
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr = (JsonArray) jobj.get("daily").getAsJsonObject().get("data").getAsJsonArray();
                temps[0] = (int) Math.round(switchToCelsius(jarr.get(1).getAsJsonObject().get("temperatureHigh").getAsInt()));
                temps[1] = (int) Math.round(switchToCelsius(jarr.get(1).getAsJsonObject().get("temperatureLow").getAsInt()));

                return temps;
            }else {
                temps[0] = -1000;// ошибка
                temps[1] = 1000;
                return temps;
            }

        } catch (IOException e) {
            e.printStackTrace();
            temps[0] = -1000;// ошибка
            temps[1] = 1000;
            return temps;
        }
    }
    public void upload() {
        int[] result = coordinates();
        if (result[0] != -1000)
            PostgreSQL.addWeather("darkskyweather",result);
        else
            System.out.println("Не могу подключиться к DarkSky");
    }
    public int[][] takeThreeDays(String[] result) {
        this.finalUrl = "https://api.darksky.net/forecast/9fcea4c0b66bc1251a7256a4294024ba/" + result[3] + "," + result[2];
        int[][] temps = new int[3][2];
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr = (JsonArray) jobj.get("daily").getAsJsonObject().get("data").getAsJsonArray();
                for (int i = 0; i < 3; i++) {
                    temps[i][0] = (int) Math.round(switchToCelsius(jarr.get(i).getAsJsonObject().get("temperatureHigh").getAsInt()));
                    temps[i][1] = (int) Math.round(switchToCelsius(jarr.get(i).getAsJsonObject().get("temperatureLow").getAsInt()));
                }

                return temps;
            }else {

                return temps;
            }

        } catch (IOException e) {
            e.printStackTrace();

            return temps;
        }
    }
    public static Double switchToCelsius(double temp) {
        return (temp - 32) * 5 / 9;
        }

    }


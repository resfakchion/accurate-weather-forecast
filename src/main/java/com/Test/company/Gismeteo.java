package com.Test.company;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Gismeteo {
    public static void main(String[] args) {
        new Gismeteo();
    }

    String finalUrl = "https://api.gismeteo.net/v2/weather/forecast/aggregate/?latitude=59.939095&longitude=30.315868&days=3";

    Gismeteo() {

    }



    public int[] coordinates() {
        int[] temps = new int[2];

        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Gismeteo-Token", "5e21a0d240ae36.51672911");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr = (JsonArray) jobj.get("response").getAsJsonArray();
                temps[0] = (int) Math.round((jarr.get(1).getAsJsonObject().get("temperature").getAsJsonObject().get("air").getAsJsonObject().get("max").getAsJsonObject().get("C").getAsDouble()));
                temps[1] = (int) Math.round((jarr.get(1).getAsJsonObject().get("temperature").getAsJsonObject().get("air").getAsJsonObject().get("min").getAsJsonObject().get("C").getAsDouble()));

                return temps;
            } else {
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
            PostgreSQL.addWeather("gismeteo", result);
        else
            System.out.println("Не могу подключиться к Gismeteo");
    }

    public int[][] takeThreeDays(String[] result) {
        finalUrl = "https://api.gismeteo.net/v2/weather/forecast/aggregate/?latitude=" + result[3] + "&longitude=" + result[2] + "&days=3";
        int[][] temps = new int[3][2];
        String baseUrl = "https://api.weather.yandex.ru/v1/forecast?";
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Gismeteo-Token", "5e21a0d240ae36.51672911");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr = (JsonArray) jobj.get("response").getAsJsonArray();
                for (int i = 0; i < 3; i++) {
                    temps[i][0] = (int) Math.round((jarr.get(i).getAsJsonObject().get("temperature").getAsJsonObject().get("air").getAsJsonObject().get("max").getAsJsonObject().get("C").getAsDouble()));
                    temps[i][1] = (int) Math.round((jarr.get(i).getAsJsonObject().get("temperature").getAsJsonObject().get("air").getAsJsonObject().get("min").getAsJsonObject().get("C").getAsDouble()));
                }

                return temps;
            } else {

                return temps;
            }

        } catch (IOException e) {
            e.printStackTrace();

            return temps;
        }

    }

    public Forecast takeDescription(String[] result, int k) {
        finalUrl = "https://api.gismeteo.net/v2/weather/forecast/aggregate/?latitude=" + result[3] + "&longitude=" + result[2] + "&days=3";
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Gismeteo-Token", "5e21a0d240ae36.51672911");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr = (JsonArray) jobj.get("response").getAsJsonArray();
                JsonObject day = jarr.get(k).getAsJsonObject();
                int humidity = day.get("humidity").getAsJsonObject().get("percent").getAsJsonObject().get("avg").getAsInt();
                int pressure = day.get("pressure").getAsJsonObject().get("mm_hg_atm").getAsJsonObject().get("max").getAsInt();
                String description = day.get("description").getAsJsonObject().get("full").getAsString();
                String icon = day.get("icon").getAsString();

                return new Forecast(humidity,pressure,description,icon);




            } else {

                return null;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}


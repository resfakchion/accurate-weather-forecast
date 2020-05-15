package com.Test.company;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YandexWeather {
    String finalUrl = "https://api.weather.yandex.ru/v1/forecast?lat=59.939095&lon=30.315868";
    public static void main(String[] args) {
        new YandexWeather();
    }
   public  YandexWeather() {
    }

    public int[] coordinates() {
        int[] temps = new int[2];

        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Yandex-API-Key", "473556ad-8159-4cad-bf97-93a14c25005e");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr =  jobj.get("forecasts").getAsJsonArray();
                JsonObject days =  jarr.get(1).getAsJsonObject().get("parts").getAsJsonObject();
                String[] dayParts = {"night", "morning", "day", "evening"};
                int min = days.get(dayParts[0]).getAsJsonObject().get("temp_min").getAsInt();
                int max = days.get(dayParts[0]).getAsJsonObject().get("temp_max").getAsInt();
                for (int i = 1; i <dayParts.length;i++) {
                    if (min > days.get(dayParts[i]).getAsJsonObject().get("temp_min").getAsInt())
                        min = days.get(dayParts[i]).getAsJsonObject().get("temp_min").getAsInt();
                    if (max < days.get(dayParts[i]).getAsJsonObject().get("temp_max").getAsInt())
                        max = days.get(dayParts[i]).getAsJsonObject().get("temp_max").getAsInt();
                }
                temps[0] = max; //max temp
                temps[1] = min;// min temp
                return temps;
            } else {
                temps[0] = 1500;// ошибка
                temps[1] = -1000;
                return temps;
            }

        } catch (IOException e) {
            e.printStackTrace();
            temps[0] = -15000;// ошибка
            temps[1] = 1000;
            return temps;
        }
    }
    public void upload() {
        int[] result = coordinates();
        if (result[0] != 1500)
            PostgreSQL.addWeather("yandexweather",result);
        else
            System.out.println("Не могу подключиться к Яндекс Погоде");
    }
    public int[][] takeThreeDays(String[] result) {
        int[][] temps = new int[3][2];
        String baseUrl = "https://api.weather.yandex.ru/v1/forecast?";
        this.finalUrl = baseUrl + "lat=" + result[3]  + "&lon=" + result[2];
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Yandex-API-Key", "473556ad-8159-4cad-bf97-93a14c25005e");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr =  jobj.get("forecasts").getAsJsonArray();
                for (int k = 0; k < 3; k++) {
                    JsonObject days = jarr.get(k).getAsJsonObject().get("parts").getAsJsonObject();
                    String[] dayParts = {"night", "morning", "day", "evening"};
                    int min = days.get(dayParts[0]).getAsJsonObject().get("temp_min").getAsInt();
                    int max = days.get(dayParts[0]).getAsJsonObject().get("temp_max").getAsInt();
                    for (int i = 1; i < dayParts.length; i++) {
                        if (min > days.get(dayParts[i]).getAsJsonObject().get("temp_min").getAsInt())
                            min = days.get(dayParts[i]).getAsJsonObject().get("temp_min").getAsInt();
                        if (max < days.get(dayParts[i]).getAsJsonObject().get("temp_max").getAsInt())
                            max = days.get(dayParts[i]).getAsJsonObject().get("temp_max").getAsInt();
                    }
                    temps[k][0] = max; //max temp
                    temps[k][1] = min;// min temp
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
}


package com.Test.company;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class AccuWeather extends  ForecastOperator {
private  String finalUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/295212?apikey=aWzMqIBmAqnHZQFUIMGkNDFrNJbG1Pp2&language=ru&metric=true";

    public AccuWeather() {

    }


    public void coordinates() {
        int[] temps = new int[2];
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();

                JsonArray jarr =  jobj.get("DailyForecasts").getAsJsonArray();
                JsonObject day = jarr.get(1).getAsJsonObject().get("Temperature").getAsJsonObject();
                temps[0] = (int) Math.round(day.get("Maximum").getAsJsonObject().get("Value").getAsDouble());
                temps[1] = (int) Math.round(day.get("Minimum").getAsJsonObject().get("Value").getAsDouble());
                forecast = new SmallForecast ("accuweather",temps[1],temps[0],true);
            }
            else forecast = new SmallForecast("yandexweather",temps[1],temps[0],false);
        } catch (IOException ignored) {

        }
    }



    public String takeCoordinates(String lat, String lon) {
        String finalUrl = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=aWzMqIBmAqnHZQFUIMGkNDFrNJbG1Pp2&q=" + lat + "%2C" + lon;
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                return jobj.get("Key").getAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Ошибка поиска города, выведена погода для Санкт-Петербурга");
        return null;
    }
    public int[][] takeThreeDays(String[] result) {
        String key = takeCoordinates(result[3],result[2]);
        if (key != null)
            finalUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/" + key + "?apikey=aWzMqIBmAqnHZQFUIMGkNDFrNJbG1Pp2&language=ru&metric=true";
        int[][] temps = new int[3][2];
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();

                JsonArray jarr = (JsonArray) jobj.get("DailyForecasts").getAsJsonArray();
                for (int i = 0; i < 3; i++) {
                    JsonObject day = jarr.get(i).getAsJsonObject().get("Temperature").getAsJsonObject();
                    temps[i][0] = (int) Math.round(day.get("Maximum").getAsJsonObject().get("Value").getAsDouble());
                    temps[i][1] = (int) Math.round(day.get("Minimum").getAsJsonObject().get("Value").getAsDouble());
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


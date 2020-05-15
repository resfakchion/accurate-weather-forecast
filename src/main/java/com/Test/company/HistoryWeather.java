package com.Test.company;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class HistoryWeather {
    public static void main(String[] args) {
     HistoryWeather historyWeather =  new HistoryWeather();
      historyWeather.coordinates();
        System.out.println(Arrays.toString(historyWeather.coordinates()));
    }
   HistoryWeather() {

    }

    public int[] coordinates() {
        int[] temps = new int[2];
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String today = formatForDateNow.format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        String yesterday= formatForDateNow.format(cal.getTime());

        String baseUrl = "https://api.weather.yandex.ru/v1/forecast?";
        String finalUrl = "https://api.weatherbit.io/v2.0/history/daily?&lat=59.939095&lon=30.315868&start_date=" + yesterday + "&end_date=" + today + "&key=32d826d4f60947e29490c14ce1159b94" ;
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr =  jobj.get("data").getAsJsonArray();
                temps[0]  =  jarr.get(0).getAsJsonObject().get("max_temp").getAsInt();
                temps[1]  =  jarr.get(0).getAsJsonObject().get("min_temp").getAsInt();
                return temps;

            } else {
                System.out.println(connection.getResponseCode() + "Ошибка в истории ");
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
}


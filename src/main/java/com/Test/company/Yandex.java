package com.Test.company;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Yandex {

    public static void main(String[] args) {
        new Yandex();
    }
    private Yandex() {
        System.out.println(translate("Геогрий пидорас", "ru-en"));
    }
    public String translate(String text, String lang) {
        byte[] out = ("text=" + text).getBytes();
        if (out.length>10000) {
            return "Error. Text too long";
        }
        String key = "trnsl.1.1.20200212T095155Z.2087901037d410b0.2121ba7196726e4704e01b5be2a029a60b9fad36"; //Подставьте сюда свой полученный ключ между кавычек
        String baseUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate";
        String finalUrl = baseUrl + "?lang=" + lang + "&key=" + key;
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", out.length + "");
            connection.setRequestProperty("Accept", "*/*");
            connection.getOutputStream().write(out);
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr = jobj.get("text").getAsJsonArray();
                return jarr.get(0).getAsString();
            } else {
                return "Error. Site response non 200";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
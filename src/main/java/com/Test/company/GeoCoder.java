package com.Test.company;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GeoCoder {
    public static void main(String[] args) throws IOException {
        GeoCoder geoCoder = new GeoCoder();
        System.out.println(Arrays.toString(geoCoder.coordinates("СПБ")));
    }
    public String[] coordinates(String address ) throws IOException {

        String baseUrl = "https://api.weather.yandex.ru/v1/forecast?";
        String apikey = "c785ac7c-4fe3-4a0e-973a-43b96ec1e3a3";
        String Url = "https://geocode-maps.yandex.ru/1.x/?apikey=c785ac7c-4fe3-4a0e-973a-43b96ec1e3a3&format=json&geocode=";
        address = encodeValue(address);
        String finalUrl = Url + address;

            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                JsonObject jobj = new  JsonParser().parse(
                        new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray jarr =  jobj.get("response").getAsJsonObject().get("GeoObjectCollection").getAsJsonObject().get("featureMember").getAsJsonArray();
                String description = jarr.get(0).getAsJsonObject().get("GeoObject").getAsJsonObject().get("description").getAsString();
                String name = jarr.get(0).getAsJsonObject().get("GeoObject").getAsJsonObject().get("name").getAsString();
                String[] parse = jarr.get(0).getAsJsonObject().get("GeoObject").getAsJsonObject().get("Point").getAsJsonObject().get("pos").getAsString().split(" ");
                String[] result = new String[4];
                result[0] = description;
                result[1] = name;
                result[2] = parse[0];
                result[3] = parse[1];
                return result;
            } else {
                System.out.println("Ошибка соединения");
                return null;
            }


    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}


package com.Test.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TakeWeather {

    public static int[][] calculateTemp(int[][] gismeteoTemps,int[][] accweatherTemps, int[][] yandexTemps, int[][] darkskyTemps) {
        int[][] calculateResult = new int[3][2];
        int[] resultAvg = PostgreSQL.checkAvgAccuracy();
        double k = 1.0 / (resultAvg[0] + resultAvg[1] + resultAvg[2] + resultAvg[3]);
        double gismeteoK = resultAvg[0] * k;
        double accuweatherK = resultAvg[1] * k;
        double yandexweatherK = resultAvg[1] * k;
        double darkSkyK = resultAvg[1] * k;
        for (int i = 0; i < 3; i++) {
            calculateResult[i][0] = (int) (Math.round (gismeteoTemps[i][0] * gismeteoK)  + (accweatherTemps[i][0] * accuweatherK) + yandexTemps[i][0] * yandexweatherK +
                    darkskyTemps[i][0] * darkSkyK);
            calculateResult[i][1] = (int) (Math.round (gismeteoTemps[i][1] * gismeteoK)  + (accweatherTemps[i][1] * accuweatherK) + yandexTemps[i][1] * yandexweatherK +
                    darkskyTemps[i][1] * darkSkyK);
        }
        return calculateResult;
    }
    public ArrayList<Forecast> takeDescription(String[] result){
        ArrayList<Forecast> list = new ArrayList<>();
        Gismeteo gismeteo = new Gismeteo();
        gismeteo.takeThreeDays(result);
        for (int i = 0; i < 3; i++) {
            Forecast forecast = gismeteo.takeDescription(result,i);
            list.add(forecast);
        }
        return list;
    }
    public String[] takeGeoCode(String s) throws IOException {
        GeoCoder geoCoder = new GeoCoder();
        return geoCoder.coordinates(s);
    }
    public int[][] takeWeather(String[] result){
        System.out.println("Погода в " + result[0] + " " + result[1]);
        Gismeteo gismeteo = new Gismeteo();
        AccuWeather accuWeather = new AccuWeather();
        YandexWeather yandexWeather = new YandexWeather();
        DarkSkyWeather darkSkyWeather = new DarkSkyWeather();
        gismeteo.takeThreeDays(result);

        return calculateTemp(gismeteo.takeThreeDays(result),accuWeather.takeThreeDays(result),yandexWeather.takeThreeDays(result),darkSkyWeather.takeThreeDays(result));
    }

}

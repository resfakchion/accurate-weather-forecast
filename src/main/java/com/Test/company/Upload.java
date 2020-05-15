package com.Test.company;


public class Upload {
    static double time;
    public Upload() {
        time = System.currentTimeMillis();
        Thread thread1 = new DarkSkyThread();
        Thread thread2 = new YandexWeatherThread();
        Thread thread3 = new AccuWeatherThread();
        Thread thread4 = new GismeteoThread();
        thread2.start();
        thread4.start();
        thread3.start();
        thread1.start();
    }

      public static class DarkSkyThread extends Thread {
        @Override
        public void run() {
            DarkSkyWeather darkSkyWeather =  new DarkSkyWeather();
            darkSkyWeather.upload();
            System.out.println("DarkSky - "  + (System.currentTimeMillis() - time));
        }
    }
    public static class YandexWeatherThread extends Thread {
        @Override
        public void run() {
            YandexWeather yandexWeather = new YandexWeather();
            yandexWeather.upload();
            System.out.println("YandexWeather - "  + (System.currentTimeMillis() - time));
        }
    }
    public static class AccuWeatherThread extends Thread {
        @Override
        public void run() {
           AccuWeather accuWeather = new AccuWeather();
           accuWeather.upload();
            System.out.println("AccuWeather - " + (System.currentTimeMillis() - time));
        }
    }
    public static class GismeteoThread extends Thread {
        @Override
        public void run() {
            Gismeteo gismeteo = new Gismeteo();
            gismeteo.upload();
            System.out.println("Gismeteo - "  + (System.currentTimeMillis() - time));
        }
    }


}

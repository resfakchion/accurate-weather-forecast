package com.Test.company;

public abstract class ForecastOperator {
    public SmallForecast forecast;


    public abstract void coordinates();

    public void upload() {
        this.coordinates();
        if (forecast.connection)
            PostgreSQL.addWeather(forecast);
        else
            System.out.println("Не могу подключиться к " + forecast.companyName);
    }
}

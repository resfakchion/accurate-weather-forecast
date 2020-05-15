package com.Test.company;

public class Forecast {
    int humidity;
    String icon;
    String description;
    int pressure;

    Forecast(int humidity, int pressure, String description, String icon ) {
        this.humidity = humidity;
        this.pressure = pressure;
        this.description = description;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }
}

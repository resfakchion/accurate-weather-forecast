package com.Test.company;

public class SmallForecast {
    public String companyName;
    public int min,max;
    public boolean connection = false;

    public SmallForecast(String companyName, int min, int max, boolean connection) {
        this.companyName = companyName;
        this.min = min;
        this.max = max;
        this.connection = connection;
    }
}

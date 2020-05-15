package com.Test.company;

import java.sql.*;

public class PostgreSQL {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        addAccuracy();
    }


    public PostgreSQL(String weather, int[] temps) {
        System.out.println(addWeather(weather, temps));
    }

    public static String addWeather(String weather, int[] temps) {
        Connection c;
        Statement stmt;


        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://ec2-176-34-97-213.eu-west-1.compute.amazonaws.com:5432/de0fq49vrl5liu", "ygcdcsmuyqjeju", "8401ebcb2b17ba4a441afc91668cf11d3862733af0e27e0e07510005ba66f669");
            c.setAutoCommit(false);
            String sql;

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("Select day FROM " + weather + " where day = current_date + 1;");
            if (rs.next()) {
                sql = "DELETE from " + weather + " where day = current_date + 1;";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO " + weather + " (day,TempMin,TempMax) VALUES (current_date + 1 , + " + temps[1] + " , " + temps[0] + " )";
                stmt.executeUpdate(sql);
                System.out.println("Данные прогноза " + weather + " были обновлены");
            } else {
                sql = "INSERT INTO " + weather + " (day,TempMin,TempMax) VALUES (current_date + 1 , + " + temps[1] + " , " + temps[0] + " )";
                stmt.executeUpdate(sql);
                System.out.println("Данные прогноза " + weather + " внесены");
            }
            rs.close();
            stmt.close();
            c.commit();

            stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery("Select tempmin , tempmax FROM " + weather + " where day = current_date - 1;");
            if (resultSet.next()) {
                int forecastTempMin = resultSet.getInt(1);
                int forecastTempMax = resultSet.getInt(2);
                HistoryWeather historyWeather = new HistoryWeather();
                int[] historyTemp = historyWeather.coordinates();
                int accuracy = 0;
                if (Math.abs(forecastTempMax - historyTemp[0]) <= 2 && Math.abs(forecastTempMin - historyTemp[1]) <= 2)
                    accuracy = 100;
                sql = "UPDATE " + weather + " set TempRealMax  = " + historyTemp[0] + ", TempRealMin =  " + historyTemp[1] + ", accuracy =  " + accuracy + " where day = current_date - 1;";
                stmt.executeUpdate(sql);
                System.out.println("Внесены данные о реальной погоде " + weather);
            } else System.out.println("Данные о реальной погоде " + weather + " не были внесены");
            rs.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return "-- " + weather + " - Done!";

    }

    private static void addAccuracy() throws SQLException, ClassNotFoundException {
        Connection c;
        Statement stmt;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://ec2-176-34-97-213.eu-west-1.compute.amazonaws.com:5432/de0fq49vrl5liu", "ygcdcsmuyqjeju", "8401ebcb2b17ba4a441afc91668cf11d3862733af0e27e0e07510005ba66f669");
        c.setAutoCommit(false);
        String sql;
        stmt = c.createStatement();
        String[] forecast = {"accuweather", "yandexweather", "darkskyweather", "gismeteo"};
        for (String s : forecast) {
            for (int j = 0; j < 140; j++) {
                ResultSet resultSet = stmt.executeQuery("Select tempmin , tempmax , temprealmin , temprealmax FROM " + s + " where id = " + j);
                if (resultSet.next()) {
                    int accuracy = 0;
                    int forecastTempMin = resultSet.getInt(1);
                    int forecastTempMax = resultSet.getInt(2);
                    int tempRealMin = resultSet.getInt(3);
                    int tempRealMax = resultSet.getInt(4);
                    if (Math.abs(forecastTempMax - tempRealMax) <= 2 && Math.abs(forecastTempMin - tempRealMin) <= 2)
                        accuracy = 100;
                    sql = "UPDATE " + s + " set  accuracy =  " + accuracy + " where id = " + j;
                    stmt.executeUpdate(sql);
                }
            }
            System.out.println(s + " - done");

        }
        stmt.close();
        c.commit();
        c.close();
    }

    public static int[] checkAvgAccuracy()  {
        Connection c = null;
        Statement stmt = null;
        int[] result = new int[4];
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            c = DriverManager
                    .getConnection("jdbc:postgresql://ec2-176-34-97-213.eu-west-1.compute.amazonaws.com:5432/de0fq49vrl5liu", "ygcdcsmuyqjeju", "8401ebcb2b17ba4a441afc91668cf11d3862733af0e27e0e07510005ba66f669");
            c.setAutoCommit(false);
            String sql;
            try {
                stmt = c.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String[] forecast = {"gismeteo", "accuweather", "yandexweather", "darkskyweather"};

            for (int i = 0; i < 4;i++) {
                ResultSet resultSet = stmt.executeQuery("Select AVG(accuracy) FROM " + forecast[i]);
                if(resultSet.next())
                result[i] = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}

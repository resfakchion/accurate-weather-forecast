package com.Test.company;

import java.sql.*;

public class PostgreSQL {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        addAccuracy();
    }


    public PostgreSQL(SmallForecast forecastOperator) {
        System.out.println(addWeather(forecastOperator));
    }

    public static String addWeather(SmallForecast forecastOperator) {
        Connection c;
        Statement stmt;


        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://ec2-46-137-124-19.eu-west-1.compute.amazonaws.com:5432/demjopbme5bn2i", "cldwzutmpwcybh", "ffbe97c6844752d8b10a7cbe6fbe6e33e44f5487f0e2a88ffff4a48e52203d62");
            c.setAutoCommit(false);
            String sql;

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("Select day FROM " + forecastOperator.companyName + " where day = current_date + 1;");
            if (rs.next()) {
                sql = "DELETE from " + forecastOperator.companyName + " where day = current_date + 1;";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO " + forecastOperator.companyName + " (day,TempMin,TempMax) VALUES (current_date + 1 , + " + forecastOperator.min + " , " + forecastOperator.max + " )";
                stmt.executeUpdate(sql);
                System.out.println("Данные прогноза " + forecastOperator.companyName + " были обновлены");
            } else {
                sql = "INSERT INTO " + forecastOperator.companyName + " (day,TempMin,TempMax) VALUES (current_date + 1 , + " + forecastOperator.min + " , " + forecastOperator.max + " )";
                stmt.executeUpdate(sql);
                System.out.println("Данные прогноза " + forecastOperator.companyName + " внесены");
            }
            rs.close();
            stmt.close();
            c.commit();

            stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery("Select tempmin , tempmax FROM " + forecastOperator.companyName + " where day = current_date - 1;");
            if (resultSet.next()) {
                int forecastTempMin = resultSet.getInt(1);
                int forecastTempMax = resultSet.getInt(2);
                HistoryWeather historyWeather = new HistoryWeather();
                int[] historyTemp = historyWeather.coordinates();
                int accuracy = 0;
                if (Math.abs(forecastTempMax - historyTemp[0]) <= 2 && Math.abs(forecastTempMin - historyTemp[1]) <= 2)
                    accuracy = 100;
                sql = "UPDATE " + forecastOperator.companyName + " set TempRealMax  = " + historyTemp[0] + ", TempRealMin =  " + historyTemp[1] + ", accuracy =  " + accuracy + " where day = current_date - 1;";
                stmt.executeUpdate(sql);
                System.out.println("Внесены данные о реальной погоде " + forecastOperator.companyName);
            } else System.out.println("Данные о реальной погоде " + forecastOperator.companyName + " не были внесены");
            rs.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return "-- " + forecastOperator.companyName + " - Done!";

    }

    private static void addAccuracy() throws SQLException, ClassNotFoundException {
        Connection c;
        Statement stmt;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://ec2-46-137-124-19.eu-west-1.compute.amazonaws.com:5432/demjopbme5bn2i", "cldwzutmpwcybh", "ffbe97c6844752d8b10a7cbe6fbe6e33e44f5487f0e2a88ffff4a48e52203d62");
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
                    .getConnection("jdbc:postgresql://ec2-46-137-124-19.eu-west-1.compute.amazonaws.com:5432/demjopbme5bn2i", "cldwzutmpwcybh", "ffbe97c6844752d8b10a7cbe6fbe6e33e44f5487f0e2a88ffff4a48e52203d62");
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

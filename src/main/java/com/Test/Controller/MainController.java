package com.Test.Controller;

import com.Test.company.Forecast;
import com.Test.company.PostgreSQL;
import com.Test.company.TakeWeather;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@Controller
public class MainController {



    @GetMapping("/")
    public String main(Model model) throws IOException {

        int[] temps = PostgreSQL.checkAvgAccuracy();
        model.addAttribute("avgTemp",temps);

        return "index";
    }
    @PostMapping("/")
    public String readCity(@RequestParam String city , Model model)  {
        TakeWeather weather = new TakeWeather();
        try {
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("E,dd MMMM y");
            Calendar cal = Calendar.getInstance();
            String[] days = new String[3];
            for (int i = 0; i < 3;i++) {
                days[i] = formatForDateNow.format(cal.getTime());
                cal.add(Calendar.DATE, 1);
            }
            String[] coordinates = weather.takeGeoCode(city);
        ArrayList<Forecast> forecasts = weather.takeDescription(coordinates);
        int[][] tempThreeDays = weather.takeWeather(coordinates);
        model.addAttribute("threeDaysTemp",tempThreeDays);
        model.addAttribute("description",forecasts);
        model.addAttribute("days",days);
        model.addAttribute("city",coordinates[1]);
        return "forecast";
        } catch (Exception e) {
            return "404";
        }
    }




}

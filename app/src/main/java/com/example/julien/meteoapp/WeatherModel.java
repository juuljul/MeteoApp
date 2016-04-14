package com.example.julien.meteoapp;

import java.util.ArrayList;

/**
 * Created by julien on 13/04/2016.
 */
public class WeatherModel {
    // Le String dt_txt repésente la date de la météo sous la forme "yyyy-MM-dd HH:mm:ss"
    String dt_txt;
    Mesures main;
    ArrayList <WeatherDescription> weather;

    public Mesures getMain() {
        return main;
    }

    public ArrayList<WeatherDescription> getWeather() {
        return weather;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public static class Mesures{
        double temp_min;
        double temp_max;

        public double getTemp_max() {
            return temp_max;
        }

        public double getTemp_min() {
            return temp_min;
        }
    }

    public static class WeatherDescription{
        // Le String main représente la nature du temps :"Clear", "Clouds", "Rain"
        String main;

        public String getMain() {
            return main;
        }
    }

}

package com.example.assignment3.ui.home;

import java.util.ArrayList;

//version 1.0.1: get openWeather API data --Hongyu
public class Root {
    public Coord coord;
    public ArrayList<Weather> weather;
    public String base;
    public Main main;
    public int visibility;
    public Wind wind;
    public Clouds clouds;
    public int dt;
    public Sys sys;
    public int timezone;
    public int id;
    public String name;
    public int cod;

    public Main getMain() {
        return main;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }
}

class Coord {
    public double lon;
    public double lat;
}

class Weather {
    public int id;
    public String main;
    public String description;
    public String icon;

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}

class Main {
    public double temp;
    public double feels_like;
    public double temp_min;
    public double temp_max;
    public int pressure;
    public int humidity;

    public double getTemp() {
        return temp;
    }
}

class Wind {
    public double speed;
    public int deg;
}

class Clouds {
    public int all;
}

class Sys {
    public int type;
    public int id;
    public String country;
    public int sunrise;
    public int sunset;
}





package com.CountriesAndPeople.ergo;

public class Countries {
    private String make;
    private String population;
    private String area;
    private int iconID;
    boolean mark;
    String  color;

    public Countries (String make, String population, String area, int iconID, boolean mark, String color) {
        super();
        this.make = make;
        this.population = population;
        this.area = area;
        this.iconID = iconID;
        this.mark = mark;
        this.color = color;
    }

    public String getMake() {
        return make;
    }
    public String getPopulation() {
        return population;
    }
    public String getArea() {
        return area;
    }
    public int getIconID() {
        return iconID;
    }
    public boolean getMark() {return mark;}
    public String getColor() {return color;}
}

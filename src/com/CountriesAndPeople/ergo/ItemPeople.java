package com.CountriesAndPeople.ergo;


public class ItemPeople {


    private String name;
    private String country;
    boolean mark = false;

    public ItemPeople (String name, String country, boolean mark) {
        super();
        this.name = name;
        this.country = country;
        this.mark = mark;

    }

    public String getName() {
        return name;
    }
    public String getCountry() {
        return country;
    }
    public boolean getMark() {return mark;}

}



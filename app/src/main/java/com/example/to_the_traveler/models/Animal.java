package com.example.to_the_traveler.models;

public class Animal {
    private String name;
    private String description;
    private int imageResId;
    private String wikiUrl;

    public Animal(String name, String description, int imageResId, String wikiUrl) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.wikiUrl = wikiUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public String getWikiUrl() { return wikiUrl; }
}

package com.example.to_the_traveler.models;

public class SharedAnimal {
    private String id;
    private String firstName;
    private String lastName;
    private String animalName;
    private String animalDescription;
    private String animalSources;
    private boolean approved; // חדש: האם המנהל אישר?

    public SharedAnimal() {
    }

    public SharedAnimal(String id, String firstName, String lastName, String animalName, String animalDescription, String animalSources, boolean approved) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.animalName = animalName;
        this.animalDescription = animalDescription;
        this.animalSources = animalSources;
        this.approved = approved;
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAnimalName() { return animalName; }
    public String getAnimalDescription() { return animalDescription; }
    public String getAnimalSources() { return animalSources; }
    public boolean isApproved() { return approved; }

    public void setId(String id) { this.id = id; }
    public void setApproved(boolean approved) { this.approved = approved; }
}

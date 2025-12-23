package com.giuzep89.helpinghandbackend.models;

public enum HelpType {
    GARDENING("Gardening", Prize.GARDENING),
    TAXES("Taxes", Prize.TAXES),
    COMPANY("Company", Prize.COMPANY),
    PLUMBING("Plumbing", Prize.PLUMBING),
    PAINTING("Painting", Prize.PAINTING),
    MOVING("Moving", Prize.MOVING),
    IT("IT Support", Prize.IT),
    BUREAUCRACY("Bureaucracy", Prize.BUREAUCRACY),
    LANGUAGE("Language", Prize.LANGUAGE),
    GROCERIES("Groceries", Prize.GROCERIES),
    PETSITTING("Pet Sitting", Prize.PETSITTING),
    TRANSPORT("Transport", Prize.TRANSPORT),
    REPAIRS("Repairs", Prize.REPAIRS),
    HOUSE_CHORES("House Chores", Prize.HOUSE_CHORES);

    private String helpType;
    private Prize prize;

    HelpType(String helpType, Prize prize) {
        this.helpType = helpType;
        this.prize = prize;
    }

    public String getHelpType() {
        return helpType;
    }

    public Prize getPrize() {
        return prize;
    }

    // An admin might want to use setters to change these in a bigger app, and same story goes for
    // the Prize class, but for the sake of this PoC application, I'm just offering
    // to retrieve these values using getters.
}





package com.giuzep89.helpinghandbackend.models;

public enum HelpType {
    GARDENING("gardening", Prize.GARDENING),
    TAXES("taxes", Prize.TAXES),
    COMPANY("company", Prize.COMPANY),
    PLUMBING("plumbing", Prize.PLUMBING),
    PAINTING("painting", Prize.PAINTING),
    MOVING("moving", Prize.MOVING),
    IT("IT Support", Prize.IT),
    BUREAUCRACY("bureaucracy", Prize.BUREAUCRACY),
    LANGUAGE("language", Prize.LANGUAGE),
    GROCERIES("groceries", Prize.GROCERIES),
    PETSITTING("pet sitting", Prize.PETSITTING),
    TRANSPORT("transport", Prize.TRANSPORT),
    REPAIRS("repairs", Prize.REPAIRS),
    HOUSE_CHORES("house chores", Prize.HOUSE_CHORES);

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





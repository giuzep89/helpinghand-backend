package com.giuzep89.helpinghandbackend.models;

public enum Prize {
    GARDENING("Thumbs Are Green All Over"),
    TAXES("Knows How To Fudge The Numbers"),
    COMPANY("Drank All My Coffee"),
    PLUMBING("No Leaks Can Defeat Them"),
    PAINTING("Gave Those Walls A Nice Coat"),
    MOVING("Took All My Things Away"),
    IT("Certified Nerd"),
    BUREAUCRACY("Knows Their Way Around The Government"),
    LANGUAGE("Master Of Tongues"),
    GROCERIES("Pusher Of Carts"),
    PETSITTING("A Cat AND A Dog Person"),
    TRANSPORT("Brings Me Around Like I'm Royalty"),
    REPAIRS("A True Fixer-Upper"),
    HOUSE_CHORES("Finally Stuff Gets Done");

    private final String prizeDescription;

    Prize(String prizeDescription) {
        this.prizeDescription = prizeDescription;
    }

    public String getPrizeDescription() {
        return prizeDescription;
    }
}


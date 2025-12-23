package com.giuzep89.helpinghandbackend.models;

public class HelpRequest extends Post {
    private HelpType helpType;
    private boolean helpFound;

    public HelpRequest() {
        super();
    }

    public HelpRequest(User author, String description, String location, HelpType helpType, boolean helpFound) {
        super(author, description, location);
        this.helpType = helpType;
        this.helpFound = false;
    }


}

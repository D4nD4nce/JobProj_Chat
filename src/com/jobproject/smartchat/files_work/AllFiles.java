package com.jobproject.smartchat.files_work;

/*
* keeps paths for helping files with answers
 */

public enum AllFiles
{
    FIRST_FILE("./bin/general.txt"),
    SECOND_FILE("./bin/morning.txt"),
    THIRD_FILE("./bin/small.txt"),
    FORTH_FILE("./bin/afternoon.txt");

    // current description
    private String description;

    // private custom enum implementation with description
    AllFiles(String description)
    {
        this.description = description;
    }

    // get current enum object description
    public String getDescription()
    {
        return description;
    }
}

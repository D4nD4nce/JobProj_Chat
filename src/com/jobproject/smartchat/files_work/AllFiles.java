package com.jobproject.smartchat.files_work;

/*
*
 */

public enum AllFiles
{
    FIRST_FILE("./bin/general.txt"),
    SECOND_FILE("./bin/morning.txt"),
    THIRD_FILE("./bin/small.txt"),
    FORTH_FILE("./bin/afternoon.txt");

    private String description;

    AllFiles(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}

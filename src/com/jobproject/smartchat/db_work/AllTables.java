package com.jobproject.smartchat.db_work;

import java.util.Random;

public enum AllTables {
    FIRST_TABLE("TEST_ANSWERS_1"),
    SECOND_TABLE("TEST_ANSWERS_2"),
    THIRD_TABLE("TEST_ANSWERS_3"),
    FORTH_TABLE("TEST_ANSWERS_4");

    // current description
    private String description;

    // private custom enum implementation with description
    AllTables(String description) {
        this.description = description;
    }

    // get current enum object description
    public String getDescription() {
        return description;
    }

    // getting random table with answers
    public static String getNewRandomTable(String currentTable) {
        Random rand = new Random();
        AllTables tables[] = AllTables.values();
        int lengh = tables.length;
        String newTable = tables[rand.nextInt(lengh-1)].getDescription();       // get new table
        if (currentTable != null && !currentTable.isEmpty()) {
            while(currentTable.compareTo(newTable) == 0) {
                newTable = tables[rand.nextInt(lengh-1)].getDescription();      // check for new table, get another if it's the same
            }
        }
        return newTable;
    }
}

package database;

import java.util.Random;

/*
 * enum for all "moods" - different types of answers
 * every mood has it's value, written in DB
 * also they have indexes - id_mood. it's the key, using in DB to find mood
 */

public enum DBTypesKeys {
    SO_SO_MOOD  ("so so"),
    SAD_MOOD    ("sad"),
    ANGRY_MOOD  ("angry"),
    HAPPY_MOOD  ("happy");

    private String moodValue;

    DBTypesKeys(String mood) {
        this.moodValue = mood;
    }

    // mood value - name of mood (answer type), written id DB
    public String getTypeValue() {
        return moodValue;
    }

    // mood ID - key to find mood in DB table
    public int getTypeId() {
        return  this.ordinal() + 1;
    }

    // get new random mood (type of answers)
    public static DBTypesKeys getRandomType(DBTypesKeys excludedMood) {
        Random rand = new Random();
        DBTypesKeys moods[] = DBTypesKeys.values();
        int bound = moods.length - 1;
        DBTypesKeys newMood = moods[rand.nextInt(bound)];        // choose new mood
        if (excludedMood != null) {
            while (newMood.compareTo(excludedMood) == 0) {                  // while the same - choose another
                newMood = moods[rand.nextInt(bound)];
            }
        }
        return newMood;
    }
}
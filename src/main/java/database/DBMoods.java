package database;

import java.util.Random;

/*
 * enum for all "moods" - different types of answers
 * every mood has it's value, written in DB
 * also they have indexes - id_mood. it's the key, using in DB to find mood
 */
public enum DBMoods {
    SO_SO_MOOD  ("so so"),
    SAD_MOOD    ("sad"),
    ANGRY_MOOD  ("angry"),
    HAPPY_MOOD  ("happy");

    private String moodValue;

    DBMoods(String mood) {
        this.moodValue = mood;
    }

    // mood value - name of mood, written id DB
    public String getMoodValue() {
        return moodValue;
    }

    // mood ID - key to find mood in DB table
    public int getMoodId() {
        return  this.ordinal() + 1;
    }

    // get new random mood (type of answers)
    public static DBMoods getRandomMood(DBMoods excludedMood) {
        Random rand = new Random();
        DBMoods moods[] = DBMoods.values();
        int length = moods.length;
        DBMoods newMood = moods[rand.nextInt(length-1)];        // choose new mood
        if (excludedMood != null) {
            while (newMood.compareTo(excludedMood) == 0) {              // while the same - choose another
                newMood = moods[rand.nextInt(length-1)];
            }
        }
        return newMood;
    }
}
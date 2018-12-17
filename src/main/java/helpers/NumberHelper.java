package helpers;

import java.util.Random;

public class NumberHelper {
    private static final LogHelper logger = new LogHelper(NumberHelper.class.getName());

    // get random number in borders, oldValue excluded
    public static int getRandomValue(int from, int to, int oldValue) {
        //int lowBound = Math.abs(from);
        //int highBound = Math.abs(to);
        if (to < from) {
            logger.printLogWarning("invalid params", "getRandomValue");
            return to;
        }
        Random random = new Random();
        int randomValue = random.nextInt(to - from);
        if (oldValue >= 0) {
            while (oldValue == randomValue) {
                randomValue = random.nextInt(to - from);
            }
        }
        return from + randomValue;
    }
}

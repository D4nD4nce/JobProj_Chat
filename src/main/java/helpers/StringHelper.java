package helpers;

import java.util.List;

public class StringHelper {
    public static String arrayToString(String[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for(String particle : array) {
            if(!first) {
                stringBuilder.append(' ');
            }
            stringBuilder.append(particle);
            first = false;
        }
        return stringBuilder.toString();
    }

    public static String arrayToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for(String particle : list) {
            if(!first) {
                stringBuilder.append(' ');
                stringBuilder.append(',');
            }
            stringBuilder.append(particle);
            first = false;
        }
        return stringBuilder.toString();
    }
}

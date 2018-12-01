package answer_logic;

import java.util.*;
import java.util.regex.Pattern;

/*
 * general enum for work with user phrases to generate typical "humanLike" answers
 */

public enum Answer {
    NOTHING_FOUND,
    HELLO,
    GOODBYE,
    WHO_ARE_YOU,
    WHERE_ARE_YOU_FROM;

    public static Answer checkLogic(String strUserText) {
        if (strUserText == null || strUserText.isEmpty()) {
            return NOTHING_FOUND;
        }
        strUserText = strUserText
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "");
        if (checkFirstWord(strUserText, hello_Requests)) {
            return HELLO;
        }
        if (checkFirstWord(strUserText, goodbye_Requests)) {
            return GOODBYE;
        }
        if (checkPhrase(strUserText, who_are_you_Requests)) {
            return WHO_ARE_YOU;
        }
        if (checkPhrase(strUserText, where_are_you_from_Requests)) {
            return WHERE_ARE_YOU_FROM;
        }
        return NOTHING_FOUND;
    }

    // for "hello" and "bye" like words
    private static boolean checkFirstWord (String phraseToCheck, List<String> listToCompare) {
        for (String compareWord : listToCompare) {
            if (compareWord.equals(phraseToCheck.substring(0, compareWord.length()))) {
                return true;
            }
        }
        return false;
    }

    // for comparative phrases
    private static boolean checkPhrase (String userPhrase, List<String> lstPhraseWords) {
        StringBuffer regularEpression = new StringBuffer("[a-z]");
        for (String word : lstPhraseWords) {
            regularEpression
                    .append(word)
                    .append("[a-z]");
        }
        return Pattern.matches(regularEpression.toString(), userPhrase);
        //return false;
    }

    private static final List<String> hello_Requests;
    static {
        hello_Requests = new ArrayList<>();
        hello_Requests.add("hi");
        hello_Requests.add("hello");
        hello_Requests.add("hey");
    }

    private static final List<String> goodbye_Requests;
    static {
        goodbye_Requests = new ArrayList<>();
        goodbye_Requests.add("bye");
        goodbye_Requests.add("goodbye");
    }

    private static final List<String> who_are_you_Requests;
    static {
        who_are_you_Requests = new ArrayList<>();
        who_are_you_Requests.add("who");
        who_are_you_Requests.add("are");
        who_are_you_Requests.add("you");
    }

    private static final List<String> where_are_you_from_Requests;
    static {
        where_are_you_from_Requests = new ArrayList<>();
        where_are_you_from_Requests.add("where");
        where_are_you_from_Requests.add("are");
        where_are_you_from_Requests.add("you");
        where_are_you_from_Requests.add("from");
    }
}
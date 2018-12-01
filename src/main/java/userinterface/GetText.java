package userinterface;

/*
* Class encapsulates work with user input
* */

import java.util.Scanner;

public class GetText {

    public String getInputText() {
        return inputConsole();
    }

    private String inputConsole() {
        Scanner input = new Scanner(System.in);
        String result;
        if (input.hasNextLine() && !(result = input.nextLine()).isEmpty()) {
            return result;
        }
        return "";
    }

}

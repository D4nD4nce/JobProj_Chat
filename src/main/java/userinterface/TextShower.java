package userinterface;

/*
 * Class encapsulates work with output for user
 * */

public class TextShower {
    public void setOutputText(String str) {
        if (str == null) {
            return;
        }
        outputConsole(str);
    }

    private void outputConsole(String str) {
        System.out.println(str);
    }

}

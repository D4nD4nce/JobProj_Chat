import commands.CommandWork;
import userinterface.GetText;

public class StartChat {
    public static void main (String args[]) {
        //DataBase dataBase = new DataBase(1);
        boolean flag = true;
        GetText getText = new GetText();                    // class to get user input
        CommandWork comWork = new CommandWork();            // class to interpret user commands and execute them
        while(flag){
            flag = comWork.answer(getText.getInputText());  // take input in cycle while user writes anything until activating shutdown command
        }
    }
}

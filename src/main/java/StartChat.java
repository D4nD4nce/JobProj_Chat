import chat_commands.ChatCommandsWork;
import userinterface.GetText;

public class StartChat {
    public static void main (String args[]) {
        boolean flag = true;
        GetText getText = new GetText();                    // class to get user input
        ChatCommandsWork comWork = new ChatCommandsWork();            // class to interpret user commands and execute them
        while(flag){
            flag = comWork.answer(getText.getInputText());  // take input in cycle while user writes anything until activating shutdown command
        }
    }
}

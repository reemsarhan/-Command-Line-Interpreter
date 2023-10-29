import CLI.Parser;
import CLI.Terminal;
import java.util.ArrayList; // Import ArrayList
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        ArrayList<String> CommandsHistory = new ArrayList<>(); // Use ArrayList to store command history
        int cnt = 1;
        Terminal terminal = new Terminal();
        Parser parser = new Parser();
        String command;
        String[] Arguments;
        String INPUT;
        boolean stay = true;

        Scanner input = new Scanner(System.in);
        while (stay) {
            System.out.print(">");
            INPUT = input.nextLine();
            if (INPUT.equals("exit")) {
                stay = false;
                break;
            }

            if (parser.parse(INPUT)) {
                command = parser.getCommandName();
                Arguments = parser.getArgs();
                CommandsHistory.add(command); // Add the command to the command history
                if (command.equals("history")) { // Use .equals to compare strings
                    for (String x : CommandsHistory) {
                        System.out.print(cnt++ + " ");
                        System.out.println(x);
                    }
                } else {
                    terminal.chooseCommandAction(command, Arguments);
                }
            }
        }
    }
}

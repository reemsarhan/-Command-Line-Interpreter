// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.


import CLI.Parser;
import CLI.Terminal;
import java.io.File;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Terminal terminal=new Terminal();
        Parser   parser=new Parser();
        String command;
        String[] Arguments;
        String INPUT;
        boolean stay=true;

        Scanner input=new Scanner(System.in);
        while (stay)
        {
            System.out.print(">");
            INPUT=input.nextLine();
            if(INPUT.equals("exit"))
            {
                stay=false;
                break;
            }

            if(parser.parse(INPUT))
            {
                command=parser.getCommandName();
                Arguments= parser.getArgs();

                terminal.chooseCommandAction(command,Arguments);
            }
        }

    }
    }

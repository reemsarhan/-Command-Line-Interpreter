package CLI;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Terminal
{
    Parser parser;
    private Path currentDirectory;

    public Terminal()
    {
        // Initialize currentDirectory with the current working directory
        currentDirectory = Paths.get(System.getProperty("user.dir"));
    }

    public String pwd()
    {
        return currentDirectory.toFile().getAbsolutePath();
    }

    public void help()
    {
        System.out.println("Available Commands:");
        System.out.println("pwd ->Print the current working directory.");
        System.out.println("cd ->Change the current directory.");
        System.out.println("ls ->List files and directories in the current directory.");
        System.out.println("ls -r ->List files and directories recursively.");
        System.out.println("mkdir ->Create a new directory.");
        System.out.println("touch ->Create a new file.");
        System.out.println("cp ->Copy a file or directory.");
        System.out.println("cp -r ->Copy a directory recursively.");
        System.out.println("rm ->Remove a file or directory.");
        System.out.println("cat ->Display the contents of a file.");
        System.out.println("wc ->Count words, lines, and characters in a file.");
        System.out.println("history ->Displays a list with the commands you’ve used in the past");
    }

    public void history()
    {


    }
//    public String pwd()
//    {
//
//    }
    public void cd(String[] args)
    {

    }
    public void echo(String[] args)
    {
        for (String x:args)
        {
            System.out.println(x);
        }
    }

    //This method will choose the suitable command method to be called
    public void chooseCommandAction( String command,String[] arguments)
    {
        switch (command)
        {
            case "help":
                help();
                break;
            case "history":
                history();
                break;
            case "echo":
                echo(arguments);
                break;
            case "pwd":
                System.out.println(pwd());
                break;
            case "cd":

                break;

            case "ls":

                break;
            case "mkdir":

                break;
            case "rmdir":

                break;
            case "touch":

                break;
            case "cp":

                break;
            case "rm":

                break;

            case "cat":

                break;
            case "wc":

                break;




        }

    }
}


package CLI;
import java.io.File;
public class Parser
{
    String commandName;
    String[] args;

    // This method will divide the input into commandName and args
    // where "input" is the string command entered by the user
    
    public boolean parse(String input)
    {
        // Split the input into parts (e.g., command name and arguments)
        String[] parts = input.trim().split(" ");
        if (parts.length > 0)
        {
            commandName = parts[0];
            args = new String[parts.length - 1];
            for (int i = 1; i < parts.length; i++)
            {
                args[i - 1] = parts[i];
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    public String getCommandName()
    {
        return commandName;
    }
    public String[] getArgs()
    {
        return args;
    }
}

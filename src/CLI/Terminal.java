package CLI;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
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

    public void echo(String[] args)
    {
        for (String x:args)
        {
            System.out.println(x);
        }
    }
    public void ls()
    {
        try
        {
            List<Path> contents = new ArrayList<>();

            // Open a directory stream to list directory contents
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentDirectory)) {
                for (Path entry : directoryStream)
                {
                    contents.add(entry);
                }
            }

            // Sort the contents alphabetically
            Collections.sort(contents);

            // Print the sorted contents
            for (Path entry : contents)
            {
                System.out.println(entry.getFileName());
            }
        }
        catch (IOException e)
        {
            System.err.println("Error listing directory contents: " + e.getMessage());
        }
    }
    public void lsreversed() {
        try {
            List<Path> contents = new ArrayList<>();

            // Open a directory stream to list directory contents
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentDirectory)) {
                for (Path entry : directoryStream) {
                    contents.add(entry);
                }
            }

            // Sort the contents in reverse alphabetical order
            Collections.sort(contents, Collections.reverseOrder());

            // Print the sorted contents
            for (Path entry : contents) {
                System.out.println(entry.getFileName());
            }
        } catch (IOException e) {
            System.err.println("Error listing directory contents: " + e.getMessage());
        }
    }

    public void history()
    {


    }
     
    public void cpfile(String sourcePath, String targetPath)
    {
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);

            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied " + sourcePath + " to " + targetPath);
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }
    public void cpDirectory(String sourcePath, String targetPath) {
        Path source = Paths.get(sourcePath);
        Path target = Paths.get(targetPath);

        if (Files.exists(source))
        {
            try {
                Files.walkFileTree(source, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        Path targetDir = target.resolve(source.relativize(dir));
                        Files.createDirectories(targetDir);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Path targetFile = target.resolve(source.relativize(file));
                        Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.CONTINUE;
                    }
                });

                System.out.println("Copied " + sourcePath + " to " + targetPath);
            } catch (IOException e) {
                System.err.println("Error copying directory: " + e.getMessage());
            }
        } else {
            System.err.println("Source directory does not exist.");
        }
    }
    public void mkdir(String... directories)
    {
        for (String directory : directories)
        {
            Path newDir = Paths.get(directory);

            if (!newDir.isAbsolute())
            {
                // If the provided directory path is relative, create it in the current directory
                newDir = currentDirectory.resolve(newDir);
            }

            try {
                Files.createDirectories(newDir);
                System.out.println("Created directory: " + newDir.toString());
            } catch (IOException e) {
                System.err.println("Error creating directory: " + e.getMessage());
            }
        }
    }

    public void cat(String[] arguments) {
        if (arguments.length == 1) {
            // Case 1: cat <file> - Print the content of a single file
            String fileName = arguments[0];
            Path filePath = currentDirectory.resolve(fileName);

            if (Files.isRegularFile(filePath)) {
                try {
                    List<String> lines = Files.readAllLines(filePath);
                    for (String line : lines) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + fileName);
                }
            } else {
                System.err.println("File does not exist or is not a regular file: " + fileName);
            }
        } else if (arguments.length == 2) {
            // Case 2: cat <file1> <file2> - Concatenate and print the content of two files
            String fileName1 = arguments[0];
            String fileName2 = arguments[1];

            Path filePath1 = currentDirectory.resolve(fileName1);
            Path filePath2 = currentDirectory.resolve(fileName2);

            if (Files.isRegularFile(filePath1) && Files.isRegularFile(filePath2)) {
                try {
                    List<String> lines1 = Files.readAllLines(filePath1);
                    List<String> lines2 = Files.readAllLines(filePath2);

                    for (String line : lines1) {
                        System.out.println(line);
                    }
                    for (String line : lines2) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading files: " + fileName1 + " or " + fileName2);
                }
            } else {
                System.err.println("One or both of the files do not exist or are not regular files: " + fileName1 + ", " + fileName2);
            }
        } else {
            System.err.println("Usage: cat <file> or cat <file1> <file2>");
        }
    }

    public void rmdir(String dir) {
        if (dir.equals("*"))
        {
            // Case 1: rmdir * - Remove all empty directories in the current directory
            removeEmptyDirectories(currentDirectory);
        } else {
            Path targetDir = Paths.get(dir);

            if (!targetDir.isAbsolute()) {
                // If the provided directory path is relative, resolve it against the current directory
                targetDir = currentDirectory.resolve(targetDir);
            }

            if (Files.isDirectory(targetDir)) {
                // Case 2: rmdir <directory> - Remove the given directory if it is empty
                if (isEmptyDirectory(targetDir)) {
                    try {
                        Files.delete(targetDir);
                        System.out.println("Removed directory: " + targetDir.toString());
                    } catch (IOException e) {
                        System.err.println("Error removing directory: " + e.getMessage());
                    }
                } else {
                    System.err.println("Directory is not empty: " + targetDir.toString());
                }
            } else {
                System.err.println("Directory does not exist: " + targetDir.toString());
            }
        }
    }

    public void cd(String[] arguments)
    {
        if (arguments.length == 0) {
            // Case 1: cd - Change the current path to the home directory
            currentDirectory = Paths.get(System.getProperty("user.home"));
        } else if (arguments.length == 1) {
            String arg = arguments[0];
            if (arg.equals("..")) {
                // Case 2: cd .. - Change the current directory to the previous directory
                currentDirectory = currentDirectory.getParent();
            } else {
                // Case 3: cd <path> - Change the current path to the specified path
                Path targetPath = Paths.get(arg);

                if (!targetPath.isAbsolute()) {
                    // If the provided directory path is relative, resolve it against the current directory
                    targetPath = currentDirectory.resolve(targetPath);
                }

                if (targetPath.toFile().isDirectory()) {
                    currentDirectory = targetPath;
                } else {
                    System.err.println("Directory does not exist: " + targetPath.toString());
                }
            }
        } else {
            System.err.println("Usage: cd [<path>|..]");
        }
    }

    public void rm(String fileName)
    {
        Path filePath = currentDirectory.resolve(fileName);

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            try {
                Files.delete(filePath);
                System.out.println("Removed file: " + fileName);
            } catch (IOException e) {
                System.err.println("Error removing file: " + e.getMessage());
            }
        } else {
            System.err.println("File does not exist or is not a regular file: " + fileName);
        }
    }

    private void removeEmptyDirectories(Path directory) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    if (isEmptyDirectory(entry)) {
                        Files.delete(entry);
                        System.out.println("Removed directory: " + entry.toString());
                    } else {
                        removeEmptyDirectories(entry);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error while removing empty directories: " + e.getMessage());
        }
    }

    private boolean isEmptyDirectory(Path directory) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            return !stream.iterator().hasNext();
        } catch (IOException e) {
            System.err.println("Error checking if directory is empty: " + e.getMessage());
            return false;
        }
    }
    public void touch(String filePath)
    {
        Path path = Paths.get(filePath);

        try {
            Files.createFile(path);
            System.out.println("Created file: " + filePath);
        }
        catch (IOException e)
        {
            System.err.println("Error creating file: " + e.getMessage());
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
            case "ls":
                 if(arguments.length==0)
                {
                    ls();
                }
               else  if(arguments[0].equals("-r"))
                {
                    lsreversed();
                }
                break;

            case "cp":
                if (arguments.length == 2)
                {
                    String sourcePath = arguments[0];
                    String targetPath = arguments[1];
                    cpfile(sourcePath, targetPath);
                }
                else if(arguments.length==3)
                {
                    String sourcedir=arguments[1];
                    String targetdir=arguments[2];
                    if(arguments[0].equals("-r"))
                    {
                        cpDirectory(sourcedir,targetdir);
                    }
                }
                break;

            case "touch":
                if(arguments.length==1)
                {
                    touch(arguments[0]);
                }
                break;

            case "mkdir":
                mkdir(arguments);
                break;

            case "rmdir":
                if(arguments.length==1)
                {
                    String dir=arguments[0];
                    rmdir(dir);
                }
                break;

            case "cd":
                cd(arguments);
                break;

            case "rm":
                if(arguments.length==1)
                {
                    String rem=arguments[0];
                    rm(rem);
                }
                break;

            case "cat":
                cat(arguments);
                break;

        }

    }
}


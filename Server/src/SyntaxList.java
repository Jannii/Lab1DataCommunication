
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Swashy
 */
public class SyntaxList {

    Path path;
    File file;
    FileOutputStream fileOut;

    public SyntaxList() {

        file = new File(new File(".").getAbsolutePath());
    }

    public String checkForCommands(String command) {
        String[] syntaxCommand;
        syntaxCommand = new String[2];
        syntaxCommand = command.split(":");
        System.out.println(syntaxCommand.length);
        if (syntaxCommand[0].equalsIgnoreCase("dirr")) {

            return syntaxDirr();

        } else if (syntaxCommand[0].equalsIgnoreCase("List")) {

            return syntaxList();

        } else if (syntaxCommand[0].equalsIgnoreCase("dl")) {

            return syntaxDL(syntaxCommand);

        } else if (syntaxCommand[0].equalsIgnoreCase("sup")) {

            //??
        } else {

            return "Command doesent exist";

        }

        return "";

    }

    public String syntaxDirr() {

        try {

            String currentPath = file.getCanonicalPath();
            return currentPath;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String syntaxList() {

        try {
            String[] fileList = file.list();
            StringBuilder allFiles = new StringBuilder();
            System.out.println("Files in directory " + file.getCanonicalPath());
            allFiles.append("Files in directory " + file.getCanonicalPath() + ",");
            for (int i = 0; i < fileList.length; i++) {

                allFiles.append(fileList[i] + ",");
            }
            System.out.println(allFiles);
            return allFiles.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String syntaxDL(String[] syntaxCommand) {

        if (!syntaxCommand[1].equals("")) {
            try {
                String filePath = file.getCanonicalPath() + "\\" + syntaxCommand[1];

                File f = new File(filePath);
                if (f.exists() == false) {

                    System.out.println("File doesent exist");
                    return "";
                    
                } else if (f.exists() == true) {

                    System.out.println("downloading " + filePath);
                    return filePath;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            return "Chosse a file";
        }
        return "";
    }

    public String test() {

        String[] value = file.list();
        String result = Arrays.toString(value);
        System.out.println(Arrays.toString(value));

        return result;

    }
}

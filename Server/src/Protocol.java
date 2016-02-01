
import java.io.File;
import java.nio.file.Path;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Swashy
 */
public class Protocol {

    Path path;
    File file;

    public Protocol() {

        file = new File(new File(".").getAbsolutePath());
    }

    public String checkForCommands(String command) {

        if (command.equalsIgnoreCase("apa")) {

            try {
                String currentPath = file.getCanonicalPath();
                return currentPath;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }else if(command.equalsIgnoreCase("List")){
            try{
            String[] fileList = file.list();
            StringBuilder allFiles = new StringBuilder();
            System.out.println("Files in directory "+file.getCanonicalPath());
                allFiles.append("Files in directory "+file.getCanonicalPath());
                for(int i = 0 ;i<fileList.length; i++){
                
                  allFiles.append(fileList[i]+",");
                }
                  System.out.println(allFiles);
                return allFiles.toString();
            }catch (Exception ex) {
                ex.printStackTrace();
            }

                        
        }
        else{
            return "Command doesent exist";
        }
        return "";

    }
}

package soeldichat.soeldichat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScFiles {

    private ScFiles(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException ioException) {
            System.out.println("Could not create new ChatLog file.");
        }
    }

    public static boolean createDirectory(Path directoryPath){
        try {
            Files.createDirectory(directoryPath);
        }
        catch (IOException ioException){
            return false;
        }
        return true;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public static String saveImage(Path oldImagePath, Path newImagePath){
        if(!Files.exists(newImagePath.getParent())){
            ScFiles.createDirectory(newImagePath.getParent());
        }
        try{
            Files.copy(oldImagePath, newImagePath);}
        catch (Exception exception){
            return "";
        }
        return newImagePath.toString();
    }

    public static void logToJson(File jsonLogFile, Object toJsonObject){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            JsonWriter writer = gson.newJsonWriter(new FileWriter(jsonLogFile, false));
            gson.toJson(toJsonObject, toJsonObject.getClass(), writer);
            writer.flush();
            writer.close();
        } catch (IOException fileOpenException) {
            //create file if it cannot be found
            if (fileOpenException.getClass().equals(FileNotFoundException.class)) {
                ScFiles.createFile(jsonLogFile);
                logToJson(jsonLogFile, toJsonObject);
            }
        }
    }

    public static List<Message> fetchMessagesFromJson(File jsonLogFile){
        Gson gson = new Gson();
        try (Reader reader = new FileReader(jsonLogFile)) {

            Message[] messageArray = gson.fromJson(reader, Message[].class);

            if (messageArray != null) {
                return Arrays.asList(messageArray);
            }

        } catch (IOException fileOpenException) {
            //create file if it cannot be found
            if (fileOpenException.getClass().equals(FileNotFoundException.class)) {
                ScFiles.createFile(jsonLogFile);
                return fetchMessagesFromJson(jsonLogFile);
            }

        }
        return Collections.emptyList();
    }

    public static List<Contact> fetchContactsFromJson(File jsonLogFile){
        Gson gson = new Gson();
        try (Reader reader = new FileReader(jsonLogFile)) {

            Contact[] contactArray = gson.fromJson(reader, Contact[].class);

            if (contactArray != null) {
                return Arrays.asList(contactArray);
            }

        } catch (IOException fileOpenException) {
            //create file if it cannot be found
            if (fileOpenException.getClass().equals(FileNotFoundException.class)) {
                ScFiles.createFile(jsonLogFile);
                return fetchContactsFromJson(jsonLogFile);
            }

        }
        return Collections.emptyList();
    }

    public static String getDateFormatted(String timeStamp) {
        String year = timeStamp.substring(0, 4);
        String month = timeStamp.substring(5,7);
        String day = timeStamp.substring(8,10);

        return day + "." + month + "." + year;
    }

    public static String getDate(String timeStamp) {
        return timeStamp.substring(0,10);
    }
}

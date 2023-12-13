package soeldichat.soeldichat;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class SoeldiChatApplication extends Application {

    private final String userNumber = "0000000000";
    private String currentContactNumber;
    private List<Contact> contactList;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SoeldiChatApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 900);
        Controller controller = fxmlLoader.getController();

        //give controller access to this instance of application
        controller.setApplication(this);

        //set stage and style of scene
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setMinWidth(650);
        stage.setMinHeight(400);
        stage.setTitle("SoeldiChat");
        stage.setScene(scene);
        stage.show();

        //load and display contacts
        this.contactList = loadContacts();
        this.currentContactNumber = contactList.getFirst().getNumber();

        //load and display messages (of first contact)
        contactList.forEach(x -> x.setMessageList(new ArrayList<>(loadMessages(x.getNumber()))));
        controller.displayContacts(contactList);
        controller.displayChat(contactList.getFirst().getMessageList());
    }

    public static void main(String[] args) {
        launch();
    }

    public void setCurrentContactNumber(String currentContactNumber) {
        this.currentContactNumber = currentContactNumber;
    }

    public String getCurrentContactNumber() {
        return currentContactNumber;
    }

    public String getUserNumber() {
        return userNumber;
    }

    private List<Contact> loadContacts(){
        Gson gson = new Gson();

        try (Reader reader = new FileReader("C:/Users/morit/IdeaProjects/RoboRally/src/main/resources/soeldichat/soeldichat/chats/chats-info.json")) {

            Contact[] contactArray = gson.fromJson(reader,Contact[].class);

            if(contactArray != null){
                return Arrays.asList(contactArray);
            }
            else return Collections.emptyList();

        } catch (IOException fileOpenException) {
            if(fileOpenException.getClass().equals(FileNotFoundException.class)) {
                System.out.println("Failed to load chats-info.json for contact information");
            }
        }
        return Collections.emptyList();
    }

    private List<Message> loadMessages(String contactNumber){

        File chatFile = new File("C:/Users/morit/IdeaProjects/RoboRally/src/main/resources/soeldichat/soeldichat/chats/" + contactNumber +".json");
        Gson gson = new Gson();

        try (Reader reader = new FileReader(chatFile)) {

            Message[] messageArray = gson.fromJson(reader,Message[].class);

            if(messageArray != null){
                return Arrays.asList(messageArray);
            }
            else return Collections.emptyList();


        } catch (IOException fileOpenException) {
            //create file if it cannot be found
            if(fileOpenException.getClass().equals(FileNotFoundException.class)){
                createNewFile(chatFile);
            }

        }
        return Collections.emptyList();

    }

    private void logMessage(Message newMessage){
        File chatFile = new File("C:\\Users\\morit\\IdeaProjects\\RoboRally\\src\\main\\resources\\soeldichat\\soeldichat\\chats\\" + determineChatPartnerNumber(newMessage) + ".json");

        Gson gson = new Gson();

        try{
            //load messages and append new message
            ArrayList<Message> messageList = new ArrayList<>(loadMessages(currentContactNumber));
            messageList.add(newMessage);

            //log messages
            System.out.println("hier beim schreiben der message in die json file");
            JsonWriter writer = new JsonWriter(new FileWriter(chatFile, false));
            gson.toJson(messageList, ArrayList.class, writer);
            writer.flush();
            writer.close();
        }
        catch(IOException fileOpenException){
            //create file if it cannot be found
            System.out.println("exception wurde geworfen");
            if(fileOpenException.getClass().equals(FileNotFoundException.class)){
                System.out.println("file wurde nicht gefunden");
                createNewFile(chatFile);
            }
        }
    }

    private void createNewFile(File file){
        try{
            file.createNewFile();
        }
        catch (IOException ioException){
            System.out.println("Could not create new ChatLog file for " + currentContactNumber + ".");
        }
    }

    private String determineChatPartnerNumber(Message message){
        if(message.getSender().equals(userNumber)){
            return message.getReceiver();
        }
        return message.getSender();
    }

    public void addMessageToChat(Message newMessage){

        Contact.getContactByNumber(contactList, determineChatPartnerNumber(newMessage)).getMessageList().add(newMessage);

        //add message to log
        logMessage(newMessage);
    }

}


package soeldichat.soeldichat;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SoeldiChatApplication extends Application {

    private final String userNumber = "0000000000";
    private String currentContactNumber;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SoeldiChatApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 900);
        Controller controller = ((Controller)fxmlLoader.getController());
        controller.setApplication(this);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setMinWidth(650);
        stage.setMinHeight(400);
        stage.setTitle("SoeldiChat");
        stage.setScene(scene);
        stage.show();

        //load and display contacts
        List<Contact> contactList = loadContacts();
        this.currentContactNumber = contactList.getFirst().getNumber();

        //load and display messages (of first contact)
        contactList.forEach(x -> x.setMessageList(loadMessages()));
        controller.displayContacts(contactList, userNumber);
        controller.displayChat(currentContactNumber, userNumber);

    }

    public static void main(String[] args) {
        launch();
    }

    public void setCurrentContactNumber(String currentContactNumber) {
        this.currentContactNumber = currentContactNumber;
    }

    public List<Contact> loadContacts(){
        Gson gson = new Gson();

        try (Reader reader = new FileReader("C:/Users/morit/IdeaProjects/RoboRally/src/main/resources/soeldichat/soeldichat/chats-info.json")) {

            Contact[] contactArray = gson.fromJson(reader,Contact[].class);

            if(contactArray != null){
                return Arrays.asList(contactArray);
            }
            else return Collections.emptyList();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Message> loadMessages(){
        Gson gson = new Gson();

        try (Reader reader = new FileReader("C:/Users/morit/IdeaProjects/RoboRally/src/main/resources/soeldichat/soeldichat/" + currentContactNumber +".json")) {

            Message[] messageArray = gson.fromJson(reader,Message[].class);

            if(messageArray != null){
                return Arrays.asList(messageArray);
            }
            else return Collections.emptyList();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();

    }

    public void logMessage(String text){
        Message newMessage = new Message(userNumber,currentContactNumber,text, "");
        String fileName = "C:\\Users\\morit\\IdeaProjects\\RoboRally\\src\\main\\resources\\soeldichat\\soeldichat\\" + currentContactNumber + ".json";

        Gson gson = new Gson();

        try(FileWriter writer = new FileWriter(fileName, true)){
            String newJsonObjectLiteral = gson.toJson(newMessage);
            System.out.println(newJsonObjectLiteral);

            writer.append(newJsonObjectLiteral);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}


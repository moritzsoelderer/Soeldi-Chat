package soeldichat.soeldichat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

@Setter
@Getter
public class SoeldiChatApplication extends Application {

    private final String userNumber = "0000000000";
    private String focusedContactNumber;
    private List<Contact> contactList;
    private String defaultProfilePictureString = "default-profile-picture.png";

    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(SoeldiChatApplication.class.getResource("soeldi-chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
        Controller controller = fxmlLoader.getController();

        //give controller access to this instance of application
        controller.setApplication(this);

        //set stage and style of scene
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setMinWidth(650);
        stage.setMinHeight(400);
        stage.setTitle("SoeldiChat");
        stage.getIcons().add(new Image("file:src\\main\\resources\\soeldichat\\soeldichat\\img\\application-icon.jpg"));
        stage.setScene(scene);
        stage.show();

        //load and display contacts
        this.contactList = loadContacts();
        this.focusedContactNumber = contactList.getFirst().getNumber();

        //load and display messages (of first contact)
        contactList.forEach(x -> x.setMessageList(new ArrayList<>(loadMessages(x.getNumber()))));
        controller.displayContacts(contactList);
        controller.focusFirstContact();
        controller.setupchatMenuBarProfilePicture(contactList.getFirst());
        controller.displayChat(contactList.getFirst().getMessageList());
        controller.updateChatMenuBar();
    }

    public static void main(String[] args) {
        launch();
    }


    public void addMessageToChat(Message newMessage) {
        Contact.getContactByNumber(contactList, newMessage.determineChatPartnerNumber(this.userNumber)).getMessageList().add(newMessage);
        logMessage(newMessage);
    }

    public void updateContactList(String focusedContactNumber) {
        Contact focusedContact = Contact.getContactByNumber(getContactList(), focusedContactNumber);
        ArrayList<Contact> contactArrayList = new ArrayList<>(getContactList());
        contactArrayList.remove(focusedContact);
        contactArrayList.add(0, focusedContact);
        setContactList(contactArrayList);
    }

    private List<Contact> loadContacts() {
        File contactFile = new File("src/main/resources/soeldichat/soeldichat/chats/chats-info.json");
        return ScFiles.fetchContactsFromJson(contactFile);
    }

    private List<Message> loadMessages(String contactNumber) {
        File chatFile = new File("src/main/resources/soeldichat/soeldichat/chats/" + contactNumber + ".json");
        return ScFiles.fetchMessagesFromJson(chatFile);
    }

    public Image loadImage(String imageFileName) {
        return new Image("file:src\\main\\resources\\soeldichat\\soeldichat\\img\\profile-pictures\\" + imageFileName);
    }

    public void logContacts(){
        File chatFile = new File("src\\main\\resources\\soeldichat\\soeldichat\\chats\\chats-info.json");
        try {
            ScFiles.logToJson(chatFile, getContactList());
        } catch (NullPointerException nullPointerException) {
            System.out.println("contactList is null");
        }
    }

    private void logMessage(Message newMessage) {
        String chatPartnerNumber = newMessage.determineChatPartnerNumber(userNumber);
        File chatFile = new File("src\\main\\resources\\soeldichat\\soeldichat\\chats\\" + chatPartnerNumber + ".json");

        try {
            ArrayList<Message> messageList = new ArrayList<>(Contact.getContactByNumber(contactList, chatPartnerNumber).getMessageList());
            ScFiles.logToJson(chatFile, messageList);
        } catch (NullPointerException nullPointerException) {
            System.out.println("message logging failed");
        }
    }

    public String saveImage(String imageUrl) {
        if(imageUrl.isEmpty()){return "";}
        String currentDateTime = java.time.LocalDateTime.now().toString();
        String newImageUrl = "src\\main\\resources\\soeldichat\\soeldichat\\img\\" + focusedContactNumber + "\\" + new File(currentDateTime.replace(':', '-')) + ScFiles.getFileExtension(new File(imageUrl));
        return "file:" + ScFiles.saveImage(Path.of(imageUrl.substring(5)),Path.of(newImageUrl));
    }
}


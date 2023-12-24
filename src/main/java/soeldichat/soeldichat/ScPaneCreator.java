package soeldichat.soeldichat;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class ScPaneCreator {

    private ScPaneCreator(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static HBox createContactHBox(Contact contact, SoeldiChatApplication application) {
        //add Container for contact
        HBox contactHBox = new HBox();
        contactHBox.getStyleClass().add("contactHBox");

        //add ImageView for profile picture
        ImageView imageView = createContactProfilePictureImageView(contact, application);

        contactHBox.getChildren().add(imageView);

        //add VBox for name and lastMessageText
        VBox nameStatusVbox = new VBox();
        HBox.setHgrow(nameStatusVbox, Priority.ALWAYS);
        nameStatusVbox.getStyleClass().add("contactVBox");
        contactHBox.getChildren().add(nameStatusVbox);

        //add Label for name
        Label name = new Label(contact.getFirstName() + " " + contact.getLastName());
        name.getStyleClass().add("contactName");
        nameStatusVbox.getChildren().add(name);

        //add label for lastMessageText and LastMessageTimeStamp
        Label lastMessageText = new Label(contact.getStatus());
        Label lastMessageTimeStampLabel = new Label();

        try {
            //try to fetch data
            lastMessageText.setText(contact.getMessageList().getLast().getText());
            lastMessageTimeStampLabel.setText(contact.getMessageList().getLast().getTimeStamp().substring(11, 16));
        } catch (Exception ignored) {
        }

        lastMessageText.getStyleClass().add("contactStatus");
        lastMessageTimeStampLabel.getStyleClass().add("contactLastMessageTimeStampLabel");
        nameStatusVbox.getChildren().add(lastMessageText);
        contactHBox.getChildren().add(lastMessageTimeStampLabel);

        return contactHBox;
    }

    public static HBox createMessageHBox(Message message, boolean alignRight, SoeldiChatApplication application) {
        HBox messageWrapper = new HBox();
        VBox messageVBox = new VBox();
        messageVBox.getStyleClass().add("messageVBox");
        messageWrapper.getStyleClass().add("messageWrapper");

        //add image if image property is not empty
        if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()) {
            Image image = new Image(message.getImageUrl());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
            Rectangle clip = new Rectangle(200 * (image.getWidth() / image.getHeight()), imageView.getFitHeight());
            imageView.setClip(clip);
            clip.setArcHeight(20);
            clip.setArcWidth(20);

            imageView.setOnMouseClicked(x -> createPopup(new Image(message.getImageUrl()), application.getStage()));
            messageVBox.getChildren().add(imageView);
        }

        //add timestamp
        Label timeStampLabel = new Label(message.getTimeStamp().substring(11, 16));
        timeStampLabel.getStyleClass().add("timeStamp");

        //add text
        Label textLabel = new Label(message.getText());
        textLabel.setWrapText(true);

        if (alignRight) {
            if (!message.getText().isEmpty()) {
                textLabel.getStyleClass().add("sentMessages");
                messageVBox.getChildren().add(textLabel);
            }

            messageWrapper.setAlignment(Pos.CENTER_RIGHT);
            messageVBox.setAlignment(Pos.TOP_RIGHT);
            messageWrapper.getChildren().add(messageVBox);
            messageWrapper.getChildren().add(timeStampLabel);
            messageWrapper.setSpacing(10.0);
        } else {
            if (!message.getText().isEmpty()) {
                textLabel.getStyleClass().add("recievedMessages");
                messageVBox.getChildren().add(textLabel);
            }

            messageWrapper.setAlignment(Pos.CENTER_LEFT);
            messageVBox.setAlignment(Pos.TOP_LEFT);
            messageWrapper.getChildren().add(timeStampLabel);
            messageWrapper.getChildren().add(messageVBox);
            messageWrapper.setSpacing(4.0);
        }


        return messageWrapper;
    }

    public static ImageView createContactProfilePictureImageView(Contact contact, SoeldiChatApplication application) {
        Circle circle = new Circle(25);
        circle.setCenterX(25);
        circle.setCenterY(25);
        ImageView imageView = new ImageView();
        imageView.setClip(circle);
        if (contact.getProfilePicture().isEmpty()) {
            //default profile picture
            imageView.setImage(application.loadImage(application.getDefaultProfilePictureString()));
        } else {
            try {
                imageView.setImage(application.loadImage(contact.getProfilePicture()));
            } catch (Exception exception) {
                //default profile picture
                imageView.setImage(application.loadImage(application.getDefaultProfilePictureString()));
            }
        }

        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        imageView.getStyleClass().add("contactProfilePicture");

        return imageView;
    }

    private static void createPopup(Image image, Stage stage) {
        Popup popup = new Popup();

        StackPane windowStackPane = new StackPane();
        windowStackPane.setMinHeight(stage.getHeight() -30);
        windowStackPane.setMinWidth(stage.getWidth() -15);
        windowStackPane.setMaxHeight(stage.getHeight() -30);
        windowStackPane.setMaxWidth(stage.getWidth() -15);
        windowStackPane.getStyleClass().add("popupWrapperStackpane");

        StackPane stackPane = new StackPane();

        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(stage.getHeight()*.85);
        imageView.setFitWidth(stage.getWidth()*.85);
        imageView.setPreserveRatio(true);

        stackPane.getChildren().add(imageView);
        stackPane.getStyleClass().add("popupStackpane");
        windowStackPane.getChildren().add(stackPane);

        windowStackPane.setOnMouseClicked(x->popup.hide());
        popup.getContent().add(windowStackPane);
        popup.show(stage, stage.getX(), stage.getY() + 55);
    }

    public static StackPane createDayTagStackpane(String lastMessageTimeStamp) {
        StackPane dayStackPane = new StackPane(new Label(lastMessageTimeStamp));
        dayStackPane.getStyleClass().add("dayTagStackPane");
        return  dayStackPane;
    }
}

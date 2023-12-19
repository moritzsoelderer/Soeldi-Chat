package soeldichat.soeldichat;

import java.time.LocalDateTime;

public class Message {
    private String sender;
    private String receiver;
    private String text;
    private String image = "";
    private String timeStamp;

    public Message(String sender, String receiver, String text, String image, String timeStamp){
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.image = image;
        this.timeStamp = timeStamp;
    }

    public Message(String text, String timeStamp) {
        this.text = text;
        this.timeStamp = timeStamp;
    }


    public String getSender() {
        return this.sender;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public String getText() {
        return this.text;
    }

    public String getImage() {
        return this.image;
    }

    public String getTimeStamp() {return timeStamp;}

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

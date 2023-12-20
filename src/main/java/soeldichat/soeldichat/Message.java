package soeldichat.soeldichat;

public class Message {
    private String sender;
    private String receiver;
    private String text;
    private String imageUrl;
    private String timeStamp;

    public Message(String sender, String receiver, String text, String imageUrl, String timeStamp){
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.imageUrl = imageUrl;
        this.timeStamp = timeStamp;
    }


    public Message(String text, String currentDateTime, String imageUrl) {
        this.text = text;
        this.timeStamp = currentDateTime;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return this.imageUrl;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

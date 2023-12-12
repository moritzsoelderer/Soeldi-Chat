package soeldichat.soeldichat;

public class Message {
    private String sender;
    private String receiver;
    private String text;
    private String image;

    public Message(String sender, String receiver, String text, String image){
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.image = image;
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

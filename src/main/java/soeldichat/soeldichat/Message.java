package soeldichat.soeldichat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    public String determineChatPartnerNumber(String userNumber) {
        if (this.getSender().equals(userNumber)) {
            return this.getReceiver();
        }
        return this.getSender();
    }


}

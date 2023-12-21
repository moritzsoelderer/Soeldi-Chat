package soeldichat.soeldichat;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class Contact {
    private String number;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String status;

    private List<Message> messageList;

    public static Contact getContactByNumber(List<Contact> contactList, String number){

        for(Contact currentContact : contactList){
            if(currentContact.getNumber().equals(number)){
                return currentContact;
            }
        }
        return null;
    }

    public Contact(String number, List<Message> messageList) {
        this.number = number;
        this.messageList = messageList;
    }

    public Contact(String number, List<Message> messageList, String firstName) {
        this.number = number;
        this.firstName = firstName;
        this.messageList = messageList;
    }

    public Contact(String number, List<Message> messageList, String firstName, String lastName) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.messageList = messageList;
    }

    public Contact(String number, List<Message> messageList, String firstName, String lastName, String profilePicture) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.messageList = messageList;
    }

    public Contact(String number, List<Message> messageList, String firstName, String lastName, String profilePicture, String status) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.status = status;
        this.messageList = messageList;
    }

}



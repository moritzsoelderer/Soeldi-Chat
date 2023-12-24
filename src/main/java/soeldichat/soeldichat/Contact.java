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
        return contactList.stream().filter(x->x.getNumber().equals(number)).toList().getFirst();
    }

    public static List<Contact> filterContactListByName(List<Contact> contactList, String subString){
        return contactList.stream().filter(contact -> {
            boolean firstNameStartsWithSubstring = subString.length() <= contact.getFirstName().length() ? contact.getFirstName().substring(0,subString.length()).toLowerCase().equals(subString.toLowerCase()) : false;
            boolean lastNameStartsWithSubstring = subString.length() <= contact.getLastName().length() ? contact.getLastName().substring(0,subString.length()).toLowerCase().equals(subString.toLowerCase()) : false;
            return (firstNameStartsWithSubstring || lastNameStartsWithSubstring);
        }).toList();
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



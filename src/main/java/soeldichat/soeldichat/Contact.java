package soeldichat.soeldichat;

import java.util.List;


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

    public String getNumber() {
        return this.number;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public String getStatus() {
        return this.status;
    }

    public List<Message> getMessageList() {
        return this.messageList;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}



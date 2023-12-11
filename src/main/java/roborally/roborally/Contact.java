package roborally.roborally;

public class Contact {
    private String number;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String status;

    private String chat;

    public Contact(String number, String chat){
        this.number = number;
        this.chat = chat;
    }

    public Contact(String number, String chat, String firstName){
        this.number = number;
        this.firstName = firstName;
        this.chat = chat;
    }

    public Contact(String number, String chat, String firstName, String lastName){
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.chat = chat;
    }

    public Contact(String number, String chat, String firstName, String lastName, String profilePicture){
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.chat = chat;
    }

    public Contact(String number, String chat, String firstName, String lastName, String profilePicture, String status){
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.status = status;
        this.chat = chat;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNumber() {
        return number;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getStatus() {
        return status;
    }

    public String getChat() {
        return chat;
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
}

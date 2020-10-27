package model;

//Contains first and last name information of the player, who is the main character
public class Player {

    String firstName;
    String lastName;
    String fullName;
    String userName;

    //EFFECTS: constructs a player with default name "Sakura Sato"
    public Player() {
        firstName = "Sakura";
        lastName = "Sato";
        fullName = firstName + " " + lastName;
        userName = "not_sakura";
    }


    //MODIFIES: this
    //EFFECTS: changes first name of player
    public void changePlayerFirstName(String newFirstName) {
        firstName = newFirstName;
        fullName = firstName + " " + lastName;
    }

    // MODIFIES: this
    // EFFECTS: changes last name of player
    public void changePlayerLastName(String newLastName) {
        lastName = newLastName;
        fullName = firstName + " " + lastName;
    }

    // MODIFIES: this
    // EFFECTS: changes username of player
    public void changePlayerUserName(String newUserName) {
        userName = newUserName;
    }

    //EFFECTS: returns first name
    public String getFirstName() {
        return firstName;
    }

    //EFFECTS: returns last name
    public String getLastName() {
        return lastName;
    }

    //EFFECTS: returns full name
    public String getFullName() {
        return fullName;
    }

    //EFFECTS: returns username
    public String getUserName() {
        return userName;
    }

}

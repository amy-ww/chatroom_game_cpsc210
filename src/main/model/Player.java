package model;

//Contains first and last name information of the player, who is the main character
// in the game
public class Player {

    String firstName;
    String lastName;
    String fullName;

    //EFFECTS: constructs a player with default name "Sakura Sato"
    public Player() {
        firstName = "Sakura";
        lastName = "Sato";
        fullName = firstName + " " + lastName;
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

}

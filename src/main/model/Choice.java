package model;
//Represents a choice that the player can select during the game
public class Choice {

    String text;

    //EFFECTS: constructs a blank choice that is an empty string
    public Choice() {
        text = "";
    }

    //MODIFIES: this
    //EFFECTS: changes the choice content
    public void changeChoice(String newText) {
        text = newText;
    }

    //EFFECTS: return choice text
    public String returnChoice() {
        return text;
    }

}

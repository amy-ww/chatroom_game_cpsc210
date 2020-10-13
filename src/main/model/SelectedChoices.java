package model;

import java.util.ArrayList;

//Represents the collection of responses that the player selects
public class SelectedChoices {

    public ArrayList<Choice> choices;

    //EFFECTS: constructs an empty choices list
    public SelectedChoices() {
        choices = new ArrayList<>();
    }

    //MODIFIES: this
    //EFFECTS: adds a choice to the list
    public void addSelectedChoice(Choice c) {
        choices.add(c);
    }

    //EFFECTS: returns the size of the list
    public int sizeSelectedChoices() {
        return choices.size();
    }

    //EFFECTS: returns the choice at the given index
    public Choice retrieveSelectedChoice(int index) {
        return choices.get(index);
    }

}

package model;

import java.util.ArrayList;

//Represents the collection of correct responses to get the good ending; there is a method that produces the number of
// correct responses that the player selected
public class CorrectChoices {

    public ArrayList<Choice> choicesKey;

    //EFFECTS: constructs an empty list of correct choices
    public CorrectChoices() {
        choicesKey = new ArrayList<>();
    }

    //MODIFIES: this
    //EFFECTS: adds a correct choice to the list
    public void addCorrectChoice(Choice c) {
        choicesKey.add(c);
    }

    //EFFECTS: returns the size of the list
    public int sizeCorrectChoices() {
        return choicesKey.size();
    }

    //REQUIRES: non-empty list
    //EFFECTS: returns the choice at the given index
    public Choice retrieveCorrectChoice(int index) {
        return choicesKey.get(index);
    }

    //EFFECTS: returns the number of correct choices chosen by player
    public int correctChoiceChecker(SelectedChoices playerAnswers) {
        int numOfCorrectChoices = 0;
        for (int choiceNum = 0; choiceNum < playerAnswers.sizeSelectedChoices(); choiceNum++) {
            if ((playerAnswers.retrieveSelectedChoice(choiceNum)).returnChoice()
                    .equals((choicesKey.get(choiceNum)).returnChoice())) {
                numOfCorrectChoices = numOfCorrectChoices + 1;
            }
        }
        return numOfCorrectChoices;
    }

}


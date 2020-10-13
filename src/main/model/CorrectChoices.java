package model;

import java.util.ArrayList;

//Represents the collection of correct responses to get the best ending and produces the number of
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

    //EFFECTS: returns the choice at the given index
    public Choice retrieveCorrectChoice(int index) {
        return choicesKey.get(index);
    }

    //REQUIRES: size of correct choices = size of selected choices
    //MODIFIES: nothing
    //EFFECTS: records the number of correct choices chosen by player, if first choice is correct
    public int correctChoiceChecker(SelectedChoices playerAnswers) {
        int numOfCorrectChoices;
        if ((playerAnswers.retrieveSelectedChoice(0)).returnChoice()
                == (choicesKey.get(0)).returnChoice()) {
            numOfCorrectChoices = 1;
            for (int choiceNum = 1; choiceNum < choicesKey.size(); choiceNum++) {
                if ((playerAnswers.retrieveSelectedChoice(choiceNum)).returnChoice()
                        == (choicesKey.get(choiceNum)).returnChoice()) {
                    numOfCorrectChoices = numOfCorrectChoices + 1;
                }
            }
        } else {
            return 0;
        }
        return numOfCorrectChoices;
    }

}

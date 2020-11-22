package ui;

import model.Dialogue;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

//toJson and dialogueToJson code is modelled from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

//Represents a list that contains game text dialogue
//Used mainly for saving purposes (checkpoint game text is saved to a gametext object)
public class GameText implements Writable {

    ArrayList<Dialogue> conversation;

    //EFFECTS: Constructs GameText as an empty array list
    public GameText() {
        conversation = new ArrayList<>();
    }

    //REQUIRES: a string input
    //MODIFIES: this, Game
    //EFFECTS: creates a new dialogue w/ string s, adds it to the gametext object, and prints it out in Game
    public void printSaveLine(String s) {
        Dialogue d = new Dialogue(s);
        addDialogue(d);
        System.out.println(s);
    }

    //MODIFIES: this
    //EFFECTS: creates a new dialogue w/ string s and adds it the gametext object
    public void saveLine(String s) {
        Dialogue d = new Dialogue(s);
        addDialogue(d);
    }

    //MODIFIES: this
    //EFFECTS: adds a dialogue to the gametext object
    public void addDialogue(Dialogue dialogue) {
        conversation.add(dialogue);
    }

    //MODIFIES: this
    //EFFECTS: sets a dialogue to the gametext object at indicated index
    public void setDialogueWithIndex(Dialogue dialogue, int index) {
        conversation.set(index, dialogue);
    }

    //EFFECTS: returns the dialogue object at the given index
    public Dialogue returnDialogue(int index) {
        return conversation.get(index);
    }

    //EFFECTS: returns the index of an string
    public int returnIndex(String s) {
        int i = 0;
        for (Dialogue d : conversation) {
            if (d.getDialogueString().equals(s)) {
                i = conversation.indexOf(d);
            }
        }
        return i;
    }

    //EFFECTS: returns the string of the dialogue object at the given index
    public String returnDialogueString(int index) {
        return conversation.get(index).getDialogueString();
    }

    //EFFECTS: returns the string of the dialogue object in last position
    public String returnLast() {
        return conversation.get(conversation.size() - 1).getDialogueString();
    }

    //EFFECTS: returns the size of the gametext object
    public int gameTextSize() {
        return conversation.size();
    }

    //toJson method from Writeable
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("dialogue", dialogueToJson());
        return json;
    }

    // EFFECTS: returns things in this gametext as a JSON array
    private JSONArray dialogueToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Dialogue d : conversation) {
            jsonArray.put(d.toJson());
        }

        return jsonArray;
    }
}

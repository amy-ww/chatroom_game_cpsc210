package model;

import org.json.JSONObject;
import persistence.Writable;

//toJson method code is modelled from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

//Represents general dialogue strings that will occur in the game
public class Dialogue implements Writable {

    String dialogue;

    //EFFECTS: constructs a dialogue object that is the string s
    public Dialogue(String s) {
        dialogue = s;
    }

    //EFFECTS: returns the string of the dialogue object
    public String getDialogueString() {
        return dialogue;
    }

    //toJson method from Writeable
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("dialogue", dialogue);
        return json;
    }
}

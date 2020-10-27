package persistence;

import model.Dialogue;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Code is modelled from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

public class JsonTest {

    protected void checkDialogue(String s, Dialogue d) {
        d = new Dialogue(s);
        assertEquals(s, d.getDialogueString());
    }
}

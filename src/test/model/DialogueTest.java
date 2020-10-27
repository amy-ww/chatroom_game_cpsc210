package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DialogueTest {

    @Test
    //checks: constructor of dialogue
    public void dialogueConstructorTest() {
        Dialogue d = new Dialogue("Hi");
        assertEquals("Hi", d.getDialogueString());
    }
}

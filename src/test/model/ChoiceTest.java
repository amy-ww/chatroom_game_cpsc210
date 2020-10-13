package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChoiceTest {

    @Test
    //checks: constructs choice that is an empty string
    public void testConstructedChoiceIsEmptyString() {
        Choice choice = new Choice();
        assertEquals("", choice.text);
    }

    @Test
    //checks: text of choice is changed correctly using changeChoice
    public void testChoiceTextChangedCorrectly() {
        Choice c = new Choice();
        c.changeChoice("Apologize to Takahiro.");
        assertEquals("Apologize to Takahiro.", c.text);
    }

    @Test
    //checks: returns text of choice
    public void testGetChoice() {
        Choice c = new Choice();
        c.changeChoice("Hi!");
        assertEquals("Hi!", c.returnChoice());
    }


}

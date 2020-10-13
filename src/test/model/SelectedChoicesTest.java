package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SelectedChoicesTest {

    @Test
    //checks: constructs empty selected choices list
    public void testIsNewConstructedListEmpty() {
        SelectedChoices sc = new SelectedChoices();
        assertEquals(0, sc.sizeSelectedChoices());
    }

    @Test
    //checks: one choice can be added into the list
    public void testAddOneChoice() {
        SelectedChoices sc = new SelectedChoices();
        Choice choice = new Choice();
        sc.addSelectedChoice(choice);
        assertEquals(1,sc.sizeSelectedChoices());
        assertEquals("",choice.returnChoice());
        }
    }

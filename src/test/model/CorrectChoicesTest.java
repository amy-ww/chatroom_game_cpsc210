package model;

import model.exceptions.NoChoicesSelectedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CorrectChoicesTest {
    @Test
    //checks: constructs empty selected choices list
    public void testIsNewConstructedListEmpty() {
        CorrectChoices cc = new CorrectChoices();
        assertEquals(0, cc.sizeCorrectChoices());
    }

    @Test
    //checks: one choice can be inserted into the list
    public void testInsertOneChoice() {
        CorrectChoices cc = new CorrectChoices();
        Choice choice = new Choice();
        cc.addCorrectChoice(choice);
        assertEquals(1, cc.sizeCorrectChoices());
        assertEquals("", choice.returnChoice());
    }

    @Test
    //checks: correct choice is retrieved
    public void testRetrieveRightChoice() {
        CorrectChoices cc = new CorrectChoices();
        Choice choice = new Choice();
        Choice choiceTwo = new Choice();
        choiceTwo.changeChoice("Hi!");
        cc.addCorrectChoice(choice);
        cc.addCorrectChoice(choiceTwo);
        cc.addCorrectChoice(choice);
        assertEquals(choiceTwo,cc.retrieveCorrectChoice(1));
    }

    @Test
    //checks: correct number of correct choices is produced by CorrectChoiceChecker (1st choice wrong)
    //Exception should not be caught
    public void testCorrectChoiceCheckerFirstWrong() {
        CorrectChoices cc = new CorrectChoices();
        Choice choiceOne = new Choice();
        choiceOne.changeChoice("Say no.");
        Choice choiceTwo = new Choice();
        choiceTwo.changeChoice("Say yes.");
        cc.addCorrectChoice(choiceOne);
        cc.addCorrectChoice(choiceTwo);
        SelectedChoices sc = new SelectedChoices();
        sc.addSelectedChoice(choiceTwo);
        sc.addSelectedChoice(choiceOne);
        try {
            assertEquals(0,cc.correctChoiceChecker(sc));
        } catch (NoChoicesSelectedException e) {
            e.printStackTrace();
            fail("Should not have caught exception.");
        }
    }

    @Test
    //checks: correct number of correct choices is produced by CorrectChoiceChecker (all correct)
    //Exception should not be caught
    public void testCorrectChoiceCheckerAllCorrect() {
        CorrectChoices cc = new CorrectChoices();
        Choice choiceOne = new Choice();
        choiceOne.changeChoice("Say no.");
        Choice choiceTwo = new Choice();
        choiceTwo.changeChoice("Say yes.");
        cc.addCorrectChoice(choiceOne);
        cc.addCorrectChoice(choiceTwo);
        SelectedChoices sc = new SelectedChoices();
        sc.addSelectedChoice(choiceOne);
        sc.addSelectedChoice(choiceTwo);
        try {
            assertEquals(2,cc.correctChoiceChecker(sc));
        } catch (NoChoicesSelectedException e) {
            e.printStackTrace();
            fail("Should not have caught exception.");
        }
    }

    @Test
    //checks: correct number of correct choices is produced by CorrectChoiceChecker (middle choice wrong,
    // rest is right)
    //Exception should not be caught
    public void testCorrectChoiceCheckerMiddleChoiceWrongRestRight() {
        CorrectChoices cc = new CorrectChoices();
        Choice choiceOne = new Choice();
        choiceOne.changeChoice("Say no.");
        Choice choiceTwo = new Choice();
        choiceTwo.changeChoice("Say yes.");
        Choice choiceThree = new Choice();
        choiceThree.changeChoice("Push him away.");
        Choice choiceExtra = new Choice();
        choiceExtra.changeChoice("Hug him.");
        cc.addCorrectChoice(choiceOne);
        cc.addCorrectChoice(choiceTwo);
        cc.addCorrectChoice(choiceThree);
        SelectedChoices sc = new SelectedChoices();
        sc.addSelectedChoice(choiceOne);
        sc.addSelectedChoice(choiceExtra);
        sc.addSelectedChoice(choiceThree);
        try {
            assertEquals(2,cc.correctChoiceChecker(sc));
        } catch (NoChoicesSelectedException e) {
            e.printStackTrace();
            fail("Should not have caught exception.");
        }
    }

    @Test
    //checks: correct number of correct choices is produced by CorrectChoiceChecker (4 right)
    //Exception should not be caught
    public void testCorrectChoiceCheckerFourChoices() {
        CorrectChoices cc = new CorrectChoices();
        Choice choiceOne = new Choice();
        choiceOne.changeChoice("Say no.");
        Choice choiceTwo = new Choice();
        choiceTwo.changeChoice("Say yes.");
        Choice choiceThree = new Choice();
        choiceThree.changeChoice("Push him away.");
        Choice choiceExtra = new Choice();
        choiceExtra.changeChoice("Hug him.");
        cc.addCorrectChoice(choiceOne);
        cc.addCorrectChoice(choiceTwo);
        cc.addCorrectChoice(choiceThree);
        cc.addCorrectChoice(choiceExtra);
        SelectedChoices sc = new SelectedChoices();
        sc.addSelectedChoice(choiceOne);
        sc.addSelectedChoice(choiceTwo);
        sc.addSelectedChoice(choiceThree);
        sc.addSelectedChoice(choiceExtra);
        try {
            assertEquals(4,cc.correctChoiceChecker(sc));
        } catch (NoChoicesSelectedException e) {
            e.printStackTrace();
            fail("Should not have caught exception.");
        }
    }

    @Test
    //checks: the selectedChoices has a value of 0
    //Exception should be thrown
    public void testNoChoicesSelectedShouldBeThrown() {
        CorrectChoices cc = new CorrectChoices();
        Choice choiceOne = new Choice();
        choiceOne.changeChoice("Say no.");
        Choice choiceTwo = new Choice();
        choiceTwo.changeChoice("Say yes.");
        Choice choiceThree = new Choice();
        choiceThree.changeChoice("Push him away.");
        Choice choiceExtra = new Choice();
        choiceExtra.changeChoice("Hug him.");
        cc.addCorrectChoice(choiceOne);
        cc.addCorrectChoice(choiceTwo);
        cc.addCorrectChoice(choiceThree);
        cc.addCorrectChoice(choiceExtra);
        SelectedChoices sc = new SelectedChoices();
        try {
            cc.correctChoiceChecker(sc);
            fail("Exception wasn't thrown");
        } catch (NoChoicesSelectedException e) {
            //expected
            e.printStackTrace();
        }
    }


}

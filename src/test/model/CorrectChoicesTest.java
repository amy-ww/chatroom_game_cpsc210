package model;

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
        assertEquals(0,cc.correctChoiceChecker(sc));
    }

    @Test
    //checks: correct number of correct choices is produced by CorrectChoiceChecker (all correct)
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
        assertEquals(2,cc.correctChoiceChecker(sc));
    }

    @Test
    //checks: correct number of correct choices is produced by CorrectChoiceChecker (middle choice wrong,
    // rest is right)
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
        assertEquals(2,cc.correctChoiceChecker(sc));
    }

}

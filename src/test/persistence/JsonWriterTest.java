package persistence;

import model.Dialogue;
import org.junit.jupiter.api.Test;
import ui.GameText;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//Code is modelled from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

public class JsonWriterTest extends JsonTest {

    @Test
    //checks: writer with invalid file
    void testWriterInvalidFile() {
        try {
            GameText gt = new GameText();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    //checks: writer with empty game text
    void testWriterEmptyGameText() {
        try {
            GameText gt = new GameText();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyGameText.json");
            writer.open();
            writer.writeGameText(gt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyGameText.json");
            gt = reader.read();
            assertEquals(0, gt.gameTextSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    //checks: writer with general game text
    void testWriterGeneralGameText() {
        try {
            GameText gt = new GameText();
            gt.addDialogue(new Dialogue("Hi"));
            gt.addDialogue(new Dialogue("Bye"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralGameText.json");
            writer.open();
            writer.writeGameText(gt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralGameText.json");
            gt = reader.read();
            assertEquals(2, gt.gameTextSize());
            checkDialogue("Hi", gt.returnDialogue(0));
            checkDialogue("Bye", gt.returnDialogue(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
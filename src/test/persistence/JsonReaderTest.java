package persistence;

import model.Dialogue;
import org.junit.jupiter.api.Test;
import ui.GameText;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//Code is modelled from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

public class JsonReaderTest extends JsonTest {

    @Test
    //checks: tests reader with non-existent file
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            GameText gt = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    //checks: tests reader with an empty gametext file
    void testReaderEmptyGameText() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyGameText.json");
        try {
            GameText gt = reader.read();
            assertEquals(0, gt.gameTextSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    //checks: tests a general gametext file
    void testReaderGeneralGameText() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralGameText.json");
        try {
            GameText gt = reader.read();
            assertEquals(2, gt.gameTextSize());
            checkDialogue("Hi", gt.returnDialogue(0));
            checkDialogue("Bye", gt.returnDialogue(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}

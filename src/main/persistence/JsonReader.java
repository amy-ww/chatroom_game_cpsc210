package persistence;

import model.Dialogue;
import org.json.JSONArray;
import org.json.JSONObject;
import ui.GameText;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

//Code is modelled from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

//Represents a reader that reads gametext from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    //EFFECTS: reads gametext from file and returns it;
    //throws IOException if an error occurs reading data from file
    public GameText read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGameText(jsonObject);
    }

    //EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    //EFFECTS: parses gametext from JSON object and returns it
    private GameText parseGameText(JSONObject jsonObject) {
        GameText gt = new GameText();
        addDialogues(gt, jsonObject);
        return gt;
    }

    //MODIFIES: gt
    //EFFECTS: parses dialogue from JSON object and adds them to gametext
    private void addDialogues(GameText gt, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("dialogue");
        for (Object json : jsonArray) {
            JSONObject nextDialogue = (JSONObject) json;
            addDialogue(gt, nextDialogue);
        }
    }


    //MODIFIES: gt
    //EFFECTS: parses dialogue from JSON object and adds it to gametext
    private void addDialogue(GameText gt, JSONObject jsonObject) {
        String dialogue = jsonObject.getString("dialogue");
        Dialogue d = new Dialogue(dialogue);
        gt.addDialogue(d);
    }
}
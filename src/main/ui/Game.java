package ui;

import model.Choice;
import model.CorrectChoices;
import model.Player;
import model.SelectedChoices;

import java.util.Scanner;

//Contains the start menu and methods to run the game

public class Game {

    CorrectChoices yutoGoodEnd;
    String userInput;
    Player you;
    SelectedChoices yourChoices;
    Scanner scanner = new Scanner(System.in);

    //MODIFIES: yutoGoodEnd
    //EFFECTS: constructs a correct choices list, player, selected choices list, adds the correct choices to get
    // Yuto's good ending, and opens start menu
    public Game() {
        yutoGoodEnd = new CorrectChoices();
        createYutoGoodEnd();
        you = new Player();
        yourChoices = new SelectedChoices();
        run();
    }

    //MODIFIES: this
    //EFFECTS: opens start menu
    void run() {
        System.out.println("A White Day Love Story");
        System.out.println("Please select a game mode:");
        System.out.println("1. Story");
        System.out.println("2. Settings");
        System.out.println("3. View Progress");
        System.out.println("4. Exit");

        userInput = scanner.nextLine();

        switch (userInput) {
            case "1":
                changeYourName();
                yutoStory();
                break;
            case "2":
                changeVolume();
                break;
            case "3":
                viewProgress();
                break;
            case "4":
                System.out.println("You have exited.");
                break;
        }

    }

    // MODIFIES: yourChoices, this
    //EFFECTS: builds playable story of Yuto's route
    public void yutoStory() {
        branchPoint("Himari: Have you ever thought about dating Yuto?", "1. Ew, no!",
                "2. Now that you mention it...");
        if (userInput.equals("1")) {
            selectedChoiceAdder("Ew,no!", yourChoices);
            initialBadEndChecker();
        } else if (userInput.equals("2")) {
            selectedChoiceAdder("Now that you mention it...", yourChoices);
            branchPoint("Himari: Look at your face, you totally like him, " + you.getFirstName(),
                    "1. Stop it, Himari!",
                    "2. We basically grew up together, he would never like me back...");
            if (userInput.equals("1")) {
                selectedChoiceAdder("Stop it, Himari!", yourChoices);
                branchPoint("Himari: Isn't it White Day in two days? You should drop hints around him!",
                        "1. You're right!", "2. Nah, I'll just be forever alone.");
                lastBranch();
            } else if (userInput.equals("2")) {
                selectedChoiceAdder("We basically grew up together, he would never like me back...",
                        yourChoices);
                branchPoint("Himari: Isn't it White Day in two days? You should drop hints around him!",
                        "1. You're right!", "2. Nah, I'll just be forever alone.");
                lastBranch();
            }
        }
    }

    //MODIFIES: you, this
    //EFFECTS: allows the player to change their first and/or last name
    private void changeYourName() {
        System.out.println("Your default name is Sakura Sato.");
        System.out.println("Would you like to change it?");
        System.out.println("1.Yes");
        System.out.println("2. No");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            System.out.println("Please enter your first name.");
            userInput = scanner.nextLine();
            you.changePlayerFirstName(userInput);
            System.out.println("Please enter your last name.");
            userInput = scanner.nextLine();
            you.changePlayerLastName(userInput);
        }
    }

    //MODIFIES: yutoGoodEnd
    //EFFECTS: adds the correct choices list needed to reach Yuto's good end
    public void createYutoGoodEnd() {
        Choice choiceOne = new Choice();
        choiceOne.changeChoice("Now that you mention it...");
        yutoGoodEnd.addCorrectChoice(choiceOne);

        Choice choiceTwo = new Choice();
        choiceTwo.changeChoice("We basically grew up together, he would never like me back...");
        yutoGoodEnd.addCorrectChoice(choiceTwo);

        Choice choiceThree = new Choice();
        choiceThree.changeChoice("You're right!");
        yutoGoodEnd.addCorrectChoice(choiceThree);

    }

    //REQUIRES: positive integer
    //MODIFIES: this
    //EFFECTS: prints good end if enough correct choices were selected; bad end printed otherwise
    public void endingChecker(int required) {
        if (yutoGoodEnd.correctChoiceChecker(yourChoices) >= required) {
            System.out.println("GOOD ENDING");
        } else {
            System.out.println("BAD ENDING");
        }
    }

    //MODIFIES: this
    //EFFECTS: checks if first choice was incorrect; if first choice is incorrect, print bad end
    public void initialBadEndChecker() {
        if (yutoGoodEnd.correctChoiceChecker(yourChoices) == 0) {
            System.out.println("BAD ENDING");
        }
    }

    //REQUIRES: String type arguments
    //MODIFIES: this
    //EFFECTS: prints out the branch point console dialogue
    public void branchPoint(String trigger, String option1, String option2) {
        System.out.println(trigger);
        System.out.println(you.getFullName() + ":");
        System.out.println(option1);
        System.out.println(option2);
        userInput = scanner.nextLine();
    }

    //REQUIRES: String and SelectedChoices type arguments
    //MODIFIES: SelectedChoice Object (yourChoices)
    //EFFECTS: creates new choice with the selected choice text, adds it to a SelectedChoice Object
    private void selectedChoiceAdder(String chosenOption, SelectedChoices sc) {
        Choice c = new Choice();
        c.changeChoice(chosenOption);
        sc.addSelectedChoice(c);
        viewProgress();
    }

    //MODIFIES: this
    //EFFECTS: branch checker with choices from last branch point
    private void lastBranch() {
        if (userInput.equals("1")) {
            selectedChoiceAdder("You're right!", yourChoices);
            endingChecker(2);
        } else if (userInput.equals("2")) {
            selectedChoiceAdder("Nah, I'll just be forever alone.", yourChoices);
            endingChecker(2);
        }
    }

    //MODIFIES: this
    //EFFECTS: allows the player to view their progress of correct branches
    public void viewProgress() {
        System.out.println("Would you like to view your progress?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            System.out.println(yutoGoodEnd.correctChoiceChecker(yourChoices) + "/" + "3");
        }
    }

    //MODIFIES: this
    //EFFECTS: allows the player to adjust volume levels
    public void changeVolume() {
        String volume = "50";
        System.out.println("Would you like to change the volume?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            System.out.println("Your current volume is" + " " + volume + ".");
            System.out.println("Please enter your desired volume.");
            volume = scanner.nextLine();
            System.out.println("Your current volume is" + " " + volume + ".");
        }
        run();
    }
}


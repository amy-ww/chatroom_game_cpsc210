package ui;

import model.Choice;
import model.CorrectChoices;
import model.Player;
import model.SelectedChoices;

import java.util.Scanner;

public class Game {

    CorrectChoices yutoGoodEnd;
    String userInput;
    Player you;
    SelectedChoices yourChoices;
    Scanner scanner = new Scanner(System.in);
    Choice choiceOne;

    public Game() {
        yutoGoodEnd = new CorrectChoices();
        you = new Player();
        yourChoices = new SelectedChoices();
        createYutoGoodEnd();
        run();
    }

    void run() {
        System.out.println("A White Day Love Story");
        System.out.println("Please select a game mode:");
        System.out.println("1. Story");
        System.out.println("2. Settings");
        System.out.println("3. Exit");

        userInput = scanner.nextLine();

        if (userInput.equals("1")) {
            changeYourName();
            yutoStoryPartOne();
            yutoStoryPartTwo();
        } else if (userInput.equals("2")) {
            System.out.println("1.Change volume");

        } else if (userInput.equals("3")) {
            System.out.println("You have exited.");
        }

    }

    private void yutoStoryPartOne() {
        branchOnePrintOut();
        if (userInput.equals("1")) {
            Choice c = new Choice();
            c.changeChoice("Ew, no!");
            yourChoices.addSelectedChoice(c);
            badEndChecker();
        } else if (userInput.equals("2")) {
            yourChoices.addSelectedChoice(choiceOne);
            branchTwoPrintOut();
            if (userInput.equals("1")) {
                Choice d = new Choice();
                d.changeChoice("Stop it, Himari!");
                yourChoices.addSelectedChoice(d);
                branchThreePrintOut();
                if (userInput.equals("1")) {
                    Choice f = new Choice();
                    f.changeChoice("You're right!");
                    yourChoices.addSelectedChoice(f);
                    goodEndingChecker();
                }
            }
        }
    }

    private void yutoStoryPartTwo() {
        if (userInput.equals("2")) {
            Choice e = new Choice();
            e.changeChoice("Nah, I'll just be forever alone.");
            yourChoices.addSelectedChoice(e);
            goodEndingChecker();
        } else if (userInput.equals("2")) {
            Choice g = new Choice();
            g.changeChoice("We basically grew up together, he would never like me back...");
            yourChoices.addSelectedChoice(g);
            branchThreePrintOut();
            if (userInput.equals("1")) {
                Choice e = new Choice();
                e.changeChoice("You're right!");
                yourChoices.addSelectedChoice(e);
                goodEndingChecker();
            }
        } else if (userInput.equals("2")) {
            Choice e = new Choice();
            e.changeChoice("Nah, I'll just be forever alone.");
            yourChoices.addSelectedChoice(e);
            goodEndingChecker();
        }
    }

    private void changeYourName() {
        System.out.println("Please enter your first name.");
        userInput = scanner.nextLine();
        you.changePlayerFirstName(userInput);
        System.out.println("Please enter your last name.");
        userInput = scanner.nextLine();
        you.changePlayerLastName(userInput);
    }

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

    public void chat() {
        System.out.println("Welcome to chat! Who would you like to talk to?");
        System.out.println("1. Yuto");
        System.out.println("2. Himari");
    }

    public void branchOnePrintOut() {
        System.out.println("Himari: Have you ever thought about dating Yuto?");
        System.out.println(you.getFullName() + ":");
        System.out.println("1. Ew, no!");
        System.out.println("2. Now that you mention it...");
        userInput = scanner.nextLine();
    }

    public void branchTwoPrintOut() {
        System.out.println("Himari: Look at your face, you totally like him, " + you.getFirstName());
        System.out.println(you.getFullName() + ":");
        System.out.println("1. Stop it, Himari!");
        System.out.println("2. We basically grew up together, he would never like me back...");
        userInput = scanner.nextLine();
    }

    public void branchThreePrintOut() {
        System.out.println("Himari: Isn't it White Day in two days? You should drop hints around him!");
        System.out.println(you.getFullName() + ":");
        System.out.println("1. You're right!");
        System.out.println("2. Nah, I'll just be forever alone.");
        userInput = scanner.nextLine();
    }

    public void goodEndingChecker() {
        if (yutoGoodEnd.correctChoiceChecker(yourChoices) >= 2) {
            System.out.println("GOOD ENDING");
        } else {
            System.out.println("BAD ENDING");
        }
    }

    public void badEndChecker() {
        if (yutoGoodEnd.correctChoiceChecker(yourChoices) == 0) {
            System.out.println("BAD ENDING");
        }
    }
}

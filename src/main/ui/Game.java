package ui;

import model.*;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//saveGameText and loadPlayerRecord code is
//modelled from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

//Contains the start menu and route information for running the game
public class Game {

    private static final String JSON_STORE = "./data/GameChatAway.json";
    String userInput;
    Player you;
    SelectedChoices yourChoices;
    CorrectChoices goodEndChoicesKuro;
    CorrectChoices goodEndChoicesHaruki;
    CorrectChoices goodEndChoicesYuto;
    Scanner scanner = new Scanner(System.in);
    GameText playerRecord;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //MODIFIES: this
    //EFFECTS: Constructs a new game with player information and selected choices,good end choice lists for each
    //character, and runs the start menu to initiate game
    public Game() throws FileNotFoundException {
        you = new Player();
        yourChoices = new SelectedChoices();
        goodEndChoicesKuro = new CorrectChoices();
        goodEndChoicesHaruki = new CorrectChoices();
        goodEndChoicesYuto = new CorrectChoices();
        playerRecord = new GameText();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        createGoodEndChoicesKuro();
        createGoodEndChoicesHaruki();
        createGoodEndChoicesYuto();
        runMenu();
    }

    //MODIFIES: this
    //EFFECTS: displays the menu
    public void displayMenu() {
        System.out.println("Welcome to ChatAway.");
        System.out.println("Please select a menu option:");
        System.out.println("1. New Chat");
        System.out.println("2. Load Save File");
        System.out.println("3. Settings");
        System.out.println("4. Exit");
    }

    //MODIFIES: this
    //EFFECTS: runs the menu and game
    public void runMenu() {
        do {
            displayMenu();

            userInput = scanner.nextLine();

            if (userInput.equals("1")) {
                clearJson();
                saveDefault();
                changeYourName();
                changeYourUserName();
                prologueConversation();
                prologueRouteSelect();
            } else if (userInput.equals("2")) {
                continueGame();
            } else if (userInput.equals("3")) {
                changeVolume();
            }
        } while (!userInput.equals("4"));

        System.out.println("You have exited.");
        System.exit(0);
    }

    //MODIFIES: playerRecord
    //EFFECTS: saves default names into index 0, 1, 2 on playerRecord
    public void saveDefault() {
        playerRecord.addDialogue(new Dialogue("Sakura"));
        playerRecord.addDialogue(new Dialogue("Sato"));
        playerRecord.addDialogue(new Dialogue("not_sakura"));
    }

    //MODIFIES: this, you
    //EFFECTS: allows the player to change their first and/or last name
    public void changeYourName() {
        System.out.println("Your default name is Sakura Sato.");
        System.out.println("Would you like to change it?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            System.out.println("Please enter your first name.");
            userInput = scanner.nextLine();
            you.changePlayerFirstName(userInput);
            playerRecord.setDialogueWithIndex(new Dialogue(you.getFirstName()), 0);
            System.out.println("Please enter your last name.");
            userInput = scanner.nextLine();
            you.changePlayerLastName(userInput);
            playerRecord.setDialogueWithIndex(new Dialogue(you.getLastName()), 1);
        }
    }

    //MODIFIES: this, you
    //EFFECTS: allows the player to change their username
    public void changeYourUserName() {
        System.out.println("Your default username is not_sakura.");
        System.out.println("Would you like to change it?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            System.out.println("Please enter your username.");
            userInput = scanner.nextLine();
            you.changePlayerUserName(userInput);
            playerRecord.setDialogueWithIndex(new Dialogue(you.getUserName()), 2);
        }
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: allows the player to save their progress in the game and (return to the main menu)
    public void saveProgress() {
        System.out.println("ChatAway SYSTEM MESSAGE: Would you like to save?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            saveGameText();
        }
        System.out.println("Would you like to return to the main menu?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            runMenu();
        }
    }

    //MODIFIES: yourChoices
    //EFFECTS: prints out the number of correct choices chosen out of all 4 choice branches
    public void viewProgress(CorrectChoices cc) {
        System.out.println("Would you like to see how many correct choices you made?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            loadQuiet();
            if (playerRecord.gameTextSize() >= 5) {
                for (int i = 4; i < playerRecord.gameTextSize(); i++) {
                    String s = playerRecord.returnDialogueString(i).replace(you.getUserName() + ": ",
                            "");
                    Choice c = new Choice();
                    c.changeChoice(s);
                    yourChoices.addSelectedChoice(c);
                }
            }
            System.out.println(cc.correctChoiceChecker(yourChoices) + "/4");
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
        runMenu();
    }

    //EFFECTS: prints out the prologue conversation
    public void prologueConversation() {
        prologueConversation1();
        prologueConversation2();
        prologueConversation3();
        prologueConversation4();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: prints out the route selection; if a certain route is selected, that route will begin
    public void prologueRouteSelect() {
        profiles();
        System.out.println("Who is the one for you?");
        userInput = scanner.nextLine();
        switch (userInput) {
            case "1":
                System.out.println("Congratulations! You can now talk to Kuro!");
                playerRecord.addDialogue(new Dialogue("1"));
                returnSystemMessage();
                routeKuro();
                break;
            case "2":
                System.out.println("Congratulations! You can now talk to Haruki!");
                playerRecord.addDialogue(new Dialogue("2"));
                returnSystemMessage();
                routeHaruki();
                break;
            case "3":
                System.out.println("Congratulations! You can now talk to Yuto!");
                playerRecord.addDialogue(new Dialogue("3"));
                returnSystemMessage();
                routeYuto();
                break;
        }
    }

    //MODIFIES: this
    //EFFECTS: prints out profiles of the selectable characters
    public void profiles() {
        System.out.println("Profile 1");
        System.out.println("Name: Kuro Ichikawa");
        System.out.println("Username: bored_and_cold");
        System.out.println("Type: Cold and sad");
        System.out.println("Location: WestU");
        System.out.println("A special message to you: ... Whatever.");
        fillerScannerInput();
        System.out.println("Profile 2");
        System.out.println("Name: Haruki Kubo ");
        System.out.println("Username: haruharu");
        System.out.println("Type: Friendly and happy");
        System.out.println("Location: WestU");
        System.out.println("A special message to you: How was your day? :)");
        fillerScannerInput();
        System.out.println("Profile 3");
        System.out.println("Name: Yuto Watanabe");
        System.out.println("Username: best_dude");
        System.out.println("Type: Your athletic player");
        System.out.println("Location: WestU");
        System.out.println("A message to you: Did it hurt when you fell from the vending machine? "
                + "Because you look like a snacc.");
        fillerScannerInput();
    }

    //MODIFIES: this
    //EFFECTS: prints out the end-of-prologue system message
    public void returnSystemMessage() {
        System.out.println("ChatAway SYSTEM MESSAGE: You are currently on a prepaid plan of 1 chatroom courtesy of "
                + "hehehehimari. Thank you for choosing the ChatAway Rent-A-'Friend' service!");
        fillerScannerInput();
    }

    //MODIFIES: this
    //EFFECTS: prints out first part of prologue conversation
    public void prologueConversation1() {
        System.out.println("hehehehimari has entered the chatroom.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": " + "Himari? Is that you?");
        fillerScannerInput();
        System.out.println("hehehehimari: Hey " + you.getFirstName() + ", you figured it out!");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": " + "Man, this chatroom thing was so hard to set up, "
                + "it looks super sketchy too. Why can't we just text normally?");
        fillerScannerInput();
        System.out.println("hehehehimari: " + you.getFirstName() + ", ur single right? A single pringle ready"
                + " to mingle?");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Maybe... Why?");
        fillerScannerInput();
    }

    //MODIFIES: this
    //EFFECTS: prints out second part of prologue conversation
    public void prologueConversation2() {
        System.out.println("hehehehimari: As weird and outdated looking this app is, it has its charms.");
        fillerScannerInput();
        System.out.println("hehehehimari: When I mean charms, I mean people rent themselves out on here and "
                + "pretend to be your online boyfriend, girlfriend, companion, whatever. ");
        fillerScannerInput();
        System.out.println("hehehehimari: It's virtual, but also kind of real because it's not a bot.");
        fillerScannerInput();
        System.out.println("hehehehimari: They also advertise themselves with different roles they can play. "
                + "The cold type, the smooth player... Whatever you want. Oh! And guess what? ");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": ... What?");
        fillerScannerInput();
        System.out.println("hehehehimari: I bought you a chatroom session so you can experience having a someone "
                + "special to talk to!");
        fillerScannerInput();
        System.out.println("hehehehimari: I went through so many profiles, and there were 3 that came up as students "
                + "who go to our school.");
        fillerScannerInput();
    }

    //MODIFIES: this
    //EFFECTS: prints out third part of prologue conversation
    public void prologueConversation3() {
        System.out.println(you.getUserName() + ": Himari, I appreciate the gesture, but this is so weird."
                + " From our school? What if I run into them at some point? ");
        fillerScannerInput();
        System.out.println("hehehehimari: Here's another charming thing, this chatroom service requires them "
                + "to upload info, but you don't.");
        fillerScannerInput();
        System.out.println("hehehehimari: You know who they are, but all they have from you is your "
                + "username. Isn't that convenient? ");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": I still think this is kind of creepy. Can you get a refund?");
        fillerScannerInput();
        System.out.println("hehehehimari: Nope!");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": It's just one chatroom... How much was this?");
        fillerScannerInput();
        System.out.println("hehehehimari: Hmm, probably could've spent this money on better things.");
        fillerScannerInput();
        System.out.println("hehehehimari: Why don't you give it a try? Maybe you could make a virtual "
                + "boyfriend into a real one, *wink*. ");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": ... What do you mean?");
        fillerScannerInput();
        System.out.println("hehehehimari: Heh heh, well, remember how I told you that I met Aoi online?");
    }

    //MODIFIES: this
    //EFFECTS: prints out fourth part of prologue conversation
    public void prologueConversation4() {
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Oh yeah, you mentioned it before.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Omg, please don't tell me you met her on here.");
        fillerScannerInput();
        System.out.println("hehehehimari: I sure did! Anyway, I sent you the profiles. "
                + "Look ‘em over, and pick your poison ;) ");
        fillerScannerInput();
    }

    //MODIFIES: this
    //EFFECTS: initiates Kuro's route by running majorBranchKuro
    public void routeKuro() {
        System.out.println("ChatAway SYSTEM MESSAGE: This chatroom is prepaid, so your credit card information is not "
                + "required. Please be vigilant of monetary scams.");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro) has entered the chatroom.");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): What do you want?");
        fillerScannerInput();
        majorBranchKuro();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: lets the player choose between the good/neutral end route or the bad end route for Kuro
    public void majorBranchKuro() {
        System.out.println("1. Um... Can we please forget about the role play stuff and just talk normally... Please?");
        System.out.println("2. Brrrr, so cold, so mean! But I love it so much,Roro~");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": Um... Can we please forget about the role"
                    + " play stuff and just talk normally... Please?");
            saveProgress();
            goodNeutralPathKuro();
        } else if (userInput.equals("2")) {
            System.out.println(you.getUserName() + ": Brrrr, so cold, so mean! But I love it so much,Roro~");
            badPathKuro();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs all parts of Kuro's neutral/good end route
    public void goodNeutralPathKuro() {
        goodNeutralPathKuro1();
        goodNeutralPathKuro2();
        goodNeutralPathKuro3();
        goodNeutralPathKuro4();
        goodNeutralPathKuro5();
    }

    //MODIFIES: this
    //EFFECTS: runs all parts of Kuro's bad end route
    public void badPathKuro() {
        badPathKuro1();
        badPathKuro2();
        badPathKuro3();
        badPathKuro4();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: runs first part of Kuro's neutral/good end route (has a choice)
    public void goodNeutralPathKuro1() {
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): ... Alright. But didn't you pay for that?");
        fillerScannerInput();
        System.out.println("1. Aha... Well, long story short, this is a gift from a friend, "
                + "so no biggie.");
        System.out.println("2. I'm not crazy enough to pay for this, but my friend is.");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": Aha... Well, long story short, this is a gift from a "
                    + "friend, so no biggie.");
            saveProgress();
        } else if (userInput.equals("2")) {
            playerRecord.printSaveLine(you.getUserName() + ": I'm not crazy enough to pay for this, "
                    + "but my friend is.");
            saveProgress();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs second part of Kuro's neutral/good end route
    public void goodNeutralPathKuro2() {
        System.out.println("bored_and_cold (Kuro): Oh. Lol.");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): Anything you want to talk about? We're stuck here until the "
                + "chatroom expires.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": If you don't mind me asking, why this gig out of all things?");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): It's an effective way to pay for kitten-fostering costs.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Omg, you foster kittens? Aww, that's so cute!"
                + " I'd love to but my roommate's allergic.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Is the kitten in your profile pic one of them?");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro):  Oh, I actually adopted him.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": He's adorable! What's his name?");
        fillerScannerInput();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: runs third part of Kuro's neutral/good end route (has a choice)
    public void goodNeutralPathKuro3() {
        System.out.println("bored_and_cold (Kuro): Why don't you take a guess?");
        fillerScannerInput();
        System.out.println("1. Whiskers?");
        System.out.println("2. Triangle?");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": Whiskers?");
            saveProgress();
            System.out.println("bored_and_cold (Kuro): Nice try, but it's actually Triangle.");
            fillerScannerInput();
        } else if (userInput.equals("2")) {
            playerRecord.printSaveLine(you.getUserName() + ": Triangle?");
            saveProgress();
            System.out.println("bored_and_cold (Kuro): Lol, yeah, it is.");
            fillerScannerInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs fourth part of Kuro's neutral/good end route
    public void goodNeutralPathKuro4() {
        System.out.println(you.getUserName() + ": Is it because the colouring on his face looks like a triangle?");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): Finally, someone else who gets it. That's exactly why.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": I love it when pets have names that are completely random LOL.");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): Most people I've met tend to disagree.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Welp, they're no fun! I'm so tired of people naming their cats stuff "
                + "like Whiskers or Tiger. But it's usually the best bet. ");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): The pet shelter gives the kittens names like that because "
                + "people are more likely to adopt. ");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Oh, do you foster the kittens from the shelter next to WestU? ");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): Yeah, why?");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": I saw in your profile that you go to WestU. I go there too, "
                + "so it's the only shelter I could think of.");
        fillerScannerInput();
        System.out.println("ChatAway SYSTEM MESSAGE: The chatroom will be closing soon. You can now leave anytime.");
        fillerScannerInput();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: runs fifth part of Kuro's neutral/good end route (has a choice)
    public void goodNeutralPathKuro5() {
        System.out.println("bored_and_cold (Kuro): I don't mean to be creepy but, you seem like an okay person. "
                + "If you're free tomorrow, you can drop by, and I can show you some kittens.");
        fillerScannerInput();
        System.out.println("1. Yes!! That would be so nice. I can't wait :)");
        System.out.println("2. Uh, I'm busy, but thanks for the offer!");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": Yes!! That would be so nice. I can't wait :)");
            saveQuiet();
            viewProgress(goodEndChoicesKuro);
            System.out.println("bored_and_cold (Kuro): Cool! I guess I'll see you tomorrow"
                    + " then. Just ask for Kuro.");
            fillerScannerInput();
            System.out.println("ChatAway SYSTEM MESSAGE: Good End. You got yourself a potential date! "
                    + "Himari would be proud.");
        } else if (userInput.equals("2")) {
            playerRecord.printSaveLine(you.getUserName() + ": Uh, I'm busy, but thanks for the offer!");
            saveQuiet();
            viewProgress(goodEndChoicesKuro);
            System.out.println("bored_and_cold (Kuro): Ah, okay. Well, it was nice talking to you.");
            System.out.println("ChatAway SYSTEM MESSAGE: Neutral End. You had a nice chat with a stranger online.");
            fillerScannerInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs first part of Kuro's bad end route (has a choice)
    public void badPathKuro1() {
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): don't call me that.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Pretty please!");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): ... Ugh, okay, fine.");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): I don't know why I can't say no to you.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Aww, you have a soft spot for me, don't you? ");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): ... Shut up.");
        fillerScannerInput();
        System.out.println("1. Are you bluuuushing? Just admit it~");
        System.out.println("2. Aww, little bad boy still trying to play tough?");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            System.out.println(you.getUserName() + ": Are you bluuuushing? Just admit it~");
        } else if (userInput.equals("2")) {
            System.out.println(you.getUserName() + ": Aww, little bad boy still trying to play tough?");
        }
    }

    //MODIFIES: this
    //EFFECTS: runs second part of Kuro's bad end route
    public void badPathKuro2() {
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): ...");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): No, I");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): I guess I am.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": You don't have to hide from me, because I'm the only one for you.");
        fillerScannerInput();
        System.out.println("bad_and_cold (Kuro): Shh, don't let anyone else know. This will be our little secret, hm?");
        fillerScannerInput();
        System.out.println("bad_and_cold (Kuro): I need to be tough... So I can protect you.");
        fillerScannerInput();
    }

    //MODIFIES: this
    //EFFECTS: runs third part of Kuro's bad end route (has a choice)
    public void badPathKuro3() {
        System.out.println("1. Be a bad boy in the streets, and just for me, be a good boy who gets treats ;) xoxo");
        System.out.println("2. I can be the bad girl so you don't need to be tough ;)");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            System.out.println(you.getUserName() + ": Be a bad boy in the streets, and just for me, be a good boy who "
                    + "gets treats ;) xoxo");
            System.out.println("bad_and_cold (Kuro): Oh, I wonder what treats I'll get ;)");
            fillerScannerInput();
        } else if (userInput.equals("2")) {
            System.out.println(you.getUserName() + ": I can be the bad girl so you don't need to be tough ;)");
            System.out.println("bad_and_cold (Kuro): Why not bad boy and a bad girl? ;)");
            fillerScannerInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs the fourth part of Kuro's bad end route
    public void badPathKuro4() {
        System.out.println("ChatAway SYSTEM MESSAGE: The chatroom will be closing soon. You can now leave "
                + "anytime.");
        fillerScannerInput();
        System.out.println("bored_and_cold (Kuro): Oh thank God, it's finally over. Bye.");
        fillerScannerInput();
        System.out.println("ChatAWAY SYSTEM MESSAGE: Bad End, where Himari was surprised you were actually into "
                + "that.");
        fillerScannerInput();
    }

    //MODIFIES: goodEndChoicesKuro
    //EFFECTS: adds the correct choices to the Kuro's good end choice key
    public void createGoodEndChoicesKuro() {
        Choice majorBranch = new Choice();
        majorBranch.changeChoice("Um... Can we please forget about the role play stuff and just talk normally..."
                + " Please?");
        goodEndChoicesKuro.addCorrectChoice(majorBranch);
        Choice crazyFriend = new Choice();
        crazyFriend.changeChoice("Aha... Well, long story short, this is a gift from a friend, "
                + "so no biggie.");
        goodEndChoicesKuro.addCorrectChoice(crazyFriend);
        Choice catName = new Choice();
        catName.changeChoice("Triangle?");
        goodEndChoicesKuro.addCorrectChoice(catName);
        Choice visitShelter = new Choice();
        visitShelter.changeChoice("Yes!! That would be so nice. I can't wait :)");
        goodEndChoicesKuro.addCorrectChoice(visitShelter);
    }

    //MODIFIES: this
    //EFFECTS: initiates Haruki's route by running majorBranchHaruki
    public void routeHaruki() {
        System.out.println("ChatAway SYSTEM MESSAGE: This chatroom is prepaid, so your credit card information is not "
                + "required. Please be vigilant of monetary scams.");
        fillerScannerInput();
        System.out.println("haruharu (Haruki) has entered the chatroom.");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): Hello! How was your day? :) Have you eaten yet?");
        fillerScannerInput();
        majorBranchHaruki();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: allows player to choose Haruki's neutral/good end route or bad end route
    public void majorBranchHaruki() {
        System.out.println("1. I'm doing well! ... Uhh, you're not going to do weird role play stuff right?");
        System.out.println("2. Ugh, my day was awful. I'm sorry, but I really need a place to vent.");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": I'm doing well! ... Uhh, you're not going to do weird "
                    + "role play stuff right?");
            saveProgress();
            goodNeutralPathHaruki();
        } else if (userInput.equals("2")) {
            System.out.println(you.getUserName() + ": Ugh, my day was awful. I'm sorry, but "
                    + "I really need a place to vent.");
            badPathHaruki();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs Haruki's neutral/good end route
    public void goodNeutralPathHaruki() {
        goodNeutralPathHaruki1();
        goodNeutralPathHaruki2();
        goodNeutralPathHaruki3();
        goodNeutralPathHaruki4();
        goodNeutralPathHaruki5();
    }

    //MODIFIES: this
    //EFFECTS: runs Haruki's bad end route
    public void badPathHaruki() {
        badPathHaruki1();
        badPathHaruki2();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: runs first part of Haruki's neutral/good end route (has a choice)
    public void goodNeutralPathHaruki1() {
        fillerScannerInput();
        System.out.println("haruharu (Haruki): Haha, I can do that if that's what you like. I don't judge. :)");
        fillerScannerInput();
        System.out.println("1. LOL nononono, I'm not into that. Um, anyways, how was your day?");
        System.out.println("2. That's what all they say but ur probably judging me. It's okay lmao. Let's just not"
                + " talk about this... So, how are you doing?");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": LOL nononono, I'm not into that. "
                    + "Um, anyways, how was your day?");
            saveProgress();
        } else if (userInput.equals("2")) {
            playerRecord.printSaveLine(you.getUserName() + ": That's what all they say but ur probably "
                    + "judging me. It's okay lmao. Let's just not talk about this... So, how are you doing?");
            saveProgress();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs second part of Haruki's neutral/good end route
    public void goodNeutralPathHaruki2() {
        fillerScannerInput();
        System.out.println("haruharu (Haruki): To be honest, not too great. :(");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): Ah, don't worry about me, this is your chatroom! "
                + "What do you want to talk about?");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": It's okay, I'm down to listen to what's bothering you.");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): My problems aren't as bad what a lot of other people are going "
                + "through, so I don't want to sound complain-y or, I don't know, pretentious?");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Just because other people have it worse doesn't mean your "
                + "feelings are invalid!");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": ... Oh god, I didn't mean to pressure you. If ur not comfortable"
                + " you don't have to say anything.");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): This is probably very annoying but lately... It's been getting hard for "
                + "me to be happy.  I'm just... tired.");
        fillerScannerInput();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: runs third part of Haruki's neutral/good end route (has a choice)
    public void goodNeutralPathHaruki3() {
        System.out.println("1. Everyone's tired once in a while... I'm tired too. What's making you tired?");
        System.out.println("2. Tired from what? :(");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": Everyone's tired once in a while... I'm tired too. "
                    + "What's making you tired?");
            saveProgress();
            fillerScannerInput();
        } else if (userInput.equals("2")) {
            playerRecord.printSaveLine(you.getUserName() + ": Tired from what? :(");
            saveProgress();
            fillerScannerInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs fourth part of Haruki's neutral/good end route
    public void goodNeutralPathHaruki4() {
        System.out.println("haruharu (Haruki): I'm not sure if I like my major "
                + "anymore, but if I switch, I feel like everyone would be disappointed in me.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": I know it's easier said than done, but if it's making you unhappy, "
                + "you should probably do something about it. ");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): It was really hard to get into this program at WestU. "
                + "My parents were so happy when I did, and I don't want to let them down. ");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Oh, is it the LMLA program?");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): Yeah, how did you know?");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": I go to WestU too. Some of my friends are in LMLA, but they realized "
                + "it's not what they thought it was, so they're switching out.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": It's pretty scary to switch programs, but I was happier after too.");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): You've also switched programs too...");
        fillerScannerInput();
        System.out.println("ChatAway SYSTEM MESSAGE: The chatroom will be closing soon. You can now leave anytime.");
        fillerScannerInput();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: runs fifth part of Haruki's neutral/good end route (has a choice)
    public void goodNeutralPathHaruki5() {
        System.out.println("haruharu (Haruki): Hey, not to be weird but... I've talked about this only to you."
                + " Do you think we could grab coffee tomorrow and talk a bit more? My treat :D");
        fillerScannerInput();
        System.out.println("1. Of course :) Does 3pm at the campus Moonbucks work for you?");
        System.out.println("2. Sorry :( I wish I could, but I got stuff going on tomorrow.");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": Of course :) Does 3pm at the campus "
                    + "Moonbucks work for you?");
            saveQuiet();
            viewProgress(goodEndChoicesHaruki);
            System.out.println("haruharu (Haruki): Works for me :) See you then! I'll be wearing a blue hoodie.");
            fillerScannerInput();
            System.out.println("ChatAway SYSTEM MESSAGE: Good End. You got yourself a potential date! "
                    + "Himari would be proud.");
        } else if (userInput.equals("2")) {
            playerRecord.printSaveLine(you.getUserName() + ": I wish I could, but I got stuff going on tomorrow :(");
            saveQuiet();
            viewProgress(goodEndChoicesHaruki);
            System.out.println("haruharu (Haruki): It's okay! Thanks for hearing me out anyway :)");
            System.out.println("ChatAway SYSTEM MESSAGE: Neutral End. You had a nice chat with a stranger online.");
            fillerScannerInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs first part of Haruki's bad end route (has a choice)
    public void badPathHaruki1() {
        fillerScannerInput();
        System.out.println("haruharu (Haruki): Oh, it's no problem! I'm here to listen :)");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": I think I just failed my midterm today, and "
                + "I don't really know what to do now. I really liked the class too.");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): Oh no! I'm a student too, so I can relate. But it's only a midterm, "
                + "so there's still time!");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": I know there's still time, but I just feel like if I can't do well"
                + " now, I won't be able to do well later. ");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): If you don't mind, I can share some of my experience with you, "
                + "and hopefully it'll make you feel better :)");
        fillerScannerInput();
        System.out.println("1. Not to be rude, but I'm not really interested in hearing it...");
        System.out.println("2. Sorry, but can you let me finish?");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            System.out.println(you.getUserName() + ": Not to be rude, but I'm not really interested in hearing it...");
        } else if (userInput.equals("2")) {
            System.out.println(you.getUserName() + ": Sorry, but can you let me finish?");
        }
    }

    //MODIFIES: this
    //EFFECTS: runs second part of Haruki's bad end route
    public void badPathHaruki2() {
        fillerScannerInput();
        System.out.println("haruharu (Haruki): Ahhh, I'm sorry for interrupting :(");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": It's okay. I shouldn't be lashing out at you...");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": You know what? Just forget it. Hopefully this chatroom ends soon.");
        fillerScannerInput();
        System.out.println("ChatAway SYSTEM MESSAGE: The chatroom will be closing soon. You can now leave anytime.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Finally, it's done. I'm going to leave now, bye...");
        fillerScannerInput();
        System.out.println("haruharu (Haruki): ... Sorry, I wasn't cheer you up...");
        fillerScannerInput();
        System.out.println("ChatAWAY SYSTEM MESSAGE: Bad End, where you made a ball of sunshine feel bad.");
    }

    //MODIFIES: goodEndChoicesHaruki
    //EFFECTS: adds correct choices to Haruki's good end choices key
    public void createGoodEndChoicesHaruki() {
        Choice majorBranch = new Choice();
        majorBranch.changeChoice("I'm doing well! ... Uhh, you're not going to do weird role play stuff right?");
        goodEndChoicesHaruki.addCorrectChoice(majorBranch);
        Choice notIntoThat = new Choice();
        notIntoThat.changeChoice("LOL nononono, I'm not into that. Um, anyways, how was your day?");
        goodEndChoicesHaruki.addCorrectChoice(notIntoThat);
        Choice tired = new Choice();
        tired.changeChoice("Tired from what? :(");
        goodEndChoicesHaruki.addCorrectChoice(tired);
        Choice grabCoffee = new Choice();
        grabCoffee.changeChoice("Of course :) Does 3pm at the campus Moonbucks work for you?");
        goodEndChoicesHaruki.addCorrectChoice(grabCoffee);
    }

    //MODIFIES: this
    //EFFECTS: initiates Yuto's route by running majorBranchYuto
    public void routeYuto() {
        System.out.println("ChatAway SYSTEM MESSAGE: This chatroom is prepaid, so your credit card information is not "
                + "required. Please be vigilant of monetary scams.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto) has entered the chatroom.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): Well, here I am. What are your other two wishes?");
        fillerScannerInput();
        majorBranchYuto();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: allows the player to choose between Yuto's neutral/good end route or bad end route
    public void majorBranchYuto() {
        System.out.println("1. Sorry dude, but I'm not the biggest fan of pick-up lines. That was pretty good though.");
        System.out.println("2. My other wish? Well, I wish I could rearrange the alphabet so I could put U and "
                + "I together ;) ");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": Sorry dude, but I'm not the biggest fan of "
                    + "pick-up lines. That was pretty good though.");
            saveProgress();
            goodNeutralPathYuto();
        } else if (userInput.equals("2")) {
            System.out.println(you.getUserName() + ": My other wish? Well, I wish I could rearrange the "
                    + "alphabet so I could put U and I together ;)");
            badPathYuto();
            fillerScannerInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs Yuto's neutral/good end route
    public void goodNeutralPathYuto() {
        goodNeutralPathYuto1();
        goodNeutralPathYuto2();
        goodNeutralPathYuto3();
        goodNeutralPathYuto4();
        goodNeutralPathYuto5();
    }

    //MODIFIES: this
    //EFFECTS: runs Yuto's bad end route
    public void badPathYuto() {
        badPathYuto1();
        badPathYuto2();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: runs first part of Yuto's neutral/good end route (has a choice)
    public void goodNeutralPathYuto1() {
        System.out.println("best_dude (Yuto): Damn, so I guess you didn't like the one I put in my profile eh?");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Ahaha, that one was punny, so it was pretty good.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): How do you want me to entertain you? Since "
                + "flirting doesn't seem to work lmao.");
        fillerScannerInput();
        System.out.println("1. I think you could just try to relax and be yourself? We're all strangers here anyway.");
        System.out.println("2. Is this flirtation thing a front you put up?");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": I think you could just try to relax and be yourself? "
                    + "We're all strangers here anyway.");
            saveProgress();
        } else if (userInput.equals("2")) {
            playerRecord.printSaveLine(you.getUserName() + ": Is this flirtation thing a front you put up?");
            saveProgress();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs second part of Yuto's neutral/good end route
    public void goodNeutralPathYuto2() {
        System.out.println("best_dude (Yuto): Hah... I could try to be myself...");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): No, you're just going to be like the others, and think I'm boring.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Whoa whoa whoa, don't lump me together with everyone else! I "
                + "don't even know you yet, bro.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): ... Sorry, I got a bit angsty there.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Hey, it's no problem. We all have some painful memories lingering "
                + "around.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": I guess we can just talk about hobbies. What do you like to do"
                + " in you free time?");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): I like to play basketball, but you probably guessed that.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": ... Well, you do have a basketball in your profile picture.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): Oh lmao, I forget about that. Yeah, that's fair.");
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: runs third part of Yuto's neutral/good end route (has a choice)
    public void goodNeutralPathYuto3() {
        fillerScannerInput();
        System.out.println("best_dude (Yuto): What else do you think I do in my free time?");
        fillerScannerInput();
        System.out.println("1. Hang out with your buddies?");
        System.out.println("2. Might be a long shot but, do you read Shakespeare?");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": Hang out with your buddies?");
            saveProgress();
            fillerScannerInput();
            System.out.println("Yeah, I definitely do that But I also read Shakespeare.");
            fillerScannerInput();
        } else if (userInput.equals("2")) {
            playerRecord.printSaveLine(you.getUserName() + ": Might be a long shot but, do you read Shakespeare?");
            saveProgress();
            fillerScannerInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs fourth part of Yuto's neutral/good end route
    public void goodNeutralPathYuto4() {
        System.out.println(you.getUserName() + ": Well, there is a certain finesse you have with your words. LOL, "
                + "I don't mean to make fun of you, it's all in good jest.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): Hey, that's a compliment. Shakespeare had some pretty good pick-up lines"
                + " if you didn't know.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): Did my heart love till now? forswear it, sight!"
                + " For I ne'er saw true beauty till this night.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Is that... Romeo and Juliet?");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): IT ISSS");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Eyyy, I still got my WestU English Lit knowledge yo.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): ... I'm impressed! You don't think it's weird that I like Shakespeare?");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Of course not! I think it's nice that you're so excited about it :)");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): Thanks, that really means a lot to me. Seriously.");
        fillerScannerInput();
        System.out.println("ChatAway SYSTEM MESSAGE: The chatroom will be closing soon. You can now leave anytime.");
        fillerScannerInput();
    }

    //MODIFIES: this, playerRecord
    //EFFECTS: runs fifth part of Yuto's neutral/good end route (has a choice)
    public void goodNeutralPathYuto5() {
        System.out.println("best_dude (Yuto): I, uh, have a basketball tomorrow at the WestU gym. I know, we're just "
                + "2 random strangers online but, would you like to come watch me play?");
        fillerScannerInput();
        System.out.println("1. I would love to! I'll be your cheerleader :)");
        System.out.println("2. I really wish I could, but I'm busy tomorrow. Break a leg!");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            playerRecord.printSaveLine(you.getUserName() + ": I would love to! I'll be your cheerleader :)");
            saveQuiet();
            viewProgress(goodEndChoicesYuto);
            System.out.println("best_dude (Yuto): Hell yeah! See you tomorrow then, my Juliet~ ;)");
            fillerScannerInput();
            System.out.println("ChatAway SYSTEM MESSAGE: Good End. You got yourself a potential date! "
                    + "Himari would be proud.");
        } else if (userInput.equals("2")) {
            playerRecord.printSaveLine(you.getUserName() + ": I really wish I could, but I'm busy tomorrow. Break a "
                    + "leg!");
            saveQuiet();
            viewProgress(goodEndChoicesYuto);
            System.out.println("best_dude (Yuto): Lmao thanks. Hopefully we'll talk again someday.");
            System.out.println("ChatAway SYSTEM MESSAGE: Neutral End. You had a nice chat with a stranger online.");
            fillerScannerInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: runs first part of Yuto's bad end route (has a choice)
    public void badPathYuto1() {
        fillerScannerInput();
        System.out.println("best_dude (Yuto): Ooo, I've met my match it seems. Bring it on!");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": If you were a Transformer, you'd be Optimus Fine.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): Baby, if you were words on a page, you'd be fine print.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Oh, I was wondering if you had extra heart.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): Did yours just get stolen?");
        fillerScannerInput();
        System.out.println("1. Not only do I need a heart from you, but I also need a band-aid.");
        System.out.println("2. If stealing hearts were a crime, you'd be guilty as charged ;)");
        userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            System.out.println(you.getUserName() + ": Not only do I need a heart from you, "
                    + "but I also need a band-aid.");
            fillerScannerInput();
            System.out.println(you.getUserName() + ": Because I just scraped my knee, falling for you <3");
        } else if (userInput.equals("2")) {
            System.out.println(you.getUserName() + ": If stealing hearts were a crime, you'd be guilty as charged ;)");
        }
    }

    //MODIFIES: this
    //EFFECTS: runs second part of Yuto's bad end route
    public void badPathYuto2() {
        fillerScannerInput();
        System.out.println("best_dude (Yuto): Well well, on the topics of hearts and injuries, do you know CPR?");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": I sure do, because I just took your breath away.");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): I'm no mathematician, but I'm pretty good with numbers. "
                + "Tell you what, give me yours and watch what I can do with it.");
        fillerScannerInput();
        System.out.println("ChatAway SYSTEM MESSAGE: The chatroom will be closing soon. You can now leave anytime.");
        fillerScannerInput();
        System.out.println(you.getUserName() + ": Ahaha, that was a lot of fun! Thanks dude!");
        fillerScannerInput();
        System.out.println("best_dude (Yuto): No problems, man!");
        fillerScannerInput();
        System.out.println("ChatAWAY SYSTEM MESSAGE: Bad End, where you had a pick-up line battle with someone, and "
                + "only that.");

    }

    //MODIFIES: goodEndChoicesYuto
    //EFFECTS: adds correct choices to Yuto's good end choice key
    public void createGoodEndChoicesYuto() {
        Choice majorBranch = new Choice();
        majorBranch.changeChoice("Sorry dude, but I'm not the biggest fan of pick-up lines. "
                + "That was pretty good though.");
        goodEndChoicesYuto.addCorrectChoice(majorBranch);
        Choice beYourself = new Choice();
        beYourself.changeChoice("I think you could just try to relax and be yourself? "
                + "We're all strangers here anyway.");
        goodEndChoicesYuto.addCorrectChoice(beYourself);
        Choice shakespeare = new Choice();
        shakespeare.changeChoice("Might be a long shot but, do you read Shakespeare?");
        goodEndChoicesYuto.addCorrectChoice(shakespeare);
        Choice basketballGame = new Choice();
        basketballGame.changeChoice("I would love to! I'll be your cheerleader :)");
        goodEndChoicesYuto.addCorrectChoice(basketballGame);
    }

    //EFFECTS: scans next line for any input
    public void fillerScannerInput() {
        userInput = scanner.nextLine();
        userInput = " ";
    }

    // EFFECTS: saves the gametext to file
    private void saveGameText() {
        try {
            jsonWriter.open();
            jsonWriter.writeGameText(playerRecord);
            jsonWriter.close();
            System.out.println("Saved " + you.getFullName() + " to " + JSON_STORE);

        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: saves the gametext to file without system output message
    private void saveQuiet() {
        try {
            jsonWriter.open();
            jsonWriter.writeGameText(playerRecord);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: loads gametext from file
    private void loadPlayerRecord() throws JSONException {
        try {
            playerRecord = jsonReader.read();
            you.changePlayerFirstName(playerRecord.returnDialogueString(0));
            you.changePlayerLastName(playerRecord.returnDialogueString(1));
            you.changePlayerUserName(playerRecord.returnDialogueString(2));
            System.out.println("Loaded " + you.getFullName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        } catch (JSONException e) {
            System.out.println("Load failed. Please start new chat.");
            runMenu();
        }
    }

    //MODIFIES: this
    //EFFECTS: loads gametext from file without system output message
    private void loadQuiet() {
        try {
            playerRecord = jsonReader.read();
            you.changePlayerFirstName(playerRecord.returnDialogueString(0));
            you.changePlayerLastName(playerRecord.returnDialogueString(1));
            you.changePlayerUserName(playerRecord.returnDialogueString(2));
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: clears the json file for a new chat
    private void clearJson() {
        try {
            jsonWriter.open();
            playerRecord = new GameText();
            jsonWriter.writeGameText(playerRecord);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: loads gametext file and continues game based on file information
    private void continueGame() {
        loadPlayerRecord();
        if (playerRecord.returnDialogueString(3).equals("1")) {
            continueGameKuro();
        } else if (playerRecord.returnDialogueString(3).equals("2")) {
            continueGameHaruki();
        } else if (playerRecord.returnDialogueString(3).equals("3")) {
            continueGameYuto();
        }
    }

    //MODIFIES: this
    //EFFECTS: loads gametext file and continues Kuro's route based on file information
    private void continueGameKuro() {
        if (playerRecord.returnLast().equals(you.getUserName() + ": Um... Can we please forget about the role"
                + " play stuff and just talk normally... Please?")) {
            goodNeutralPathKuro();
        } else if (playerRecord.returnLast().equals(you.getUserName() + ": Aha... Well, long story short, this is a "
                + "gift from a friend, so no biggie.")
                || playerRecord.returnLast().equals(you.getUserName() + ": I'm not crazy enough to pay for this, "
                + "but my friend is.")) {
            goodNeutralPathKuro2();
            goodNeutralPathKuro3();
            goodNeutralPathKuro4();
            goodNeutralPathKuro5();
        } else if (playerRecord.returnLast().equals(you.getUserName() + ": Whiskers?")) {
            System.out.println("bored_and_cold (Kuro): Nice try, but it's actually Triangle.");
            goodNeutralPathKuro4();
            goodNeutralPathKuro5();
        } else if (playerRecord.returnLast().equals(you.getUserName() + ": Triangle?")) {
            System.out.println("Bored_and_cold (Kuro): Lol, yeah, it is.");
            goodNeutralPathKuro4();
            goodNeutralPathKuro5();
        }
    }

    //MODIFIES: this
    //EFFECTS: loads gametext file and continues Haruki's route based on file information
    private void continueGameHaruki() {
        if (playerRecord.returnLast().equals(you.getUserName() + ": I'm doing well! ... Uhh, you're not going to do "
                + "weird role play stuff right?")) {
            goodNeutralPathHaruki();
        } else if (playerRecord.returnLast().equals(you.getUserName() + ": LOL nononono, I'm not into that. Um,"
                + " anyways, how was your day?") || playerRecord.returnLast().equals(you.getUserName() + ": "
                + "That's what all they say but ur probably judging me. It's okay lmao. "
                + "Let's just not talk about this... So, how are you doing?")) {
            goodNeutralPathHaruki2();
            goodNeutralPathHaruki3();
            goodNeutralPathHaruki4();
            goodNeutralPathHaruki5();
        } else if (playerRecord.returnLast().equals(you.getUserName() + ": Everyone's tired once in a while... "
                + "I'm tired too. What's making you tired?") || playerRecord.returnLast().equals(you.getUserName()
                + ": Tired from what? :(")) {
            goodNeutralPathHaruki4();
            goodNeutralPathHaruki5();
        }
    }

    //MODIFIES: this
    //EFFECTS: loads gametext file and continues Yuto's route based on file information
    private void continueGameYuto() {
        if (playerRecord.returnLast().equals(you.getUserName() + ": Sorry dude, but I'm not the biggest fan of pick-up"
                + " lines. That was pretty good though.")) {
            goodNeutralPathYuto();
        } else if (playerRecord.returnLast().equals(you.getUserName() + ": I think you could just try to relax and be"
                + " yourself? We're all strangers here anyway.")
                || playerRecord.returnLast().equals(you.getUserName() + ": Is this flirtation thing a front you "
                + "put up?")) {
            goodNeutralPathYuto2();
            goodNeutralPathYuto3();
            goodNeutralPathYuto4();
            goodNeutralPathYuto5();
        } else if (playerRecord.returnLast().equals(you.getUserName() + ": Hang out with your buddies?")) {
            System.out.println("best_dude (Yuto): Yeah, I definitely do that. But I also read Shakespeare.");
            goodNeutralPathYuto4();
            goodNeutralPathYuto5();
        } else if (playerRecord.returnLast().equals(you.getUserName() + ": Might be a long shot but, do you read "
                + "Shakespeare?")) {
            goodNeutralPathYuto4();
            goodNeutralPathYuto5();
        }
    }

}



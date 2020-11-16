package ui;

import model.Choice;
import model.CorrectChoices;
import model.Player;
import model.SelectedChoices;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//References:
//https://lunalucid.itch.io/free-creative-commons-bgm-collection for the background music used in playBGM

//https://freesound.org/people/Sirkoto51/sounds/449547/ for the sound effect used in playSoundEffect

//https://stackoverflow.com/questions/6379061/how-to-auto-scroll-to-bottom-in-java-swing used to model some code to keep
//scroll bar at the bottom (e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()))

//https://www.codota.com/code/java/methods/javax.sound.sampled.AudioSystem/getClip used to model some code to play audio
//(playBGM and playSoundEffect)

//https://stackoverflow.com/questions/14301618/can-java-sound-be-used-to-control-the-system-volume used to model some
//code for volume change listener

//https://stackoverflow.com/questions/4048006/java-swing-how-to-remove-an-anonymous-actionlistener-from-a-component used
//to model some code for removeAllActionListeners

//Contains the graphical user interface
public class GUI {

    public static int WIDTH = 1000;
    public static int HEIGHT = 700;
    public JFrame frame;
    CardLayout card;
    File music;
    FloatControl volumeCtrl;
    GameText playerRecord;
    SelectedChoices yourChoices;
    Player you;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private static final String JSON_STORE = "./data/GameChatAway.json";
    JList<String> choiceArea;
    JList<String> textArea;
    DefaultListModel<String> strings = new DefaultListModel<>();
    DefaultListModel<String> strings2 = new DefaultListModel<>();
    JButton send;
    CorrectChoices goodEndChoicesKuro;
    CorrectChoices goodEndChoicesHaruki;
    private final CorrectChoices goodEndChoicesYuto;

    //EFFECTS: constructs a new GUI; initializes the game
    public GUI() {
        frame = new JFrame("ChatAway");
        music = new File("src/main/ui/sound/Moar BGM (online-audio-converter.com).wav");
        you = new Player();
        playerRecord = new GameText();
        yourChoices = new SelectedChoices();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        goodEndChoicesKuro = new CorrectChoices();
        goodEndChoicesHaruki = new CorrectChoices();
        goodEndChoicesYuto = new CorrectChoices();
        start();
    }

    //MODIFIES: frame, textArea, choiceArea, music, goodEndChoicesKuro, goodEndChoicesHaruki, goodEndChoicesYuto
    //EFFECTS: contains the methods for setting up the JFrame, JLists, background music, CorrectChoices for each route,
    //displays the menu
    public void start() {
        setUpFrame();
        setUpTextAndChoiceArea();
        playBGM();
        createGoodEndChoicesKuro();
        createGoodEndChoicesHaruki();
        createGoodEndChoicesYuto();
        displayMenu();
        frame.setVisible(true);
    }

    //MODIFIES: frame
    //EFFECTS: sets up JFrame with the preferred size and applies a card layout
    public void setUpFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        card = new CardLayout();
        frame.getContentPane().setLayout(card);
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.pack();
    }

    //MODIFIES: textArea, choiceArea
    //EFFECTS: sets up main text area and choice display area
    public void setUpTextAndChoiceArea() {
        textArea = new JList<>(strings);
        textArea.setFont(new Font("Sans Serif", Font.PLAIN, 15));
        choiceArea = new JList<>(strings2);
        choiceArea.setFont(new Font("Sans Serif", Font.PLAIN, 15));
        choiceArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        choiceArea.setLayoutOrientation(JList.VERTICAL);
    }

    //MODIFIES: music
    //EFFECTS: plays background music clip on a continuous loop, sets up music for volume control
    public void playBGM() {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(music);
            clip.open(ais);
            volumeCtrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    //MODIFIES: this
    //EFFECTS: plays a sound effect
    public void playSoundEffect() {
        try {
            File music = new File("src/main/ui/sound/449547__sirkoto51__notification-chime.wav");
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(music);
            clip.open(ais);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    //MODIFIES: frame
    //EFFECTS: displays the start menu of the game
    public void displayMenu() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.pink);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(panel);
        Icon icon = new ImageIcon("src/main/ui/images/chataway.gif");
        Icon icon2 = new ImageIcon("src/main/ui/images/rentafriend.gif");
        JLabel chatawayIcon = new JLabel(icon);
        chatawayIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel rentAFriendIcon = new JLabel(icon2);
        rentAFriendIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(chatawayIcon);
        panel.add(Box.createVerticalStrut(20));
        panel.add(rentAFriendIcon);
        Icon icon3 = new ImageIcon("src/main/ui/images/logo.png");
        JLabel logoIcon = new JLabel(icon3);
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        displayMenuButtons(panel);
        panel.add(Box.createVerticalStrut(40));
        panel.add(logoIcon);
    }

    //EFFECTS: displays the menu buttons
    public void displayMenuButtons(JPanel panel) {
        JButton newChat = getNewChatButton();
        JButton load = getLoadButton();
        JButton settings = getSettingsButton();
        panel.add(Box.createVerticalStrut(50));
        panel.add(newChat);
        panel.add(Box.createVerticalStrut(30));
        panel.add(load);
        panel.add(Box.createVerticalStrut(30));
        panel.add(settings);
        newChat.setAlignmentX(Component.CENTER_ALIGNMENT);
        load.setAlignmentX(Component.CENTER_ALIGNMENT);
        settings.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    //EFFECTS: creates the load button
    public JButton getLoadButton() {
        Icon icon2 = new ImageIcon("src/main/ui/images/load.gif");
        JButton load = new JButton(icon2);
        load.setBackground(Color.white);
        load.addActionListener(e -> continueGame());
        return load;
    }

    //MODIFIES: send, frame
    //EFFECTS: when a file is loaded, used to decide what point in the game should the player be loaded in
    public void continueGame() {
        send = setUpChatRoom();
        load();
        switch (playerRecord.returnDialogueString(3)) {
            case "0":
                progress(goodEndChoicesKuro);
                continueGameKuro();
                break;
            case "1":
                progress(goodEndChoicesHaruki);
                continueGameHaruki();
                break;
            case "2":
                progress(goodEndChoicesYuto);
                continueGameYuto();
                break;
        }
        card.last(frame.getContentPane());
    }

    //MODIFIES: strings2, yourChoices
    //EFFECTS: continues game if the player is on Kuro's route
    public void continueGameKuro() {
        if (playerRecord.returnLast().equals("Um... Can we please forget about the role"
                + " play stuff and just talk normally... Please?")) {
            choicePaneAdd("Um... Can we please forget about the role"
                    + " play stuff and just talk normally... Please?");
            kuroGoodChoice1();
        } else if (playerRecord.gameTextSize() == 6) {
            choicePaneAdd(playerRecord.returnDialogueString(4));
            choicePaneAdd(playerRecord.returnDialogueString(5));
            kuroGoodChoice2();
        } else if (playerRecord.gameTextSize() == 7) {
            choicePaneAdd(playerRecord.returnDialogueString(4));
            choicePaneAdd(playerRecord.returnDialogueString(5));
            choicePaneAdd(playerRecord.returnDialogueString(6));
            kuroGoodChoice4();
        }
    }

    //MODIFIES: strings2, yourChoices
    //EFFECTS: continues game if the player is on Haruki's route
    public void continueGameHaruki() {
        if (playerRecord.returnLast().equals("I'm doing well! ... Uhh, you're not going to do weird role play "
                + "stuff right?")) {
            choicePaneAdd("I'm doing well! ... Uhh, you're not going to do weird role play stuff right?");
            harukiGoodChoice1();
        } else if (playerRecord.gameTextSize() == 6) {
            choicePaneAdd(playerRecord.returnDialogueString(4));
            choicePaneAdd(playerRecord.returnDialogueString(5));
            harukiGoodChoice2();
        } else if (playerRecord.gameTextSize() == 7) {
            choicePaneAdd(playerRecord.returnDialogueString(4));
            choicePaneAdd(playerRecord.returnDialogueString(5));
            choicePaneAdd(playerRecord.returnDialogueString(6));
            harukiGoodChoice4();
        }
    }

    //MODIFIES: strings2, yourChoices
    //EFFECTS: continues game if the player is on Yuto's route
    public void continueGameYuto() {
        if (playerRecord.returnLast().equals("Sorry dude, but I'm not the biggest fan of pick-up lines. That was "
                + "pretty good though.")) {
            choicePaneAdd("Sorry dude, but I'm not the biggest fan of pick-up lines. That was "
                    + "pretty good though.");
            yutoGoodChoice1();
        } else if (playerRecord.gameTextSize() == 6) {
            choicePaneAdd(playerRecord.returnDialogueString(4));
            choicePaneAdd(playerRecord.returnDialogueString(5));
            yutoGoodChoice2();
        } else if (playerRecord.gameTextSize() == 7) {
            choicePaneAdd(playerRecord.returnDialogueString(4));
            choicePaneAdd(playerRecord.returnDialogueString(5));
            choicePaneAdd(playerRecord.returnDialogueString(6));
            yutoGoodChoice4();
        }
    }

    //MODIFIES: strings, strings2, playerRecord, you
    //EFFECTS: creates the newChat button, displays name changing JOptionPanes, clears GameText for a new game
    public JButton getNewChatButton() {
        Icon icon = new ImageIcon("src/main/ui/images/newchat.gif");
        JButton newChat = new JButton(icon);
        newChat.setBackground(Color.white);
        newChat.addActionListener(e -> {
            clearJson();
            strings.removeAllElements();
            strings2.removeAllElements();
            String firstName = JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(frame),
                    "Please enter your first name.", "Sakura");
            you.changePlayerFirstName(firstName);
            playerRecord.saveLine(firstName);
            String lastName = JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(frame),
                    "Please enter your last name.", "Sato");
            you.changePlayerLastName(lastName);
            playerRecord.saveLine(lastName);
            String userName = JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(frame),
                    "Please enter your username.", "not_sakura");
            you.changePlayerUserName(userName);
            playerRecord.saveLine(userName);
            prologue();
        });
        return newChat;
    }

    //MODIFIES: volumeCtrl
    //EFFECTS: creates settings button, leads to slider for changing volume
    public JButton getSettingsButton() {
        Icon icon3 = new ImageIcon("src/main/ui/images/settings.gif");
        JButton settings = new JButton(icon3);
        settings.setBackground(Color.white);
        settings.addActionListener(e -> {
            JSlider volume = new JSlider(
                    (int) volumeCtrl.getMinimum() + 100,
                    (int) volumeCtrl.getMaximum() + 100,
                    (int) volumeCtrl.getValue() + 100);
            volume.setMajorTickSpacing(10);
            volume.setPaintLabels(true);
            volume.setPaintTicks(true);
            JOptionPane changeVolume = new JOptionPane();
            volumeChangeListener(volume, changeVolume);
            changeVolume.setMessage(new Object[]{"Adjust to your desired volume:", volume});
            changeVolume.setOptionType(JOptionPane.OK_CANCEL_OPTION);
            JDialog dialog = changeVolume.createDialog(JOptionPane.getFrameForComponent(frame),
                    "Volume");
            dialog.setVisible(true);
        });
        return settings;
    }

    //MODIFIES: volumeCtrl
    //EFFECTS: creates the change listener needed to control volume
    public void volumeChangeListener(JSlider volume, JOptionPane changeVolume) {
        volume.addChangeListener(e1 -> {
            JSlider source = (JSlider) e1.getSource();
            if (!source.getValueIsAdjusting()) {
                changeVolume.setInputValue(source.getValue());
                volumeCtrl.setValue((float) source.getValue() - 100);
            }
            if (source.getValueIsAdjusting()) {
                changeVolume.setInputValue(source.getValue());
                volumeCtrl.setValue((float) source.getValue() - 100);
            }
        });
    }

    //MODIFIES: strings, send
    //EFFECTS: creates the initial prologue needed for route selection
    public void prologue() {
        send = setUpChatRoom();
        save(); // save player name information
        send.addActionListener(e -> {
            if (strings.isEmpty()) {
                strings.add(0, prologueList().get(0));
            } else if (!strings.lastElement().contains("poison")) {
                strings.addElement(prologueList().get(strings.size()));
            } else {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                        "hehehehimari has left the chatroom. Your free chatroom must be used now. "
                                + "Please read the following profiles and choose carefully.",
                        "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                routeSelect();
            }
        });
        card.last(frame.getContentPane());
    }

    //MODIFIES: playerRecord
    //EFFECTS: sets up route select part of prologue
    public void routeSelect() {
        ImageIcon profiles = new ImageIcon("src/main/ui/images/profiles.jpg");
        String[] options = {"Kuro", "Haruki", "Yuto"};
        int x = JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(frame), "Who is the one for you?",
                "ChatAway SYSTEM MESSAGE",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, profiles, options, options[0]);
        playerRecord.saveLine(Integer.toString(x));
        save(); //save player's choice
        if (x == 0) {
            progress(goodEndChoicesKuro);
            kuroRoute();
        } else if (x == 1) {
            progress(goodEndChoicesHaruki);
            harukiRoute();
        } else if (x == 2) {
            progress(goodEndChoicesYuto);
            yutoRoute();
        }
    }

    //MODIFIES: send, strings, frame, playerRecord
    //EFFECTS: creates Kuro's route
    public void kuroRoute() {
        removeAllActionListeners(send);
        strings.removeAllElements();
        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                "This chatroom is prepaid, so your credit card information is not required. Please be vigilant"
                        + " of monetary scams.",
                "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        send.addActionListener(e -> {
            if (strings.isEmpty()) {
                strings.add(0, "bored_and_cold has entered the chatroom.");
            } else {
                strings.addElement(createKuroText("What do you want?"));
                int x = choicePane("1. Um... Can we please forget about the role play stuff and just talk normally... "
                        + "Please?" + "\n" + "2. Brrrr, so cold, so mean! But I love it so much, Roro~");
                kuroRouteActionListener(x);
            }
        });
    }

    //MODIFIES: strings, strings2, playerRecord, frame
    //EFFECTS: creates following part of the action listener in kuroRoute
    public void kuroRouteActionListener(int x) {
        if (x == 0) {
            addSaveLine("Um... Can we please forget about the role play stuff and just talk "
                    + "normally... Please?");
            choicePaneAdd("Um... Can we please forget about the role play stuff and just talk "
                    + "normally... Please?");
            int y = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(frame),
                    "Would you like to save?", "Save", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                save();
                kuroGoodChoice1();
            } else if (y == 1) {
                kuroGoodChoice1();
            }
        } else if (x == 1) {
            playerRecord.saveLine("Brrrr, so cold, so mean! But I love it so much, Roro~");
            kuroBadChoice1();
        }
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates first part of Kuro's good end route
    public void kuroGoodChoice1() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            strings.addElement(createKuroText("... Alright. But didn't you pay for that?"));
            int x = choicePane("1. Aha... Well, long story short, this is a gift from a friend, so no biggie."
                    + "\n" + "2. I'm not crazy enough to pay for this, but my friend is.");
            if (x == 0) {
                addSaveLine("Aha... Well, long story short, this is a gift from a friend, so no biggie.");
                choicePaneAdd("Aha... Well, long story short, this is a gift from a friend, so no biggie.");
            } else if (x == 1) {
                addSaveLine("I'm not crazy enough to pay for this, but my friend is.");
                choicePaneAdd("I'm not crazy enough to pay for this, but my friend is.");
            }
            int y = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(frame),
                    "Would you like to save?", "ChatAway SYSTEM MESSAGE", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                save();
                kuroGoodChoice2();
            } else if (y == 1) {
                kuroGoodChoice2();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates second part of Kuro's good end route
    public void kuroGoodChoice2() {
        strings.removeAllElements();
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        list.add(createKuroText("Oh. Lol."));
        list.add(createKuroText("Anything you want to talk about? We're stuck here until the chatroom expires."));
        list.add(createPlayerText("If you don't mind me asking, why this gig out of all things?"));
        list.add(createKuroText("It's an good way to pay for kitten-fostering costs."));
        list.add(createPlayerText("Omg, you foster kittens? That's so cute! I'd love to but my roommate's allergic."));
        list.add(createPlayerText("Is the kitten in your profile pic one of them?"));
        list.add(createKuroText("Oh, I actually adopted him."));
        list.add(createPlayerText("He's adorable! What's his name?"));
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                kuroGoodChoice3();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates third part of Kuro's good end route
    public void kuroGoodChoice3() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            strings.addElement(createKuroText("Why don't you take a guess?"));
            int x = choicePane("1. Whiskers?" + "\n" + "2. Triangle?");
            if (x == 0) {
                playerRecord.saveLine("Whiskers?");
                strings.addElement(createPlayerText("Whiskers?"));
                choicePaneAdd("Whiskers?");
            } else if (x == 1) {
                playerRecord.saveLine("Triangle?");
                strings.addElement(createPlayerText("Triangle?"));
                choicePaneAdd("Triangle?");
            }
            int y = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(frame),
                    "Would you like to save?", "Save", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                save();
            }
            kuroGoodChoice4();
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates fourth part of Kuro's good end route
    public void kuroGoodChoice4() {
        strings.removeAllElements();
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        kuroGoodChoice4List(list);
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                        "The chatroom will be closing soon. You can now leave anytime.",
                        "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                kuroGoodChoice5();
            }
        });
    }

    //EFFECTS: creates list of text dialogue needed for kuroGoodChoice4
    public void kuroGoodChoice4List(ArrayList<String> list) {
        list.add(createPlayerText("Is it because the colouring on his face looks like a triangle?"));
        list.add(createKuroText("Finally, someone else who gets it. That's exactly why."));
        list.add(createPlayerText("I love it when pets have names that are completely random LOL."));
        list.add(createKuroText("Most people I've met tend to disagree."));
        list.add(createPlayerText("Welp, they're no fun!"));
        list.add(createPlayerText("I'm so tired of people naming their cats stuff like Whiskers. "
                + "But it's usually the best bet."));
        list.add(createKuroText("The pet shelter gives cats names like that because people are more likely to adopt."));
        list.add(createPlayerText("Oh, do you foster the kittens from the shelter next to WestU?"));
        list.add(createKuroText("Yeah, why?"));
        list.add(createPlayerText("I go to WestU, so it was the only pet shelter I could think of."));
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates fifth part of Kuro's good end route
    public void kuroGoodChoice5() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            strings.addElement(createKuroText("I don't mean to be creepy but, you seem okay."
                    + " If you're free tomorrow, I can show you some kittens."));
            int x = choicePane("1. Yes!! That would be so nice. I can't wait :)" + "\n"
                    + "2. Sadly, I'm busy, but thanks for the offer!");
            kuroGoodChoice5ActionListener(x);
        });
    }

    //MODIFIES: strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates following part of action listener for kuroGoodChoice5
    public void kuroGoodChoice5ActionListener(int x) {
        if (x == 0) {
            addSaveLine("Yes!! That would be so nice. I can't wait :)");
            choicePaneAdd("Yes!! That would be so nice. I can't wait :)");
            strings.addElement(createKuroText("Cool! I guess I'll see you tomorrow then. Just hit up "
                    + "the shelter and ask for Kuro."));
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Good End. You got yourself a potential date! Himari would be proud." + "\n"
                            + "You will now be exited.",
                    "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);

        } else if (x == 1) {
            addSaveLine("Sadly, I'm busy, but thanks for the offer!");
            choicePaneAdd("Sadly, I'm busy, but thanks for the offer!");
            strings.addElement(createKuroText("Ah, okay. Well, it was nice talking to you."));
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Neutral End. You had a nice chat with a stranger online."
                            + "\n" + "You will now exited.",
                    "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates first part of Kuro's bad end route
    public void kuroBadChoice1() {
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        strings2.addElement("YOU ARE ON THE BAD END ROUTE.");
        list.add(createPlayerText("Brrrr, so cold, so mean! But I love it so much, Roro~"));
        list.add(createKuroText("Don't call me that."));
        list.add(createPlayerText("Pretty please!"));
        list.add(createKuroText("Ugh... Okay, fine."));
        list.add(createKuroText("I don't know why I can't say no to you."));
        list.add(createPlayerText("Aww, you have a soft spot for me, don't you?"));
        list.add(createKuroText("... Shut up."));
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                kuroBadChoice2();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates second part of Kuro's bad end route
    public void kuroBadChoice2() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            int x = choicePane("1. Are you bluuuushing? Just admit it~" + "\n"
                    + "2. Aww, little bad boy still trying to play tough?");
            if (x == 0) {
                strings.addElement(createPlayerText("Are you bluuuushing? Just admit it~"));
            } else if (x == 1) {
                strings.addElement(createPlayerText("Aww, little bad boy still trying to play tough?"));
            }
            kuroBadChoice3();
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates third part of Kuro's bad end route
    public void kuroBadChoice3() {
        strings.removeAllElements();
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        list.add(createKuroText("..."));
        list.add(createKuroText("No, I"));
        list.add(createKuroText("I guess I am."));
        list.add(createPlayerText("You don't have to hide from me, because I'm the only one for you."));
        list.add(createKuroText("Shh, don't let anyone else know. This will be our little secret, hm?"));
        list.add(createKuroText("I need to be tough... So I can protect you."));
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                kuroBadChoice4();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates fourth part of Kuro's bad end route
    public void kuroBadChoice4() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            int x = choicePane("1. Be a bad boy in the streets, and just for me, be a good boy who gets treats ;) xoxo"
                    + "\n" + "2. I can be the bad girl so you don't need to be tough ;)");
            if (x == 0) {
                strings.addElement(createPlayerText("Be a bad boy in the streets, and just for me, "
                        + "be a good boy who gets treats ;) xoxo"));
                kuroBadChoice5();
            } else if (x == 1) {
                strings.addElement(createPlayerText("I can be the bad girl so you don't need to be tough ;)"));
                kuroBadChoice5();
            }
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "The chatroom will be closing soon. You can now leave anytime.",
                    "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates fifth part of Kuro's bad end route
    public void kuroBadChoice5() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            strings.addElement(createKuroText("Oh thank God, it's finally over. Bye."));
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Bad End, where Himari was surprised you were actually into that."
                            + "\n" + "You will now be exited.");
            System.exit(0);
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates Haruki's route
    public void harukiRoute() {
        removeAllActionListeners(send);
        strings.removeAllElements();
        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                "This chatroom is prepaid, so your credit card information is not required. Please be vigilant"
                        + " of monetary scams.",
                "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        send.addActionListener(e -> {
            if (strings.isEmpty()) {
                strings.add(0, "haruharu has entered the chatroom.");
            } else {
                strings.addElement(createHarukiText("Hello! How was your day? :) Have you eaten yet?"));
                int x = choicePane("1. I'm doing well! ... Uhh, you're not going to do weird role play stuff right?"
                        + "\n" + "2. Ugh, my day was awful. I'm sorry, but I really need a place to vent.");
                harukiRouteActionListener(x);
            }
        });
    }

    //MODIFIES: strings, strings2, playerRecord, frame, yourChoices
    //EFFECTS: creates following part of action listener for harukiRoute
    public void harukiRouteActionListener(int x) {
        if (x == 0) {
            addSaveLine("I'm doing well! ... Uhh, you're not going to do weird role play stuff right?");
            choicePaneAdd("I'm doing well! ... Uhh, you're not going to do weird role play stuff right?");
            int y = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(frame),
                    "Would you like to save?",
                    "ChatAway SYSTEM MESSAGE", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                save();
                harukiGoodChoice1();
            } else if (y == 1) {
                harukiGoodChoice1();
            }
        } else if (x == 1) {
            addSaveLine("Ugh, my day was awful. I'm sorry, but I really need a place to vent.");
            harukiBadChoice1();
        }
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates first part of Haruki's good end route
    public void harukiGoodChoice1() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            strings.addElement(createHarukiText("Haha, I can do that if that's what you like. I don't judge. :)"));
            int x = choicePane("1. LOL nononono, I'm not into that. Um, anyways, how was your day?"
                    + "\n" + "2. Let's just not talk about this... So, how are you doing?");
            if (x == 0) {
                playerRecord.saveLine("LOL nononono, I'm not into that. Um, anyways, how was your day?");
                strings.addElement(createPlayerText("LOL nononono, I'm not into that. Um, anyways, how was your day?"));
                choicePaneAdd("LOL nononono, I'm not into that. Um, anyways, how was your day?");
            } else if (x == 1) {
                playerRecord.saveLine("Let's just not talk about this... So, how are you doing?");
                strings.addElement(createPlayerText("Let's just not talk about this... So, how are you doing?"));
                choicePaneAdd("Let's just not talk about this... So, how are you doing?");
            }
            int y = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(frame),
                    "Would you like to save?", "ChatAway SYSTEM MESSAGE", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                save();
            }
            harukiGoodChoice2();
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates second part of Haruki's good end route
    public void harukiGoodChoice2() {
        strings.removeAllElements();
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        list.add(createHarukiText("To be honest, not too great. :("));
        list.add(createHarukiText("Ah, don't worry about me, this is your chatroom! What do you want to talk about?"));
        list.add(createPlayerText("It's okay, I'm down to listen to what's bothering you."));
        list.add(createHarukiText("My problems aren't as bad what a lot of other people are going through."));
        list.add(createHarukiText("I don't want to sound complain-y or, I don't know, pretentious?"));
        list.add(createPlayerText("Just because other people have it worse doesn't mean your feelings are invalid!"));
        list.add(createPlayerText("Oh god, I didn't mean to pressure you. If you're not comfortable, you don't "
                + "need to say anything."));
        list.add(createHarukiText("This is probably very annoying to hear but lately... It's been getting hard for"
                + " me to be happy.  I'm just... tired."));
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                harukiGoodChoice3();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates third part of Haruki's good end route
    public void harukiGoodChoice3() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            int x = choicePane("1. Everyone's tired once in a while... I'm tired too. What's making you tired?"
                    + "\n" + "2. Tired from what? :(");
            if (x == 0) {
                playerRecord.saveLine("Everyone's tired once in a while... I'm tired too. What's making you tired?");
                strings.addElement(createPlayerText("Everyone's tired once in a while... I'm tired too. "
                        + "What's making you tired?"));
                choicePaneAdd("Everyone's tired once in a while... I'm tired too. What's making you tired?");
            } else if (x == 1) {
                playerRecord.saveLine("Tired from what? :(");
                strings.addElement(createPlayerText("Tired from what? :("));
                choicePaneAdd("Tired from what? :(");
            }
            int y = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(frame),
                    "Would you like to save?", "ChatAway SYSTEM MESSAGE", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                save();
            }
            harukiGoodChoice4();
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates fourth part of Haruki's good end route
    public void harukiGoodChoice4() {
        strings.removeAllElements();
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        harukiGoodChoice4List(list);
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                        "The chatroom will be closing soon. You can now leave anytime.",
                        "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                harukiGoodChoice5();
            }
        });
    }

    //EFFECTS: creates list of dialogue text for harukiGoodChoice4
    public void harukiGoodChoice4List(ArrayList<String> list) {
        list.add(createHarukiText("I'm not sure if I like my major anymore, but if I switch, I feel like everyone"
                + " would be disappointed in me."));
        list.add(createPlayerText("I know it's easier said than done, but if it's making you unhappy, you need to "
                + "do something about it."));
        list.add(createHarukiText("It was really hard to get into my program at WestU."));
        list.add(createHarukiText("My parents were so happy when I did. I don't want to let them down."));
        list.add(createPlayerText("Oh, is it the XYC program?"));
        list.add(createHarukiText("Yeah, how did you know?"));
        list.add(createPlayerText("I go to WestU too."));
        list.add(createPlayerText("Some of my friends are in XYC, but they realized it's not for them, "
                + "so they're switching out."));
        list.add(createPlayerText("It's pretty scary to switch programs, but I was happier after I switched."));
        list.add(createHarukiText("You've also switched programs too..."));
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates fifth part of Haruki's good end route
    public void harukiGoodChoice5() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            strings.addElement(createHarukiText("Hey, not to be weird but... Do "
                    + " you think we could grab coffee tomorrow and talk a bit more? My treat :D"));
            int x = choicePane("1. Of course :) Does 3pm at the campus Moonbucks work for you?" + "\n"
                    + "2. Sorry :( I wish I could, but I got stuff going on tomorrow.");
            harukiGoodChoice5ActionListener(x);
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates following part of action listener for harukiGoodChoice5
    public void harukiGoodChoice5ActionListener(int x) {
        if (x == 0) {
            addSaveLine("Of course :) Does 3pm at the campus Moonbucks work for you?");
            choicePaneAdd("Of course :) Does 3pm at the campus Moonbucks work for you?");
            strings.addElement(createKuroText("Works for me :) See you then! I'll be wearing a blue hoodie."));
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Good End. You got yourself a potential date! Himari would be proud." + "\n"
                            + "You will now be exited.",
                    "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else if (x == 1) {
            addSaveLine("Sorry :( I wish I could, but I got stuff going on tomorrow.");
            choicePaneAdd("Sorry :( I wish I could, but I got stuff going on tomorrow.");
            strings.addElement(createKuroText("It's okay! Thanks for hearing me out anyway :)"));
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Neutral End. You had a nice chat with a stranger online."
                            + "\n" + "You will now be exited.",
                    "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates first part of Haruki's bad end route
    public void harukiBadChoice1() {
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        strings2.addElement("YOU ARE ON THE BAD END ROUTE.");
        list.add(createHarukiText("Oh, it's no problem! I'm here to listen :)"));
        list.add(createPlayerText("I think I just failed my midterm today, and I don't really know what to do now."));
        list.add(createPlayerText("I really liked the class too."));
        list.add(createHarukiText("Oh no! I'm a student too, so I can relate. It's "
                + "only one midterm, so there's still hope!"));
        list.add(createPlayerText("I just feel like if I can't do well now, I won't do well later either."));
        list.add(createHarukiText("If you don't mind, I can share some of my experiences with you."));
        list.add(createHarukiText("Hopefully it will make you fell better :)"));
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                harukiBadChoice2();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates first part of Haruki's bad end route
    public void harukiBadChoice2() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            int x = choicePane("1. Not to be rude, but I'm not really interested in hearing it..." + "\n"
                    + "2. Sorry, but can you let me finish?");
            if (x == 0) {
                strings.addElement(createPlayerText("Not to be rude, but I'm not really interested in hearing it..."));
                harukiBadChoice3();
            } else if (x == 1) {
                strings.addElement(createPlayerText("Sorry, but can you let me finish?"));
                harukiBadChoice3();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates first part of Haruki's bad end route
    public void harukiBadChoice3() {
        strings.removeAllElements();
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        list.add(createHarukiText("Ahhh, I'm sorry for interrupting :("));
        list.add(createPlayerText("It's okay. I shouldn't be lashing out at you..."));
        list.add(createPlayerText("You know what? Just forget it. Hopefully this chatroom ends soon."));
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                harukiBadChoice4();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates first part of Haruki's bad end route
    public void harukiBadChoice4() {
        removeAllActionListeners(send);
        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                "The chatroom will be closing soon. You can now leave anytime.",
                "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        send.addActionListener(e -> {
            strings.addElement(createHarukiText("... Sorry, I wasn't cheer you up..."));
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Bad End, where you made a ball of sunshine feel bad." + "\n" + "You will now be exited.");
            System.exit(0);
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates Yuto's route
    public void yutoRoute() {
        removeAllActionListeners(send);
        strings.removeAllElements();
        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                "This chatroom is prepaid, so your credit card information is not required. Please be vigilant"
                        + " of monetary scams.",
                "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        send.addActionListener(e -> {
            if (strings.isEmpty()) {
                strings.add(0, "best_dude has entered the chatroom.");
            } else {
                strings.addElement(createYutoText("Well, here I am. What are your other two wishes?"));
                int x = choicePane("1. Sorry dude, but I'm not the biggest fan of pick-up lines. That was pretty good "
                        + "though." + "\n"
                        + "2. My other wish? Well, I wish I could rearrange the alphabet so I could put U and "
                        + "I together ;)");
                yutoRouteActionListener(x);
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates following part of action listener for yutoRoute
    public void yutoRouteActionListener(int x) {
        if (x == 0) {
            addSaveLine("Sorry dude, but I'm not the biggest fan of pick-up lines. "
                    + "That was pretty good though.");
            choicePaneAdd("Sorry dude, but I'm not the biggest fan of pick-up lines. "
                    + "That was pretty good though.");
            int y = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(frame),
                    "Would you like to save?",
                    "ChatAway SYSTEM MESSAGE", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                save();
                yutoGoodChoice1();
            } else if (y == 1) {
                yutoGoodChoice1();
            }
        } else if (x == 1) {
            addSaveLine("My other wish? Well, I wish I could rearrange the alphabet so I could"
                    + " put U and I together ;)");
            yutoBadChoice1();
        }
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates first part of Yuto's good end route
    public void yutoGoodChoice1() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            strings.addElement(createYutoText("I hope you liked the one in my profile. LOL. How do you want me to"
                    + " entertain you? Since flirting doesn't fly."));
            int x = choicePane("1. I think you could just try to relax and be yourself? We're all strangers here "
                    + "anyway." + "\n" + "2. Is this flirtation thing a front you put up?");
            if (x == 0) {
                addSaveLine("I think you could just try to relax and be yourself? We're all strangers "
                        + "here anyway.");
                choicePaneAdd("I think you could just try to relax and be yourself? We're all strangers "
                        + "here anyway.");
            } else if (x == 1) {
                addSaveLine("Is this flirtation thing a front you put up?");
                choicePaneAdd("Is this flirtation thing a front you put up?");
            }
            int y = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(frame),
                    "Would you like to save?", "ChatAway SYSTEM MESSAGE", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                save();
            }
            yutoGoodChoice2();
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates second part of Yuto's good end route
    public void yutoGoodChoice2() {
        strings.removeAllElements();
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        list.add(createYutoText("Hah... I could try to be myself..."));
        list.add(createYutoText("No, you're just going to be like the others, and think I'm boring."));
        list.add(createPlayerText("Whoa whoa, don't lump me together with everyone else! I don't even know you yet."));
        list.add(createYutoText("... Sorry, I got a bit angsty there."));
        list.add(createPlayerText("Hey, it's no problem. We all have some painful memories lingering around."));
        list.add(createPlayerText("I guess we can just talk about hobbies. What do you like to do in your free time?"));
        list.add(createYutoText("I like to play basketball, but you probably guessed that."));
        list.add(createPlayerText("... Well, you do have a basketball in your profile picture."));
        list.add(createYutoText("Oh lmao, I forget about that. Yeah, that's fair."));
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                yutoGoodChoice3();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates third part of Yuto's good end route
    public void yutoGoodChoice3() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            strings.addElement(createYutoText("What else do you think I do in my free time?"));
            int x = choicePane("1. Hang out with your buddies?"
                    + "\n" + "2. Might be a long shot but, do you read Shakespeare?");
            if (x == 0) {
                playerRecord.saveLine("Hang out with your buddies?");
                strings.addElement(createPlayerText("Hang out with your buddies?"));
                choicePaneAdd("Hang out with your buddies?");
            } else if (x == 1) {
                playerRecord.saveLine("Might be a long shot but, do you read Shakespeare?");
                strings.addElement(createPlayerText("Might be a long shot but, do you read Shakespeare?"));
                choicePaneAdd("Might be a long shot but, do you read Shakespeare?");
            }
            int y = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(frame),
                    "Would you like to save?", "ChatAway SYSTEM MESSAGE", JOptionPane.YES_NO_OPTION);
            if (y == 0) {
                save();
            }
            yutoGoodChoice4();
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates fourth part of Yuto's good end route
    public void yutoGoodChoice4() {
        strings.removeAllElements();
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        yutoGoodChoice4List(list);
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                        "The chatroom will be closing soon. You can now leave anytime.",
                        "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                yutoGoodChoice5();
            }
        });
    }

    //EFFECTS: creates list of dialogue text for yutoGoodChoice4
    public void yutoGoodChoice4List(ArrayList<String> list) {
        list.add(createYutoText("I love Shakespeare, man. Damn, do I not seem like a Shakespeare guy?"));
        list.add(createPlayerText("Well, there is a certain finesse you have with your words LOL."));
        list.add(createPlayerText("I don't mean to make fun of you, it's all in good jest."));
        list.add(createYutoText("Hey, that's a compliment. Shakespeare had some pretty good pick-up lines."));
        list.add(createYutoText("Did my heart love till now? forswear it, sight! For I ne'er saw true beauty"
                + " till this night."));
        list.add(createPlayerText("Is that... Romeo and Juliet?"));
        list.add(createYutoText("YESSS, IT IS. My lady, you are very cultured."));
        list.add(createPlayerText("Eyyy, I still got my WestU English Lit knowledge yo."));
        list.add(createYutoText("I'm impressed! ... You don't think it's weird that I like Shakespeare?"));
        list.add(createPlayerText("Of course not! I think it's nice that you're so excited about it :)"));
        list.add(createYutoText("Thanks, that really means a lot to me. Seriously."));
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates fifth part of Yuto's good end route
    public void yutoGoodChoice5() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            strings.addElement(createYutoText("I know we're just 2 random strangers online, but would you like to come"
                    + " watch me play a game tomorrow at the WestU gym?"));
            int x = choicePane("1. I would love to! I'll be your cheerleader :)" + "\n"
                    + "2. I really wish I could, but I'm busy tomorrow. Break a leg!");
            yutoGoodChoice5ActionListener(x);
        });
    }

    //MODIFIES: send, strings, strings2, frame, playerRecord, yourChoices
    //EFFECTS: creates following part of action listener for yutoGoodChoice5
    public void yutoGoodChoice5ActionListener(int x) {
        if (x == 0) {
            addSaveLine("I would love to! I'll be your cheerleader :)");
            choicePaneAdd("I would love to! I'll be your cheerleader :)");
            strings.addElement(createKuroText("Hell yeah! See you tomorrow then, my Juliet~ ;)"));
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Good End. You got yourself a potential date! Himari would be proud." + "\n"
                            + "You will now be exited.",
                    "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else if (x == 1) {
            addSaveLine("I really wish I could, but I'm busy tomorrow. Break a leg!");
            choicePaneAdd("I really wish I could, but I'm busy tomorrow. Break a leg!");
            strings.addElement(createKuroText("Lmao thanks. Hopefully we'll talk again someday."));
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Neutral End. You had a nice chat with a stranger online."
                            + "\n" + "You will now be exited.",
                    "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates first part of Yuto's bad end route
    public void yutoBadChoice1() {
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        strings2.addElement("YOU ARE ON THE BAD END ROUTE.");
        list.add(createYutoText("Ooo, I've met my match it seems. Bring it on!"));
        list.add(createPlayerText("If you were a Transformer, you'd be Optimus Fine."));
        list.add(createYutoText("Baby, if you were words on a page, you'd be fine print."));
        list.add(createPlayerText("Oh, I was wondering if you had extra heart."));
        list.add(createYutoText("Did yours just get stolen?"));
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                yutoBadChoice2();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates second part of Yuto's bad end route
    public void yutoBadChoice2() {
        removeAllActionListeners(send);
        send.addActionListener(e -> {
            int x = choicePane("1. Not only do I need a heart from you, but I also need a band-aid. Because I "
                    + "scraped my knee, falling for you <3" + "\n"
                    + "2. If stealing hearts were a crime, you'd be guilty as charged ;)");
            if (x == 0) {
                strings.addElement(createPlayerText("Not only do I need a heart from you, but I also need a band-aid."
                        + " Because I scraped my knee, falling for you <3"));
                yutoBadChoice3();
            } else if (x == 1) {
                strings.addElement(createPlayerText("If stealing hearts were a crime, you'd be guilty as charged ;)"));
                yutoBadChoice3();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates third part of Yuto's bad end route
    public void yutoBadChoice3() {
        strings.removeAllElements();
        removeAllActionListeners(send);
        ArrayList<String> list = new ArrayList<>();
        list.add(createYutoText("Well well, on the topics of hearts and injuries, do you know CPR?"));
        list.add(createPlayerText("I sure do, because I just took your breath away."));
        list.add(createYutoText("I'm no math whiz, but I'm pretty good with numbers."
                + " Tell you what, give me yours and watch what I can do with it."));
        send.addActionListener(e -> {
            if (strings.size() < list.size()) {
                strings.addElement(list.get(strings.size()));
            } else {
                yutoBadChoice4();
            }
        });
    }

    //MODIFIES: send, strings, strings2, frame
    //EFFECTS: creates fourth part of Yuto's good end route
    public void yutoBadChoice4() {
        removeAllActionListeners(send);
        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                "The chatroom will be closing soon. You can now leave anytime.",
                "ChatAway SYSTEM MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        send.addActionListener(e -> {
            strings.addElement(createPlayerText("Ahaha, that was a lot of fun! Thanks dude!"));
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Bad End, where you had a pick-up line battle with someone, and only that."
                            + "\n" + "You will now be exited.");
            System.exit(0);
        });
    }

    //EFFECTS: removes all action listeners from the JButton
    public void removeAllActionListeners(JButton button) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
    }

    //MODIFIES: yourChoices, strings2
    //EFFECTS: creates choice that is added to yourChoices, displays element in strings2, and plays sound when it occurs
    public void choicePaneAdd(String message) {
        Choice c = new Choice();
        c.changeChoice(message);
        yourChoices.addSelectedChoice(c);
        strings2.addElement(message + "\n");
        playSoundEffect();
    }

    //MODIFIES: strings, playerRecord
    //EFFECTS: displays string in strings, saves line to playerRecord
    public void addSaveLine(String message) {
        strings.addElement(createPlayerText(message));
        playerRecord.saveLine(message);
    }

    //MODIFIES: frame
    //EFFECTS: creates a choice pane so the player can select an option
    public int choicePane(String message) {
        String[] options = {"1", "2"};
        return JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(frame), message,
                "ChatAway SYSTEM MESSAGE",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    }

    //MODIFIES: send, frame
    //EFFECTS: creates chatroom interface
    public JButton setUpChatRoom() {
        JPanel crPanel = new JPanel(new BorderLayout(20, 10));
        JScrollPane scroll = new JScrollPane(textArea);
        JScrollBar scrollBar = scroll.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));
        scroll.setPreferredSize(new Dimension(WIDTH - 200, HEIGHT - 100));
        JScrollPane choice = new JScrollPane(choiceArea);
        choice.setPreferredSize(new Dimension(200, 100));
        crPanel.setBackground(Color.pink);
        JTextField textField = new JTextField(50);
        JPanel bottom = new JPanel();
        bottom.setBackground(Color.pink);
        bottom.add(textField);
        send = new JButton("Send");
        bottom.add(send);
        ImageIcon icon = new ImageIcon("src/main/ui/images/chatawaylogo.gif");
        JLabel gifLogo = new JLabel(icon);
        frame.getContentPane().add(crPanel);
        crPanel.add(Box.createVerticalStrut(20));
        crPanel.add(gifLogo, BorderLayout.PAGE_START);
        crPanel.add(scroll, BorderLayout.CENTER);
        crPanel.add(choice, BorderLayout.LINE_END);
        crPanel.add(bottom, BorderLayout.PAGE_END);
        return send;
    }

    //MODIFIES: frame
    //EFFECTS: creates JOptionPane telling the player their progress up until their selected choice
    private void progress(CorrectChoices cc) {
        choiceArea.addListSelectionListener(e -> {
            int numOfCorrectChoices = 0;
            if (strings2.size() == 1) {
                numOfCorrectChoices = 1;
            } else if (strings2.size() > 1) {
                String s = choiceArea.getSelectedValue();
                int x = strings2.indexOf(s);
                System.out.println(x);
                for (int i = 0; i < x + 1; i++) {
                    if ((yourChoices.retrieveSelectedChoice(i)).returnChoice()
                            .equals((cc.retrieveCorrectChoice(i)).returnChoice())) {
                        numOfCorrectChoices = numOfCorrectChoices + 1;
                    }
                }
            }
            playSoundEffect();
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Up until your currently selected choice, you have gotten"
                            + " " + numOfCorrectChoices + "/4 correct.", "ChatAway SYSTEM MESSAGE",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    //EFFECTS: creates list of dialogue test for prologue
    private ArrayList<String> prologueList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("hehehehimari has entered the chatroom.");
        list.add(createPlayerText("Himari? Is that you?"));
        list.add(createHimariText("Hey " + you.getFirstName() + ", you figured it out!"));
        list.add(createPlayerText("Man, this chatroom thing was so hard to set up. It looks super sketchy too."));
        list.add(createPlayerText("Why can't we just text normally?"));
        list.add(createHimariText(you.getFirstName() + ", you're single right? A single pringle ready to mingle?"));
        list.add(createPlayerText("Maybe... Why?"));
        list.add(createHimariText("Let me tell you, as weird and as outdated this app is, it has its charms."));
        list.add(createHimariText("People rent themselves out on here and "
                + "pretend to be your online 'friend', boyfriend, or girlfriend."));
        list.add(createPlayerText("... Is that what you mean by charms?"));
        list.add(createHimariText("Yes!! Isn't it kinda fun?"));
        list.add(createHimariText("It's virtual, but also sort of real because it's not a bot."));
        list.add(createHimariText("They also advertise themselves with different roles they can play."));
        list.add(createHimariText("The cold type, the smooth player... Whatever you want. Oh! And guess what?"));
        list.add(createPlayerText("..."));
        list.add(createPlayerText("What...?"));
        list.add(createHimariText("Since I have a platinum membership, I can gift people chatrooms."));
        list.add(createHimariText("I bought you a chatroom session so you can experience having a someone special"
                + " to talk to!"));
        prologueList2(list);
        return list;

    }

    //EFFECTS: creates second part of list of dialogue text for prologue
    private void prologueList2(ArrayList<String> list) {
        list.add(createHimariText("I went through so many profiles, and there were 3 that came up as students who"
                + " go to our college."));
        list.add(createPlayerText("Himari, I appreciate the gesture, but this is so weird."));
        list.add(createPlayerText("From our school? What if I run into them at some point?"));
        list.add(createHimariText("Here's another charming thing, this chatroom service requires them to"
                + " upload info, but you don't."));
        list.add(createHimariText("You know who they are, but they don't know who you are. Isn't that nice?"));
        list.add(createPlayerText("I still think this is kind of creepy. Can you get a refund?"));
        list.add(createHimariText("Nope!"));
        list.add(createPlayerText("It's just one chatroom... How much was this?"));
        list.add(createHimariText("Shhh, we don't talk about that."));
        list.add(createPlayerText("Oh dear..."));
        list.add(createHimariText(" Why don't you give it a try? Maybe you could make a virtual boyfriend"
                + " into a real one ;)"));
        list.add(createPlayerText("What do you mean, real one?"));
        list.add(createHimariText("Heh heh, well, remember how I told you that I met Aoi online?"));
        list.add(createPlayerText("Oh yeah, you mentioned it before."));
        list.add(createPlayerText("Omg, please don't tell me you met her on here."));
        list.add(createHimariText("I sure did! Anyway, I sent you the profiles. "
                + "Look them over, and pick your poison ;)"));
    }

    //EFFECTS: creates a text with the text player.s username: message
    private String createPlayerText(String message) {
        return you.getUserName() + ": " + message;
    }

    //EFFECTS: creates a text with the text Himari's username: message
    private String createHimariText(String message) {
        return "hehehehimari: " + message;
    }

    //EFFECTS: creates a text with the text Kuro's username: message
    private String createKuroText(String message) {
        return "cold_and_bored: " + message;
    }

    //EFFECTS: creates a text with the text Haruki's username: message
    private String createHarukiText(String message) {
        return "haruharu: " + message;
    }

    //EFFECTS: creates a text with the text Yuto's username: message
    private String createYutoText(String message) {
        return "best_dude: " + message;
    }


    //MODIFIES: goodEndChoicesKuro
    //EFFECTS: adds the correct choices to Kuro's good end choice key
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

    //MODIFIES: this, you
    //EFFECTS: loads GameText from json file
    private void load() throws JSONException {
        try {
            playerRecord = jsonReader.read();
            you.changePlayerFirstName(playerRecord.returnDialogueString(0));
            you.changePlayerLastName(playerRecord.returnDialogueString(1));
            you.changePlayerUserName(playerRecord.returnDialogueString(2));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Unable to read from file" + JSON_STORE);
        } catch (JSONException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Load failed. Please start a new chat.");
        }
    }

    // EFFECTS: saves the GameText to a json file without a system output message
    private void save() {
        try {
            jsonWriter.open();
            jsonWriter.writeGameText(playerRecord);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Unable to write to file: " + JSON_STORE);
        }
    }

    //MODIFIES: playerRecord
    //EFFECTS: sets playerRecord to a new GameText to clear the json file for a new chat
    private void clearJson() {
        try {
            jsonWriter.open();
            playerRecord = new GameText();
            jsonWriter.writeGameText(playerRecord);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(frame),
                    "Unable to read from file" + JSON_STORE);
        }
    }

}

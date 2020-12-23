# My Personal Project: ChatAway

## What will the application do?
*ChatAway* is a short, decision-based game that will hopefully allow you to experience 
the possibilities of finding love online using a fictional chatroom service called ChatAway.  
<br>
Pick the right choices to get the best ending! 

## Who will use it?
Anyone who is looking to play a short game.

## Why is this project of interest to you?
I was inspired to create this game after hearing my friends complain about 
not being able to get a boyfriend. Even though their real love life is lacking,
maybe their digital one can be better! 
<br> <br>
Having played many otome games (Japanese dating sims), I decided to try my hand
at making something simple along that genre.

## Phase 1
For Phase 1, four user stories were created. A console application was then built 
centered around these user stories.

## Phase 2
For Phase 2, a saving system was built using the json library. 

## Phase 3
For Phase 3, a GUI for the application was built using JavaSwing. All user stories were realized in 
this process. 

## Phase 4
For Phase 4, we were asked to apply one design principle and reflect on any changes to improve the project. 
<br> <br>
**Task 2** <br>
I chose to test and design a class in my model package to be robust.
- The class I chose to make robust is my **CorrectChoices** class.
- The method that throws a **NoChoiceSelectedException** is the **correctChoiceChecker** method.
- The method that throws a **EmptyChoiceKeyException** is the **retrieveCorrectChoice** method.
<br> <br>

**Task 3**
- I would make an abstract class or interface that SelectedChoices and CorrectChoices can extend, since they share many
add/get methods.
- The GUI has about 1500 lines, so I would want to make it more readable by refactoring it into more classes/interfaces
 for the GUI display itself, game dialogue, and character routes.
- For example, I would make a super class called Route that can be extended since all 3 character
routes have similar behaviour.

## Credits
A big thank you to the following resources that I used throughout my project: <br>
- [Data persistence for saving](https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
- [Luna Lucid's music for the BGM](https://lunalucid.itch.io/free-creative-commons-bgm-collection)
- [Sound effect used in the chatroom](https://freesound.org/people/Sirkoto51/sounds/449547/)
- [Scrollbar that auto-scrolls to bottom](https://stackoverflow.com/questions/6379061/how-to-auto-scroll-to-bottom-in-java-swing)
- [How to play audio in the application](https://www.codota.com/code/java/methods/javax.sound.sampled.AudioSystem/getClip)
- [Volume control](https://stackoverflow.com/questions/14301618/can-java-sound-be-used-to-control-the-system-volume)
- [Remove all action listeners](https://stackoverflow.com/questions/4048006/java-swing-how-to-remove-an-anonymous-actionlistener-from-a-component)
- [Create text GIFs](https://en.bloggif.com/)
## User stories 
**Phase 1**
- As a user, I want to select choices (X) that will be added to a list of choices (Y) to 
determine the ending I get.
- As a user, I want to be able to change the main character's first name, last name, and username
when the story starts.
- As a user, I want to be able to adjust the volume of sound. 
- As a user, I want to be able to view my number of correct choices at the end.
<br> <br> 

**Phase 2**
- As a user, I want to be able to save at certain prompted checkpoints in the game. 
- As a user, I want to be able to load my save file.
- As a user, I want to be able to start a new chat without previous save information.



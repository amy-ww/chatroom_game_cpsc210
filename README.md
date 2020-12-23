# My Personal Project: ChatAway

## What will the application do?

*ChatAway* is a short, decision-based game that will hopefully allow you to experience 
the possibilities of finding love online using a fictional chatroom service called ChatAway.  
Pick the right choices to get the best ending! 
<br> <br>

## Who will use it?
Anyone who is looking to play a short game.

## Why is this project of interest to you?
I was inspired to create this game after hearing my friends complain about 
not being able to get a boyfriend. Even though their real love life is lacking,
maybe their digital one can be better! 
<br> <br>
Having played many otome games (Japanese dating sims), I decided to try my hand
at making something simple along that genre.

## All user stories 
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

## Phase 4: Task 2
I chose to test and design a class in my model package to be robust.
- The class I chose to make robust is my **CorrectChoices** class.
- The method that throws a **NoChoiceSelectedException** is the **correctChoiceChecker** method.
- The method that throws a **EmptyChoiceKeyException** is the **retrieveCorrectChoice** method.

## Phase 4: Task 3
- I would make an abstract class or interface that SelectedChoices and CorrectChoices can extend, since they share many
add/get methods.
- The GUI has about 1500 lines, so I would want to make it more readable by refactoring it into more classes/interfaces
 for the GUI display itself, game dialogue, and character routes.
- For example, I would make a super class called Route that can be extended since all 3 character
routes have similar behaviour.


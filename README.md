Luke Ladas - Java Lines of Action Manual
Bug Report:
- Serialization.loadGame() does not check to see if a file has the proper layout/format of a typical save. 
- Potential integer overflow that could occur with values such as score and round wins for a player depending on how long a tournament goes on for. Realistically, the chances of a Tournament ever reaching this state are next to impossible but it is important to know this hasn’t been dealt with. 
- These are the main bugs I am aware of, like many programs, there are probably many unforeseen bugs that I could not locate.

Additional Feature Report:
- Additional strategies such as winning, and grouping, which moves a piece towards a new group of similar colored pieces.
Data Structures:
- Arrays were used pretty extensively inside my project, two of the most vital members of any of the classes in my program were using this data structure. I created a two dimensional array as a way to store pieces inside the simulated board and also used an array to store the roster of players that are inside a tournament. Without arrays I could have used vectors but I saved that data structure for specific use cases mentioned later. Arrays are fixed sizes which means I do not have to worry about someone manipulating my board grid or the amount of players in a game.
- Vectors were also used quite often inside my project as a way to store lists with dynamic sizes. I used them to store possible strategies for my move helper function in Player, and also used them to store the names of text files in my project’s directory. 
Classes:
Basic:
- Piece is a class that represents a space on the game board. The main member of a piece is the color, which is mainly used by the Player related classes, letting them know which color piece on the board they can move around, block, or capture. The three values are typically ‘B’ which is for black pieces, ‘W’ which is for white pieces, and ‘.’ which actually represents an empty space on the board. Piece also contains three more members, one for getting the best strategy associated with a piece, second being the location of the piece on the board, and the third being the location that the strategy is associated with. These three members are used in Player as well for outputting possible moves in the strategy helper function. Piece is used inside the Board class and is used for a data type in a 2D array. Piece does not reference any classes.
- Log is a class that stores a static vector of strings to essentially represent the game log and what occurs throughout a lines of action game. Any class can call this to append messages to this log. This log however is really used for the activity match controller class to be able to set the text contents inside the GUI log with the messages from this log.
Composite:
- Tournament is a model class that is essentially used for storing the group of players that will be playing in a match and holds their game state. Unlike my C++ and LISP project, this does not control a round loop as my GUI is going to.
- Round is a model class that handles the state of a round in a tournament. It decides which player can go first in a game, and at the end of every move the players will swap places. The Round class also checks for a win condition; if a player has won, a round is over and the round declares who has won. Round references Player, and Board classes. 
- Player is a model class that is used for holding vital information for a player in a tournament. Contains classifiers such as a Player’s color, which directly aligns with what pieces they can control or capture. Players also have an identifier for letting certain classes know if a player is a computer or human. Finally the last members of the Player are the amount of pieces they have in a round, the score they have in a tournament as well as their total wins. Player holds the functions for a strategy helper, which recommends a player with a best case scenario move. Depending on the state of a round, the player will be suggested different strategies and ultimately try to get wins and increase their score. Player has a play() function but it is empty, meant to be overridden by its children. Player is a parent class who passes down its members and functions onto the Human and Computer class. Player is a Player references Piece and Board
- Board is a model class and stores a code representation of the game board. Its main member and code representation of the board is a 2D array of Piece class objects. Board can call this array to select, move and capture pieces on the board. It also handles validation of moves and selected pieces from a player. Board also is responsible for finding out if a player has won by checking if pieces are in one single group. Board references Piece.
- BoardView is a controller/view class whose sole purpose is to display a visual representation of the game board to the players in the form of a Graphical User Interface. It updates the GUI by placing pieces on the board, fills each tile on the board with a button and adds a listener to each coordinate for allowing a player to click and move pieces around.This technically is connected to my match activity as this really is just a function call that gets the widgets from my match GUI like the board table. It isn't necessarily a standalone controller or view, it is more of a helper class for the match activity which will be mentioned later. The boardview has the state of the board from the Board class and loops through that 2D array to get the information to update the view.
- Serialization is how the game handles saving and loading files. Saved files need a name with no special characters to prevent errors and also need to be less than 32 characters to prevent long names. Loaded files are obtained from listing out all text files inside the game’s directory in the saves folder. The program parses the text file going through each line to try and find keywords matching the format of the project description’s format. This information is sent back to the Round class so that the game board, and player information are properly loaded in. Serialization references Player, Piece and Board classes.
- MainActivity is a view/controller class representing the main menu of my game. There are two buttons to create a new game or load a game. Listeners are set for each button to go to the match activity screen but pass in an value with intent whether the match activity will be new or loaded.
- MatchActivity is a view/controller class which is the GUI for the round/tournament loop. It contains the game board grid in the middle, on the left contains the game log as a scroll view, and on the right there are two views containing the game state and also help strategies for a player in the turn. There are also three buttons at the bottom for continuing a turn/tournament, exiting a tournament or round, and asking for help. This is essentially what drives the game loop as we call game state information from the model and pass it through to the controller which updates the GUI. Clicking continue at the end of a player’s move action will drive the round loop and tournament loop.
- WinActvity is a view/controller class that will appear after the player clicks on the exit button in my match activity after a win has occurred in a round. It will display a new screen showing who has won a tournament, the human and player scores, as well as their wins. There is a button for a user to return to the main activity after they are done with looking at this information. This gets information from the tournament game state to call information and update the view. 
- SerialActivity is a view/controller class that will appear after the player decides to click on the exit button in my match activity at the end of a turn. This will prompt a user to enter a valid save file name and if it is valid, it will call the Serialization class to pass in information from the tournament state to generate and format a save text file. 
Derived:
- Human is a model class that has its functions and members inherited from the Player. It overrides the Player’s play() function where it gets its valid selected origin and destination coordinates for a move and moves their pass on the game board. The board GUI drives the movement for a human when a player taps on a square in the grid and then selects another valid space. Human references Player as it is a child of that class. 
- Computer is a class that has its functions and members inherited from Player. It overrides the Player’s play() where it will call the strategize function and move their piece to the best possible move/strategy from the best piece.This moves the specified piece on the board to its best location. Computer is now driven by the GUI whenever there is a swap or if the starting player is a computer, it will automatically call this function for play. It technically references Player as it is a child of that class. 
Project Log:
March 21 2024:
- Translated all of my classes from C++ to Java to work in command prompt first before implementing event driven programming
Total: 2.5 hours

March 24 2024:
- Debugged and tested my Java classes
- Removed all instances of pointers as they do not exist in Java
- Serialization.Strategize() and all of the functions it calls needed to have its parameters stored as copies rather than directly modifying the game state. Because all objects are passed by reference, when testing my game board for strategies it would move the pieces and manipulate the original board. 
- Fixed input validation 
- Tested code again
- Fixed coordinate conversion as it worked different from C++ string manipulation

Total: 3 hours 

March 28 2024:
- Researching android GUI development by watching tutorials and reading documentation
- Created the main menu for my application, activity_main which only has two buttons, creating a new game and loading a game. 
- Created another activity called activity_match which is what the player will see during a tournament with info on the game state such as the board, play score and wins, and the log and help. 
- Added listeners to my main menu for clicking on the buttons that bring a user to the activity match screen with an intent message of whether or not the game is loaded in or new
- Modified the BoardView class so that instead of printing out a board in the terminal, it is used for updating the table layout view that is from match activity. Each space is a button and will have the ability to be clicked to move a piece on the board.
- Added a popup for loading a game inside activity_match where it gets the text save files from the project directory almost exactly like the C++ project and LISP. 
- Modified the Serialization.loadgame() function so that instead of asking to input a name in the 

Total: 6.5 hours

March 31  2024:
- Removed functions that were originally intended for a more console program driven project such as Tournament.initialize_tournament() and Round.initial_round(). These were driving the game loop but with event driven programming I do not need them anymore and just need to call other functions in those classes when they are needed from listenable events.
- Added selection (origin) and destination String private members to the Player class for getting access to the selected coordinates on the board that are used to make a move. These come with their own selectors and mutators and are used by the boardview class. For each button in the tablelayout, is a corresponding position on the board.
- Added an integer private member to Player class called moves. It is used to keep track of what step in the movement action a player is in right now (selecting a piece or moving a selected piece to a destination).
- Buttons in boardview have a click listener now that will properly perform movement for a human when clicking on pieces by checking and setting the private members mentioned, move, destination and origin. 
- Modified the play() function for human class, making it so that it does not ask for input but instead gets the origin and destination private members it contains and uses those to move a piece on the board. 

Total: 2.5 hours

April 3 2024:
- Modify match activity GUI to fit more for horizontal layout, kept main activity the same but for match activity I expanded the sides to add views of game state, help info and the log, and the bottom to have current player and the help, continue and exit buttons. I decided that this would be more convenient in terms of availability of info rather than have everything be popups except the board.
- Added help click listener which updated the screen in activity_match to make the tablelayout have green background at whichever squares were being suggested in a move. 
- Updated the text inside the help view to be the best recommended strategy for a human/computer
- Added on click listener to my continue button which would swap players if a win has not been reached yet or create a new round when end of round has been reached
- Implemented Computer actions into my event driven game loop, a computer will make its moves after a swap() call results in the current player becoming a computer, or if the starting player in a new round or tournament or save is computer as well.
- Testing the event game loop
Total: 6 hours

April 4 2024:
- Add coinflip popup function in my activity_match class. Displays whether a user should guess heads or tails, determines starting player in a round or tournament
- Modified my original Round.starting_player() function so that it becomes a check to see if a game is a tie or not, boolean return true or false
- Added a new function in Round called coinflip() which gets the heads or tails guess from my activity_match popup function as a parameter and uses that to check if a guess was correct. The setting of the current player is essentially the same as previous projects like C++.
Total: 2 hours

April 6 2024:
- Added Log class and created all of its inside functions. Updated all previous model classes so that instead of doing System.out.println() to output state like I did in C++, I add this message to my log and display it to the log view in my activity_match class.
- Added serialization/save activity for my game which is a new screen that asks a user to input a file to save the game state to. 
- Added event listen for click in activity_match for my exit button to open up the save activity at the end of a turn 
- Testing saves/serialization
- Save files are not discovered, needed to adjust in my serialization.savegame() function for where to save a file to. The directory is the actual internal folder of the app itself inside the emulator in a folder called “saves”. I initially had my text files inside the assets folder of my project for loading them.
- Modify serialization.loadgame() and getfiles() functions to locate files in this app directory to make sure my serialization can work

Total: 4 hours

April 7 2024:
- Added activity for winning a game, activity_win, and made a controller/view for it. It displays who has won a tournament and shows the score and wins for player and human similar to what is displayed in the match screen
- Added the check inside my exit button listen event to transition to the win screen if a win condition has been met. 
- Added into this gamestate information through an intent like tournament player and computer scores and wins.

Total: 1.5 hours
April 8 2024:
- Tested project to make sure all cases should work for the demonstration
- Adjusted font size of my GUI for better readability
- Updated my thwart from C++ so that it properly attempts to block the winning coordinate rather than just blocking any random opponent piece as a last ditch effort
- Adjusted documentation/headers for functions to match java standard

Total: 3.5 hours

Total Hours For Project: 31.5 hours

How to Run Program:
Running the program is straightforward. Using android studio, there should be a play button to click that will automatically run the function inside the activity_main java file. Which will open the app and display the main menu to start the game. Make sure you have an emulator installed or an android device that can connect to android studio. 

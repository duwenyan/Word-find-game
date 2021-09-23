Requirements:
1. When the application is started, the player may choose to (1) Play a word game, (2) View statistics, or (3) Adjust the game settings.
#To achieve this requirement, I created a player class and added all 3 actions as a method for the player class

2. When choosing to adjust the game settings, the player (1) may choose for the game to end after a certain number of minutes, from 1 to 5, defaulting to 3, (2) may adjust the size of the square board, between 4(x4) and 8(x8), defaulting to 4, and (3) may adjust the weights of the letters of the alphabet between 1 and 5, defaulting to 1.
#To achieve this requirement, I created a separate settings class which has attributes like minutes, size, weights and also added the methods to change all 3 parameters and also to display the default values

3. When choosing to play a word game, a player will:
Be shown a ‘board’ of randomly generated letters.
Be shown a timer counting down the number of minutes available for the game, as set in the settings.
Start with 0 points, which is not required to be displayed during the game.
Until the game timer counts to zero and the game ends:
Enter a unique word made up of two or more letters on the board.  The word must contain only letters from the board that are each adjacent to the next (horizontally, vertically, or diagonally) and a single letter on the board may not be used twice.  The word does not need to be checked against a dictionary (for simplicity, we will assume the player enters only real words that are spelled correctly).
or
Choose to re-roll the board at a cost of 5 points.  The board will be re-created in the same way it is generated at the start of each game, replacing each letter.  The timer will not be reset or paused.  The player’s score may go into negative values.
or
Choose to exit the game early.
At the end of the game, when the timer has run out or the player chooses to exit, the final score for the game will be displayed.
Each word will score 1 point per letter (‘Qu’ will count as 2 letters), and the cost for each board reset will be subtracted.
After the player views the score, they will continue back to the main menu.
#To achieve this requirement, I created 2 classes. One for word game and other for board. The word class has attributes related to the game such as letters, minutes and words and its related actions are created as methods such as evaluate word, exit game, display score, display minutes etc.
#Similarly for the board class its attributes such as size and location are added and methods such as reroll board and display letters are added

4. Whenever the board is generated, or re-generated it will meet the following criteria:
 The board will consist of a square full of letters.  The square should have the number of letters, both horizontally and vertically, indicated by the size of the square board from the game settings (4x4, 5x5, 6x6, 7x7, or 8x8).  
⅕ (rounded up) of the letters will be vowels (a,e,i,o,u). ⅘ will be consonants.
The letter Q will be displayed as ‘Qu’ (so that Q never appears alone).  
The location and particular letters should be randomly selected from a distribution of letters reflecting the weights of letters from the settings.  A letter with a weight of 5 should be 5 times as likely to be chosen as a letter with a weight of 1 (assuming both are consonants or both are vowels).  In this way, more common letters can be set to appear more often.
A letter may appear on the board more than once.
#To achieve this requirement, the board class created in the above step will be utilized. Reroll method will address the regenerate scenario. Display letters method will address the location of the vowels or consonants in the board

5. When choosing to view statistics, the player may view (1) game score statistics, or (2) word statistics.
#To achieve this requirement, I created a separate Statistics class as the player class will be interacting with this one. This class has attributes such as final game score, no of times resetted, no of words entered. The actions of this class are created as dsiplayFinalScore,
#displayNoOfTimeReset, displayNoOfWordsEntered methods.

6. For game score statistics, the player will view the list of scores, in descending order by final game score, displaying:
The final game score
The number of times the board was reset
The number of words entered in the game
The player may select any of the game scores from this list to view the settings for that game’s board size, number of minutes, and the highest scoring word played in the game (if multiple words score an equal number of points, the first played will be displayed).
#This requirement is achieved in the statistics class by creating methods to view final score, no of times the board was reset and no of words entered in the game.

7. For the word statistics, the player will view the list of words used, starting from the most frequently played, displaying:
The word
The number of times the word has been played, across all games
#This requirement is achieved in the similar way as above in statistic class by using showWordStatistics method

8. The user interface must be intuitive and responsive.
#This is not represented in the design as it will be handled in the UI implementation

9. The performance of the game should be such that students do not experience any considerable lag between their actions and the response of the application.
#This is a non functional requirement and it will be handled at the time of implementation

10. For simplicity, you may assume there is a single system running the application.
#This requirement is not represented in the design document as it is assumed that the gaming app is the system in design.

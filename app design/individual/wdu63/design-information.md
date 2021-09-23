### Requirements
1. When the application is started, the player may choose to (1) Play a word game, (2) View statistics, or (3) Adjust the game settings.
  
   >**This requirement will be implemented in UI.** 

2. When choosing to adjust the game settings, the player (1) may choose for the game to end after a certain number of minutes, from 1 to 5, defaulting to 3, (2) may adjust the size of the square board, between 4(x4) and 8(x8), defaulting to 4, and (3) may adjust the weights of the letters of the alphabet between 1 and 5, defaulting to 1.
  
   >**This requirement will be implemented in UI. The game setting modification can be done by the functions of the class Setting, such as setTime, setBoardSize and setWeights.**

3. When choosing to play a word game, a player will:

   a. Be shown a ‘board’ of randomly generated letters.
	
    >**Under the class Game, the function newGame will create the board with random letters. The board will be shown by UI.**
	
   b. Be shown a timer counting down the number of minutes available for the game, as set in the settings.
	   
    >**This requirement will be implemented in UI.** 
	
   c. Start with 0 points, which is not required to be displayed during the game.
    >**The function newGame will initialize the score to be 0.**
	
  	d. Until the game timer counts to zero and the game ends:
	 <p>i. Enter a unique word made up of two or more letters on the board.  The word must contain only letters from the board that are each adjacent to the next (horizontally, vertically, or diagonally) and a single letter on the board may not be used twice.  The word does not need to be checked against a dictionary (for simplicity, we will assume the player enters only real words that are spelled correctly).</p>
		
    >**Under the class Game, the function inputWord will identify if the word entered by the player meets the requirement.**
		
   or

   ii. Choose to re-roll the board at a cost of 5 points.  The board will be re-created in the same way it is generated at the start of each game, replacing each letter.  The timer will not be reset or paused.  The player’s score may go into negative values.
		
   >**Under the class Game, the function reRoll will regenerate the board and take off 5 points from the total score.**
		
   or

    iii. Choose to exit the game early.
   
   >**This requirement will be implemented in UI. Aslo, under the class Stats, function addGame will be called to renew the statistics.**
   
    e. At the end of the game, when the timer has run out or the player chooses to exit, the final score for the game will be displayed.

    >**This requirement will be implemented in UI. Under the class Game, function getScores will return the final score to be shown in the UI.**

    f.Each word will score 1 point per letter (‘Qu’ will count as 2 letters), and the cost for each board reset will be subtracted.
    
    >**Under the class Game, the function inputWord will identify if the word entered by the player meets the requirement and will adjust accordingly. The function reRoll will subtract the points for reset.** 

    g. After the player views the score, they will continue back to the main menu.
    
    >**This requirement will be implemented in UI.**
4. Whenever the board is generated, or re-generated it will meet the following criteria:

   <p>a. The board will consist of a square full of letters.  The square should have the number of letters, both horizontally and vertically, indicated by the size of the square board from the game settings (4x4, 5x5, 6x6, 7x7, or 8x8).<br>
   b. ⅕ (rounded up) of the letters will be vowels (a,e,i,o,u). ⅘ will be consonants.<br>
   c. The letter Q will be displayed as ‘Qu’ (so that Q never appears alone).<br>  
   d. The location and particular letters should be randomly selected from a distribution of letters reflecting the weights of letters from the settings.  A letter with a weight of 5 should be 5 times as likely to be chosen as a letter with a weight of 1 (assuming both are consonants or both are vowels).  In this way, more common letters can be set to appear more often.<br>
   e. A letter may appear on the board more than once.<p>

   >**All these requirements will be fulfilled in the function newGame and reRoll in class Game.**
   
5. When choosing to view statistics, the player may view (1) game score statistics, or (2) word statistics.
   >**This requirement will be implemented in UI.**
   
6. For game score statistics, the player will view the list of scores, in descending order by final game score, displaying:
   <p>a. The final game score<br>
   b. The number of times the board was reset<br>
   c. The number of words entered in the game<p>
   The player may select any of the game scores from this list to view the settings for that game’s board size, number of minutes, and the highest scoring word played in the game (if multiple words score an equal number of points, the first played will be displayed).
    
   >**Under the class Stats, the function getStatsScores will return the ordered list for the above three numbers and the board size, number of minutes and the highest score. The results can be displayed by the UI.**
7. For the word statistics, the player will view the list of words used, starting from the most frequently played, displaying:
   <p>i. The word<br>
   ii. The number of times the word has been played, across all games<p>
    
   >**Under the class Stats, the function getStatsWord will return the ordered list for the word and the number of times. The results can be displayed by the UI.**
   
8. The user interface must be intuitive and responsive.
   >**This requirement will be implemented in UI.**
9. The performance of the game should be such that students do not experience any considerable lag between their actions and the response of the application.
    
10. For simplicity, you may assume there is a single system running the application.

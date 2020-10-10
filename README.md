NOT DONE yet

#Mexican Train
*Author: Annel Cota*
##Description
This is a domino game that is played with a double-9 set or double-12 set
of dominoes. Each player has a train they can play on that has to match the 
center domino, then match the rest of the dominoes on the train as they are played.
When a player empties their hand or there are no more plays, a round is over.
At the end of a round, the dominoes left in the players hands are added to their score.
The pips of all the dominoes are added and every score from every round is added up.
The player with the lowest score wins.
All of the rules for the game can be found here:
https://www.mexicantrainrulesandstrategies.com/

###Console Version
The console version of the game is playable just through the console. The whole
game is printed out along with the game state of the game. This includes the players,
player's hands, player's trains, boneyard, and so on. This is printed after every 
player makes a move to show the updated version of the game state and the board.

###JavaFX Version
The JavaFX version of the game is playable through a GUI. The center domino is shown
on the center of the GUI and the player's trains go off of the center piece in 
different directions.

###Game Setup
For the console version, there can be up to eight players. If you want to have
more than 4 players, a double-12 domino set it used. If there are four players or
less, you can choose between using a double-12 or double-9 set.

For the JavaFX Version there can only be four players.

For both versions, there can be any combo of human players and computer players.

##Testing Tools
* The game is set to only print the player's hand when it is their turn, if it 
is more than one human player.(console version) To print all the player's hands,
set printAllPlayersHands to true.

##Bugs and Comments
* In the JavaFX Version, sometimes when players add dominoes to the train, the domino
does not rotate to correctly match the pip it is supposed to match. This does not change
the logic though, the correct pip will be used to compare to the next playable domino.
* In the console version, sometimes after the computer player plays, an infinite
print loop happens. If using no computer players, this bug doesn't occur.
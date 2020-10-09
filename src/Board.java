/**
 * @author Annel Cota
 *
 * Board class creates the board of the game.
 * It creates the center domino, the Mexican Train,
 * and prints the board when that method is called.
 */

import java.util.ArrayList;

public class Board {
    private final ArrayList<Player> players;
    private int playerTurnInc = 0;
    private Player mexTrain;
    private Domino center;

    /**
     * Creates the center piece, Mexican Train, and adds
     * domino [0|centerNum] to each train, but is not
     * shown when printed. It adds that domino so
     * it can be easier to check if the next play domino
     * can go on the train. I made the other pip a 0
     * so it doesn't clash with my double domino
     * checking.
     * @param players : list of players in game
     * @param n : center domino number [n | n]
     * @param by : boneyard of dominoes
     */
    public Board(ArrayList<Player> players, int n, Boneyard by) {
        this.players = players;
        //gets image path of center domino for GUI
        String imagePath = "Resources/dominoes/" + n + "|" + n + ".png";
        String imagePath2 = "Resources/dominoes/" + 0 + "|" + n + ".png";
        center = new Domino(n,n,imagePath);
        mexTrain = new Player("Mexican Train",0);
        //adds [0| centerNum] domino to each train to make it easier to see if
        mexTrain.addDomToTrain(new Domino(0, by.centerNum,imagePath2));
        mexTrain.makeStateTrue();
        for(Player p : players){
            p.addDomToTrain(new Domino(0, by.centerNum,imagePath2));
        }
        changePlayerTurn();
    }

    /**
     * Method to get the center domino.
     * @return center domino
     */
    public Domino getCenter() {
        return center;
    }

    /**
     * Method to get the Mexican Train Player
     * @return Mexican Train Player
     */
    public Player getMexTrain() {
        return mexTrain;
    }

    /**
     * Changes the turn of the players.
     * Makes all of the player's turn false
     * and then increments by the players
     * list size to change the player turn one
     * after another.
     */
    public void changePlayerTurn() {
        System.out.println("Board chnage player turn");
        for(Player p : players){
            p.makePlayerTurnFalse();
            System.out.println(p.getPlayerNum());
        }
        System.out.println(playerTurnInc);
        players.get(playerTurnInc).makePlayerTurn();
        players.get(playerTurnInc).makeHasDrawnFalse();
        if(playerTurnInc == (players.size() - 1)) {
            playerTurnInc = 0;
        } else {
            playerTurnInc++;
        }
    }

    /**
     * Prints the board, which consists of the
     * player trains, to the console.
     * First prints the center domino, then the
     * Mexican Train, and then the player trains.
     */
    public void printBoard() {
        System.out.println("Center:" + center.toString());
        mexTrain.printTrain();
        for(Player p : players){
            p.printTrain();
        }
    }
}

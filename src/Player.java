/**
 * @author Annel Cota
 *
 * Player class creates a player with hand, train,
 * and a name/number. It keeps track of its train,
 * whether it has drawn, whether it is a computer player,
 * and it also prints the player's hand.
 */

import java.util.ArrayList;

public class Player {
    private String playerNum;
    private ArrayList<Domino> hand;
    private ArrayList<Domino> train;
    private boolean trainState = false;
    private boolean isComputer = false;
    private boolean hasDrawn = false;
    private boolean playerTurn = false;
    //play on train other than one with double if player played the double
    private boolean canPlayNonDoubleTrain = false;
    private int startingDomNum;
    private int score = 0;

    /**
     * Creates player with a hand and a train.
     * Sets the player's name/number.
     * @param playerNum : player's name/number
     * @param i : number of starting dominoes for player's hand
     */
    public Player(String playerNum, int i) {
        this.playerNum = playerNum;
        hand = new ArrayList<>();
        train = new ArrayList<>();
        startingDomNum = i;
    }

    /**
     * Creates player with a hand and train.
     */
    public Player() {
        hand = new ArrayList<>();
        train = new ArrayList<>();
    }

    /**
     * Calculates the score of the player by
     * adding up all the left and right pips
     * of the dominoes in the player's hand.
     */
    public void calculateScore() {
        for(int i = 0; i < hand.size(); i++) {
            score += hand.get(i).getLeftNum();
            score += hand.get(i).getRightNum();
        }
    }

    /**
     * Updates the player's score.
     * @param score : the update score
     */
    public void updateScore(int score) {
        this.score = score;
    }

    /**
     * Creates the player's hand by drawing from
     * the boneyard until the hand has the specified
     * number of starting dominoes.
     * @param by : the boneyard of dominoes
     */
    public void createHand(Boneyard by) {
        for (int i = 0; i < startingDomNum; i++) {
            this.addDomToHand(by.drawDom());
        }
    }

    /**
     * Prints the player's hand by looping through
     * the player's hand and getting the string representation
     * of the domino.
     */
    public void printHand() {
        for(int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i).toString() + "  " );
        }
        System.out.println();
    }

    /**
     * Prints out the player's train in two parallel rows.
     */
    public void printTrain() {
        ArrayList<Domino> temp1 = new ArrayList<>();
        ArrayList<Domino> temp2 = new ArrayList<>();
        for (int i = 1; i < train.size(); i++){
            if ((i + 2) % 2 == 0) {
                temp2.add(train.get(i));
            }
            else {
                temp1.add(train.get(i));
            }
        }
        System.out.println(getPlayerNum() + "("+getTrainState()+"):");
        for(int i = 0; i < temp1.size(); i++) {
            System.out.print(temp1.get(i).toString() + " ");
        }
        System.out.println();
        System.out.print("   ");
        for(int i = 0; i < temp2.size(); i++) {
            System.out.print(temp2.get(i).toString() + " ");
        }
        System.out.println();
    }

    /**
     * Gets the player's score
     * @return player score
     */
    public int getScore() {
        return score;
    }

    /**
     * When the player draws, hasDrawn becomes true.
     */
    public void makeHasDrawnTrue() {
        hasDrawn = true;
    }

    /**
     * The player has not drawn, so hasDrawn is false.
     */
    public void makeHasDrawnFalse() { hasDrawn = false; }

    /**
     * If a player plays a double, they can still play
     * on a non double train, so this becomes true.
     */
    public void makeTrueCanPlayNonDoubleTrain() { canPlayNonDoubleTrain = true; }

    /**
     * Once the player plays their extra turn after playing
     * a double, this becomes false;
     */
    public void makeFalseCanPlayNonDoubleTrain() { canPlayNonDoubleTrain = false; }

    /**
     * Checks if the player can play on a train that
     * does not have the double domino.
     * @return if can play on train other than double train
     */
    public boolean checkIfCanPlayNonDoubleTrain() { return canPlayNonDoubleTrain; }

    /**
     * Checks if the player has drawn yet.
     * @return has drawn
     */
    public boolean checkIfDrawn() { return hasDrawn; }

    /**
     * Makes the player the current player.
     */
    public void makePlayerTurn() { playerTurn = true; }

    /**
     * Makes the player not the current player;
     */
    public void makePlayerTurnFalse() { playerTurn = false; }

    /**
     * Checks if the player is the current player.
     * @return the player turn
     */
    public boolean getPlayerTurn() { return playerTurn; }

    /**
     * If the player is a computer player,
     * isComputer is true.
     */
    public void setComputer() { isComputer = true; }

    /**
     * Checks if the player is a computer.
     * @return is computer or not
     */
    public boolean checkIfComputer() { return isComputer; }

    /**
     * Adds a domino to the player's train.
     * @param dom : domino being added
     */
    public void addDomToTrain(Domino dom) { train.add(dom); }

    /**
     * Get the player's name/number.
     * In the form of player#.
     * @return player's number
     */
    public String getPlayerNum() { return playerNum; }

    /**
     * Get the size of player's hand.
     * @return player's hand size
     */
    public int getHandSize() { return hand.size(); }

    /**
     * Get the size of the train.
     * @return train size
     */
    public int getTrainSize() { return train.size(); }

    /**
     * See if the train is true(available to play on)
     * or false(unavailable to play on).
     * @return train's state
     */
    public boolean getTrainState() { return trainState; }

    /**
     * Makes the train's state true.
     */
    public void makeStateTrue() { trainState = true; }

    /**
     * Make the train's state false;
     */
    public void makeStateFalse() { trainState = false; }

    /**
     * Add a domino to player's hand.
     * @param dom : domino being added
     */
    public void addDomToHand(Domino dom) { hand.add(dom); }

    /**
     * Remove a domino from player's hand.
     * @param dom : domino being removed
     */
    public void removeDomFromHand(Domino dom) { hand.remove(dom); }

    /**
     * Get a domino from player's hand.
     * @param i : the index of the domino
     * @return the specified domino
     */
    public Domino getDomino(int i) { return hand.get(i); }

    /**
     * Get a domino from the player's train.
     * @param i : the index of the domino
     * @return the specified domino from the train
     */
    public Domino getTrainDomino(int i) { return train.get(i); }

    /**
     * Get the last domino from the train.
     * @return last domino from train
     */
    public Domino getLastTrainDom() { return train.get(train.size()-1); }

}

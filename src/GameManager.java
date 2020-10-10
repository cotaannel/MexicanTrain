/**
 * @author Annel Cota
 *
 * GameManager class creates the boneyard, the board, and the list
 * of players. This class handles all of the gameplay and logic of
 * the game.
 */

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import static java.lang.System.exit;

public class GameManager {
    private Boneyard boneyard;
    private Board board;
    private Player currentPlayer = new Player();
    private Player mexTrain = new Player();
    private ArrayList<Player> players = new ArrayList<>();
    //holds the player's scores for each round
    private ArrayList<Integer> scoreHolder = new ArrayList<>();
    private ArrayList<ImageView> imageStack;
    private boolean printAllPlayersHands = true;
    private boolean domRotated = false;
    private boolean roundOver = false;
    private boolean changeTurn = true;
    private boolean console;
    private final Scanner in = new Scanner(System.in);
    private int round = 1;
    private int totalPlayers;
    private int numHumanPlayers;
    private int numComPlayers;
    private int totalStartingDom = 0;
    private int centerNum;
    private int guiCenterNum = 9;
    private final int domWidth = 40;
    private final int domHeight = 21;
    //label that gets updated and prints game state updates to GUI
    private String label = " ";

    /**
     * This method starts the game, starts a new turn, and keeps
     * track of when a round is over. When a round is over,
     * it prints out the scores. Once the max rounds have been played,
     * the game is over and it gives the winner of the game.
     */
    public GameManager() {
        Values values = Main.getValues();
        this.console = values.checkIfConsole();
        if(!console) {
            boneyard = new Boneyard(guiCenterNum);
            imageStack = new ArrayList<>();
            if(round > 10) {
                updateLabel("Game Over! \n" + getWinner().getPlayerNum() +" is the winner!");
            }
        } else {
            startGame(totalPlayers, numHumanPlayers, numComPlayers);
            boolean gameOver = false;
            while(!gameOver) {
                startTurn();

                if(checkIfRoundOver()) {
                    whenRoundIsOver();
                }
                if(centerNum == 12) {
                    if(round > 13) {
                        gameOver = true;
                    }
                }
                if(centerNum == 9) {
                    if(round > 10) {
                        gameOver = true;
                    }
                }
            }
            System.out.println("Game Over!");
            System.out.println(getWinner().getPlayerNum() + " is the winner!");
            in.close();
        }
    }

    /**
     * When the round is over, prints scores and starts new round.
     */
    public void whenRoundIsOver() {
        if(console) {
            System.out.println("Round " + round + " is over.");
            getScore();
            System.out.println("Scores:");
            for(Player p : players){
                System.out.println(p.getPlayerNum() + ": " + p.getScore());
            }
            System.out.println();
        } else {
            String s = "Round " + round + " is over.";
            getScore();
            s += "\nScores:\n";
            for(Player p : players){
                s += p.getPlayerNum() + ": " + p.getScore();
            }
            updateLabel(s);
        }
        roundOver = true;
        startNewRound();
    }


    /**
     * Checks to see if the round is over. The round is over when
     * the boneyard is empty and there are no more plays or
     * when a player empties their hand.
     * @return whether or not the round is over
     */
    public boolean checkIfRoundOver() {
        if((boneyard.boneyard.isEmpty() && checkIfNoMorePlays()) || checkIfPlayersHandEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Checks if any of the players hands are empty by checking
     * to see if they are of size 0.
     * @return any hands are empty or not
     */
    public boolean checkIfPlayersHandEmpty() {
        for(Player p : players) {
            if(p.getHandSize() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the players score and updates the score.
     */
    public void getScore() {
        int i = 0;
        for(Player p : players){
            p.calculateScore();
            p.updateScore(scoreHolder.get(i)+p.getScore());
            scoreHolder.set(i, p.getScore());
            i++;
        }
    }

    /**
     * Checks if a double is open by looking at the last domino
     * of each train and checking if it is a double domino.
     * @return double is open or not
     */
    public boolean checkIfDoubleOpen() {
        if(checkIfDomDouble(mexTrain.getLastTrainDom())) {
            return true;
        }
        for(Player p : players) {
            if(checkIfDomDouble(p.getLastTrainDom())){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the player with the train that has an open double.
     * @return player train that has open double
     */
    public Player getOpenDoubleTrain() {
        if(checkIfDomDouble(mexTrain.getLastTrainDom())) {
            return mexTrain;
        }
        for(Player p : players) {
            if(checkIfDomDouble(p.getLastTrainDom())){
                return p;
            }
        }
        return null;
    }

    /**
     * Gets the winner of the game by getting the lowest score of all
     * the players.
     * @return player with lowest score
     */
    public Player getWinner() {
        return players.get(scoreHolder.indexOf(Collections.min(scoreHolder)));
    }

    /**
     * Whichever players turn it is, becomes the current player.
     */
    public void getPlayerTurn() {
        for(Player p : players){
            if(p.getPlayerTurn()) {
                currentPlayer = p;
            }
        }
    }

    /**
     * Checks if a player can draw by checking if there are
     * any playable spots for the player and whether or not they
     * have drawn during their turn already.
     */
    public void checkIfCanDraw() {
        //checking if there are playable plays for when there is a double open
        if(checkIfDoubleOpen() && !currentPlayer.checkIfCanPlayNonDoubleTrain()){
            if(checkPlayableSpotsOpenDouble(currentPlayer)) {
                String s = "Cannot draw, there is a playable domino.";
                if(console) {
                    System.out.println(s);

                } else {
                    updateLabel(s);
                }
            } else {
                // does not let them draw twice in a turn
                if(!currentPlayer.checkIfDrawn()) {
                    currentPlayer.addDomToHand(boneyard.drawDom());
                    currentPlayer.makeHasDrawnTrue();
                    if(console) {
                        System.out.println("Domino was drawn.");
                    } else {
                        updateLabel("Domino was drawn.");
                    }
                    if(console) { printGameState();}

                    startTurn();
                } else {
                    if(console) {
                        System.out.println("Cannot draw again. Either play or skip.");
                    } else {
                        updateLabel("Cannot draw again. Either play or skip.");
                    }
                }
            }
        } else {
            //checks if there are playable plays for current players
            if(checkPlayableSpots(currentPlayer)) {
                if(console) {
                    System.out.println("Cannot draw, there is a playable domino.");
                } else {
                    updateLabel("Cannot draw, there is a playable domino.");
                }
            } else {
                // does not let them draw twice in a turn
                if(!currentPlayer.checkIfDrawn()) {
                    currentPlayer.addDomToHand(boneyard.drawDom());
                    currentPlayer.makeHasDrawnTrue();
                    if(console) {
                        System.out.println("Domino was drawn.");
                        printGameState();
                    } else {
                        updateLabel("Domino was drawn");
                    }
                    startTurn();
                } else {
                    if(console) {
                        System.out.println("Cannot draw again. Either play or skip.");
                    } else {
                        updateLabel("Cannot draw again. Either play or skip.");
                    }
                }
            }
        }
    }

    /**
     * Checks if a player can skip by making sure the player
     * can make no more plays and if they have already drawn first.
     */
    public void checkIfCanSkip() {
        if(checkPlayableSpots(currentPlayer)) {
            //if there are playable spots, can't skip
            String s = "Cannot skip, there is a playable domino.";
            if(console) {
                System.out.println(s);
            } else {
                updateLabel(s);
            }
        } else if(!currentPlayer.checkIfDrawn()) {
            //if there aren't playable spots, but hasn't drawn yet, can't skip
            if(console) {
                System.out.println("Cannot skip, first draw and try to play again.");
            } else {
                updateLabel("Cannot skip, first draw and try to play again.");
            }
        } else {
            //no playable spots & has drawn
            if(console) {
                System.out.println(currentPlayer.getPlayerNum() + " has skipped.");
            } else {
                updateLabel(currentPlayer.getPlayerNum() + " has skipped.");
            }
            board.changePlayerTurn();
            //makes current player's train true since they were unable to play
            currentPlayer.makeStateTrue();
            if(console) { printGameState(); }
        }
    }

    /**
     * Checks if a player can skip when there is double open
     * by making sure the player has no plays for when there is
     * an open double and if they have drawn first.
     */
    public void checkIfCanSkipDoubleOpen() {
        if(checkPlayableSpotsOpenDouble(currentPlayer)) {
            //if there are playable spots, can't skip
            String s = "Cannot skip, there is a playable domino.";
            if(console) {
                System.out.println(s);
            } else {
                updateLabel(s);
            }
        } else if(!currentPlayer.checkIfDrawn()) {
            //if there aren't playable spots, but hasn't drawn yet, can't skip
            if(console) {
                System.out.println("Cannot skip, first draw and try to play again.");
            } else {
                updateLabel("Cannot skip, first draw and try to play again.");
            }
        } else {
            //no playable spots & has drawn
            if(console) {
                System.out.println(currentPlayer.getPlayerNum() + " has skipped.");
            } else {
                updateLabel(currentPlayer.getPlayerNum() + " has skipped.");
            }
            board.changePlayerTurn();
            //makes current player's train true
            currentPlayer.makeStateTrue();
            if(console) { printGameState(); }
        }
    }

    /**
     * Checks if there are any of the players can make
     * any more moves.
     * @return if there are any more plays or not
     */
    public boolean checkIfNoMorePlays() {
        for(Player p : players){
            if(checkPlayableSpots(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if any of the dominoes in the current player's
     * hand can go on any playable train or on the Mexican Train.
     * It returns true if it does have a play.
     * @param currentPlayer : the current player
     * @return if there are playable spots or not
     */
    public boolean checkPlayableSpots(Player currentPlayer) {
        //goes through each dom in players hand
        for(int i = 0; i < currentPlayer.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            //check if domino can go on a playable train
            for(Player p : players){
                if(p.getTrainState() || p.getPlayerTurn()) {
                    Domino lastDom = p.getLastTrainDom();
                    if(checkIfDomMatches(lastDom, dom)) {
                        return true;
                    }
                }
            }
            Player mexTrain = board.getMexTrain();
            Domino lastMexDom = mexTrain.getLastTrainDom();
            if(checkIfDomMatches(lastMexDom, dom)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any of the dominoes in the current player's
     * hand can go on the train with the open double.
     * It returns true if they do have a play.
     * @param currentPlayer : the current player
     * @return if there is a play for the open double or not
     */
    public boolean checkPlayableSpotsOpenDouble(Player currentPlayer) {
        Player doubleTrain = getOpenDoubleTrain();
        for(int i = 0; i < currentPlayer.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            Domino lastDom = doubleTrain.getLastTrainDom();
            if (checkIfDomMatches(lastDom, dom)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is called when player tries to play.
     * Gets the domino and train choice from the player.
     * If it's a computer player, it call's a method to
     * get the computers play choices.
     */
    public void playDominoSetup() {
        int domChoice = 0;
        int trainChoice = 0;
        if(!currentPlayer.checkIfComputer()) {
            System.out.println("Which domino?");
            domChoice = in.nextInt();
            System.out.println("Which train?");
            System.out.println("For Mexican Train:0, Rest of Players as shown:1,2,3,4 & so on.");
            trainChoice = in.nextInt();
            playDominoSetupChoices(domChoice,trainChoice);
        } else {
            if(checkIfDoubleOpen()) {
                getComPlayDouble();
            } else {
                getComPlay();
            }
        }
    }

    /**
     * Sends the domino and train choices to a method
     * to check if they are legal plays.
     * @param domChoice : the domino that player chose
     * @param trainChoice : the train that player chose
     */
    public void playDominoSetupChoices(int domChoice, int trainChoice) {
        Domino playDom = currentPlayer.getDomino(domChoice-1);
        Player playTrain;
        if(trainChoice == 0) {
            playTrain = mexTrain;
        } else {
            playTrain = players.get(trainChoice-1);
        }
        if(!checkIfDoubleOpen()) {
            checkIfLegal(playDom, playTrain);
        } else {
            if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                checkIfLegal(playDom, playTrain);
            } else {
                checkIfLegalDoubleOpen(playDom, playTrain);
            }
        }
    }

    /**
     * Gets the choices for the computer player and sends
     * them to check if they are legal moves.
     * @param playDom : play domino choice of computer player
     * @param playTrain : play train choice of computer player
     */
    public void playDominoSetupChoicesComputer(Domino playDom, Player playTrain) {
        if(!checkIfDoubleOpen()) {
            checkIfLegal(playDom, playTrain);
        } else {
            if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                checkIfLegal(playDom, playTrain);
            } else {
                checkIfLegalDoubleOpen(playDom, playTrain);
            }
        }
    }

    /**
     * Checks if the domino and train selected are a legal play
     * for when there is a double open.
     * @param playDom : domino that is being played
     * @param playTrain : train that domino is being played on
     */
    public void checkIfLegalDoubleOpen(Domino playDom, Player playTrain) {
        Player trainDouble = getOpenDoubleTrain();
        if(playTrain == trainDouble) {
            if(playTrain == mexTrain) {
                Domino lastMexDom = mexTrain.getLastTrainDom();
                if(checkIfDomMatches(lastMexDom, playDom)) {
                    if(checkRotation(lastMexDom, playDom)) {
                        playDom.rotateDom();
                        domRotated = true;
                    }
                    mexTrain.addDomToTrain(playDom);
                    currentPlayer.removeDomFromHand(playDom);
                    changeTurn = true;
                } else {
                    if(console) {
                        System.out.println("That domino cannot go on the Mexican Train.");
                        System.out.println("Please pick again.");
                    } else {
                        updateLabel("That domino cannot go on the Mexican Train. \nPlease pick again.");
                    }
                    changeTurn = false;
                }
            } else {
                //if a player train chosen
                Domino lastDom = playTrain.getLastTrainDom();
                if(checkIfDomMatches(lastDom, playDom)) {
                    if(checkRotation(lastDom, playDom)) {
                        playDom.rotateDom();
                        domRotated = true;
                    }
                    playTrain.addDomToTrain(playDom);
                    currentPlayer.removeDomFromHand(playDom);
                    if(playTrain == currentPlayer) {
                        currentPlayer.makeStateFalse();
                    }
                    changeTurn = true;
                } else {
                    if(console) {
                        System.out.println("That domino does not match. Try again.");
                        printGameState();
                    } else {
                        updateLabel("That domino does not match. Try again.");
                    }
                    changeTurn = false;
                }
            }
        } else {
            if(console) {
                System.out.println("There is a double open that has to be closed.");
            } else {
                updateLabel("There is a double open that has to be closed.");
            }
            changeTurn = false;
        }

        if(changeTurn) {
            board.changePlayerTurn();
        }
        if(console) { printGameState(); }
    }

    /**
     * Checks if the domino and train selected are a legal play.
     * @param playDom : domino that is being played
     * @param playTrain : train that domino is being played on
     */
    public void checkIfLegal(Domino playDom, Player playTrain) {
        if(playTrain == mexTrain) {
            Domino lastMexDom = mexTrain.getLastTrainDom();
            if(checkIfDomMatches(lastMexDom, playDom)) {
                if(checkRotation(lastMexDom, playDom)) {
                    playDom.rotateDom();
                    domRotated = true;
                }
                mexTrain.addDomToTrain(playDom);
                currentPlayer.removeDomFromHand(playDom);
                changeTurn = true;
                if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                    currentPlayer.makeFalseCanPlayNonDoubleTrain();
                }
                if(checkIfDomDouble(playDom)) {
                    if(console) {
                        System.out.println("Double played. Go again.");
                    } else {
                        updateLabel("Double played. Go again.");
                    }
                    changeTurn = false;
                    currentPlayer.makeTrueCanPlayNonDoubleTrain();
                }

            } else {
                if(console) {
                    System.out.println("That domino cannot go on the Mexican Train.");
                    System.out.println("Please pick again.");
                } else {
                    updateLabel("That domino cannot go on the Mexican Train. \nPlease pick again.");
                }
                changeTurn = false;
            }
        } else {
            //if a player train chosen
            if(playTrain == currentPlayer || playTrain.getTrainState()) {
                Domino lastDom = playTrain.getLastTrainDom();
                if(checkIfDomMatches(lastDom, playDom)) {
                    if(checkRotation(lastDom, playDom)) {
                        playDom.rotateDom();
                        domRotated = true;
                    }
                    playTrain.addDomToTrain(playDom);
                    currentPlayer.removeDomFromHand(playDom);
                    if(playTrain == currentPlayer) {
                        currentPlayer.makeStateFalse();
                    }
                    changeTurn = true;
                    if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                        currentPlayer.makeFalseCanPlayNonDoubleTrain();
                    }
                    if(checkIfDomDouble(playDom)) {
                        if(console) {
                            System.out.println("Double played. Go again.");
                        } else {
                            updateLabel("Double played. Go again.");
                        }
                        changeTurn = false;
                        currentPlayer.makeTrueCanPlayNonDoubleTrain();
                    }
                } else {
                    if(console) {
                        System.out.println("That domino does not match. Try again.");
                        printGameState();
                    } else {
                        updateLabel("That domino does not match. \nTry again.");
                    }
                    changeTurn = false;
                }
            } else {
                if(console) {
                    System.out.println("This train is not playable. Try again.");
                } else {
                    updateLabel("This train is not playable. Try again.");
                }
                changeTurn = false;
            }
        }

        if(changeTurn) {
            board.changePlayerTurn();
        }
        if(console) { printGameState(); }
    }

    /**
     * Checks if the domino matches the domino it is being played to.
     * @param lastDom : domino that is getting compared to play domino
     * @param playDom : domino being played
     * @return whether or not the dominoes match
     */
    public boolean checkIfDomMatches(Domino lastDom, Domino playDom) {
        return (lastDom.getRightNum() == playDom.getLeftNum()) ||
                (lastDom.getRightNum() == playDom.getRightNum());
    }

    /**
     * Check if the domino need to be rotated.
     * @param lastDom : domino that is on the board
     * @param playDom : domino that is being played on the board
     * @return whether or not the domino needs to be rotated
     */
    public boolean checkRotation(Domino lastDom, Domino playDom) {
        return lastDom.getRightNum() != playDom.getLeftNum();
    }

    /**
     * Starts a new game. If it is a console game, asks user for input for
     * the total players and so on. If GUI version, those values are brought
     * in when the method is called. The players are then dealt their hands,
     * the board is created, and the players start their turn.
     * @param totalPlayers : total number of players
     * @param numHumanPlayers : total number of human players
     * @param numComPlayers : total number of computer players
     */
    public void startGame(int totalPlayers, int numHumanPlayers, int numComPlayers) {
        if(console) {
            Scanner in = new Scanner(System.in);
            System.out.println("Welcome to Mexican Train!");
            System.out.println("Up to 8 players can play with any mix of human and computer players.");
            System.out.println("Please enter 12 to play with double-12 set. (Only for 8 players or less)" +
                    " Or enter 9 to play with double-9. (Only for 4 players or less");
            centerNum = in.nextInt();
            boneyard = new Boneyard(centerNum);
            System.out.println("Please enter the TOTAL number of players:");
            boneyard = new Boneyard(centerNum);
            totalPlayers = in.nextInt();
            System.out.println("Please enter the number of human players:");
            numHumanPlayers = in.nextInt();
            System.out.println("Please enter the number of computer players:");
            numComPlayers = in.nextInt();
        } else {
            centerNum = 9;
        }
        //changes starting number of dominoes depending on number of players
        if(centerNum == 12) {
            if(totalPlayers == 8) {
                totalStartingDom = 9;
            } else if(totalPlayers == 7) {
                totalStartingDom = 10;
            } else if(totalPlayers == 6) {
                totalStartingDom = 12;
            } else if(totalPlayers == 5) {
                totalStartingDom = 14;
            } else if(totalPlayers == 4) {
                totalStartingDom = 15;
            } else if(totalPlayers == 3) {
                totalStartingDom = 16;
            } else if(totalPlayers == 2) {
                totalStartingDom = 16;
            }
        }
        if(centerNum == 9) {
            if(totalPlayers == 4) {
                totalStartingDom = 10;
            } else if(totalPlayers == 3) {
                totalStartingDom = 13;
            } else if(totalPlayers == 2) {
                totalStartingDom = 15;
            }
        }

        for(int i = 1; i <= numHumanPlayers; i++) {
            Player p = new Player("Player" + i, totalStartingDom);
            p.createHand(boneyard);
            players.add(p);
            scoreHolder.add(0);
        }

        for(int i = 1; i <= numComPlayers; i++) {
            Player p = new Player("Computer" + i, totalStartingDom);
            p.createHand(boneyard);
            p.setComputer();
            players.add(p);
            scoreHolder.add(0);
        }
        this.totalPlayers = totalPlayers;
        this.numHumanPlayers = numHumanPlayers;
        this.numComPlayers = numComPlayers;
        board = new Board(players, centerNum,boneyard);
        mexTrain = board.getMexTrain();
        getPlayerTurn();
        if(console) { printGameState(); }
    }

    /**
     * Decrements the center number by one.
     */
    public void decrementCenterNum() { centerNum--; }

    /**
     * Starts a new round, which creates a new boneyard and
     * deals the players new hands.
     */
    public void startNewRound() {
        decrementCenterNum();
        boneyard = new Boneyard(centerNum);
        players = new ArrayList<>();
        round++;
        for (int i = 1; i <= numHumanPlayers; i++) {
            Player p = new Player("Player" + i, totalStartingDom);
            p.createHand(boneyard);
            players.add(p);
        }
        for (int i = 1; i <= numComPlayers; i++) {
            Player p = new Player("Computer" + i, totalStartingDom);
            p.createHand(boneyard);
            p.setComputer();
            players.add(p);
        }
        board = new Board(players, centerNum,boneyard);
        mexTrain = board.getMexTrain();
        if(console) { printGameState(); }
        roundOver = false;
    }

    /**
     * Checks if a domino is a double by comparing both the
     * left and right pip number.
     * @param dom : domino being checked
     * @return whether the domino is a double or not
     */
    public boolean checkIfDomDouble(Domino dom) { return dom.getLeftNum() == dom.getRightNum(); }

    /**
     * Prints out the game state of the game.
     * This includes the round number, all of the players,
     * the player's hands, and the boneyard. It also prints
     * the board which consists of the center domino, the
     * Mexican Train, and the other player's trains.
     */
    public void printGameState() {
        getPlayerTurn();
        System.out.println("Round " + round);
        System.out.println("GameState:");
        System.out.println("Humans:");
        for(Player p : players){
            if(!p.checkIfComputer()) {
                if(printAllPlayersHands) {
                    System.out.println(p.getPlayerNum() + ": ");
                    p.printHand();
                    //adds labels to dominoes: 1,2,3,...
                    for(int i = 1; i < (p.getHandSize()+1); i++) {
                        System.out.print(i + "      ");
                    }
                } else {
                    System.out.println(p.getPlayerNum() + ": ");
                    if(p == currentPlayer) {
                        p.printHand();
                        //adds labels to dominoes: 1,2,3,...
                        for(int i = 1; i < (p.getHandSize()+1); i++) {
                            System.out.print(i + "      ");
                        }
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("Computers:");
        for(Player p : players){
            if(p.checkIfComputer()) {
                if(printAllPlayersHands) {
                    System.out.println(p.getPlayerNum() + ": ");
                    p.printHand();
                    //adds labels to dominoes: 1,2,3,...
                    for(int i = 1; i < (p.getHandSize()+1); i++) {
                        System.out.print(i + "      ");
                    }
                } else {
                    System.out.println(p.getPlayerNum() + ": ");
                    if(p == currentPlayer) {
                        p.printHand();
                        //adds labels to dominoes: 1,2,3,...
                        for(int i = 1; i < (p.getHandSize()+1); i++) {
                            System.out.print(i + "      ");
                        }
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("Board:");
        board.printBoard();
        System.out.println();
        System.out.println("Boneyard:");
        boneyard.printBoneyard();
        System.out.println();
        if(!roundOver) {
            System.out.println("Current player:");
            for(Player p : players){
                if(p.getPlayerTurn()) {
                    System.out.println(p.getPlayerNum());
                }
            }
        }
        System.out.println("------------------------------------");
    }

    /**
     * Starts current players turn.
     * It asks whether they want to play, skip, or draw
     * and gets their decisions from the users input.
     * If it is a computer player, a check is made to see
     * what they can do, either play, skip, or draw.
     */
    public void startTurn() {
        getPlayerTurn();
        String playerOption;
        if(console) {
            if(!currentPlayer.checkIfComputer()) {
                System.out.println(currentPlayer.getPlayerNum() + "'s Turn");
                System.out.println("[p] play domino");
                System.out.println("[d] draw from boneyard");
                System.out.println("[s] skip turn");
                System.out.println("[q] quit");
                playerOption = in.nextLine();
            } else {
                if(!checkIfDoubleOpen()) {
                    playerOption = getComputerMove();
                } else {
                    playerOption = getComputerMoveDouble();
                }
            }
        } else {
            if(!checkIfDoubleOpen()) {
                playerOption = getComputerMove();
            } else {
                playerOption = getComputerMoveDouble();
            }
        }
        switch (playerOption) {
            case "p":
                playDominoSetup();
                break;
            case "d":
                checkIfCanDraw();
                break;
            case "s":
                if(!checkIfDoubleOpen() || currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                    checkIfCanSkip();
                } else if(checkIfDoubleOpen()) {
                    checkIfCanSkipDoubleOpen();
                }
                break;
            case "q":
                exit(0);
        }
    }

    /**
     * Gets the computer player's by checking if there are any playable spots.
     * If there aren't the computer player will then draw or skip.
     * @return computer player move
     */
    public String getComputerMove() {
        if(checkPlayableSpots(currentPlayer)) {
            return "p";
        } else {
            if(!currentPlayer.checkIfDrawn()) {
                return "d";
            } else {
                return "s";
            }
        }
    }

    /**
     * Gets the computer player's move when there is an
     * open double by checking if there are any playable spots.
     * If there aren't the computer player will then draw or skip.
     * @return computer player move
     */
    public String getComputerMoveDouble() {
        if(checkPlayableSpotsOpenDouble(currentPlayer)) {
            return "p";
        } else {
            if(!currentPlayer.checkIfDrawn()) {
                return "d";
            } else {
                return "s";
            }
        }
    }

    /**
     * Checks to see which dominoes can match any of the playable trains
     * and gets the domino with the highest score.
     */
    public void getComPlay() {
        ArrayList<Domino> tempMatches = new ArrayList<>();
        ArrayList<Player> tempTrainMatches = new ArrayList<>();
        for(int i = 0; i < currentPlayer.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            if(checkIfDomMatches(mexTrain.getLastTrainDom(), dom)) {
                tempMatches.add(dom);
                tempTrainMatches.add(mexTrain);

            }
            for(Player player : players) {
                if(player.getTrainState() || player == currentPlayer) {
                    if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                        tempMatches.add(dom);
                        tempTrainMatches.add(player);
                    }
                }
            }
        }
        Domino dom = getMaxScoreDom(tempMatches);
        Player playTrain = new Player();

        for(Player player : tempTrainMatches) {
            if(player.getTrainState() || player == currentPlayer) {
                if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                    playTrain = player;
                    break;
                }
            }
        }
        playDominoSetupChoicesComputer(dom, playTrain);

    }

    /**
     * Checks to see which dominoes matches the open double train
     * and gets the domino with the highest score.
     */
    public void getComPlayDouble() {
        Player player = getOpenDoubleTrain();
        ArrayList<Domino> tempMatches = new ArrayList<>();
        ArrayList<Player> tempTrainMatches = new ArrayList<>();
        for(int i = 0; i < currentPlayer.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                tempMatches.add(dom);
            }
        }
        Domino dom = getMaxScoreDom(tempMatches);
        playDominoSetupChoicesComputer(dom, player);
    }

    /**
     * Gets the domino from the possible playable dominoes
     * list and returns the domino with the highest points.
     * @param temp : list of possible playable dominoes
     * @return domino with highest score
     */
    public Domino getMaxScoreDom(ArrayList temp) {
        System.out.println(temp);
        ArrayList<Integer> scoreTemp = new ArrayList<>();
        for(int i = 0; i < temp.size(); i++) {
            Domino dom = (Domino) temp.get(i);
            int s = dom.getPipTotal();
            scoreTemp.add(s);
        }
        int index;
        if(scoreTemp.size() > 1) {
            index = Collections.max(scoreTemp);
        } else {
            System.out.println(scoreTemp);
            index = scoreTemp.get(0);
        }

        int indexOfTemp = 0;
        for(int i = 0; i < scoreTemp.size(); i++) {
            if(scoreTemp.get(i) == index) {
                indexOfTemp = i;
            }
        }
        return (Domino) temp.get(indexOfTemp);
    }

    /**
     * Updates the components of the GUI.
     * @param currentPlayerLbl : label of current player
     * @param currentPlayerLbl1 : label of current player's hand
     * @param playerHand : HBox of player's hand
     */
    public void updateComponents(Label currentPlayerLbl, Label currentPlayerLbl1, HBox playerHand) {
        getPlayerTurn();
        currentPlayerLbl.setText("Current Player: " + currentPlayer.getPlayerNum());
        currentPlayerLbl1.setText(currentPlayer.getPlayerNum() + "'s hand:");
        updatePlayerHand(playerHand);
    }

    /**
     * Updates the board part of the GUI.
     * @param center : HBox of center domino
     * @param p1Box1 : first VBox of player 1 train
     * @param p1Box2 : second VBox of player 1 train
     * @param p2Box1 : first VBox of player 2 train
     * @param p2Box1 : second VBox of player 2 train
     * @param p3Box1 : first HBox of player 3 train
     * @param p3Box2 : second HBox of player 3 train
     * @param p4Box1 : first HBox of player 4 train
     * @param p4Box2 : second HBox of player 4 train
     * @param mexTrainBox1 : first HBox of Mexican Train
     * @param mexTrainBox2 : second HBox of Mexican Train
     * @param p1Label : label of player 1 train
     * @param p2Label : label of player 2 train
     * @param p3Label : label of player 3 train
     * @param p4Label : label of player 4 train
     * @param mexTrainLabel : label of Mexican Train
     * @param gameStateLabel : label of game state
     */
    public void updateBoard(HBox center, VBox p1Box1, VBox p1Box2, VBox p2Box1, VBox p2Box2,
                            HBox p3Box1, HBox p3Box2, HBox p4Box1, HBox p4Box2,
                            HBox mexTrainBox1, HBox mexTrainBox2, Label p1Label, Label p2Label, Label p3Label,
                            Label p4Label, Label mexTrainLabel, Label gameStateLabel) {
        updateCenter(center);
        updateMexTrain(mexTrainBox1, mexTrainBox2, mexTrain, mexTrainLabel);
        //updates trains depending on the number of players that there is
        if(totalPlayers == 2) {
            updateTrain1(p1Box1, p1Box2, players.get(0), p1Label);
            updateTrain2(p2Box1, p2Box2, players.get(1), p2Label);
        } else if(totalPlayers == 3) {
            updateTrain1(p1Box1, p1Box2, players.get(0), p1Label);
            updateTrain2(p2Box1, p2Box2, players.get(1), p2Label);
            updateTrain3(p3Box1, p3Box2, players.get(2), p3Label);
        } else if(totalPlayers == 4) {
            updateTrain1(p1Box1, p1Box2, players.get(0), p1Label);
            updateTrain2(p2Box1, p2Box2, players.get(1), p2Label);
            updateTrain3(p3Box1, p3Box2, players.get(2), p3Label);
            updateTrain4(p4Box1, p4Box2, players.get(3), p4Label);
        }
        gameStateLabel.setText(getLabel());
        //clears game state updates
        label = " ";
    }

    /**
     * Updates the label of the game state that is in the GUI.
     * @param s : game state label
     */
    public void updateLabel(String s) { label = s; }

    /**
     * Gets the label of the game state that is in the GUI.
     * @return game state label
     */
    public String getLabel() { return label; }

    /**
     * Updates player one train.
     * It gets the images of the dominoes from the train
     * and adds them to the VBox.
     * @param p11 : first VBox of player 1 train
     * @param p12 : second VBox of player 1 train
     * @param player1 : first player
     * @param p1Label : label of player 1 train
     */
    public void updateTrain1(VBox p11, VBox p12, Player player1, Label p1Label) {
        int rotation = 270;
        if(domRotated) {
            rotation = rotation + 180;
        }
        p11.getChildren().clear();
        p12.getChildren().clear();
        p1Label.setText(player1.getPlayerNum() + " Train" + "(" + player1.getTrainState() + ")");
        ArrayList<Domino> temp1 = new ArrayList<>();
        ArrayList<Domino> temp2 = new ArrayList<>();
        //used to make the train form two parallel rows
        for (int i = 1; i < player1.getTrainSize(); i++){
            if ((i + 2) % 2 == 0) {
                temp2.add(player1.getTrainDomino(i));
            }
            else {
                temp1.add(player1.getTrainDomino(i));
            }
        }
        ArrayList<ImageView> imageStack1 = new ArrayList<>();
        ArrayList<ImageView> imageStack2 = new ArrayList<>();

        for(int i = 0; i < temp1.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp1.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + rotation);
            currentImageView.setImage(currentDominoImage);
            imageStack1.add(currentImageView);
        }

        for(int i = 0; i < temp2.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp2.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 270);
            currentImageView.setImage(currentDominoImage);
            imageStack2.add(currentImageView);
        }
        p11.getChildren().addAll(imageStack1);
        p12.getChildren().addAll(imageStack2);
        domRotated = false;
    }

    /**
     * Updates player two train.
     * It gets the images of the dominoes from the train
     * and adds them to the HBox.
     * @param p21 : first VBox of player 2 train
     * @param p22 : second VBox of player 2 train
     * @param player2 : second player
     * @param p2Label : label of player 2 train
     */
    public void updateTrain2(VBox p21, VBox p22, Player player2, Label p2Label) {
        int rotation = 270;
        if(domRotated) {
            rotation = rotation + 180;
        }
        p21.getChildren().clear();
        p22.getChildren().clear();
        p2Label.setText(player2.getPlayerNum() + " Train" + "(" + player2.getTrainState() + ")");
        ArrayList<Domino> temp1 = new ArrayList<>();
        ArrayList<Domino> temp2 = new ArrayList<>();
        //used to make the train form two parallel rows
        for (int i = 1; i < player2.getTrainSize(); i++){
            if ((i + 2) % 2 == 0) {
                temp2.add(player2.getTrainDomino(i));
            }
            else {
                temp1.add(player2.getTrainDomino(i));
            }
        }
        ArrayList<ImageView> imageStack1 = new ArrayList<>();
        ArrayList<ImageView> imageStack2 = new ArrayList<>();

        for(int i = 0; i < temp1.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp1.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 270);
            currentImageView.setImage(currentDominoImage);
            imageStack1.add(currentImageView);
        }

        for(int i = 0; i < temp2.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp2.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 270);
            currentImageView.setImage(currentDominoImage);
            imageStack2.add(currentImageView);
        }
        p21.getChildren().addAll(imageStack1);
        p22.getChildren().addAll(imageStack2);
        domRotated = false;
    }

    /**
     * Updates player three train.
     * It gets the images of the dominoes from the train
     * and adds them to the VBox.
     * @param p31 : first HBox of player 3 train
     * @param p32 : second HBox of player 3 train
     * @param player3 : third player
     * @param p3Label : label of player 3 train
     */
    public void updateTrain3(HBox p31, HBox p32, Player player3, Label p3Label) {
        int rotation = 180;
        if(domRotated) {
            rotation = rotation + 180;
        }
        p31.getChildren().clear();
        p32.getChildren().clear();
        p3Label.setText(player3.getPlayerNum() + " Train" + "(" + player3.getTrainState() + ")");
        ArrayList<Domino> temp1 = new ArrayList<>();
        ArrayList<Domino> temp2 = new ArrayList<>();
        //used to make the train form two parallel rows
        for (int i = 1; i < player3.getTrainSize(); i++){
            if ((i + 2) % 2 == 0) {
                temp2.add(player3.getTrainDomino(i));
            }
            else {
                temp1.add(player3.getTrainDomino(i));
            }
        }
        ArrayList<ImageView> imageStack1 = new ArrayList<>();
        ArrayList<ImageView> imageStack2 = new ArrayList<>();

        for(int i = 0; i < temp1.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp1.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 180);
            currentImageView.setImage(currentDominoImage);
            imageStack1.add(currentImageView);
        }

        for(int i = 0; i < temp2.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp2.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 180);
            currentImageView.setImage(currentDominoImage);
            imageStack2.add(currentImageView);
        }
        p31.getChildren().addAll(imageStack1);
        p32.getChildren().addAll(imageStack2);
        domRotated = false;
    }

    /**
     * Updates player four train.
     * It gets the images of the dominoes from the train
     * and adds them to the VBox.
     * @param p41 : first HBox of player 4 train
     * @param p42 : second HBox of player 4 train
     * @param player4 : fourth player
     * @param p4Label : label of player 4 train
     */
    public void updateTrain4(HBox p41, HBox p42, Player player4, Label p4Label) {
        int rotation = 180;
        if(domRotated) {
            rotation = rotation + 180;
        }
        p41.getChildren().clear();
        p42.getChildren().clear();
        p4Label.setText(player4.getPlayerNum() + " Train" + "(" + player4.getTrainState() + ")");
        ArrayList<Domino> temp1 = new ArrayList<>();
        ArrayList<Domino> temp2 = new ArrayList<>();
        //used to make the train form two parallel rows
        for (int i = 1; i < player4.getTrainSize(); i++){
            if ((i + 2) % 2 == 0) {
                temp2.add(player4.getTrainDomino(i));
            }
            else {
                temp1.add(player4.getTrainDomino(i));
            }
        }
        ArrayList<ImageView> imageStack1 = new ArrayList<>();
        ArrayList<ImageView> imageStack2 = new ArrayList<>();

        for(int i = 0; i < temp1.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp1.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 180);
            currentImageView.setImage(currentDominoImage);
            imageStack1.add(currentImageView);
        }

        for(int i = 0; i < temp2.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp2.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 180);
            currentImageView.setImage(currentDominoImage);
            imageStack2.add(currentImageView);
        }
        p41.getChildren().addAll(imageStack1);
        p42.getChildren().addAll(imageStack2);
        domRotated = false;
    }

    /**
     * Updates the Mexican Train in the GUI.
     * It gets the images of the dominoes from the Mexican
     * Train and adds them to the HBoxes.
     * @param mexTrainBox1 : first HBox of the Mexican Train
     * @param mexTrainBox2 : second HBox of the Mexican Train
     * @param mexTrain : Mexican Train player
     * @param mexTrainLabel : Mexican Train label
     */
    public void updateMexTrain(HBox mexTrainBox1, HBox mexTrainBox2, Player mexTrain, Label mexTrainLabel) {
        int rotation = 180;
        if(domRotated) {
            rotation = rotation + 180;
        }
        mexTrainBox1.getChildren().clear();
        mexTrainBox2.getChildren().clear();
        mexTrainLabel.setText(mexTrain.getPlayerNum() + "(" + mexTrain.getTrainState() + ")");
        ArrayList<Domino> temp1 = new ArrayList<>();
        ArrayList<Domino> temp2 = new ArrayList<>();
        //used to make the train form two parallel rows
        for (int i = 1; i < mexTrain.getTrainSize(); i++){
            if ((i + 2) % 2 == 0) {
                temp2.add(mexTrain.getTrainDomino(i));
            }
            else {
                temp1.add(mexTrain.getTrainDomino(i));
            }
        }
        ArrayList<ImageView> imageStack1 = new ArrayList<>();
        ArrayList<ImageView> imageStack2 = new ArrayList<>();

        for(int i = 0; i < temp1.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp1.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 180);
            currentImageView.setImage(currentDominoImage);
            imageStack1.add(currentImageView);
        }

        for(int i = 0; i < temp2.size(); i++) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(temp2.get(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 180);
            currentImageView.setImage(currentDominoImage);
            imageStack2.add(currentImageView);
        }
        mexTrainBox1.getChildren().addAll(imageStack1);
        mexTrainBox2.getChildren().addAll(imageStack2);
        domRotated = false;
    }

    /**
     * Updates the center domino in the HBox on the GUI.
     * @param center : HBox of center domino
     */
    public void updateCenter(HBox center) {
        center.getChildren().clear();
        ImageView currentImageView = new ImageView();
        Image currentDominoImage = new Image(board.getCenter().getDomImage(),60,40,true,true);
        currentImageView.setImage(currentDominoImage);
        center.getChildren().addAll(currentImageView);
    }

    /**
     * Updates the dominoes in the HBox of the players hand
     * that is in the GUI.
     * @param playerHands : HBox of player's hand
     */
    public void updatePlayerHand(HBox playerHands) {
        playerHands.getChildren().clear();
        imageStack.clear();
        for(int i = 0; i < currentPlayer.getHandSize(); ++i) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(currentPlayer.getDomino(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setImage(currentDominoImage);
            imageStack.add(currentImageView);
        }
        playerHands.getChildren().addAll(imageStack);
    }
}

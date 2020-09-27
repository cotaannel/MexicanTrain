import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import static java.lang.System.exit;

public class GameManager {
    private Boneyard boneyard;
    private ArrayList<Player> players;
    private ArrayList<Integer> scoreHolder = new ArrayList<>();
    private boolean roundOver = false;
    private boolean doublePlayed = false;
    private boolean doubleOpen = false;
    private boolean changeTurn = true;
    private Board board;
    private Player currentPlayer = new Player();
    private Player doubleDom = new Player();
    private final Scanner in = new Scanner(System.in);
    private int round = 9;
    private int numHumanPlayers;
    private int numComPlayers;
    private int totalStartingDom = 0;
    private int centerNum = 9;

    public GameManager() {
        boneyard = new Boneyard(centerNum);
        startGame();
        boolean gameOver = false;
        while(!gameOver) {
            startTurn();
            if(boneyard.boneyard.isEmpty() || checkIfNoMorePlays()) {
                System.out.println("Round " + round + " is over.");
                getScore();
                System.out.println("Scores:");
                for(Player p : players){
                    System.out.println(p.getPlayerNum() + ": " + p.getScore());
                }
                System.out.println();
                roundOver = true;
                startNewRound();
            }
            if(round > 10) {
                gameOver = true;
            }
        }
        System.out.println("Game Over!");
        System.out.println(getWinner().getPlayerNum() + " is the winner!");
        in.close();
    }

    public void getScore() {
        int i = 0;
        for(Player p : players){
            p.calculateScore();
            p.updateScore(scoreHolder.get(i)+p.getScore());
            scoreHolder.set(i, p.getScore());
            i++;
        }
    }

    public Player getWinner() {
        return players.get(scoreHolder.indexOf(Collections.min(scoreHolder)));
    }

    public void getPlayerTurn() {
        for(Player p : players){
            if(p.getPlayerTurn()) {
                currentPlayer = p;
            }
        }
    }

    public void startTurn() {
        getPlayerTurn();
        System.out.println(currentPlayer.getPlayerNum() + "'s Turn");
        System.out.println("[p] play domino");
        System.out.println("[d] draw from boneyard");
        System.out.println("[s] skip turn");
        System.out.println("[q] quit");
        String playerOption = in.nextLine();
        switch (playerOption) {
            case "p":
                playDominoSetup();
                if(doublePlayed) {
                    doublePlayed = false;
                }
//                if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
//                    currentPlayer.makeFalseCanPlayNonDoubleTrain();
//                }
                break;
            case "d":
                //if there is a playable domino, cannot draw
                if(checkPlayableSpots(currentPlayer)) {
                    System.out.println("Cannot draw, there is a playable domino.");
                } else {
                    // does not let them draw twice in a turn
                    if(!currentPlayer.checkIfDrawn()) {
                        currentPlayer.addDomToHand(boneyard.drawDom());
                        currentPlayer.makeHasDrawnTrue();
                        System.out.println("Domino was drawn.");
                        printGameState();
                        startTurn();
                    } else {
                        System.out.println("Cannot draw again. Either play or skip.");
                    }
                }
                break;
            case "s":
                //have to fix, if double played, and cannot make another play, cannot skip until drawn
                if(checkPlayableSpots(currentPlayer)) {
                    //if there are playable spots, can't skip
                    System.out.println("Cannot skip, there is a playable domino.");
                } else if(!currentPlayer.checkIfDrawn()) {
                    //if there aren't playable spots, but hasn't drawn yet, can't skip
                    //has to draw first
                    System.out.println("Cannot skip, first draw and try to play again.");
                } else {
                    //no playable spots & has drawn
                    //makes current player's train true
                    if(currentPlayer.checkIfDrawn()) {
                        currentPlayer.makeStateTrue();
                    }
                    board.changePlayerTurn();
                    currentPlayer.makeStateTrue();
                    printGameState();
                }
                break;
            case "q":
                exit(0);
        }

    }

    public boolean checkIfNoMorePlays() {
        for(Player p : players){
            if(checkPlayableSpots(p)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkPlayableSpots(Player currentPlayer) {
        //goes through each dom in players hand
        for(int i = 0; i < currentPlayer.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            Domino center = board.getCenterDom();
            //check if dom can go on a playable train
            for(Player p : players){
                if(p.checkIfTrainEmpty()) {
                    if(center.getRightNum() == dom.getLeftNum()
                            || center.getRightNum() == dom.getRightNum()) {
                        return true;
                    }
                } else {
                    if(p.getTrainState() || p.getPlayerTurn()) {
                        Domino lastDom = p.getLastTrainDom();
                        if(lastDom.getRightNum() == dom.getLeftNum() ||
                                lastDom.getRightNum() == dom.getRightNum()) {
                            return true;
                        }
                    }
                }
            }
            Player mexTrain = board.getMexTrain();
            Domino lastMexDom = mexTrain.getLastTrainDom();
            if(lastMexDom.getRightNum() == dom.getLeftNum() ||
                    lastMexDom.getRightNum() == dom.getRightNum()) {
                return true;
            }
        }
        return false;
    }

    public void playDominoSetup() {
        System.out.println("Which domino?");
        int domChoice = in.nextInt();
        System.out.println("Which train?");
        System.out.println("For Mexican Train:0, Rest of Players as shown:1,2,3,4 & so on.");
        int trainChoice = in.nextInt();
        checkIfLegal(domChoice, trainChoice);
    }

    public void checkIfLegal(int dom, int train) {
        Domino playDom = currentPlayer.getDomino(dom-1);
        Domino center = board.getCenterDom();
        Player mexTrain = board.getMexTrain();

        Player playTrain;
        if(train == 0) {
            playTrain = mexTrain;
        } else {
            playTrain = players.get(train-1);
        }

        if(playTrain == mexTrain) {
            Domino lastDom = mexTrain.getLastTrainDom();
            if(lastDom.getRightNum() == playDom.getLeftNum() ||
                    lastDom.getRightNum() == playDom.getRightNum()) {
                if(lastDom.getRightNum() != playDom.getLeftNum()) {
                    playDom.rotateTile();
                }
                mexTrain.addDomToTrain(playDom);
                currentPlayer.removeDomFromHand(playDom);
                if(checkIfDomDouble(playDom)) {
                    doubleDom = mexTrain;
                    doublePlayed = true;
                    System.out.println("Double played. Go again.");
                    currentPlayer.makeTrueCanPlayNonDoubleTrain();
                }

            } else {
                System.out.println("That domino cannot go on the Mexican Train.");
                System.out.println("Please pick again.");
                changeTurn = false;
            }
        } else {
            //if a player train chosen
            if(playTrain == currentPlayer || playTrain.getTrainState()) {
                //checks if train is empty
                Domino lastDom = playTrain.getLastTrainDom();
                if(lastDom.getRightNum() == playDom.getLeftNum() ||
                        lastDom.getRightNum() == playDom.getRightNum()) {
                    if (lastDom.getRightNum() != playDom.getLeftNum()) {
                        playDom.rotateTile();
                    }
                    playTrain.addDomToTrain(playDom);
                    currentPlayer.removeDomFromHand(playDom);
                    currentPlayer.makeStateFalse();
                    changeTurn = true;
                    if(checkIfDomDouble(playDom)) {
                        doubleDom = playTrain;
                        doublePlayed = true;
                        System.out.println("Double played. Go again.");
                        currentPlayer.makeTrueCanPlayNonDoubleTrain();
                    }
                } else {
                    System.out.println("That domino does not match.");
                    changeTurn = false;
                }
            } else {
                System.out.println("This train is not playable. Try again.");
                changeTurn = false;
            }
        }









//        if(!doubleOpen || currentPlayer.checkIfCanPlayNonDoubleTrain()) {
//            if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
//                currentPlayer.makeFalseCanPlayNonDoubleTrain();
//                doubleOpen = true;
//            }
//            if(playTrain == mexTrain) {
//                Domino lastDom = mexTrain.getLastTrainDom();
//                if(lastDom.getRightNum() == playDom.getLeftNum() ||
//                        lastDom.getRightNum() == playDom.getRightNum()) {
//                    if(lastDom.getRightNum() != playDom.getLeftNum()) {
//                        playDom.rotateTile();
//                    }
//                    mexTrain.addDomToTrain(playDom);
//                    currentPlayer.removeDomFromHand(playDom);
//                    if(checkIfDomDouble(playDom)) {
//                        doubleDom = mexTrain;
//                        doublePlayed = true;
//                        System.out.println("Double played. Go again.");
//                        currentPlayer.makeTrueCanPlayNonDoubleTrain();
//                    }
//
//                } else {
//                    System.out.println("That domino cannot go on the Mexican Train.");
//                    System.out.println("Please pick again.");
//                    changeTurn = false;
//                }
//            } else {
//                //if a player train chosen
//                if(playTrain == currentPlayer || playTrain.getTrainState()) {
//                    //checks if train is empty
//                    if(playTrain.checkIfTrainEmpty()) {
//                        if(center.getRightNum() == playDom.getLeftNum()
//                                || center.getRightNum() == playDom.getRightNum()) {
//                            if(center.getRightNum() != playDom.getLeftNum()) {
//                                playDom.rotateTile();
//                            }
//                            playTrain.addDomToTrain(playDom);
//                            currentPlayer.removeDomFromHand(playDom);
//                            currentPlayer.makeStateFalse();
//                            playTrain.makeTrainNonempty();
//                            changeTurn = true;
//                            if(checkIfDomDouble(playDom)) {
//                                doubleDom = playTrain;
//                                doublePlayed = true;
//                                System.out.println("Double played. Go again.");
//                                currentPlayer.makeTrueCanPlayNonDoubleTrain();
//                            }
//                        } else {
//                            System.out.println("This domino cannot go there. Try again.");
//                            changeTurn = false;
//                        }
//                    } else {
//                        //if train not empty
//                        Domino lastDom = playTrain.getLastTrainDom();
//                        if(lastDom.getRightNum() == playDom.getLeftNum() ||
//                                lastDom.getRightNum() == playDom.getRightNum()) {
//                            if (lastDom.getRightNum() != playDom.getLeftNum()) {
//                                playDom.rotateTile();
//                            }
//                            playTrain.addDomToTrain(playDom);
//                            currentPlayer.removeDomFromHand(playDom);
//                            currentPlayer.makeStateFalse();
//                            changeTurn = true;
//                            if(checkIfDomDouble(playDom)) {
//                                doubleDom = playTrain;
//                                doublePlayed = true;
//                                System.out.println("Double played. Go again.");
//                                currentPlayer.makeTrueCanPlayNonDoubleTrain();
//                            }
//                        } else {
//                            System.out.println("That domino does not match.");
//                            changeTurn = false;
//                        }
//                    }
//                } else {
//                    System.out.println("This train is not playable. Try again.");
//                    changeTurn = false;
//                }
//            }
//        } else if(doubleOpen){
////            System.out.println("There is a double open that needs to be played.");
////            changeTurn = false;
//            if(playTrain != doubleDom) {
//                System.out.println("There is a double open that needs to be played.");
//                changeTurn = false;
//            } else {
//                doubleOpen = false;
//                checkIfLegal(dom,train);
//            }
//        }

        if (!doublePlayed) {
            if(changeTurn) {
                board.changePlayerTurn();
            }
            doublePlayed = false;
            if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                currentPlayer.makeFalseCanPlayNonDoubleTrain();
                System.out.println("There is a double open that needs to be closed");
            }
        }
        if(changeTurn) {
            printGameState();
        }
    }

    public void startGame() {
        Scanner in = new Scanner(System.in);
        players = new ArrayList<>();

        System.out.println("Welcome to Mexican Train!");
        System.out.println("Up to 4 players can play with any mix of human and computer players.");
        System.out.println("Please enter the TOTAL number of players:");
        int totalPlayers = in.nextInt();


        //changes starting number of dominoes depending on number of players
        if(totalPlayers == 4) {
            totalStartingDom = 10;
        } else if(totalPlayers == 3) {
            totalStartingDom = 13;
        } else if(totalPlayers == 2) {
            totalStartingDom = 15;
        }

        System.out.println("Please enter the number of human players:");
        numHumanPlayers = in.nextInt();
        for(int i = 1; i <= numHumanPlayers; i++) {
            Player p = new Player("Player" + i, totalStartingDom);
            p.createHand(boneyard);
            players.add(p);
            scoreHolder.add(0);
        }

        System.out.println("Please enter the number of computer players:");
        numComPlayers = in.nextInt();
        for(int i = 1; i <= numComPlayers; i++) {
            Player p = new Player("Computer" + i, totalStartingDom);
            p.createHand(boneyard);
            p.setComputer();
            players.add(p);
            scoreHolder.add(0);
        }
        board = new Board(players, centerNum);
        printGameState();
    }
    public void decrementCenterNum() {
        centerNum--;
    }

    public void startNewRound() {
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
        decrementCenterNum();
        board = new Board(players, centerNum);
        printGameState();
        roundOver = false;

    }

    public boolean checkIfDomDouble(Domino dom) {
        return dom.getLeftNum() == dom.getRightNum();
    }

    public void printGameState() {
        System.out.println("Round " + round);
        System.out.println("GameState:");
        System.out.println("Humans:");
        for(Player p : players){
            if(!p.checkIfComputer()) {
                p.printHand();
                //adds labels to dominoes: 1,2,3,...
                for(int i = 1; i < (p.getHandSize()+1); i++) {
                    System.out.print(i + "      ");
                }
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("Computers:");
        for(Player p : players){
            if(p.checkIfComputer()) {
                p.printHand();
                //adds labels to dominoes: 1,2,3,...
                for(int i = 1; i < (p.getHandSize()+1); i++) {
                    System.out.print(i + "      ");
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
}

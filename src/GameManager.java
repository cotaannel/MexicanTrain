import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

public class GameManager {
    private Boneyard boneyard;
    private ArrayList<Player> players;
    private boolean gameOver = false;
    private boolean roundOver = false;
    private boolean doublePlayed = false;
    private boolean doubleOpen = false;
    private Board board;
    private Player currentPlayer = new Player();
    private Player playTrain = new Player();
    private Player doubleDom = new Player();
    private Scanner in = new Scanner(System.in);
    private int round = 1;
    private int numHumanPlayers;
    private int numComPlayers;
    private int totalStartingDom = 0;
    private int centerNum = 9;

    public GameManager() {
        boneyard = new Boneyard(centerNum);
        startGame();
        while(!roundOver) {
            startTurn();
            //if
            if(boneyard.boneyard.isEmpty()) {
                System.out.println("Round " + round + " is over.");
                roundOver = true;
                startNewRound();
            }
        }




        in.close();
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
                if(checkPlayableSpots()) {
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
                if(checkPlayableSpots()) {
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

    public boolean checkPlayableSpots() {
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

        if(train == 0) {
            playTrain = mexTrain;
        } else {
            playTrain = players.get(train-1);
        }
        if(!doubleOpen || currentPlayer.checkIfCanPlayNonDoubleTrain()) {
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
                    playDominoSetup();
                }
            } else {
                //if a player train chosen
                if(playTrain == currentPlayer || playTrain.getTrainState()) {
                    //checks if train is empty
                    if(playTrain.checkIfTrainEmpty()) {
                        if(center.getRightNum() == playDom.getLeftNum()
                                || center.getRightNum() == playDom.getRightNum()) {
                            if(center.getRightNum() != playDom.getLeftNum()) {
                                playDom.rotateTile();
                            }
                            playTrain.addDomToTrain(playDom);
                            currentPlayer.removeDomFromHand(playDom);
                            currentPlayer.makeStateFalse();
                            playTrain.makeTrainNonempty();
                            if(checkIfDomDouble(playDom)) {
                                doubleDom = playTrain;
                                doublePlayed = true;
                                System.out.println("Double played. Go again.");
                                currentPlayer.makeTrueCanPlayNonDoubleTrain();
                            }
                        } else {
                            System.out.println("This domino cannot go there. Try again.");
                            playDominoSetup();
                        }
                    } else {
                        //if train not empty
                        Domino lastDom = playTrain.getLastTrainDom();
                        if(lastDom.getRightNum() == playDom.getLeftNum() ||
                                lastDom.getRightNum() == playDom.getRightNum()) {
                            if (lastDom.getRightNum() != playDom.getLeftNum()) {
                                playDom.rotateTile();
                            }
                            playTrain.addDomToTrain(playDom);
                            currentPlayer.removeDomFromHand(playDom);
                            currentPlayer.makeStateFalse();
                            if(checkIfDomDouble(playDom)) {
                                doubleDom = playTrain;
                                doublePlayed = true;
                                System.out.println("Double played. Go again.");
                                currentPlayer.makeTrueCanPlayNonDoubleTrain();
                            }
                        } else {
                            System.out.println("That domino does not match.");
                            //playDominoSetup();
                            startTurn();
                        }
                    }
                } else {
                    System.out.println("This train is not playable. Try again.");
                    playDominoSetup();
                }
            }
        } else {
            if(playTrain != doubleDom) {
                System.out.println("There is a double open that needs to be played.");
                //playDominoSetup();
                startTurn();
            } else {
                doubleOpen = false;
                checkIfLegal(dom,train);

            }
        }

        //have to fix, if double played, and cannot make another play, cannot skip until drawed




        if (!doublePlayed) {
            board.changePlayerTurn();
            doublePlayed = false;
            if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                currentPlayer.makeFalseCanPlayNonDoubleTrain();
                System.out.println("There is a double open that needs to be closed");
            }
        }
        if(doublePlayed) {
            if(doubleDom != playTrain) {
                doubleOpen = true;

            }
        }
        printGameState();
    }


    public void startGame() {
        Scanner in = new Scanner(System.in);
        players = new ArrayList<>();

        System.out.println("Welcome to Mexican Train!");
        System.out.println("Up to 4 players can play with any mix of human and computer players.");
        System.out.println("Please enter the TOTAL number of players:");
        int totalPlayers = in.nextInt();
        //changes starting number of dominoes depending on number of players
        if (totalPlayers == 4) {
            totalStartingDom = 10;
        } else if(totalPlayers == 3) {
            totalStartingDom = 13;
        } else if(totalPlayers == 2) {
            totalStartingDom = 15;
        }

        System.out.println("Please enter the number of human players:");
        numHumanPlayers = in.nextInt();
        for (int i = 1; i <= numHumanPlayers; i++) {
            Player p = new Player("Player" + i, totalStartingDom);
            p.createHand(boneyard);
            players.add(p);
        }

        System.out.println("Please enter the number of computer players:");
        numComPlayers = in.nextInt();
        for (int i = 1; i <= numComPlayers; i++) {
            Player p = new Player("Computer" + i, totalStartingDom);
            p.createHand(boneyard);
            p.setComputer();
            players.add(p);
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
        if(dom.getLeftNum() == dom.getRightNum()) {
            return true;
        }
        return false;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import static java.lang.System.exit;

public class GameManager {
    private Boneyard boneyard;
    private Rules rules;
    private ArrayList<Player> players;
    public ArrayList<Integer> scoreHolder = new ArrayList<>();
    private boolean roundOver = false;
    private boolean doublePlayed = false;
    private boolean changeTurn = true;
    private Board board;
    //private Controller controller;
    private Player currentPlayer = new Player();
    private Player doubleTrain = new Player();
    //private ArrayList<mexicantrain.Domino> doubleTrain = new ArrayList<>();
    private final Scanner in = new Scanner(System.in);
    private int round = 9;
    private int numHumanPlayers;
    private int numComPlayers;
    public int totalStartingDom = 0;
    private int centerNum = 9;
    private int canPlay = 0;

    private boolean consoleGame = false;

    public GameManager(ArrayList players, ArrayList scoreHolder) {
        if(consoleGame) {
            boneyard = new Boneyard(centerNum);
            startGame();
        }

        boolean gameOver = false;
//        while(!gameOver) {
//            if(consoleGame) { startTurn(); }
//            System.out.println(players);
//            if(boneyard.boneyard.isEmpty() || checkIfNoMorePlays()) {
//                getScore();
//                if(consoleGame) {
//                    System.out.println("Round " + round + " is over.");
//                    System.out.println("Scores:");
//                }
////                for(Player p : players){
////                    if(consoleGame) {System.out.println(p.getPlayerNum() + ": " + p.getScore());}
////
////                }
//                System.out.println();
//                roundOver = true;
//                startNewRound();
//            }
//            if(round > 10) {
//                gameOver = true;
//            }
//        }
        if(consoleGame) {
            System.out.println("Game Over!");
            System.out.println(getWinner().getPlayerNum() + " is the winner!");
            in.close();
        }
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
                break;
            case "d":
                //if there is a playable domino, cannot draw
                if(doublePlayed && !currentPlayer.checkIfCanPlayNonDoubleTrain()){
                    if(checkPlayableSpotsOpenDouble(currentPlayer)) {
                        System.out.println("Cannot draw, there is a playable domino.");
                    } else {
                        // does not let them draw twice in a turn
                        if(!currentPlayer.checkIfDrawn()) {
                            currentPlayer.addDomToHand(boneyard.drawDom());
                            currentPlayer.makeHasDrawnTrue();
                            System.out.println("mexicantrain.Domino was drawn.");
                            printGameState();
                            startTurn();
                        } else {
                            System.out.println("Cannot draw again. Either play or skip.");
                        }
                    }
                } else {
                    if(checkPlayableSpots(currentPlayer)) {
                        System.out.println("Cannot draw, there is a playable domino.");
                    } else {
                        // does not let them draw twice in a turn
                        if(!currentPlayer.checkIfDrawn()) {
                            currentPlayer.addDomToHand(boneyard.drawDom());
                            currentPlayer.makeHasDrawnTrue();
                            System.out.println("mexicantrain.Domino was drawn.");
                            printGameState();
                            startTurn();
                        } else {
                            System.out.println("Cannot draw again. Either play or skip.");
                        }
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
            //check if dom can go on a playable train
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

    public boolean checkPlayableSpotsOpenDouble(Player currentPlayer) {
        for(int i = 0; i < currentPlayer.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            Domino lastDom = doubleTrain.getLastTrainDom();
            if (checkIfDomMatches(lastDom, dom)) {
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
        if(!doublePlayed) {
            checkIfLegal(domChoice, trainChoice);
        } else if(doublePlayed){
            if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                checkIfLegal(domChoice, trainChoice);

            } else {
                checkIfLegalDoubleOpen(domChoice, trainChoice);
            }
        }
    }

    public void checkIfLegalDoubleOpen(int dom, int train) {
        Domino playDom = currentPlayer.getDomino(dom-1);
        Player mexTrain = board.getMexTrain();

        Player playTrain;
        if(train == 0) {
            playTrain = mexTrain;
        } else {
            playTrain = players.get(train-1);
        }
        if(playTrain == doubleTrain) {
            if(playTrain == mexTrain) {
                Domino lastMexDom = mexTrain.getLastTrainDom();
                if(checkIfDomMatches(lastMexDom, playDom)) {
                    if(checkRotation(lastMexDom, playDom)) {
                        playDom.rotateTile();
                    }
                    mexTrain.addDomToTrain(playDom);
                    currentPlayer.removeDomFromHand(playDom);
                    changeTurn = true;
                    doublePlayed = false;
                } else {
                    System.out.println("That domino cannot go on the Mexican Train.");
                    System.out.println("Please pick again.");
                    changeTurn = false;
                }
            } else {
                //if a player train chosen
                if(playTrain == currentPlayer || playTrain.getTrainState()) {
                    Domino lastDom = playTrain.getLastTrainDom();
                    if(checkIfDomMatches(lastDom, playDom)) {
                        if(checkRotation(lastDom, playDom)) {
                            playDom.rotateTile();
                        }
                        playTrain.addDomToTrain(playDom);
                        currentPlayer.removeDomFromHand(playDom);
                        currentPlayer.makeStateFalse();
                        changeTurn = true;
                        doublePlayed = false;
                    } else {
                        System.out.println("That domino does not match. Try again.");
                        printGameState();
                        changeTurn = false;
                    }
                } else {
                    System.out.println("This train is not playable. Try again.");
                    changeTurn = false;
                }
            }
        } else {
            System.out.println("There is a double open.");
            changeTurn = false;
        }

        if(changeTurn) {
            board.changePlayerTurn();
        }
        printGameState();
    }

    public void checkIfLegal(int dom, int train) {
        Domino playDom = currentPlayer.getDomino(dom-1);
        Player mexTrain = board.getMexTrain();

        Player playTrain;
        if(train == 0) {
            playTrain = mexTrain;
        } else {
            playTrain = players.get(train-1);
        }

        if(playTrain == mexTrain) {
            Domino lastMexDom = mexTrain.getLastTrainDom();
            if(checkIfDomMatches(lastMexDom, playDom)) {
                if(checkRotation(lastMexDom, playDom)) {
                    playDom.rotateTile();
                }
                mexTrain.addDomToTrain(playDom);
                currentPlayer.removeDomFromHand(playDom);
                changeTurn = true;
                if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                    if(playTrain == doubleTrain) {
                        doublePlayed = false;
                    } else {
                        doublePlayed = true;
                    }

                    currentPlayer.makeFalseCanPlayNonDoubleTrain();
                }
                if(rules.checkIfDomDouble(playDom)) {
                    doubleTrain = mexTrain;
                    doublePlayed = true;
                    System.out.println("Double played. Go again.");
                    changeTurn = false;
                    canPlay++;
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
                Domino lastDom = playTrain.getLastTrainDom();
                if(checkIfDomMatches(lastDom, playDom)) {
                    if(checkRotation(lastDom, playDom)) {
                        playDom.rotateTile();
                    }
                    playTrain.addDomToTrain(playDom);
                    currentPlayer.removeDomFromHand(playDom);
                    currentPlayer.makeStateFalse();
                    changeTurn = true;
                    if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                        if(playTrain == doubleTrain) {
                            doublePlayed = false;
                        } else {
                            doublePlayed = true;
                        }
                        currentPlayer.makeFalseCanPlayNonDoubleTrain();
                    }
                    if(rules.checkIfDomDouble(playDom)) {
                        doubleTrain = mexTrain;
                        doublePlayed = true;
                        System.out.println("Double played. Go again.");
                        changeTurn = false;
                        canPlay++;
                        currentPlayer.makeTrueCanPlayNonDoubleTrain();
                    }
                } else {
                    System.out.println("That domino does not match. Try again.");
                    printGameState();
                    changeTurn = false;
                }
            } else {
                System.out.println("This train is not playable. Try again.");
                changeTurn = false;
            }
        }

        if(changeTurn) {
            board.changePlayerTurn();
        }
        printGameState();
    }

    public boolean checkIfDomMatches(Domino lastDom, Domino playDom) {
        return (lastDom.getRightNum() == playDom.getLeftNum()) || (lastDom.getRightNum() == playDom.getRightNum());
    }

    public boolean checkRotation(Domino lastDom, Domino playDom) {
        return lastDom.getRightNum() != playDom.getLeftNum();
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
            Player p = new Player("mexicantrain.Player" + i, totalStartingDom);
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

//    public boolean checkIfDomDouble(Domino dom) {
//        if(dom.getLeftNum() == dom.getRightNum()) {
//            return true;
//        }
//        return false;
//    }

    public ArrayList getPLayers() {
        return players;
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
        System.out.println("mexicantrain.Board:");
        board.printBoard();
        System.out.println();
        System.out.println("mexicantrain.Boneyard:");
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
        System.out.println("Double played"+doublePlayed);
    }
}

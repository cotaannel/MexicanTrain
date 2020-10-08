import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import static java.lang.System.exit;

public class GameManager {
    private Boneyard boneyard;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Integer> scoreHolder = new ArrayList<>();
    private ArrayList<ImageView> imageStack;
    private boolean printAllPlayersHands = true;
    private boolean roundOver = false;
    private boolean changeTurn = true;
    private Board board;
    private Player currentPlayer = new Player();
    private Player mexTrain = new Player();
    private final Scanner in = new Scanner(System.in);
    private int round = 0;
    private int totalPlayers;
    private int numHumanPlayers;
    private int numComPlayers;
    private int totalStartingDom = 0;
    private int centerNum;
    private int guiCenterNum = 9;
    private final int domWidth = 40;
    private final int domHeight = 21;
    private boolean console;
    private Values values;

    public GameManager() {
        values = Main.getValues();
        this.console = values.checkIfConsole();

        if(!console) {
            boneyard = new Boneyard(guiCenterNum);
            imageStack = new ArrayList<>();
        } else {
            startGame(totalPlayers, numHumanPlayers, numComPlayers);
            boolean gameOver = false;
            while(!gameOver) {
                startTurn();

                if(boneyard.boneyard.isEmpty() || checkIfNoMorePlays() || checkIfPlayersHandEmpty()) {
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

    public boolean checkIfPlayersHandEmpty() {
        for(Player p : players) {
            if(p.getHandSize() == 0) {
                return true;
            }
        }
        return false;
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

    public void checkIfCanDraw() {
        //if there is a playable domino, cannot draw
        //checking if there are playable plays for when there is a double open
        if(checkIfDoubleOpen() && !currentPlayer.checkIfCanPlayNonDoubleTrain()){
            if(checkPlayableSpotsOpenDouble(currentPlayer)) {
                if(console) { System.out.println("Cannot draw, there is a playable domino."); }
            } else {
                // does not let them draw twice in a turn
                if(!currentPlayer.checkIfDrawn()) {
                    currentPlayer.addDomToHand(boneyard.drawDom());
                    currentPlayer.makeHasDrawnTrue();
                    if(console) { System.out.println("Domino was drawn."); }
                    printGameState();
                    startTurn();
                } else {
                    if(console) { System.out.println("Cannot draw again. Either play or skip."); }
                }
            }
        } else {
            if(checkPlayableSpots(currentPlayer)) {
                if(console) { System.out.println("Cannot draw, there is a playable domino."); }
            } else {
                // does not let them draw twice in a turn
                if(!currentPlayer.checkIfDrawn()) {
                    currentPlayer.addDomToHand(boneyard.drawDom());
                    currentPlayer.makeHasDrawnTrue();
                    if(console) { System.out.println("Domino was drawn."); }
                    printGameState();
                    startTurn();
                } else {
                    if(console) { System.out.println("Cannot draw again. Either play or skip."); }
                }
            }
        }
    }

    public void checkIfCanSkip() {
        //have to fix, if double played, and cannot make another play, cannot skip until drawn
        if(checkPlayableSpots(currentPlayer)) {
            //if there are playable spots, can't skip
            if(console) { System.out.println("Cannot skip, there is a playable domino."); }
        } else if(!currentPlayer.checkIfDrawn()) {
            //if there aren't playable spots, but hasn't drawn yet, can't skip
            //has to draw first
            if(console) { System.out.println("Cannot skip, first draw and try to play again."); }
        } else {
            //no playable spots & has drawn
            //makes current player's train true
            if(console) { System.out.println(currentPlayer.getPlayerNum() + " has skipped."); }
            board.changePlayerTurn();
            currentPlayer.makeStateTrue();
            if(console) { printGameState(); }
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

    public void playDominoSetup() {
        System.out.println("Which domino?");
        int domChoice = in.nextInt();
        System.out.println("Which train?");
        System.out.println("For Mexican Train:0, Rest of Players as shown:1,2,3,4 & so on.");
        int trainChoice = in.nextInt();
        if(!checkIfDoubleOpen()) {
            checkIfLegal(domChoice, trainChoice);
        } else {
            if(currentPlayer.checkIfCanPlayNonDoubleTrain()) {
                checkIfLegal(domChoice, trainChoice);
            } else {
                checkIfLegalDoubleOpen(domChoice, trainChoice);
            }
        }
    }

    public void checkIfLegalDoubleOpen(int dom, int train) {
        Domino playDom = currentPlayer.getDomino(dom-1);
        Player trainDouble = getOpenDoubleTrain();
        Player playTrain;
        if(train == 0) {
            playTrain = mexTrain;
        } else {
            playTrain = players.get(train-1);
        }
        if(playTrain == trainDouble) {
            if(playTrain == mexTrain) {
                Domino lastMexDom = mexTrain.getLastTrainDom();
                if(checkIfDomMatches(lastMexDom, playDom)) {
                    if(checkRotation(lastMexDom, playDom)) {
                        playDom.rotateTile();
                    }
                    mexTrain.addDomToTrain(playDom);
                    currentPlayer.removeDomFromHand(playDom);
                    changeTurn = true;
                } else {
                    if(console) {
                        System.out.println("That domino cannot go on the Mexican Train.");
                        System.out.println("Please pick again.");
                    }
                    changeTurn = false;
                }
            } else {
                //if a player train chosen
                Domino lastDom = playTrain.getLastTrainDom();
                if(checkIfDomMatches(lastDom, playDom)) {
                    if(checkRotation(lastDom, playDom)) {
                        playDom.rotateTile();
                    }
                    playTrain.addDomToTrain(playDom);
                    currentPlayer.removeDomFromHand(playDom);
                    currentPlayer.makeStateFalse();
                    changeTurn = true;
                } else {
                    if(console) {
                        System.out.println("That domino does not match. Try again.");
                        printGameState();
                    }
                    changeTurn = false;
                }
            }
        } else {
            if(console) { System.out.println("There is a double open that has to be closed."); }
            changeTurn = false;
        }

        if(changeTurn) {
            board.changePlayerTurn();
        }
        if(console) { printGameState(); }
    }

    public void checkIfLegal(int dom, int train) {
        Domino playDom = currentPlayer.getDomino(dom-1);
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
                    currentPlayer.makeFalseCanPlayNonDoubleTrain();
                }
                if(checkIfDomDouble(playDom)) {
                    if(console) { System.out.println("Double played. Go again."); }
                    changeTurn = false;
                    currentPlayer.makeTrueCanPlayNonDoubleTrain();
                }

            } else {
                if(console) {
                    System.out.println("That domino cannot go on the Mexican Train.");
                    System.out.println("Please pick again.");
                }
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
                        currentPlayer.makeFalseCanPlayNonDoubleTrain();
                    }
                    if(checkIfDomDouble(playDom)) {
                        if(console) { System.out.println("Double played. Go again."); }
                        changeTurn = false;
                        currentPlayer.makeTrueCanPlayNonDoubleTrain();
                    }
                } else {
                    if(console) {
                        System.out.println("That domino does not match. Try again.");
                        printGameState();
                    }
                    changeTurn = false;
                }
            } else {
                if(console) { System.out.println("This train is not playable. Try again."); }
                changeTurn = false;
            }
        }

        if(changeTurn) {
            board.changePlayerTurn();
        }
        if(console) { printGameState(); }
    }

    public boolean checkIfDomMatches(Domino lastDom, Domino playDom) {
        return (lastDom.getRightNum() == playDom.getLeftNum()) || (lastDom.getRightNum() == playDom.getRightNum());
    }

    public boolean checkRotation(Domino lastDom, Domino playDom) {
        return lastDom.getRightNum() != playDom.getLeftNum();
    }

    public void startGame(int totalPlayers, int numHumanPlayers, int numComPlayers) {
        this.totalPlayers = totalPlayers;
        this.numHumanPlayers = numHumanPlayers;
        this.numComPlayers = numComPlayers;
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
        board = new Board(players, centerNum,boneyard);
        mexTrain = board.getMexTrain();

        getPlayerTurn();
        if(console) { printGameState(); }
    }

    public void decrementCenterNum() { centerNum--; }

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
        board = new Board(players, centerNum,boneyard);
        mexTrain = board.getMexTrain();
        printGameState();
        roundOver = false;
    }

    public boolean checkIfDomDouble(Domino dom) { return dom.getLeftNum() == dom.getRightNum(); }

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

    public void startTurn() {
        getPlayerTurn();
        if(currentPlayer.checkIfComputer()) {
            for(Player p : players) {
                if(p.checkIfCanPlayNonDoubleTrain()) {
                    p.makeFalseCanPlayNonDoubleTrain();
                    currentPlayer = p;
                }
            }
            if(checkIfDoubleOpen()) {
                getComputerMoveDouble();
            } else {
                System.out.println(currentPlayer.getPlayerNum() + " start turn get com move");
                getComputerMove();
                startTurn();

            }
            //startTurn();
            System.out.println("in start turn before change player turn" + currentPlayer.getPlayerNum());
            //board.changePlayerTurn();
            //getPlayerTurn();
            System.out.println("in start turn change player turn" + currentPlayer.getPlayerNum());
        }
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
                checkIfCanDraw();
                break;
            case "s":
                checkIfCanSkip();
                break;
            case "q":
                exit(0);
        }

    }

    public void getComputerMove() {
        System.out.println("get com move");
        if(checkPlayableSpots(currentPlayer)) {
            getComPlay();
            board.changePlayerTurn();
            getPlayerTurn();
            startTurn();
        } else {
            if(!currentPlayer.checkIfDrawn()) {
                checkIfCanDraw();
            } else {
                checkIfCanSkip();
                getPlayerTurn();
                //startTurn();
            }
        }
    }

    public void getComputerMoveDouble() {
        if(checkPlayableSpotsOpenDouble(currentPlayer)) {
            getComPlayDouble();
            getPlayerTurn();
            board.changePlayerTurn();
            startTurn();
        } else {
            if(!currentPlayer.checkIfDrawn()) {
                checkIfCanDraw();
            } else {
                checkIfCanSkip();
                getPlayerTurn();
                startTurn();
            }
        }
    }

    //checks to see which domino matches computers train and which of those
    //that match have the highest points
    public void getComPlay() {
        boolean played = false;
        System.out.println("getcomplay");
        ArrayList<Domino> tempMatches = new ArrayList<>();
        for(int i = 0; i < currentPlayer.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            for(Player player : players) {
                if(player.getTrainState() || player == currentPlayer) {
                    if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                        tempMatches.add(dom);
                    }
                }
                if(checkIfDomMatches(mexTrain.getLastTrainDom(), dom)) {
                    tempMatches.add(dom);
                }
            }
        }
        Domino dom = getMaxScoreDom(tempMatches);

        for(Player player : players) {
            if(player == currentPlayer) {
                if(!played) {
                    if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                        if(checkRotation(player.getLastTrainDom(), dom)) {
                            dom.rotateTile();
                        }
                        player.addDomToTrain(dom);
                        currentPlayer.removeRandomDomFromHand(dom);
                        printGameState();
                        played = true;
                    }
                }
            }
            if(player.getTrainState()) {
                if(!played) {
                    if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                        if(checkRotation(player.getLastTrainDom(), dom)) {
                            dom.rotateTile();
                        }
                        player.addDomToTrain(dom);
                        currentPlayer.removeRandomDomFromHand(dom);
                        printGameState();
                        played = true;
                    }
                }
            }
        }
        if(!played) {
            if(checkIfDomMatches(mexTrain.getLastTrainDom(), dom)) {
                if(checkRotation(mexTrain.getLastTrainDom(), dom)) {
                    dom.rotateTile();
                }
                mexTrain.addDomToTrain(dom);
                currentPlayer.removeRandomDomFromHand(dom);
                printGameState();
            }
        }
        if(checkIfDomDouble(dom)) {
            currentPlayer.makeTrueCanPlayNonDoubleTrain();
        }
    }

    //checks to see which domino matches double open train and which of those
    //that match have the highest points
    public void getComPlayDouble() {
        Player player = getOpenDoubleTrain();
        ArrayList<Domino> tempMatches = new ArrayList<>();
        for(int i = 0; i < currentPlayer.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                tempMatches.add(dom);
            }
        }
        Domino dom = getMaxScoreDom(tempMatches);
        if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
            if(checkRotation(player.getLastTrainDom(), dom)) {
                dom.rotateTile();
            }
            player.addDomToTrain(dom);
            board.changePlayerTurn();
            currentPlayer.removeRandomDomFromHand(dom);
            printGameState();
        }
    }

    public Domino getMaxScoreDom(ArrayList temp) {
        System.out.println(temp);
        ArrayList<Integer> scoreTemp = new ArrayList<>();
        for(int i = 0; i < temp.size(); i++) {
            Domino dom = (Domino) temp.get(i);
            int s = dom.getPipTotal(dom);
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

    ////////////////GUI PART
    public void updateComponents(Label currentPlayerLbl, HBox playerHand) {
        getPlayerTurn();
        currentPlayerLbl.setText("Current Player: " + currentPlayer.getPlayerNum());
        updatePlayerHand(playerHand);
    }

    public void updateBoard(HBox center, VBox p1, HBox p2, VBox p3, VBox p4, HBox mexTrainBox,
                            Label p1Label, Label p2Label, Label p3Label, Label p4Label, Label mexTrainLabel,
                            Button mexTrainButton, Button p1Button, Button p2Button, Button p3Button, Button p4Button) {
        updateCenter(center);
        updateMexTrain(mexTrainBox, mexTrain, mexTrainLabel);
        mexTrainButton.setText("Play on Mexican Train");
        if(totalPlayers == 2) {
            updateTrain1(p1, players.get(0), p1Label);
            p1Button.setText("Play on P1 Train");
            updateTrain2(p2, players.get(1), p2Label);
            p2Button.setText("Play on P2 Train");
        } else if(totalPlayers == 3) {
            updateTrain1(p1, players.get(0), p1Label);
            p1Button.setText("Play on P1 Train");
            updateTrain2(p2, players.get(1), p2Label);
            p2Button.setText("Play on P2 Train");
            updateTrain3(p3, players.get(2), p3Label);
            p3Button.setText("Play on P3 Train");
        } else if(totalPlayers == 4) {
            updateTrain1(p1, players.get(0), p1Label);
            p1Button.setText("Play on P1 Train");
            updateTrain2(p2, players.get(1), p2Label);
            p2Button.setText("Play on P2 Train");
            updateTrain3(p3, players.get(2), p3Label);
            p3Button.setText("Play on P3 Train");
            updateTrain4(p4, players.get(3), p4Label);
            p4Button.setText("Play on P4 Train");
        }
    }

    public void updateTrain1(VBox p1, Player player1, Label p1Label) {
        p1Label.setText(player1.getPlayerNum() + " Train" + "(" + player1.getTrainState() + ")");
        ArrayList<ImageView> imageStack = new ArrayList<>();
        p1.getChildren().clear();
        for(int i = 0; i < player1.getTrainSize(); ++i) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(player1.getTrainDomino(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate()+90);
            currentImageView.setImage(currentDominoImage);
            imageStack.add(currentImageView);
        }
        p1.getChildren().addAll(imageStack);
    }
    public void updateTrain2(HBox p2, Player player2, Label p2Label) {
        p2Label.setText(player2.getPlayerNum() + " Train" + "(" + player2.getTrainState() + ")");
        ArrayList<ImageView> imageStack = new ArrayList<>();
        p2.getChildren().clear();
        for(int i = 0; i < player2.getTrainSize(); ++i) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(player2.getTrainDomino(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate());
            currentImageView.setImage(currentDominoImage);
            imageStack.add(currentImageView);
        }
        p2.getChildren().addAll(imageStack);
    }
    public void updateTrain3(VBox p3, Player player3, Label p3Label) {
        p3Label.setText(player3.getPlayerNum() + " Train" + "(" + player3.getTrainState() + ")");
        ArrayList<ImageView> imageStack = new ArrayList<>();
        p3.getChildren().clear();
        for(int i = 0; i < player3.getTrainSize(); ++i) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(player3.getTrainDomino(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 270);// + 180);
            currentImageView.setImage(currentDominoImage);
            imageStack.add(currentImageView);
        }
        p3.getChildren().addAll(imageStack);
    }
    public void updateTrain4(VBox p4, Player player4, Label p4Label) {
        p4Label.setText(player4.getPlayerNum() + " Train" + "(" + player4.getTrainState() + ")");
        ArrayList<ImageView> imageStack = new ArrayList<>();
        p4.getChildren().clear();
        for(int i = 0; i < player4.getTrainSize(); ++i) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(player4.getTrainDomino(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate() + 270);// + 74.1);
            currentImageView.setImage(currentDominoImage);
            imageStack.add(currentImageView);
        }
        p4.getChildren().addAll(imageStack);
    }
    public void updateMexTrain(HBox mexTrainBox, Player mexTrain, Label mexTrainLabel) {
        mexTrainLabel.setText(mexTrain.getPlayerNum() + "(" + mexTrain.getTrainState() + ")");
        ArrayList<ImageView> imageStack = new ArrayList<>();
        mexTrainBox.getChildren().clear();
        for(int i = 0; i < mexTrain.getTrainSize(); ++i) {
            ImageView currentImageView = new ImageView();
            Image currentDominoImage = new Image(mexTrain.getTrainDomino(i).getDomImage(),domWidth,domHeight,true,true);
            currentImageView.setRotate(currentImageView.getRotate());// + 118);
            currentImageView.setImage(currentDominoImage);
            imageStack.add(currentImageView);
        }
        mexTrainBox.getChildren().addAll(imageStack);
    }

    public void updateCenter(HBox center) {
        center.getChildren().clear();
        ImageView currentImageView = new ImageView();
        Image currentDominoImage = new Image(board.getCenter().getDomImage(),60,40,true,true);
        currentImageView.setImage(currentDominoImage);
        center.getChildren().addAll(currentImageView);

    }

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

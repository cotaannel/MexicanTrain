import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import static java.lang.System.exit;

public class GameManager {
    private Boneyard boneyard;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Integer> scoreHolder = new ArrayList<>();
    private boolean roundOver = false;
    private boolean changeTurn = true;
    private Board board;
    private Player currentPlayer = new Player();
    private Player mexTrain = new Player();
    private final Scanner in = new Scanner(System.in);
    private int round = 9;
    private int totalPlayers;
    private int numHumanPlayers;
    private int numComPlayers;
    private int totalStartingDom = 0;
    private int centerNum;

    public GameManager() {
        startGame();
        boolean gameOver = false;
        while(!gameOver) {
            //checkStartTurn();
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

    public void startTurn() {
        getPlayerTurn();
        if(currentPlayer.checkIfComputer()) {
            if(checkIfDoubleOpen()) {
                getComputerMoveDouble(currentPlayer);
            } else {
                getComputerMove(currentPlayer);
            }
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

    public void checkIfCanDraw() {
        //if there is a playable domino, cannot draw
        //checking if there are playable plays for when there is a double open
        if(checkIfDoubleOpen() && !currentPlayer.checkIfCanPlayNonDoubleTrain()){
            if(checkPlayableSpotsOpenDouble(currentPlayer)) {
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
        } else {
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
        }
    }

    public void checkIfCanSkip() {
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
            System.out.println(currentPlayer.getPlayerNum() + " has skipped.");
            board.changePlayerTurn();
            currentPlayer.makeStateTrue();
            printGameState();
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
                    System.out.println("That domino cannot go on the Mexican Train.");
                    System.out.println("Please pick again.");
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
                    System.out.println("That domino does not match. Try again.");
                    printGameState();
                    changeTurn = false;
                }
            }
        } else {
            System.out.println("There is a double open that has to be closed.");
            changeTurn = false;
        }

        if(changeTurn) {
            board.changePlayerTurn();
        }
        printGameState();
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
                    System.out.println("Double played. Go again.");
                    changeTurn = false;
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
                        currentPlayer.makeFalseCanPlayNonDoubleTrain();
                    }
                    if(checkIfDomDouble(playDom)) {

                        System.out.println("Double played. Go again.");
                        changeTurn = false;
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
        board = new Board(players, centerNum,boneyard);
        mexTrain = board.getMexTrain();
        printGameState();
        roundOver = false;

    }

    public boolean checkIfDomDouble(Domino dom) {
        return dom.getLeftNum() == dom.getRightNum();
    }

    public void printGameState() {
        getPlayerTurn();
        System.out.println("Round " + round);
        System.out.println("GameState:");
        System.out.println("Humans:");
        for(Player p : players){
            if(!p.checkIfComputer()) {
                System.out.println(p.getPlayerNum() + ": ");
                //if(p == currentPlayer) {
                    p.printHand();
                    //adds labels to dominoes: 1,2,3,...
                    for(int i = 1; i < (p.getHandSize()+1); i++) {
                        System.out.print(i + "      ");
                    }
               // }
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("Computers:");
        for(Player p : players){
            if(p.checkIfComputer()) {
                System.out.println(p.getPlayerNum() + ": ");
                //if(p == currentPlayer) {
                    p.printHand();
                    //adds labels to dominoes: 1,2,3,...
                    for(int i = 1; i < (p.getHandSize()+1); i++) {
                        System.out.print(i + "      ");
                    //}
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

    public void getComputerMove(Player p) {
        if(checkPlayableSpots(p)) {
            getComPlay(p);
        } else {
            if(!p.checkIfDrawn()) {
                checkIfCanDraw();
                //getComputerMove();
            } else {
                checkIfCanSkip();
            }
        }
    }
    public void getComputerMoveDouble(Player p) {
        if(checkPlayableSpotsOpenDouble(p)) {
            getComPlayDouble(p);
        } else {
            if(!p.checkIfDrawn()) {
                checkIfCanDraw();
                //getComputerMove();
            } else {
                checkIfCanSkip();
            }
        }
    }

    //checks to see which domino matches computers train and which of those
    //that match have the highest points

    // check if double
    public void getComPlay(Player p) {
        boolean played = false;
        System.out.println("getcomplay");
        ArrayList<Domino> tempMatches = new ArrayList<>();
        for(int i = 0; i < p.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            for(Player player : players) {
                if(player.getTrainState()) {
                    if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                        tempMatches.add(dom);
                    }
                }
                if(checkIfDomMatches(mexTrain.getLastTrainDom(), dom)) {
                    tempMatches.add(dom);
                }
            }

        }
        Domino dom = organizeComCombo(tempMatches);
        for(Player player : players) {
            if(player.getTrainState() || player == currentPlayer) {
                if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                    if(checkRotation(player.getLastTrainDom(), dom)) {
                        dom.rotateTile();
                    }
                    player.addDomToTrain(dom);
                    if(!checkIfDomDouble(dom)) {
                        board.changePlayerTurn();
                    }
                    p.removeRandomDomFromHand(dom);
                    printGameState();
                    played = true;
                }
            }
        }
        if(played == false) {
            if(checkIfDomMatches(mexTrain.getLastTrainDom(), dom)) {
                if(checkRotation(mexTrain.getLastTrainDom(), dom)) {
                    dom.rotateTile();
                }
                mexTrain.addDomToTrain(dom);
                if(!checkIfDomDouble(dom)) {
                    board.changePlayerTurn();
                }
                p.removeRandomDomFromHand(dom);
                printGameState();
            }
        }

    }

    //checks to see which domino matches computers train and which of those
    //that match have the highest points
    public void getComPlayDouble(Player p) {
        Player player = getOpenDoubleTrain();
        ArrayList<Domino> tempMatches = new ArrayList<>();
        for(int i = 0; i < p.getHandSize(); i++) {
            Domino dom = currentPlayer.getDomino(i);
            if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
                tempMatches.add(dom);
            }

        }
        Domino dom = organizeComCombo(tempMatches);
        if(checkIfDomMatches(player.getLastTrainDom(), dom)) {
            if(checkRotation(player.getLastTrainDom(), dom)) {
                dom.rotateTile();
            }
            player.addDomToTrain(dom);
            board.changePlayerTurn();
            p.removeRandomDomFromHand(dom);
            printGameState();
        }
    }


    public Domino organizeComCombo(ArrayList temp) {
        ArrayList<Integer> scoreTemp = new ArrayList<>();
        for(int i = 0; i < temp.size(); i++) {
            Domino dom = (Domino) temp.get(i);
            int s = dom.getPipTotal(dom);
            scoreTemp.add(s);
        }
        System.out.println(scoreTemp);
        int index = Collections.max(scoreTemp);
        int indexOfTemp = 0;
        for(int i = 0; i < scoreTemp.size(); i++) {
            if(scoreTemp.get(i) == index) {
                indexOfTemp = i;
            }
        }
        return (Domino) temp.get(indexOfTemp);
    }


}

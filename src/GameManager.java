import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

public class GameManager {
    private Boneyard boneyard;
    private ArrayList<Player> players;
    private boolean gameOver = false;
    private Board brd;
    private Player currentPlayer = new Player();
    private Scanner in = new Scanner(System.in);

    public GameManager() {
        boneyard = new Boneyard();
        startGame();
        while(!gameOver) {
            startTurn();
        };

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
                break;
            case "d":
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
                break;
            case "s":
                brd.changePlayerTurn();
                currentPlayer.makeStateTrue();
                break;
            case "q":
                exit(0);
        }
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
        Domino center = brd.getCenterDom();
        //if mexican train chosen
        if(train == 0) {
            Player mexTrain = brd.getMexTrain();
            //mexTrain.printTrain();
            Domino lastDom = mexTrain.getLastTrainDom();
            if(lastDom.getRightNum() == playDom.getLeftNum() ||
                    lastDom.getRightNum() == playDom.getRightNum()) {
                if(lastDom.getRightNum() != playDom.getLeftNum()) {
                    playDom.rotateTile();
                }
                mexTrain.addDomToTrain(playDom);
                currentPlayer.removeDomFromHand(playDom);

            } else {
                System.out.println("That domino cannot go on the Mexican Train.");
                System.out.println("Please pick again.");
                playDominoSetup();
            }
        } else {
            //if a player train chosen
            Player playTrain = players.get(train-1);
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
                        playTrain.makeTrainNonempty();
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
                    } else {
                        System.out.println("That domino does not match.");
                        playDominoSetup();
                    }
                }
            } else {
                System.out.println("This train is not playable. Try again.");
                playDominoSetup();
            }
        }
        brd.changePlayerTurn();
        printGameState();
    }


    public void startGame() {
        Scanner in = new Scanner(System.in);
        players = new ArrayList<>();
        int totalStartingDom = 0;

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
        int numHumanPlayers = in.nextInt();
        for (int i = 1; i <= numHumanPlayers; i++) {
            Player p = new Player("Player" + i, totalStartingDom);
            p.createHand(boneyard);
            players.add(p);
        }

        System.out.println("Please enter the number of computer players:");
        int numComPlayers = in.nextInt();
        for (int i = 1; i <= numComPlayers; i++) {
            Player p = new Player("Computer" + i, totalStartingDom);
            p.createHand(boneyard);
            p.setComputer();
            players.add(p);
        }
        brd = new Board(players, boneyard.centerNum);
        printGameState();
    }

    public void printGameState() {
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
        brd.printBoard();
        System.out.println();
        System.out.println("Boneyard:");
        boneyard.printBoneyard();
        System.out.println();
        System.out.println("Current player:");
        for(Player p : players){
            if(p.getPlayerTurn()) {
                System.out.println(p.getPlayerNum());
            }
        }
        System.out.println("------------------------------------");
    }
}

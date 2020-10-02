import java.util.ArrayList;

public class Board {
    private final ArrayList<Player> players;
    private Player mexTrain;
    private int playerTurnInc = 0;
    private Domino center;
    private String imagePath = "Dominos/" + center + "|" + center + ".png";

    public Board(ArrayList<Player> players, int n, Boneyard by) {
        this.players = players;
        center = new Domino(n,n,imagePath);
        mexTrain = new Player("Mexican Train",0);
        mexTrain.addDomToTrain(new Domino(0, by.centerNum, imagePath));
        mexTrain.makeStateTrue();
        for(Player p : players){
            p.addDomToTrain(new Domino(0, by.centerNum, imagePath));
        }
        changePlayerTurn();
    }


    public Player getMexTrain() {
        return mexTrain;
    }


    public void changePlayerTurn() {
        for(Player p : players){
            p.makePlayerTurnFalse();
        }
        players.get(playerTurnInc).makePlayerTurn();
        players.get(playerTurnInc).makeHasDrawnFalse();
        if(playerTurnInc == (players.size() - 1)) {
            playerTurnInc = 0;
        } else {
            playerTurnInc++;
        }
    }

    public void printBoard() {
        System.out.println("Center:" + center.toString());
        mexTrain.printTrain();
        for(Player p : players){
            p.printTrain();
        }
    }
}

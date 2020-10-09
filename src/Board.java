import java.util.ArrayList;

public class Board {
    private final ArrayList<Player> players;
    private Player mexTrain;
    private int playerTurnInc = 0;
    private Domino center;

    public Board(ArrayList<Player> players, int n, Boneyard by) {
        this.players = players;
        String imagePath = "Resources/dominoes/" + n + "|" + n + ".png";
        String imagePath2 = "Resources/dominoes/" + 0 + "|" + n + ".png";
        center = new Domino(n,n,imagePath);
        mexTrain = new Player("Mexican Train",0);
        mexTrain.addDomToTrain(new Domino(0, by.centerNum,imagePath2));
        mexTrain.makeStateTrue();
        for(Player p : players){
            p.addDomToTrain(new Domino(0, by.centerNum,imagePath2));
        }
        changePlayerTurn();
    }

    public Domino getCenter() {
        return center;
    }
    public Player getMexTrain() {
        return mexTrain;
    }

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

    public void printBoard() {
        System.out.println("Center:" + center.toString());
        mexTrain.printTrain();
        for(Player p : players){
            p.printTrain();
        }
    }
}

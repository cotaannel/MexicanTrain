import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {
    private final ArrayList<Player> players;
    private Player mexTrain;
    private int playerTurnInc = 0;

    public Board(ArrayList<Player> players) {
        this.players = players;
        mexTrain = new Player("Mexican Train",0);
        mexTrain.makeStateTrue();
        changePlayerTurn();
    }

    public void changePlayerTurn() {
        for(Player p : players){
            p.makePlayerTurnFalse();
        }
        players.get(playerTurnInc).makePlayerTurn();
        if(playerTurnInc == (players.size() - 1)) {
            playerTurnInc = 0;
        } else {
            playerTurnInc++;
        }
    }

    public void printBoard() {
        mexTrain.printTrain();
        for(Player p : players){
            p.printTrain();
        }
    }
}

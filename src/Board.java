import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {
    private final ArrayList<Player> players;
    private Player mexTrain;

    public Board(ArrayList<Player> players) {
        this.players = players;
        mexTrain = new Player("Mexican Train",0);
        mexTrain.makeStateTrue();
    }

    public void printBoard() {
        mexTrain.printTrain();
        for(Player p : players){
            p.printTrain();
        }
    }
}

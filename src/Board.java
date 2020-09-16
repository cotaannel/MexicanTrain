import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {
    private final ArrayList<Player> humPlayers;
    private final ArrayList<Player> comPlayers;
    private Player mexTrain;

    public Board(ArrayList<Player> humPlayers, ArrayList<Player> comPlayers) {
        this.humPlayers = humPlayers;
        this.comPlayers = comPlayers;
        mexTrain = new Player("Mexican Train",0);
        mexTrain.makeStateTrue();
    }

    public void printBoard() {
        mexTrain.printTrain();
        for(Player p : humPlayers){
            p.printTrain();
        }
        for(Player p : comPlayers){
            p.printTrain();
        }
    }
}

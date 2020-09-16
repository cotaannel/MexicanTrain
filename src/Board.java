import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {
    private final ArrayList<Player> humPlayers;
    private final ArrayList<Player> comPlayers;
    private Player mexTrain;

    public Board(ArrayList<Player> humPlayers, ArrayList<Player> comPlayers) {
        this.humPlayers = humPlayers;
        this.comPlayers = comPlayers;
        //printBoard();
    }

    public void printBoard() {
//        System.out.print("Mexican train(true):");
//        mexTrain.
        for(Player p : humPlayers ){
            p.printTrain();
        }
        for(Player p : comPlayers ){
            p.printTrain();
        }
    }
}

import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {
    private Boneyard boneyard;
    private ArrayList<Player> humPlayers;
    private ArrayList<Player> comPlayers;
    private Board brd;

    public GameManager() {
        boneyard = new Boneyard();
        startGame();
    }

    public void startGame() {
        Scanner in = new Scanner(System.in);
        humPlayers = new ArrayList<>();
        comPlayers = new ArrayList<>();
        int totalStartingDom = 0;

        System.out.println("Welcome to Mexican Train!");
        System.out.println("Up to 4 players can play with any mix of human and computer players.");
        System.out.println("Please enter the TOTAL number of players:");
        int totalPlayers = in.nextInt();
        //changes starting number of dominos depending on number of players
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
            humPlayers.add(p);
        }

        System.out.println("Please enter the number of computer players:");
        int numComPlayers = in.nextInt();
        for (int i = 1; i <= numComPlayers; i++) {
            Player p = new Player("Computer" + i, totalStartingDom);
            p.createHand(boneyard);
            comPlayers.add(p);
        }
        in.close();
        brd = new Board(humPlayers,comPlayers);
        printGameState();
    }

    public void printGameState() {
        System.out.println("GameState:");
        System.out.println("Humans:");
        for(Player p : humPlayers){
            p.printHand();
        }
        System.out.println("Computers:");
        for(Player p : comPlayers){
            p.printHand();
        }
        System.out.println("Center:");
        System.out.println("Board:");
        brd.printBoard();
        System.out.println("Boneyard:");
        boneyard.printBoneyard();
        System.out.println("Current player:");

    }
}

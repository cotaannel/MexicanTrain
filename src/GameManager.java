import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {
    private Boneyard boneyard;
    private ArrayList<Player> players;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    public GameManager() {
        boneyard = new Boneyard();
        player1 = new Player();
        //boneyard.printBoneyard();

//        player1.createHand(boneyard);
//        player1.printHand();
        startGame();

    }

    public void startGame() {
        Scanner in = new Scanner(System.in);
        players = new ArrayList<>();

        System.out.println("Welcome to Mexican Train!");
        System.out.println("Up to 4 players can play with any mix of human and computer players.");
        System.out.println("Please enter the number of human players:");
        int numHumanPlayers = in.nextInt();
        for (int i = 1; i <= numHumanPlayers; i++) {
            Player p = new Player("player" + i);
            p.createHand(boneyard);
            players.add(p);
            p.printHand();
        }

        System.out.println("Please enter the number of computer players:");
        int numComPlayers = in.nextInt();
        for (int i = 1; i <= numComPlayers; i++) {
            Player p = new Player("computer" + i);
            p.createHand(boneyard);
            players.add(p);
            p.printHand();
        }

    }

    public void printGameState() {
        System.out.println("GameState:");
        System.out.println("Humans:");
        System.out.println("Computers:");
        System.out.println("Center:");
        System.out.println("Board:");
        System.out.println("Boneyard:");
        boneyard.printBoneyard();
        System.out.println("Current player:");

    }
}

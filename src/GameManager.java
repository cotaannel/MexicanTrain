import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {
    private Boneyard boneyard;
    private ArrayList<Player> humPlayers;
    private ArrayList<Player> comPlayers;
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
        humPlayers = new ArrayList<>();
        comPlayers = new ArrayList<>();

        System.out.println("Welcome to Mexican Train!");
        System.out.println("Up to 4 players can play with any mix of human and computer players.");
        System.out.println("Please enter the number of human players:");
        int numHumanPlayers = in.nextInt();
        for (int i = 1; i <= numHumanPlayers; i++) {
            Player p = new Player("Player" + i);
            p.createHand(boneyard);
            humPlayers.add(p);
            //p.printHand();
        }

        System.out.println("Please enter the number of computer players:");
        int numComPlayers = in.nextInt();
        for (int i = 1; i <= numComPlayers; i++) {
            Player p = new Player("Computer" + i);
            p.createHand(boneyard);
            comPlayers.add(p);
            //p.printHand();
        }
        printGameState();
    }

    public void printGameState() {
        System.out.println("GameState:");
        System.out.println("Humans:");
        for( Player p : humPlayers ){
            //System.out.println(p.getPlayerNum() + ": ");
            p.printHand();
        }
        System.out.println("Computers:");
        for( Player p : comPlayers ){
            //System.out.println(p.getPlayerNum() + ": ");
            p.printHand();
        }
        System.out.println("Center:");
        System.out.println("Board:");
        System.out.println("Boneyard:");
        boneyard.printBoneyard();
        System.out.println("Current player:");

    }
}

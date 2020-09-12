public class GameManager {
    private Boneyard boneyard;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    public GameManager() {
        boneyard = new Boneyard();
        player1 = new Player();
        //boneyard.printBoneyard();

        player1.createHand(boneyard);
        player1.printHand();

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

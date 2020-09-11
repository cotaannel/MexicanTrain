public class Main {

    public static void main(String[] args) {
        Boneyard by = new Boneyard();
        Player player1 = new Player();

        player1.createHand(by);

        by.printBoneyard();
        player1.printHand();

        by.printBoneyard();
    }

}

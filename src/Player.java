import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
    private String playerNum;
    private ArrayList<Domino> hand;

    //for testing
    private int startingDomNum = 5;

    public Player(String playerNum) {
        this.playerNum = playerNum;
        hand = new ArrayList<Domino>();
    }

    public Player() {
        this.playerNum = playerNum;
        hand = new ArrayList<Domino>();
    }

    public ArrayList<Domino> getHand() {
        return hand;
    }

    public String getPlayerNum() {
        return playerNum;
    }

    public int getHandSize() {
        return hand.size();
    }

    public void addDomToHand(Domino dom) {
        hand.add(dom);
    }

    public void removeDomFromHand(Domino dom) {
        hand.remove(dom);
    }

    public void createHand(Boneyard by) {
        for (int i = 0; i < startingDomNum; i++) {
            this.addDomToHand(by.drawDom());
        }
    }

    public void printHand() {
        for(int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i).toString() + "," );
        }
    }
}

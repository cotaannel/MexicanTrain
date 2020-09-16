import java.util.ArrayList;

public class Player {
    private String playerNum;
    //private int playerNumInc = 1;
    private ArrayList<Domino> hand;
    private ArrayList<Domino> train;
    private boolean trainState = false;
    private int startingDomNum;

    public Player(String playerNum, int i) {
        this.playerNum = playerNum;
        hand = new ArrayList<Domino>();
        train = new ArrayList<Domino>();
        startingDomNum = i;
        //createHand(by);
    }

    public Player() {
        hand = new ArrayList<Domino>();
        train = new ArrayList<Domino>();
        //playerNum = "Player" + playerNumInc;
    }

    public ArrayList<Domino> getHand() {
        return hand;
    }

    public void addDomToTrain(Domino dom) {
        train.add(dom);
    }

    public String getPlayerNum() {
        return playerNum;
    }

    public int getHandSize() {
        return hand.size();
    }

    public boolean getTrainState() {
        return trainState;
    }
    public void setTrainState() {
        trainState = true;
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
        System.out.println(getPlayerNum() + ": ");
        for(int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i).toString() + " " );
        }
        System.out.println();
    }
    public void printTrain() {
        System.out.println(getPlayerNum() + "(" + getTrainState() + ")" + ": ");
//        for(int i = 0; i < train.size(); i++) {
//            System.out.print(train.get(i).toString() + " " );
//        }
//        System.out.println();
    }
}

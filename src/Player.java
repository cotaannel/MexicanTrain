import java.util.ArrayList;

public class Player {
    private String playerNum;
    private ArrayList<Domino> hand;
    private ArrayList<Domino> train;
    private boolean trainState = false;
    private boolean isComputer = false;
    private boolean hasDrawn = false;
    private boolean playerTurn = false;
    private boolean trainEmpty = true;
    private int startingDomNum;
    private int score = 0;

    public Player(String playerNum, int i) {
        this.playerNum = playerNum;
        hand = new ArrayList<Domino>();
        train = new ArrayList<Domino>();
        startingDomNum = i;
    }

    public Player() {
        hand = new ArrayList<Domino>();
        train = new ArrayList<Domino>();
    }

    public boolean checkIfTrainEmpty() {
        return trainEmpty;
    }

    public void makeHasDrawnTrue() {
        hasDrawn = true;
    }

    public void makeHasDrawnFalse() {
        hasDrawn = false;
    }

    public boolean checkIfDrawn() {
        return hasDrawn;
    }

    public void makeTrainNonempty() {
        trainEmpty = false;
    }

    public void makePlayerTurn() {
        playerTurn = true;
    }
    public void makePlayerTurnFalse() {
        playerTurn = false;
    }

    public boolean getPlayerTurn() {
        return playerTurn;
    }

    public void setComputer() {
        isComputer = true;
    }

    public boolean checkIfComputer() {
        return isComputer;
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
    public void makeStateTrue() {
        trainState = true;
    }

    public void makeStateFalse() {
        trainState = false;
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

    public Domino getDomino(int i) {
        return hand.get(i);
    }
    public Domino getLastTrainDom() {
        return train.get(train.size()-1);
    }


    public void printHand() {
        System.out.println(getPlayerNum() + ": ");
        for(int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i).toString() + "  " );
        }
        System.out.println();
    }
    public void printTrain() {
        System.out.println(getPlayerNum() + "(" + getTrainState() + ")" + ": ");
        for(int i = 0; i < train.size(); i++) {
            System.out.print(train.get(i).toString() + "  " );
        }
        System.out.println();
    }
}

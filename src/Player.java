import java.util.ArrayList;

public class Player {
    private String playerNum;
    private ArrayList<Domino> hand;
    private ArrayList<Domino> train;
    private ArrayList<Domino> comCombo = new ArrayList<>();
    private ArrayList<Domino> comNotCombo;
    private boolean trainState = false;
    private boolean isComputer = false;
    private boolean hasDrawn = false;
    private boolean playerTurn = false;
    private boolean canPlayNonDoubleTrain = false;
    private int startingDomNum;
    private int score = 0;

    public Player(String playerNum, int i) {
        this.playerNum = playerNum;
        hand = new ArrayList<>();
        train = new ArrayList<>();
        startingDomNum = i;
    }

    public Player() {
        hand = new ArrayList<>();
        train = new ArrayList<>();
    }

    public void calculateScore() {
        for(int i = 0; i < hand.size(); i++) {
            score += hand.get(i).getLeftNum();
            score += hand.get(i).getRightNum();
        }
    }

    public void updateScore(int score) {
        this.score = score;
    }

    public void createHand(Boneyard by) {
        //this.addDomToHand(new Domino(0, by.centerNum, null));
        for (int i = 0; i < startingDomNum; i++) {
            this.addDomToHand(by.drawDom());
        }
    }

    public void printHand() {
        for(int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i).toString() + "  " );
        }
        System.out.println();
    }
    public void printTrain() {
        ArrayList<Domino> temp1 = new ArrayList<>();
        ArrayList<Domino> temp2 = new ArrayList<>();
        for (int i = 1; i < train.size(); i++){
            if ((i + 2) % 2 == 0) {
                temp2.add(train.get(i));
            }
            else {
                temp1.add(train.get(i));
            }
        }
        System.out.println(getPlayerNum() + "("+getTrainState()+"):");
        for(int i = 0; i < temp1.size(); i++) {
            System.out.print(temp1.get(i).toString() + " ");
        }
        System.out.println();
        System.out.print("   ");
        for(int i = 0; i < temp2.size(); i++) {
            System.out.print(temp2.get(i).toString() + " ");
        }
        System.out.println();
    }

    public int getScore() {
        return score;
    }

    public void makeHasDrawnTrue() {
        hasDrawn = true;
    }

    public void makeHasDrawnFalse() {
        hasDrawn = false;
    }

    public boolean checkIfCanPlayNonDoubleTrain() {
        return canPlayNonDoubleTrain;
    }

    public void makeTrueCanPlayNonDoubleTrain() {
        canPlayNonDoubleTrain = true;
    }

    public void makeFalseCanPlayNonDoubleTrain() {
        canPlayNonDoubleTrain = false;
    }

    public boolean checkIfDrawn() {
        return hasDrawn;
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

    public Domino getDomino(int i) {
        return hand.get(i);
    }

    public Domino getLastTrainDom() { return train.get(train.size()-1); }

    public ArrayList<Domino> getComputerCombo() { return comCombo; }

    public void addDomToCombo(Domino dom) {
        comCombo.add(dom);
    }
    public Domino getLastComboDom() { return comCombo.get(comCombo.size()-1); }

    public void removeRandomDomFromHand(Domino dom) {
        for(int i = 0; i < hand.size(); i++) {
            if(dom == hand.get(i)) {
                hand.remove(i);
            }
        }
    }
}

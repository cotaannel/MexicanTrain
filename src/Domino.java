public class Domino {
    private int leftNum;
    private int rightNum;

    public Domino(int leftNum, int rightNum) {
        this.leftNum = leftNum;
        this.rightNum = rightNum;
    }

    public int getLeftNum() {
        return leftNum;
    }

    public int getRightNum() {
        return rightNum;
    }

    //print dominos in form [0|0]
    public String toString() {
        return "[" + leftNum + "|" + rightNum + "]";
    }
}

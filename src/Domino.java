public class Domino {
    private int leftNum;
    private int rightNum;
    private String imagePath;

    public Domino(int leftNum, int rightNum, String imagePath) {
        this.leftNum = leftNum;
        this.rightNum = rightNum;
        this.imagePath = imagePath;
    }

    public String getDomImage() {
        return imagePath;
    }

    public int getLeftNum() {
        return leftNum;
    }

    public int getRightNum() {
        return rightNum;
    }

    public void rotateTile() {
        int firstNum = this.leftNum;
        this.leftNum = rightNum;
        this.rightNum = firstNum;
    }

    public int getPipTotal(Domino dom) {
        return leftNum + rightNum;
    }

    //print dominoes in form [0|0]
    public String toString() {
        return "[" + leftNum + "|" + rightNum + "]";
    }
}

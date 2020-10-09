/**
 * @author Annel Cota
 *
 * Domino class creates a domino with a
 * left pip number, right pip number, and
 * its image path.
 */

public class Domino {
    private int leftNum;
    private int rightNum;
    private String imagePath;

    /**
     *
     * @param leftNum : left pip number of domino
     * @param rightNum : right pip number of domino
     * @param imagePath : image path of the domino png image
     */
    public Domino(int leftNum, int rightNum, String imagePath) {
        this.leftNum = leftNum;
        this.rightNum = rightNum;
        this.imagePath = imagePath;
    }

    /**
     * Get the image path corresponding to the domino.
     * @return image path of domino
     */
    public String getDomImage() {
        return imagePath;
    }

    /**
     * Get the left pip number of domino.
     * @return left pip number
     */
    public int getLeftNum() {
        return leftNum;
    }

    /**
     * Get the right pip number of domino.
     * @return right pip number
     */
    public int getRightNum() {
        return rightNum;
    }

    /**
     * Adds up the left pip number and the right pip
     * number of the domino.
     * @return left pip number + right pip number of domino
     */
    public int getPipTotal() {
        return leftNum + rightNum;
    }

    /**
     * Gets the string of domino in the form of [0|0].
     * @return string representation of domino
     */
    public String toString() {
        return "[" + leftNum + "|" + rightNum + "]";
    }

    /**
     * Rotates the domino by switching the left and right numbers.
     */
    public void rotateDom() {
        int firstNum = this.leftNum;
        this.leftNum = rightNum;
        this.rightNum = firstNum;
    }
}

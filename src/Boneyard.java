/**
 * @author Annel Cota
 *
 * Boneyard class creates a boneyard
 * of dominoes. It will also print the boneyard
 * when that method is called.
 */

import java.util.ArrayList;
import java.util.Collections;

public class Boneyard {
    public ArrayList<Domino> boneyard;
    public int centerNum;
    private String imagePath = "Resources/dominoes/" + centerNum + "|" + centerNum + ".png";

    /**
     * Makes a new array list for the boneyard
     * and calls method to create it.
     * @param centerNum : center domino
     */
    public Boneyard(int centerNum) {
        this.centerNum = centerNum;
        boneyard = new ArrayList<>();
        createBoneyard();
    }

    /**
     * This method creates the boneyard, depending on weather
     * the game was set by user(console version) to play
     * with a double-9 set or double-12 set. It creates
     * the dominoes and then adds them to the boneyard.
     */
    public void createBoneyard() {
        //center number is 9, so game is using double-9 set
        if(centerNum == 9) {
            for(int i = 0; i <= 9; i++) {
                int j = i;
                while (j >= 0) {
                    //adds the png domino path corresponding to it
                    String imagePath = "Resources/dominoes/" + j + "|" + i + ".png";
                    boneyard.add(new Domino(j,i,imagePath));
                    j--;
                }
            }
        }

        //center number is 12, so game is using double-12 set
        if(centerNum == 12) {
            for(int i = 0; i <= 12; i++) {
                int j = i;
                while (j >= 0) {
                    //adds the png domino path corresponding to it
                    String imagePath = "Resources/dominoes/" + j + "|" + i + ".png";
                    boneyard.add(new Domino(j,i,imagePath));
                    j--;
                }
            }
        }

        //removes the center domino from boneyard
        Domino dom = new Domino(centerNum,centerNum,imagePath);
        for(int i = 0; i < boneyard.size(); i++) {
            Domino test = boneyard.get(i);
            if(test.getLeftNum() == dom.getLeftNum() && test.getRightNum() == test.getRightNum()) {
                boneyard.remove(i);
            }
        }
        //shuffles dominoes
        Collections.shuffle(boneyard);
    }

    /**
     * Draws a domino from the boneyard and
     * then removes it from the boneyard.
     * @return domino that is being drawn
     */
    public Domino drawDom() {
        Domino dom = boneyard.get(0);
        boneyard.remove(0);
        return dom;
    }

    /**
     * Prints out the boneyard in two parallel rows.
     */
    public void printBoneyard() {
        ArrayList<Domino> temp1 = new ArrayList<>();
        ArrayList<Domino> temp2 = new ArrayList<>();
        for (int i = 0; i < boneyard.size(); i++){
            if ((i + 2) % 2 == 0) {
                temp1.add(boneyard.get(i));
            }
            else {
                temp2.add(boneyard.get(i));
            }
        }

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
}

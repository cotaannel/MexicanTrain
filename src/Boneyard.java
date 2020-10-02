import java.util.ArrayList;
import java.util.Collections;

public class Boneyard {
    public ArrayList<Domino> boneyard;
    public int centerNum;

    public Boneyard(int centerNum) {
        this.centerNum = centerNum;
        boneyard = new ArrayList<>();
        createBoneyard();
    }

    public void createBoneyard() {
        for(int i = 0; i <= 9; i++) {
            int j = i;
            while (j >= 0) {
                String imagePath = "dominoes/" + j + "|" + i + ".png";
                boneyard.add(new Domino(j,i));
                j--;
            }
        }

        //removes the center domino from boneyard
        Domino dom = new Domino(centerNum,centerNum);
        for(int i = 0; i < boneyard.size(); i++) {
            Domino test = boneyard.get(i);
            if(test.getLeftNum() == dom.getLeftNum() && test.getRightNum() == test.getRightNum()) {
                boneyard.remove(i);
            }
        }
        //shuffles dominoes
        Collections.shuffle(boneyard);
    }

    public Domino drawDom() {
        Domino dom = boneyard.get(0);
        boneyard.remove(0);
        return dom;
    }

    public void printBoneyard() {
        for(int i = 0; i < boneyard.size(); i++) {
            System.out.print(boneyard.get(i).toString() + " ");
        }
        System.out.println();
    }
}

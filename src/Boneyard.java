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
        if(centerNum == 9) {
            for(int i = 0; i <= 9; i++) {
                int j = i;
                while (j >= 0) {
                    String imagePath = "dominoes/" + j + "|" + i + ".png";
                    boneyard.add(new Domino(j,i));
                    j--;
                }
            }
        }

        if(centerNum == 12) {
            for(int i = 0; i <= 12; i++) {
                int j = i;
                while (j >= 0) {
                    String imagePath = "dominoes/" + j + "|" + i + ".png";
                    boneyard.add(new Domino(j,i));
                    j--;
                }
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Boneyard {
    public ArrayList<Domino> boneyard;

    public Boneyard() {
        boneyard = new ArrayList<Domino>();
        createBoneyard();
        printBoneyard();
    }

    public void createBoneyard() {
        for(int i = 0; i <= 9; i++) {
            int j = i;
            while (j >= 0) {
                boneyard.add(new Domino(i,j));
                j--;
            }
        }
        //shuffles dominos
        Collections.shuffle(boneyard);
    }

    public void printBoneyard() {
        for(int i = 0; i < boneyard.size(); i++) {
            System.out.println(boneyard.get(i).toString());
        }
        System.out.println(boneyard.size());
    }


}
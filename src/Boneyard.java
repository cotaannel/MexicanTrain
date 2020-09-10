import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Boneyard {
    public ArrayList<Domino> boneyard;

    public Boneyard() {
        boneyard = new ArrayList<Domino>();
        createBoneyard();
    }

    public void createBoneyard() {
        for(int i = 0; i <= 9; i++) {
            int j = i;
            while (j >= 0) {
                boneyard.add(new Domino(i,j));
                j--;
            }
        }
        Collections.shuffle(boneyard);
    }


}

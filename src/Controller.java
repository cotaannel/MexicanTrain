import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Controller {
    Board board;
    private Boneyard boneyard;
    public ArrayList<Player> players;
    private ArrayList<ImageView> images;
    public ArrayList<Integer> scoreHolder = new ArrayList<>();
    ImageView domImage;
    private int totPlayers;
    private int humanPlayers;
    private int computerPlayers;
    private int totalStartingDom = 0;


    @FXML
    private TextField totalPlayers;

    @FXML
    private TextField humPlayers;

    @FXML
    private TextField comPlayers;

    @FXML
    private void initialize() {

    }

    private void buttonClicked() {
        System.out.println("Draw");
    }

    public void getTileImage(){
        String imgPath = "dominoes/" + 1 + "-" + 1 + ".png";

    }

    public void getTotalPlayers(){
        String s = totalPlayers.getText();
        int totPlayers = Integer.parseInt(s);
    }

    public void getHumPlayers(){
        String s = humPlayers.getText();
        humanPlayers = Integer.parseInt(s);
    }

    public void getComPlayers(){
        String s = comPlayers.getText();
        computerPlayers = Integer.parseInt(s);
    }

    public void startNewGame() {
        boneyard = new Boneyard(9);
        players = new ArrayList<>();

        //changes starting number of dominoes depending on number of players
        if(totPlayers == 4) {
            totalStartingDom = 10;
        } else if(totPlayers == 3) {
            totalStartingDom = 13;
        } else if(totPlayers == 2) {
            totalStartingDom = 15;
        }

        for(int i = 1; i <= humanPlayers; i++) {
            Player p = new Player("Player" + i, totalStartingDom);
            p.createHand(boneyard);
            players.add(p);
            scoreHolder.add(0);
        }

        for(int i = 1; i <= computerPlayers; i++) {
            Player p = new Player("Computer" + i, totalStartingDom);
            p.createHand(boneyard);
            p.setComputer();
            players.add(p);
            scoreHolder.add(0);
        }
        board = new Board(players, 9);
        GameManager gm = new GameManager(players, scoreHolder);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Integer> getScoreHolder() {
        return scoreHolder;
    }
}

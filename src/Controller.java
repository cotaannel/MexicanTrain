/**
 * @author Annel Cota
 *
 * This class has
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class Controller {
    Values values;
    @FXML
    private Label currentPlayerLbl = new Label();
    @FXML
    private Label currentPlayerLbl1 = new Label();
    @FXML
    private Label mexTrainLabel = new Label();
    @FXML
    private Label p1Label = new Label();
    @FXML
    private Label p2Label = new Label();
    @FXML
    private Label p3Label = new Label();
    @FXML
    private Label p4Label = new Label();
    @FXML
    private Label gameStateLabel = new Label();
    @FXML
    private HBox playerHand = new HBox();
    @FXML
    private HBox center = new HBox();
    @FXML
    private VBox p1 = new VBox();
    @FXML
    private HBox p2 = new HBox();
    @FXML
    private VBox p3 = new VBox();
    @FXML
    private VBox p4 = new VBox();
    @FXML
    private HBox mexTrain = new HBox();
    @FXML
    private Button closeButton;
    @FXML
    private Button skipButton;
    @FXML
    private Button drawButton;
    @FXML
    private TextField playInputs;


    public GameManager gm = new GameManager();


    private int totPlayers;
    private int humanPlayers;
    private int computerPlayers;


    public Controller() {
        values = Main.getValues();
        totPlayers = values.returnTotalPlayers();
        humanPlayers = values.returnHumPlayers();
        computerPlayers = values.returnComPlayers();
    }

    @FXML
    private void initialize() {
        gm = new GameManager();
        gm.startGame(totPlayers,humanPlayers,computerPlayers);
        updateGM();
    }

    @FXML
    private void updateGM() {
        gm.updateComponents(currentPlayerLbl, currentPlayerLbl1, playerHand);
        gm.updateBoard(center, p1, p2, p3, p4, mexTrain,
                p1Label, p2Label, p3Label, p4Label, mexTrainLabel, gameStateLabel);
        //gm.updateGameStateLabel(gameStateLabel);
        //gm.startNewTurn(gameStateLabel);
    }

    @FXML
    public void drawButtonPress() {
        gm.checkIfCanDraw();
        updateGM();
    }
    @FXML
    public void skipButtonPress() {
        gm.checkIfCanSkip();
        updateGM();
    }

    @FXML
    public void getPlayInputs() {
        String s = playInputs.getText();
        List<String> tempList = Arrays.asList(s.split(","));
        int domChoice = Integer.parseInt(tempList.get(0));
        int trainChoice = Integer.parseInt(tempList.get(1));
        gm.playDominoSetupChoices(domChoice,trainChoice);
        playInputs.clear();
        updateGM();

    }


}

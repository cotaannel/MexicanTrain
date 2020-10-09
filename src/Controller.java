/**
 * @author Annel Cota
 *
 * This class has
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.Arrays;
import java.util.List;

public class Controller {
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
    private TextField playInputs;
    private Values values;
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

    /**
     * Updates the components of the GUI.
     */
    @FXML
    private void updateGM() {
        gm.updateComponents(currentPlayerLbl, currentPlayerLbl1, playerHand);
        gm.updateBoard(center, p1, p2, p3, p4, mexTrain,
                p1Label, p2Label, p3Label, p4Label, mexTrainLabel, gameStateLabel);
    }

    /**
     * If the draw button is pressed, this method is called.
     * It calls the GameManager method to check if the player
     * can draw. It then updates the GUI.
     */
    @FXML
    public void drawButtonPress() {
        gm.checkIfCanDraw();
        updateGM();
    }

    /**
     * If the skip button is pressed, this method is called.
     * It calls the GameMananger method to check if the player
     * can skip. It then updates the GUI.
     */
    @FXML
    public void skipButtonPress() {
        gm.checkIfCanSkip();
        updateGM();
    }

    /**
     * Gets the user input from the playInputs text field.
     * The user is to input their domino number choice and
     * their train number choice in the form of 1,1.
     * This method then separates the input by the comma and
     * plays the GameManager setup with the players choices.
     * It then updates the GUI.
     */
    @FXML
    public void getPlayInputs() {
        String s = playInputs.getText();
        List<String> tempList = Arrays.asList(s.split(","));
        int domChoice = Integer.parseInt(tempList.get(0));
        int trainChoice = Integer.parseInt(tempList.get(1));
        gm.playDominoSetupChoices(domChoice,trainChoice);
        //clears the text box of the players previous inputs
        playInputs.clear();
        updateGM();
    }
}

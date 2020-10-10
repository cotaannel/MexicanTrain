/**
 * @author Annel Cota
 *
 * This Controller class starts the GameManager for the GUI
 * version with the values from the setup GUI.
 * It has all the components of the GUI that make up the game
 * board, including the player trains, draw and skip button,
 * and all the labels.
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
    private VBox p1Box1 = new VBox();
    @FXML
    private VBox p1Box2 = new VBox();
    @FXML
    private VBox p2Box1 = new VBox();
    @FXML
    private VBox p2Box2 = new VBox();
    @FXML
    private HBox p3Box1 = new HBox();
    @FXML
    private HBox p3Box2 = new HBox();
    @FXML
    private HBox p4Box1 = new HBox();
    @FXML
    private HBox p4Box2 = new HBox();
    @FXML
    private HBox mexTrain1 = new HBox();
    @FXML
    private HBox mexTrain2 = new HBox();
    @FXML
    private Button comPlayerButton = new Button();
    @FXML
    private TextField playInputs;
    private Values values;
    public GameManager gm = new GameManager();
    private int totPlayers;
    private int humanPlayers;
    private int computerPlayers;

    /**
     * Gets the values from Main's Values.
     */
    public Controller() {
        values = Main.getValues();
        totPlayers = values.returnTotalPlayers();
        humanPlayers = values.returnHumPlayers();
        computerPlayers = values.returnComPlayers();
    }

    /**
     * Creates a GameManager and starts the game
     * with the values of the players.
     * It then updates the components of the GUI.
     */
    @FXML
    private void initialize() {
        gm = new GameManager();
        gm.startGame(totPlayers,humanPlayers,computerPlayers);
        updateGM();
    }

    /**
     * If the comPlayerButton is clicked, this method
     * is called. It is a computer player's turn, so
     * the start turn method is called so the computer
     * player can make its move. It then updates the GUI.
     */
    @FXML
    private void getComPlayGUI() {
        gm.startTurn();
        updateGM();
    }

    /**
     * Updates the components of the GUI.
     */
    @FXML
    private void updateGM() {
        gm.updateComponents(currentPlayerLbl, currentPlayerLbl1, playerHand);
//        gm.updateBoard(center, p1, p2, p3, p4, mexTrain,
//                p1Label, p2Label, p3Label, p4Label, mexTrainLabel, gameStateLabel);
        gm.updateBoard(center, p1Box1, p1Box2, p2Box1, p2Box2, p3Box1, p3Box2, p4Box1, p4Box2,
                mexTrain1, mexTrain2, p1Label, p2Label, p3Label, p4Label,
                mexTrainLabel, gameStateLabel);
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

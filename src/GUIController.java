/**
 * @author Annel Cota
 *
 * This GUIController class is from the first window of the GUI version.
 * It gets the total number of players and number of human and computer
 * players from the user using text fields. It also sets those values in
 * the Values class.
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class GUIController {
    private int totPlayers;
    private int humanPlayers;
    private int computerPlayers;
    private Values values;
    @FXML
    private TextField totalPlayersText;
    @FXML
    private TextField humPlayersText;
    @FXML
    private TextField comPlayersText;
    @FXML
    private Button closeButton;

    /**
     * When the closeButton is clicked, this method is called.
     * It closes the setup GUI and starts the Game.fxml file.
     * @throws IOException
     */
    @FXML
    public void closeWindow() throws IOException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        Parent anotherRoot = FXMLLoader.load(getClass().getResource("Game.fxml"));
        Scene anotherScene = new Scene(anotherRoot);
        Stage anotherStage = new Stage();
        anotherStage.setScene(anotherScene);
        anotherStage.show();
    }

    /**
     * Gets the total number of players from the user's input
     * in the text field and sets the value in Values.
     */
    public void getTotalPlayers(){
        values = Main.getValues();
        String s = totalPlayersText.getText();
        totPlayers = Integer.parseInt(s);
        values.setTotalPlayers(totPlayers);
    }

    /**
     * Gets the number of human players from the user's input
     * in the text field and sets the value in Values.
     */
    public void getHumPlayers(){
        String s = humPlayersText.getText();
        humanPlayers = Integer.parseInt(s);
        values.setHumanPlayers(humanPlayers);
    }

    /**
     * Gets the number of computer players from the user's input
     * in the text field and sets the value in Values.
     */
    public void getComPlayers(){
        String s = comPlayersText.getText();
        computerPlayers = Integer.parseInt(s);
        values.setComputerPlayers(computerPlayers);
    }
}

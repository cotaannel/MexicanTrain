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
    private int totPlayers2 = 4;
    private int humanPlayers2 = 4;
    private int computerPlayers2 = 0;

    private Values values;
    @FXML
    private TextField totalPlayersText;
    @FXML
    private TextField humPlayersText;
    @FXML
    private TextField comPlayersText;
    @FXML
    private Button closeButton;

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

    public void getTotalPlayers(){
        values = Main.getValues();
        String s = totalPlayersText.getText();
        totPlayers = Integer.parseInt(s);
        values.setTotalPlayers(totPlayers);
    }

    public void getHumPlayers(){
        String s = humPlayersText.getText();
        humanPlayers = Integer.parseInt(s);
        values.setHumanPlayers(humanPlayers);
    }

    public void getComPlayers(){
        String s = comPlayersText.getText();
        computerPlayers = Integer.parseInt(s);
        values.setComputerPlayers(computerPlayers);
    }
}

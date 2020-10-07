import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class GUIController {
    private int totPlayers;// = 4;
    private int humanPlayers;// = 4;
    private int computerPlayers;// = 0;
    private int totPlayers2 = 4;
    private int humanPlayers2 = 4;
    private int computerPlayers2 = 0;

    Values values;
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
        System.out.println(totPlayers);
        values.setTotalPlayers(totPlayers);
    }

    public void getHumPlayers(){
        String s = humPlayersText.getText();
        humanPlayers = Integer.parseInt(s);
        System.out.println(humanPlayers);
        values.setHumanPlayers(humanPlayers);
    }

    public void getComPlayers(){
        String s = comPlayersText.getText();
        computerPlayers = Integer.parseInt(s);
        System.out.println(computerPlayers);
        values.setComputerPlayers(computerPlayers);
    }

}

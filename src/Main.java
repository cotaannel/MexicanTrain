import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Values values = new Values();
    public static void main(String[] args) {
        values.makeConsoleTrue();
        //values.makeConsoleFalse();
        if(values.checkIfConsole()) {
            new GameManager();
        } else {
            launch(args);
        }
    }

    public static Values getValues() { return values; }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}


/**
 * @author Annel Cota
 *
 * Main class is where it is decided if the game will be the console
 * or the GUI version. It creates a Values class to hold the setup values
 * of the game. It loads the fxml file that starts the GUI version and gets
 * the setup values of the game.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Values values = new Values();

    /**
     * Makes console version true or false.
     * If it is console version, calls GameManager.
     * If not, starts GUI.
     * @param args : any arguments
     */
    public static void main(String[] args) {
        //values.makeConsoleTrue();
        values.makeConsoleFalse();
        if(values.checkIfConsole()) {
            new GameManager();
        } else {
            launch(args);
        }
    }

    /**
     * Gets the values from Main.
     * @return values
     */
    public static Values getValues() { return values; }

    /**
     * Starts the GUI.fxml file.
     * @param primaryStage : stage
     * @throws Exception : exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}


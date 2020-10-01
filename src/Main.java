import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {
    private ImageView imgSelectedDomino;


    public static void main(String[] args) {
        launch(args);
        //mexicantrain.GameManager gm = new mexicantrain.GameManager(null,null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Mexican Train.fxml"));
        primaryStage.setTitle("Mexican Train");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
        String imgPath = "Dominos/" + 1 + "-" + 1 + ".png";

        //imgSelectedDomino.setImage(new Image(imgPath));

    }
}

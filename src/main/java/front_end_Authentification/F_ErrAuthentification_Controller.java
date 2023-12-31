package front_end_Authentification;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class F_ErrAuthentification_Controller {
    protected static void afficherErr() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("F_ErrAuthentification.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        Stage secondStage = new Stage();

        secondStage.setTitle("Erreur d'authentification");
        secondStage.setScene(scene);
        secondStage.show();
    }
}

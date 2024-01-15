package front_end_Authentification.Portefeuilles;

import front_end_Authentification.Actions.Application_Action;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreerPortefeuille_Controller {
    public static void afficherCreerPortefeuille() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_PorteFeuille/F_CreerPorteFeuille.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Créer un portefeuille");
        secondStage.setScene(scene);
        secondStage.show();
    }
}

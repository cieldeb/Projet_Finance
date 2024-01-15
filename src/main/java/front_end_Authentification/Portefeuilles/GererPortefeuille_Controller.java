package front_end_Authentification.Portefeuilles;

import front_end_Authentification.Actions.Application_Action;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GererPortefeuille_Controller {
    protected static void afficherGererPortefeuille() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_PorteFeuille/F_GererPorteFeuille.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Gerer un portefeuille");
        secondStage.setScene(scene);
        secondStage.show();
    }



}

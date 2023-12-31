package front_end_Authentification.Virement;

import front_end_Authentification.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class F_NewDestinataire_Controller {
    public static void afficher_F_NewDestinataire() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/front_end_Virement/F_NewDestinataire.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Ajouter un destinataire");
        stage.setScene(scene);
        stage.show();

    }
}

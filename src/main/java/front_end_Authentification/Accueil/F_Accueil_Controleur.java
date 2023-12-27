package front_end_Authentification.Accueil;

import front_end_Authentification.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class F_Accueil_Controleur {
    public static void afficher_F_Accueil() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("F_Accueil.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();

        stage.setTitle("Accueil");
        stage.setScene(scene);
        stage.show();

    }
}

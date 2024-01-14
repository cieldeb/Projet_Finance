package front_end_Authentification.Accueil;

import front_end_Authentification.Application;
import front_end_Authentification.Virement.F_Virement_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class F_Accueil_Controller {

    public static void afficher_F_Accueil() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/front_end_Authentification/F_Accueil.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();

        stage.setTitle("Accueil");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void btnVirement(ActionEvent e) throws IOException {
        F_Virement_Controller.afficher_F_Virement();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void accessToActions(ActionEvent e) throws IOException{
        front_end_Authentification.Actions.MainActionController.afficherMainActions();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}

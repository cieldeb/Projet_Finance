package front_end_Authentification.Portefeuilles;

import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Actions.Application_Action;
import front_end_Authentification.F_Authentification_Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;

import static front_end_Authentification.Accueil.F_Accueil_Controller.getSelectedWallet;

public class GererPortefeuille_Controller {
    protected static Portefeuille selectedWallet = getSelectedWallet();
    @FXML
    protected Label libelleLabel;

    @FXML
    private void initialize(){
        libelleLabel.setText(selectedWallet.getName());
    }
    @FXML
    protected void accederActionButton(){

    }
    @FXML
    protected void accederCryptoButton(){

    }
    @FXML
    protected void retourButton(){

    }

    public static void afficherGererPortefeuille() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_PorteFeuille/F_GererPorteFeuille.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Gerer un portefeuille");
        secondStage.setScene(scene);
        secondStage.show();
    }



}

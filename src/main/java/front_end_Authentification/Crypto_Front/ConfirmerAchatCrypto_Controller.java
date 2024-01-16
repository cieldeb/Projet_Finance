package front_end_Authentification.Crypto_Front;

import front_end_Authentification.Actions.AcheterActions_Controller;
import front_end_Authentification.Actions.Application_Action;
import front_end_Authentification.Actions.ConfirmerAchatController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static front_end_Authentification.Crypto_Front.AcheterCryptos_Controller.getAchatCrypto;
import static front_end_Authentification.Crypto_Front.AcheterCryptos_Controller.setAchatCrypto;

public class ConfirmerAchatCrypto_Controller {
    protected static String[] achatCrypto = getAchatCrypto();
    @FXML
    private Label confirmLabel = new Label("Vous vous apprétez à effectuer l'achat de " + achatCrypto[3] +" action(s) "+ achatCrypto[1] + ". La valeur d'une action étant : " + achatCrypto[2] + "euros, vous allez payer : " + achatCrypto[4] + "euros. Si vous souhaitez donner un libellé à votre ensemble d'action que vous vous apprêtez à acheter, complétez le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");
    @FXML
    private TextField libelleTextField;
    @FXML
    protected void confirmerButton(){
        setAchatCrypto(libelleTextField.getText());
        //Ajouter toute la partie concernant l'association de l'achat à l'entité.


    }
    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
        AcheterCryptos_Controller.afficherAcheterCrypto();

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    protected static void afficherConfirmerAchatCryptos() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Cryptos/F_ConfirmerAchatCryptos.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage secondStage = new Stage();

        ConfirmerAchatCrypto_Controller controlleur = fxmlLoader.getController();
        controlleur.confirmLabel.setText("Vous vous apprétez à effectuer l'achat de " + achatCrypto[3] +" "+ achatCrypto[1] + " action(s). La valeur d'une action étant : " + achatCrypto[2] + "euros, vous allez payer : " + achatCrypto[4] + "euros. Si vous souhaitez donner un libellé à votre ensemble d'action que vous vous apprêtez à acheter, complétez le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");

        secondStage.setTitle("Confirmation d'achat des actions");
        secondStage.setScene(scene);
        secondStage.show();
    }
}

package front_end_Authentification.Actions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static front_end_Authentification.Actions.AcheterActions_Controller.setAchatAction;


public class ConfirmerAchatController {

    public static void setSetUpConfirmLabel(String[] achatAction) {
        ConfirmerAchatController.setUpConfirmLabel.setText("Vous vous apprétez à effectuer l'achat de " + achatAction[3] +" "+ achatAction[1] + " action(s). La valeur d'une action étant : " + achatAction[2] + "euros, vous allez payer : " + achatAction[4] + "euros. Si vous souhaitez donner un libellé à votre ensemble d'action que vous vous apprêtez à acheter, complétez le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");
    }

    private static Label setUpConfirmLabel;

    @FXML
    private Label confirmLabel = setUpConfirmLabel;
    @FXML
    private TextField libelleTextField;

    @FXML
    protected void confirmerButton(){
        setAchatAction(libelleTextField.getText());
        //Ajouter toute la partie concernant l'association de l'achat à l'entité.


    }
    protected static void afficherConfirmerAchatActions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Actions/F_ConfirmerAchatActions.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Confirmation d'achat des actions");
        secondStage.setScene(scene);
        secondStage.show();
    }

}

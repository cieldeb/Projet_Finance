package front_end_Authentification.Actions;

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

import static front_end_Authentification.Actions.AcheterActions_Controller.getAchatAction;
import static front_end_Authentification.Actions.AcheterActions_Controller.setAchatAction;


public class ConfirmerAchatController {
    protected static String[] achatAction = getAchatAction();
    @FXML
    private Label confirmLabel = new Label("Vous vous apprétez à effectuer l'achat de " + achatAction[3] +" action(s) "+ achatAction[1] + ". La valeur d'une action étant : " + achatAction[2] + "euros, vous allez payer : " + achatAction[4] + "euros. Si vous souhaitez donner un libellé à votre ensemble d'action que vous vous apprêtez à acheter, complétez le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");
    @FXML
    private TextField libelleTextField;
    @FXML
    protected void confirmerButton(){
        setAchatAction(libelleTextField.getText());
        //Ajouter toute la partie concernant l'association de l'achat à l'entité.


    }
    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
        AcheterActions_Controller.afficherAcheterActions();

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    protected static void afficherConfirmerAchatActions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Actions/F_ConfirmerAchatActions.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage secondStage = new Stage();

        ConfirmerAchatController controlleur = fxmlLoader.getController();
        controlleur.confirmLabel.setText("Vous vous apprétez à effectuer l'achat de " + achatAction[3] +" "+ achatAction[1] + " action(s). La valeur d'une action étant : " + achatAction[2] + "euros, vous allez payer : " + achatAction[4] + "euros. Si vous souhaitez donner un libellé à votre ensemble d'action que vous vous apprêtez à acheter, complétez le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");

        secondStage.setTitle("Confirmation d'achat des actions");
        secondStage.setScene(scene);
        secondStage.show();
    }

}

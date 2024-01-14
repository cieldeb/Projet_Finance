package front_end_Authentification.Actions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static front_end_Authentification.Actions.AcheterActions_Controller.getAchatAction;

public class ConfirmerAchatController {
    private String[] infoAchatActin = getAchatAction();
    @FXML
    private Label confirmLabel;
    @FXML
    protected void confirmerButton(){}
    protected static void afficherConfirmerAchatActions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Actions/F_ConfirmerAchatActions.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Confirmation d'achat des actions");
        secondStage.setScene(scene);
        secondStage.show();
    }

    public void setUpConfirmLabel(){

    }
}

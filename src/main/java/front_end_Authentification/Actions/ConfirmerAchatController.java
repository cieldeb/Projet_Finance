package front_end_Authentification.Actions;

import front_end_Authentification.F_Authentification_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static front_end_Authentification.Actions.AcheterActions_Controller.setAchatAction;


public class ConfirmerAchatController {
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();
    public static void setSetUpConfirmLabel(String[] achatAction) {
        ConfirmerAchatController.setUpConfirmLabel.setText("Vous vous apprétez à effectuer l'achat de " + achatAction[3] +" "+ achatAction[1] + " action(s). La valeur d'une action étant : " + achatAction[2] + "euros, vous allez payer : " + achatAction[4] + "euros. Si vous souhaitez donner un libellé à votre ensemble d'action que vous vous apprêtez à acheter, complétez le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");
    }
    private static Label setUpConfirmLabel;
    @FXML
    private Label confirmLabel = setUpConfirmLabel;
    @FXML
    private TextField libelleTextField;
    @FXML
    private ComboBox<String> actionsCompteDebite;
    @FXML
    private void initialize(){
        try {
            File jsonFile = new File("files/listeinscrits.json");
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
            JSONArray jsonArray = new JSONArray(jsonContent);

            boolean userFound = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray comptesArray = userObject.optJSONArray("COMPTES");
                    if (comptesArray != null) {
                        for (int j = 0; j < comptesArray.length(); j++) {
                            JSONObject account = comptesArray.getJSONObject(j);
                            int type = account.optInt("TYPE");
                            System.out.println("Type : " + type);

                            StringBuilder displayValue = new StringBuilder("Compte " + j + " ");
                            if (type == 1){
                                displayValue.append(" - Courant - n° " + account.optInt("IBAN"));
                            } else if (type == 2){
                                displayValue.append(" - Epargne - n° " + account.optInt("IBAN"));
                            }
                            actionsCompteDebite.getItems().add(displayValue.toString().trim());
                        }
                        userFound = true;
                    }
                    break;
                }
            }
            if (!userFound) {
                System.out.println("User not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void confirmerButton(){
        setAchatAction(libelleTextField.getText());
        //Ajouter toute la partie concernant l'association de l'achat à l'entité.


    }
    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
        front_end_Authentification.Accueil.F_Accueil_Controller.afficher_F_Accueil();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
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

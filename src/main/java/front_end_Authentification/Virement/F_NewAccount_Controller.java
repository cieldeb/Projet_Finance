package front_end_Authentification.Virement;

import front_end_Authentification.Accueil.F_Accueil_Controller;
import front_end_Authentification.Application;
import front_end_Authentification.F_Authentification_Controller;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

public class F_NewAccount_Controller {
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();
    @FXML
    private TextField soldeDepart;
    ToggleGroup typeCompte = new ToggleGroup();
    @FXML
    RadioButton cCourant;
    @FXML
    RadioButton cEpargne;
    @FXML
    public void initialize(){
        cCourant.setToggleGroup(typeCompte);
        cEpargne.setToggleGroup(typeCompte);
        typeCompte.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov, Toggle old_toggle,
                 Toggle new_toggle) -> {
                    if (typeCompte.getSelectedToggle() != null) {
                        if (typeCompte.getSelectedToggle() == cCourant){
                            type = 1;
                            System.out.println("Type de compte sélectionné : Compte Courant - Type "+ type);
                        } else if (typeCompte.getSelectedToggle() == cEpargne) {
                            type = 2;
                            System.out.println("Type de compte sélectionné : Compte Epargne - Type "+ type);
                        }
                    }
                });
    }
    public static void afficher_F_NewAccount() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/front_end_Virement/F_NewAccount.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Créer un compte");
        stage.setScene(scene);
        stage.show();
    }
    int type = 0;
    @FXML
    public void btnNewAccount(ActionEvent e ) throws IOException {

        int solde = Integer.parseInt(soldeDepart.getText());

        File jsonFile = new File("files/listeinscrits.json");
        String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
        JSONArray jsonArray = new JSONArray(jsonContent);

        JSONObject newAccount = new JSONObject();
        newAccount.put("SOLDE", "+" + solde);
        newAccount.put("TYPE", type);

        int iban;
        boolean isIbanUnique;
        do {
            iban = ThreadLocalRandom.current().nextInt(100000, 999999);
            isIbanUnique = true;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                JSONArray comptesArray = userObject.optJSONArray("COMPTES");
                if (comptesArray != null) {
                    for (int j = 0; j < comptesArray.length(); j++) {
                        JSONObject compte = comptesArray.getJSONObject(j);
                        if (compte.optInt("IBAN") == iban) {
                            isIbanUnique = false;
                            break;
                        }
                    }
                }
                if (!isIbanUnique) {
                    break;
                }
            }
        } while (!isIbanUnique);

        newAccount.put("IBAN", iban);

        boolean userFound = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject userObject = jsonArray.getJSONObject(i);
            if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                JSONArray comptesArray = userObject.optJSONArray("COMPTES");
                if (comptesArray == null) {
                    comptesArray = new JSONArray();
                    userObject.put("COMPTES", comptesArray);
                }
                comptesArray.put(newAccount);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            System.out.println("User not found");
        } else {
            try (BufferedWriter jsonWriter = new BufferedWriter(new FileWriter(jsonFile))) {
                jsonWriter.write(jsonArray.toString(4));
            }
        }

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}

package front_end_Authentification.Actions;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Crypto.Crypto;
import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Crypto_Front.AcheterCryptos_Controller;
import front_end_Authentification.F_Authentification_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static front_end_Authentification.Accueil.F_Accueil_Controller.getSelectedWallet;
import static front_end_Authentification.Actions.AcheterActions_Controller.getAchatAction;
import static front_end_Authentification.Actions.AcheterActions_Controller.setAchatAction;
import static front_end_Authentification.Virement.F_Virement_Controller.getNextAvailableID;
import static java.lang.Float.parseFloat;
import static java.lang.Float.sum;
import static java.lang.Integer.parseInt;
import static java.lang.Math.round;


public class ConfirmerAchatController {
    protected static Portefeuille selectedWallet = getSelectedWallet();
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();
    protected static String[] achatAction = getAchatAction();

    @FXML
    private Label recapLabel;
    @FXML
    private TextField libelleTextField;
    @FXML
    private ChoiceBox compteChoiceBox;
    @FXML
    private void initialize(){
        recapLabel.setText("Vous vous apprêtez à effectuer l'achat de " + achatAction[3] +" "+ achatAction[1] + ". La valeur d'un coin étant : " + achatAction[2] + "euros, vous allez payer : " + achatAction[4] + "euros. Donnez un libellé à votre ensemble de crypto que vous vous apprêtez à acheter en complétant le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");
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
                            compteChoiceBox.getItems().add(displayValue.toString().trim());
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
    protected void confirmerButton(ActionEvent e) throws IOException{
        setAchatAction(libelleTextField.getText());
        //Ajouter toute la partie concernant l'association de l'achat à l'entité.
        Action newAction = new Action(achatAction[0],achatAction[1],parseFloat(achatAction[2]),parseFloat(achatAction[2]),parseInt(achatAction[3]),parseFloat(achatAction[4]),parseFloat(achatAction[4]));
        String compteDebite = (String) compteChoiceBox.getValue();
        String[] parts = compteDebite.split("n° ");
        int ibanDebite = parseInt(parts[1]);

        float prix = parseFloat(achatAction[4]);

        //Ajout de la transaction dans la partie TRANSACTIONS de l'émetteur dans transactions.json

        try {
            JSONArray entryArray = new JSONArray(new JSONTokener(new FileReader("files/transactions.json")));
            for (int i = 0; i < entryArray.length(); i++) {
                JSONObject userObject = entryArray.getJSONObject(i);
                if (userObject.optInt("IBAN") == ibanDebite) {
                    JSONArray transacArray = userObject.has("TRANSACTIONS") ? userObject.getJSONArray("TRANSACTIONS") : new JSONArray();
                    int newID = getNextAvailableID(transacArray);

                    int indexMontantBase = transacArray.length();
                    Object montantBase = transacArray.toList().get(indexMontantBase - 1);
                    String montantBaseStr = montantBase.toString();
                    String[] part1 = montantBaseStr.split(", ");
                    String extractedAmount = "";
                    for (String part2 : part1) {
                        if (part2.startsWith("SOLDE=")) {
                            extractedAmount = part2.substring("SOLDE=".length());
                            break;
                        }
                    }

                    JSONObject newTransaction = new JSONObject();
                    newTransaction.put("ID", newID);
                    newTransaction.put("EMETTEUR", ibanDebite);
                    newTransaction.put("RECEPTEUR", 12345);
                    newTransaction.put("MONTANT", round(prix));
                    int newSoldeRecepteur = Integer.parseInt(extractedAmount) - Math.round(prix);
                    newTransaction.put("SOLDE",  newSoldeRecepteur);

                    transacArray.put(newTransaction);

                    userObject.put("TRANSACTIONS", transacArray);
                    break;
                }
            }
            try (FileWriter file = new FileWriter("files/transactions.json")) {
                file.write(entryArray.toString(4));
            } catch (IOException f) {
                f.printStackTrace();
            }
        } catch (Exception j) {
            j.printStackTrace();
        }

        //Modification du solde de l'émetteur dans listeinscrits.json et écriture sur le fichier JSON des cryptos achetées

        try {
            JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray comptesArray = userObject.getJSONArray("COMPTES");
                    JSONArray portefeuilleArray = userObject.getJSONArray("PORTEFEUILLE");
                    for (int j = 0; j < comptesArray.length(); j++) {
                        JSONObject compte = comptesArray.getJSONObject(j);
                        if (ibanDebite == compte.optInt("IBAN")) {
                            int currentSolde = compte.getInt("SOLDE");
                            compte.put("SOLDE", currentSolde - round(parseFloat(achatAction[4])));
                            break;
                        }
                    }

                    for(int j = 0; j < portefeuilleArray.length(); j++) {
                        JSONObject portefeuille = portefeuilleArray.getJSONObject(j);
                        if (portefeuille.getString("LIBELLE").equals(selectedWallet.getName())){
                            JSONArray listActions = portefeuille.getJSONArray("ACTIONS");
                            listActions.put(newAction.createJSONObject_Action());
                            portefeuille.put("ACTIONS",listActions);
                            portefeuilleArray.put(j,portefeuille);
                        }
                    }

                    try (FileWriter file = new FileWriter("files/listeinscrits.json")) {
                        file.write(usersArray.toString(4));
                        file.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }

        } catch (IOException | NumberFormatException f) {
            f.printStackTrace();
        }
        AcheterActions_Controller.afficherAcheterActions();

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();

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

    public boolean jsonArrayContainsKey(JSONArray jsonArray, String key) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has(key)) {
                return true;
            }
        }
        return false;
    }
}

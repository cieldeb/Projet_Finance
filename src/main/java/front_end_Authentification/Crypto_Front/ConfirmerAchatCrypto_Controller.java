package front_end_Authentification.Crypto_Front;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Crypto.Crypto;
import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Actions.AcheterActions_Controller;
import front_end_Authentification.Actions.Application_Action;
import front_end_Authentification.Actions.ConfirmerAchatController;
import front_end_Authentification.F_Authentification_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import static front_end_Authentification.Crypto_Front.AcheterCryptos_Controller.getAchatCrypto;
import static front_end_Authentification.Crypto_Front.AcheterCryptos_Controller.setAchatCrypto;
import static front_end_Authentification.Virement.F_Virement_Controller.getNextAvailableID;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.sum;

public class ConfirmerAchatCrypto_Controller {
    protected static Portefeuille selectedWallet = getSelectedWallet();
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();

    protected static String[] achatCrypto = getAchatCrypto();

    float montantValueFloat = AcheterCryptos_Controller.getValueSimulation;
    int montantValue = (int)Math.floor(montantValueFloat);

    @FXML
    private Label recapLabel;
    @FXML
    private TextField libelleTextField;
    @FXML
    private ChoiceBox compteChoiceBox;

    @FXML
    private void initialize(){
        recapLabel.setText("Vous vous apprêtez à effectuer l'achat de " + achatCrypto[3] +" "+ achatCrypto[1] + ". La valeur d'un coin étant : " + achatCrypto[2] + "euros, vous allez payer : " + achatCrypto[4] + "euros. Donnez un libellé à votre ensemble de crypto que vous vous apprêtez à acheter en complétant le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");
        try{
            JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray comptesArray = userObject.optJSONArray("COMPTES");
                    if (comptesArray != null){
                        for (int j = 0; j < comptesArray.length(); j++) {
                            JSONObject libelle = comptesArray.getJSONObject(j);
                            String ibanCompte = libelle.optString("IBAN");

                            compteChoiceBox.getItems().add(ibanCompte);
                        }
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void confirmerButton(ActionEvent e) throws IOException{
        setAchatCrypto(libelleTextField.getText());
        Crypto newCrypto = new Crypto(achatCrypto[0],achatCrypto[1],parseFloat(achatCrypto[2]),parseFloat(achatCrypto[2]),parseFloat(achatCrypto[3]),parseFloat(achatCrypto[4]),parseFloat(achatCrypto[4]));
        int ibanCompteDebite = parseInt((String)compteChoiceBox.getValue());
        /*      PARTIE TRANSACTION A MODIFIER (signé Gab) Je te laisse toutes cette partie en commenaire tu en fais ce que tu veux!
        String[] parts = ibanCompteDebite.split("n° ");
        int ibanDebite = Integer.parseInt(parts[1]);

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
                    newTransaction.put("MONTANT", montantValue);
                    int newSoldeRecepteur = sum(Integer.parseInt(extractedAmount), montantValue);
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
        }*/

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
                        if (ibanCompteDebite == compte.optInt("IBAN")) {
                            int currentSolde = compte.getInt("SOLDE");
                            //System.out.println("Solde du compte débité avant transaction: " + currentSolde);
                            int updatedSolde = currentSolde - montantValue;
                            //System.out.println("Solde du compte débité après transaction: " + updatedSolde);
                            compte.put("SOLDE", updatedSolde);
                            break;
                        }
                    }

                    for(int j = 0; j < portefeuilleArray.length(); j++) {
                        JSONObject portefeuille = portefeuilleArray.getJSONObject(j);
                        if (portefeuille.getString("LIBELLE").equals(selectedWallet.getName())){
                            JSONArray listCrypto = portefeuille.getJSONArray("CRYPTOS");
                            listCrypto.put(newCrypto.createJSONObject_Crypto());
                            portefeuille.put("CRYPTOS",listCrypto);
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
        AcheterCryptos_Controller.afficherAcheterCrypto();

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();

    }

    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
        AcheterCryptos_Controller.afficherAcheterCrypto();

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    protected static void afficherConfirmerAchatCryptos() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Crypto/F_ConfirmerAchatCryptos.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage secondStage = new Stage();

        secondStage.setTitle("Confirmation d'achat de crypto-monnaie");
        secondStage.setScene(scene);
        secondStage.show();
    }
}

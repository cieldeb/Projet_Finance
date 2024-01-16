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
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static front_end_Authentification.Actions.AcheterActions_Controller.setAchatAction;
import static front_end_Authentification.Virement.F_Virement_Controller.getNextAvailableID;


public class ConfirmerAchatController {
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();
    public static void setSetUpConfirmLabel(String[] achatAction) {
        ConfirmerAchatController.setUpConfirmLabel.setText("Vous vous apprétez à effectuer l'achat de " + achatAction[3] +" "+ achatAction[1] + " action(s). La valeur d'une action étant : " + achatAction[2] + "euros, vous allez payer : " + achatAction[4] + "euros. Si vous souhaitez donner un libellé à votre ensemble d'action que vous vous apprêtez à acheter, complétez le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");
    }

    float montantValueFloat = AcheterActions_Controller.getValueSimulation;
    int montantValue = (int)Math.floor(montantValueFloat);

    private static Label setUpConfirmLabel;
    @FXML
    private Label confirmLabel = setUpConfirmLabel;
    @FXML
    private TextField libelleTextField;
    @FXML
    private ComboBox<String> compteDebiteBox;
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
                            compteDebiteBox.getItems().add(displayValue.toString().trim());
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

        String compteDebite = compteDebiteBox.getValue();
        String[] parts = compteDebite.split("n° ");
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
                    //newTransaction.put("MONTANT", montantValue);
                    //newSoldeRecepteur = sum(Integer.parseInt(extractedAmount), montantValue);
                    //newTransaction.put("SOLDE",  newSoldeRecepteur);
                    //EXTRAIRE LE MONTANT DE LA TRANSACTION

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

        //Modification du solde de l'émetteur dans listeinscrits.json

        try {
            JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {

                    if (userObject.has("COMPTES")) {
                        JSONArray comptesArray = userObject.getJSONArray("COMPTES");
                        boolean containsIBAN = jsonArrayContainsKey(comptesArray, "IBAN");

                        if (containsIBAN) {
                            for (int j = 0; j < comptesArray.length(); j++) {
                                JSONObject compte = comptesArray.getJSONObject(j);
                                int ibanEnregistre = compte.optInt("IBAN");
                                if (ibanDebite == ibanEnregistre) {
                                    int currentSolde = compte.getInt("SOLDE");
                                    System.out.println("Solde du compte débité avant transaction: " + currentSolde);
                                    //updatedSolde = currentSolde - montantValue;
                                    //System.out.println("Solde du compte débité après transaction: " + updatedSolde);
                                    //compte.put("SOLDE", updatedSolde);
                                    break;
                                }
                            }
                        }
                        try (FileWriter file = new FileWriter("files/listeinscrits.json")) {
                            file.write(usersArray.toString(4));
                            file.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.err.println("La clé 'COMPTES' n'existe pas dans l'objet JSON de l'utilisateur.");
                    }
                    break;
                }
            }

        } catch (IOException | NumberFormatException f) {
            f.printStackTrace();
        }

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

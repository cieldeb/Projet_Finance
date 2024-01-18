package front_end_Authentification.Crypto_Front;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Crypto.Crypto;
import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Actions.Application_Action;
import front_end_Authentification.F_Authentification_Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.projet_finance.back_end.Crypto.Crypto.vendreCryptoJSON;
import static front_end_Authentification.Accueil.F_Accueil_Controller.getSelectedWallet;
import static front_end_Authentification.Virement.F_Virement_Controller.getNextAvailableID;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.sum;

public class VendreCrypto_Controller {
    protected static Portefeuille selectedWallet = getSelectedWallet();
    protected static LinkedList<Crypto> listCryptos =  selectedWallet.getListCrypto();
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();

    @FXML
    private TableColumn nomTableColumn;
    @FXML
    private TableColumn symboleTableColumn;
    @FXML
    private TableColumn quantiteTableColumn;
    @FXML
    private TableColumn valeurInitialeTableColumn;
    @FXML
    private TableColumn derniereValeurTableColumn;
    @FXML
    private TableColumn valeurTTLInitialeTableColumn;
    @FXML
    private TableColumn derniereValeurTTLTableColumn;
    @FXML
    TableView<Map<String, Object>> cryptosTableView;
    @FXML
    private ChoiceBox cryptoChoiceBox;
    @FXML
    private ChoiceBox compteChoiceBox;
    @FXML
    private void initialize(){
        for (int i = 0; i < listCryptos.size(); i++) {
            cryptoChoiceBox.getItems().add(listCryptos.get(i).getName());
        }
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
    protected void simulerButton(){
        setUpTableColumn();
        LinkedList<Crypto> listCryptosFromWallet = selectedWallet.getListCrypto();
        ObservableList<Map<String, Object>> listCryptos = FXCollections.observableArrayList();
        for (int i = 0 ; i < listCryptosFromWallet.size() ; i++ ){
            Crypto crypto = listCryptosFromWallet.get(i);
            if (crypto.getName().equals((String) cryptoChoiceBox.getValue())){
                Map<String, Object> cryptoRow = new HashMap<>();
                cryptoRow.put("Nom",crypto.getName());
                cryptoRow.put("Symbole",crypto.getSymbol());
                cryptoRow.put("Valeur Initiale",crypto.getInitialValue());
                cryptoRow.put("Dernière valeur",crypto.getValue());
                cryptoRow.put("Quantité",crypto.getQuantite());
                cryptoRow.put("Valeur Totale initiale",crypto.getValeurTotale());
                cryptoRow.put("Dernière valeur totale",crypto.getActuelleValeurTotale());
                listCryptos.add(cryptoRow);
                break;
            }

        }
        cryptosTableView.setItems(listCryptos);

    }
    @FXML
    protected void vendreButton(ActionEvent e) throws IOException {
        String selectedCompte = (String) compteChoiceBox.getValue();
        String selectedCrypto = (String) cryptoChoiceBox.getValue();
        float valeurTransaction = 0 ;
        for (int i =0 ; i<listCryptos.size() ; i++){
            if (listCryptos.get(i).getName().equals(selectedCrypto)){
                valeurTransaction = listCryptos.get(i).getInitialValue();
                System.out.println(valeurTransaction);
            }
        }



        //Ajout de la transaction dans la partie TRANSACTIONS de l'iban sélectionné dans transactions.json

        try {
            JSONArray entryArray = new JSONArray(new JSONTokener(new FileReader("files/transactions.json")));
            for (int i = 0; i < entryArray.length(); i++) {
                JSONObject userObject = entryArray.getJSONObject(i);
                if (Math.round(userObject.optInt("IBAN")) == Integer.parseInt(selectedCompte)) {
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
                    newTransaction.put("EMETTEUR", 67890);
                    newTransaction.put("RECEPTEUR", Integer.parseInt(selectedCompte));
                    newTransaction.put("MONTANT", valeurTransaction);
                    System.out.println(extractedAmount);
                    int newSoldeRecepteur = sum(Integer.parseInt(extractedAmount), (int) valeurTransaction);
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

        vendreCryptoJSON(selectedCrypto,parseInt(selectedCompte),Math.round(valeurTransaction),selectedWallet.getName());
        front_end_Authentification.Crypto_Front.MainCryptoController.afficherMainCryptos();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();

    }
    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
            front_end_Authentification.Crypto_Front.MainCryptoController.afficherMainCryptos();
            Node button = (Node) e.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
    }
    public static void afficherVendreCryptos() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Crypto/F_VendreCrypto.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Vendre crypto");
        secondStage.setScene(scene);
        secondStage.show();
    }
    private void setUpTableColumn(){
        nomTableColumn.setCellValueFactory( new MapValueFactory<>("Nom"));
        symboleTableColumn.setCellValueFactory( new MapValueFactory<>("Symbole"));
        quantiteTableColumn.setCellValueFactory( new MapValueFactory<>("Quantité"));
        valeurInitialeTableColumn.setCellValueFactory( new MapValueFactory<>("Valeur Initiale"));
        derniereValeurTableColumn.setCellValueFactory( new MapValueFactory<>("Dernière valeur"));
        valeurTTLInitialeTableColumn.setCellValueFactory( new MapValueFactory<>("Valeur Totale initiale"));
        derniereValeurTTLTableColumn.setCellValueFactory( new MapValueFactory<>("Dernière valeur totale"));

    }

}

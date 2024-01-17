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

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.projet_finance.back_end.Crypto.Crypto.vendreCryptoJSON;
import static front_end_Authentification.Accueil.F_Accueil_Controller.getSelectedWallet;
import static java.lang.Integer.parseInt;

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
    protected void vendreButton(){
        String selectedCompte = (String) compteChoiceBox.getValue();
        String selectedCrypto = (String) cryptoChoiceBox.getValue();
        float valeurTransaction = 0 ;
        for (int i =0 ; i<listCryptos.size() ; i++){
            if (listCryptos.get(i).getName().equals(selectedCrypto)){
                valeurTransaction = listCryptos.get(i).getInitialValue();
                System.out.println(valeurTransaction);
            }
        }
        vendreCryptoJSON(selectedCrypto,parseInt(selectedCompte),Math.round(valeurTransaction),selectedWallet.getName());

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

package front_end_Authentification.Actions;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Crypto.Crypto;
import com.example.projet_finance.back_end.Entite.Portefeuille;
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static com.example.projet_finance.back_end.Actions.Action.vendreActionJSON;
import static com.example.projet_finance.back_end.Crypto.Crypto.vendreCryptoJSON;
import static front_end_Authentification.Accueil.F_Accueil_Controller.*;
import static front_end_Authentification.Virement.F_Virement_Controller.getNextAvailableID;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.sum;

public class VendreAction_Controller {
    protected static Portefeuille selectedWallet = getSelectedWallet();
    protected static LinkedList<Action> listActions =  selectedWallet.getListActions();
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
    TableView<Map<String, Object>> actionsTableView;
    @FXML
    private ChoiceBox actionChoiceBox;
    @FXML
    private ChoiceBox compteChoiceBox;
    @FXML
    private void initialize(){
        mettreAjoursellectedWallet(getNomWallet());
        selectedWallet=getSelectedWallet();
        for (int i = 0; i < listActions.size(); i++) {
            actionChoiceBox.getItems().add(listActions.get(i).getName());
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
        LinkedList<Action> listActionsFromWallet = selectedWallet.getListActions();
        ObservableList<Map<String, Object>> listActions = FXCollections.observableArrayList();
        for (int i = 0 ; i < listActionsFromWallet.size() ; i++ ){
            Action action = listActionsFromWallet.get(i);
            if (action.getName().equals((String) actionChoiceBox.getValue())){
                Map<String, Object> actionRow = new HashMap<>();
                actionRow.put("Nom",action.getName());
                actionRow.put("Symbole",action.getSymbol());
                actionRow.put("Valeur Initiale",action.getInitialValue());
                actionRow.put("Dernière valeur",action.getValue());
                actionRow.put("Quantité",action.getQuantite());
                actionRow.put("Valeur Totale initiale",action.getValeurTotale());
                actionRow.put("Dernière valeur totale",action.getActuelleValeurTotale());
                listActions.add(actionRow);
                break;
            }

        }
        actionsTableView.setItems(listActions);

    }


    @FXML
    protected void vendreButton(ActionEvent e) throws IOException {

        String compteDebite = (String) compteChoiceBox.getValue();
        String[] parts = compteDebite.split("n° ");
        String ibanCompte = parts[1];

        System.out.println(ibanCompte);
        String selectedAction = (String) actionChoiceBox.getValue();
        float valeurTransaction = 0 ;
        for (int i = 0; i< listActions.size() ; i++){
            if (listActions.get(i).getName().equals(selectedAction)){
                valeurTransaction = listActions.get(i).getInitialValue();
                System.out.println(valeurTransaction);
            }
        }

        //Ajout de la transaction dans la partie TRANSACTIONS de l'iban sélectionné dans transactions.json

        try {
            JSONArray entryArray = new JSONArray(new JSONTokener(new FileReader("files/transactions.json")));
            for (int i = 0; i < entryArray.length(); i++) {
                JSONObject userObject = entryArray.getJSONObject(i);
                if (Math.round(userObject.optInt("IBAN")) == Integer.parseInt(ibanCompte)) {
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
                    newTransaction.put("EMETTEUR", 12345);
                    newTransaction.put("RECEPTEUR", Integer.parseInt(ibanCompte));
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

        vendreActionJSON(selectedAction,parseInt(ibanCompte),Math.round(valeurTransaction),selectedWallet.getName());
        front_end_Authentification.Actions.MainActionController.afficherMainActions();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();

    }
    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
        front_end_Authentification.Actions.MainActionController.afficherMainActions();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    public static void afficherVendreActions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Actions/F_VendreAction.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Vendre action");
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

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
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.projet_finance.back_end.Actions.Action.vendreActionJSON;
import static com.example.projet_finance.back_end.Crypto.Crypto.vendreCryptoJSON;
import static front_end_Authentification.Accueil.F_Accueil_Controller.getSelectedWallet;
import static java.lang.Integer.parseInt;

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
        String selectedCompte = (String) compteChoiceBox.getValue();
        String selectedAction = (String) actionChoiceBox.getValue();
        float valeurTransaction = 0 ;
        for (int i = 0; i< listActions.size() ; i++){
            if (listActions.get(i).getName().equals(selectedAction)){
                valeurTransaction = listActions.get(i).getInitialValue();
                System.out.println(valeurTransaction);
            }
        }
        vendreActionJSON(selectedAction,parseInt(selectedCompte),Math.round(valeurTransaction),selectedWallet.getName());
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

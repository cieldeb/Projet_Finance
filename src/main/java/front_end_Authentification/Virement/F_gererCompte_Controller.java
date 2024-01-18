package front_end_Authentification.Virement;

import front_end_Authentification.Application;
import front_end_Authentification.F_Authentification_Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class F_gererCompte_Controller {
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();
    @FXML
    private ComboBox<String> compteAffiche;
    @FXML
    private TableView<Map<String, Object>> tableTransactions;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn emetteurCol;
    @FXML
    private TableColumn recepteurCol;
    @FXML
    private TableColumn montantCol;
    @FXML
    private TableColumn newSoldeCol;
    private ObservableList<PieChart.Data> repComptesData;
    @FXML
    private PieChart repComptes = new PieChart(repComptesData);
    private ObservableList<PieChart.Data> mouvSoldeData;
    @FXML
    private void initialize(){
        tableTransactions.setPlaceholder(new Label("Ce compte n'a effectué aucune transaction"));
        compteAffiche.setVisibleRowCount(3);

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

                            StringBuilder displayValue = new StringBuilder("Compte ");
                            if (type == 1){
                                displayValue.append("Courant n° " + account.optInt("IBAN"));
                            } else if (type == 2){
                                displayValue.append("Epargne n° " + account.optInt("IBAN"));
                            }
                            compteAffiche.getItems().add(displayValue.toString().trim());
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

        //Remplissage du camembert

        repComptesData = FXCollections.observableArrayList();
        repComptes.setTitle("Répartition de la valeur entre les comptes");
        repComptes.setLabelLineLength(15);
        repComptes.setLegendSide(Side.LEFT);
        repComptes.setData(repComptesData);

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
                            String IBAN = account.optString("IBAN");
                            int poidsSolde = account.optInt("SOLDE");
                            repComptesData.add(new PieChart.Data(IBAN.toString(), poidsSolde));
                        }
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void btnNewAccount(ActionEvent e) throws IOException {
        F_NewAccount_Controller.afficher_F_NewAccount();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void btnVirement(ActionEvent e) throws IOException {
        F_Virement_Controller.afficher_F_Virement();
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
    public static void afficher_F_gererCompte() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/front_end_Virement/F_gererCompte.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Gérer les comptes");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void compteAfficheSelectionne() throws FileNotFoundException {
        String compte = compteAffiche.getValue();
        String[] parts = compte.split("n° ");
        String compteObserve = parts[1];

        try {
            JSONArray entryArray = new JSONArray(new JSONTokener(new FileReader("files/transactions.json")));
            ObservableList<Map<String, Object>> transactions = FXCollections.observableArrayList();

            for (int i = 0; i < entryArray.length(); i++) {
                JSONObject accountObject = entryArray.getJSONObject(i);
                if (accountObject.optString("IBAN").equals(compteObserve)) {
                    JSONArray transacArray = accountObject.optJSONArray("TRANSACTIONS");

                    for (int j = transacArray.length() - 1; j > - 1 ; j--) {
                        JSONObject transactionObject = transacArray.getJSONObject(j);
                        Map<String, Object> rowData = new HashMap<>();
                        int id = transactionObject.optInt("ID");
                        int solde = transactionObject.optInt("SOLDE");
                        rowData.put("ID", id);
                        rowData.put("RECEPTEUR", transactionObject.optInt("RECEPTEUR"));
                        rowData.put("EMETTEUR", transactionObject.optInt("EMETTEUR"));
                        rowData.put("SOLDE", solde);
                        rowData.put("MONTANT", transactionObject.optDouble("MONTANT"));

                        transactions.add(rowData);
                    }
                    break;
                }
            }
            setupTableColumns();
            tableTransactions.setItems(transactions);
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void setupTableColumns() {
        idCol.setCellValueFactory(new MapValueFactory<>("ID"));
        emetteurCol.setCellValueFactory(new MapValueFactory<>("EMETTEUR"));
        recepteurCol.setCellValueFactory(new MapValueFactory<>("RECEPTEUR"));
        montantCol.setCellValueFactory(new MapValueFactory<>("MONTANT"));
        newSoldeCol.setCellValueFactory(new MapValueFactory<>("SOLDE"));
    }
}
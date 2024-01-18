package front_end_Authentification.Actions;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Accueil.F_Accueil_Controller;
import front_end_Authentification.F_Authentification_Controller;
import front_end_Authentification.Portefeuilles.GererPortefeuille_Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import static front_end_Authentification.Accueil.F_Accueil_Controller.*;
import static front_end_Authentification.Actions.VendreAction_Controller.afficherVendreActions;

public class MainActionController {
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();

    protected static Portefeuille selectedWallet = getSelectedWallet();
    String walletSelectionne = selectedWallet.getName();
    private static String API_URL_SymbolSearch = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=SEARCH_SYMBOL&interval=1min&apikey=P5LEJHFFCZKVAI88" ;
    private static String API_URL_TimeSeriesIntraDay = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=SEARCH_SYMBOL&interval=1min&apikey=P5LEJHFFCZKVAI88" ;
    @FXML
    private TextField searchField;
    @FXML
    private CheckBox symboleCheckBox;
    @FXML
    private CheckBox valeurCheckBox;
    @FXML
    private Label rechercheInfoActionLabel;
    @FXML
    private Label erreurLabel;
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
    private ObservableList<PieChart.Data> nbrAcheteData;
    @FXML
    private PieChart nbrAchete = new PieChart(nbrAcheteData);
    private ObservableList<PieChart.Data> proportionCoutData;
    @FXML
    private PieChart proportionCout = new PieChart(proportionCoutData);
    @FXML
    private void initialize(){
        mettreAjoursellectedWallet(getNomWallet());
        selectedWallet=getSelectedWallet();
        setUpTableColumn();
        LinkedList<Action> listActionsFromWallet = selectedWallet.getListActions();
        ObservableList<Map<String, Object>> listActions = FXCollections.observableArrayList();
        for (int i = 0 ; i < listActionsFromWallet.size() ; i++ ){
            Action action = listActionsFromWallet.get(i);
            Map<String, Object> actionRow = new HashMap<>();
            actionRow.put("Nom",action.getName());
            actionRow.put("Symbole",action.getSymbol());
            actionRow.put("Valeur Initiale",action.getInitialValue());
            actionRow.put("Dernière valeur",action.getValue());
            actionRow.put("Quantité",action.getQuantite());
            actionRow.put("Valeur Totale initiale",action.getValeurTotale());
            actionRow.put("Dernière valeur totale",action.getActuelleValeurTotale());
            listActions.add(actionRow);
        }
        actionsTableView.setItems(listActions);

        //Initialisation du camembert de répartition des symboles possédés

        nbrAcheteData = FXCollections.observableArrayList();
        nbrAchete.setTitle("Répartition des symboles");
        nbrAchete.setLabelLineLength(15);
        //nbrAchete.setLegendSide(Side.LEFT);
        nbrAchete.setData(nbrAcheteData);

        //Initialisation du camembert de répartition de la valeur des symboles possédés

        proportionCoutData = FXCollections.observableArrayList();
        proportionCout.setTitle("Répartition de la valeur");
        proportionCout.setLabelLineLength(15);
        //proportionCout.setLegendSide(Side.RIGHT);
        proportionCout.setData(proportionCoutData);

        try {
            File jsonFile = new File("files/listeinscrits.json");
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
            JSONArray jsonArray = new JSONArray(jsonContent);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);

                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray portefeuillesArray = userObject.optJSONArray("PORTEFEUILLE");

                    if (portefeuillesArray != null) {
                        System.out.println("Portefeuilles array: " + portefeuillesArray);

                        for (int s = 0; s < portefeuillesArray.length(); s++) {
                            JSONObject walletObject = portefeuillesArray.getJSONObject(s);
                            String lookedAtWallet = walletObject.optString("LIBELLE");
                            System.out.println("Comparing: '" + lookedAtWallet + "' with '" + walletSelectionne + "'"); //Debug pour voir les comparaisons faites par la boucle suivante
                            if (lookedAtWallet.equals(walletSelectionne)) {
                                JSONArray actionsArray = walletObject.optJSONArray("ACTIONS");
                                System.out.println("Actions array: " + actionsArray);
                                int poidsQ = 0;
                                for(int r = 0; r < actionsArray.length(); r++){
                                    JSONObject objet = actionsArray.getJSONObject(r);
                                    String symbole = objet.optString("Symbole");
                                    poidsQ = objet.optInt("Quantité");
                                    nbrAcheteData.add(new PieChart.Data(symbole.toString(), poidsQ));
                                    int poidsV = objet.optInt("Dernière valeur totale");
                                    proportionCoutData.add(new PieChart.Data(symbole.toString(), poidsV));
                                }

                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void chercherButton(){
        if (symboleCheckBox.isSelected() && !valeurCheckBox.isSelected()){
            String stockSymbol = searchField.getText();
            String apiUrlWithKey = API_URL_SymbolSearch.replace("SEARCH_SYMBOL", stockSymbol);
            try {
                // Make API request and parse JSON response
                JSONObject stockData = getStockData(apiUrlWithKey);
                String suggestionsCompany = "";
                for (int i = 0 ; i<stockData.getJSONArray("bestMatches").length() ; i++){
                    String suggestionsName = stockData.getJSONArray("bestMatches").getJSONObject(i).getString("2. name");
                    String suggestionsSymbole = stockData.getJSONArray("bestMatches").getJSONObject(i).getString("1. symbol");
                    suggestionsCompany += "Entreprise : " + suggestionsName + " --> Symbole : "+ suggestionsSymbole + "\n";
                }

                if (suggestionsCompany == ""){
                    rechercheInfoActionLabel.setText("Aucun résultat trouvé");
                } else {
                    rechercheInfoActionLabel.setText(suggestionsCompany);
                }

            } catch (IOException e){
                e.printStackTrace();
                rechercheInfoActionLabel.setText("Erreur au moement de l'execution de la recherche.");

            }
        } else if (valeurCheckBox.isSelected() && !symboleCheckBox.isSelected()) {
            String stockSymbol = searchField.getText();
            String apiUrlWithKey = API_URL_TimeSeriesIntraDay.replace("SEARCH_SYMBOL", stockSymbol);
            try {
                // Make API request and parse JSON response
                JSONObject stockData = getStockData(apiUrlWithKey);
                String suggestionsCompany = "";
                String latestPrice = stockData.getJSONObject("Time Series (1min)").getJSONObject(stockData.getJSONObject("Meta Data").getString("3. Last Refreshed")).getString("4. close");
                suggestionsCompany += "La plus récente valeur de l'action est : " + latestPrice;
                if (suggestionsCompany == ""){
                    rechercheInfoActionLabel.setText("Aucun résultat trouvé");
                } else {
                    rechercheInfoActionLabel.setText(suggestionsCompany);
                }

            } catch (IOException e){
                e.printStackTrace();
                rechercheInfoActionLabel.setText("Erreur au moment de l'execution de la recherche.");

            }
            erreurLabel.setText("");
        } else if (valeurCheckBox.isSelected() && symboleCheckBox.isSelected()) {
            erreurLabel.setText("Veuillez selectionner un seul type de recherche.");
        } else{
            erreurLabel.setText("Veuillez selectionner le type de recherche.");
        }

    }

    @FXML
    protected void vendreButton(ActionEvent e) throws IOException {
            afficherVendreActions();
            Node button = (Node) e.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
    }
    @FXML
    protected void acheterButton(ActionEvent e) throws IOException {
        AcheterActions_Controller.afficherAcheterActions();

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
        GererPortefeuille_Controller.afficherGererPortefeuille();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    private JSONObject getStockData(String apiUrl) throws IOException {
        URL url = URI.create(apiUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection)
                url.openConnection();
        try (Scanner scanner = new Scanner(new
                InputStreamReader(connection.getInputStream()))) {
            scanner.useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";
            return new JSONObject(response);
        } finally {
            connection.disconnect();
        }
    }

    public static void afficherMainActions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Actions/F_mainActions.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Actions");
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


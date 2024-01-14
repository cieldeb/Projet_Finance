package front_end_Authentification.Actions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import static front_end_Authentification.Actions.ConfirmerAchatController.setSetUpConfirmLabel;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class AcheterActions_Controller {

    private static boolean simulationValid = false;

    private static String[] achatAction = new String[5];
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
    private Label simulationLabel;
    @FXML
    private TextField quantiteTextField;
    @FXML
    private TextField symboleTextField;
    @FXML
    private Label alertLabel;
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
    protected void simulerButton(){
        String stockSymbol = symboleTextField.getText();
        String apiUrlWithKey = API_URL_TimeSeriesIntraDay.replace("SEARCH_SYMBOL", stockSymbol);
        try {
            // Make API request and parse JSON response
            JSONObject stockData = getStockData(apiUrlWithKey);
            String actionValue = stockData.getJSONObject("Time Series (1min)").getJSONObject(stockData.getJSONObject("Meta Data").getString("3. Last Refreshed")).getString("4. close");
            if (actionValue == ""){
                simulationLabel.setText("Aucun résultat trouvé");
            } else {
                try{
                    float valueSimulation = parseFloat(actionValue) * parseInt(quantiteTextField.getText());
                    simulationLabel.setText(Float.toString(valueSimulation));
                    achatAction[1] = stockSymbol;
                    achatAction[2] = actionValue;
                    achatAction[3] = quantiteTextField.getText();
                    achatAction[4] = Float.toString(valueSimulation);
                    simulationValid = true;
                } catch (NumberFormatException e){
                    simulationLabel.setText("Vous ne pouvez acheter qu'un nombre entier d'actions.");
                }

            }

        } catch (IOException e){
            e.printStackTrace();
            simulationLabel.setText("Erreur au moment de l'execution de la simulation.");

        } catch(JSONException e){
            simulationLabel.setText("Aucun résultat trouvé");
        }

    }
    @FXML
    protected void acheterButton(ActionEvent e) throws IOException {
        if (simulationValid){
            alertLabel.setText("");
            ConfirmerAchatController.afficherConfirmerAchatActions();
            Node button = (Node) e.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
            setSetUpConfirmLabel(achatAction);

        } else{
            alertLabel.setText("Faites une simulation valide avant d'effectuer un achat.");
        }
        //Forcer à faire une simulation valide avant d'acheter...
    }
    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
        MainActionController.afficherMainActions();

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

    protected static void afficherAcheterActions() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Actions/F_AcheterActions.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Acheter des actions");
        secondStage.setScene(scene);
        secondStage.show();
    }
    public static String[] getAchatAction() {
        return achatAction;
    }
    public static void setAchatAction(String achatAction) {
        AcheterActions_Controller.achatAction[0] = achatAction;
    }
}

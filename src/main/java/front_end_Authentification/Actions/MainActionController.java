package front_end_Authentification.Actions;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class MainActionController {
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
        } else if (valeurCheckBox.isSelected() && symboleCheckBox.isSelected()) {
            erreurLabel.setText("Veuillez selectionner un seul type de recherche.");
        } else{
            erreurLabel.setText("Veuillez selectionner le type de recherche.");
        }

    }

    @FXML
    protected void acheterButton(){}
    @FXML
    protected void vendreButton(){}
    @FXML
    protected void retourButton(){}
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
}


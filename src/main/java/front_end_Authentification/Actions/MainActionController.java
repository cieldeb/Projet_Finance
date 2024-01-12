package front_end_Authentification.Actions;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private static String API_URL = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=SEARCH_SYMBOL&interval=1min&apikey=P5LEJHFFCZKVAI88" ;
    @FXML
    private TextField searchField;
    @FXML
    private CheckBox symboleCheckBox;
    @FXML
    private CheckBox valeurCheckBox;
    @FXML
    private Label rechercherInfoActionLabel;
    @FXML
    private Label erreurLabel;

    @FXML
    protected void chercherButton(){
        String stockSymbol = searchField.getText();
        String apiUrlWithKey = API_URL.replace("SEARCH_SYMBOL", stockSymbol);
        try {
            // Make API request and parse JSON response
            JSONObject stockData = getStockData(apiUrlWithKey);
            String suggestionsCompany = "";
            for (int i = 0 ; i<stockData.getJSONObject("bestMatches").length() ; i++){
                String suggestionsName = stockData.getJSONArray("bestMatches").getJSONObject(i).getString("2. name");
                String suggestionsSymbole = stockData.getJSONArray("bestMatches").getJSONObject(i).getString("1. symbol");
                suggestionsCompany += "Entreprise : " + suggestionsName + " --> Symbole : "+ suggestionsSymbole + "\n";
            }
            rechercherInfoActionLabel.setText(suggestionsCompany);
        } catch (IOException e){
            e.printStackTrace();
            rechercherInfoActionLabel.setText("Erreur au moement de l'execution de la recherche.");

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


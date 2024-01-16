package front_end_Authentification.Crypto_Front;

import front_end_Authentification.Actions.AcheterActions_Controller;
import front_end_Authentification.Actions.Application_Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class MainCryptoController {
    private static String API_URL_SymbolSearch = "https://api.coingecko.com/api/v3/search?query=SEARCH_SYMBOL&x_cg_api_key=CG-Hpntb6pauGUVcNfBZb4R3idc" ;
    private static String API_URL_TimeSeriesIntraDay = "https://api.coingecko.com/api/v3/simple/price?ids=SEARCH_SYMBOL&vs_currencies=eur&x_cg_api_key=CG-Hpntb6pauGUVcNfBZb4R3idc" ;
    @FXML
    private TextField searchField;
    @FXML
    private CheckBox symboleCheckBox;
    @FXML
    private CheckBox valeurCheckBox;
    @FXML
    private Label rechercheInfoCryptoLabel;
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
                String suggestionsCrypto = "";
                int lengthResult;
                if (stockData.getJSONArray("coins").length() <= 5){
                    lengthResult = stockData.getJSONArray("coins").length();
                    erreurLabel.setText("");
                } else{
                    lengthResult = 5;
                    erreurLabel.setText("Résultat tronqué, pour avoir de meilleures propositions, soyez plus précis lors de votre recherche.");
                }
                for (int i = 0 ; i<lengthResult ; i++){
                    String suggestionsName = stockData.getJSONArray("coins").getJSONObject(i).getString("name");
                    String suggestionsSymbole = stockData.getJSONArray("coins").getJSONObject(i).getString("id");
                    suggestionsCrypto += "Crypto-monnaie : " + suggestionsName + " --> Symbole : "+ suggestionsSymbole + "\n";
                }

                if (suggestionsCrypto == ""){
                    rechercheInfoCryptoLabel.setText("Aucun résultat trouvé");
                } else {
                    rechercheInfoCryptoLabel.setText(suggestionsCrypto);
                }

            } catch (IOException e){
                e.printStackTrace();
                rechercheInfoCryptoLabel.setText("Erreur au moement de l'execution de la recherche.");

            }
        } else if (valeurCheckBox.isSelected() && !symboleCheckBox.isSelected()) {
            String stockSymbol = searchField.getText();
            String apiUrlWithKey = API_URL_TimeSeriesIntraDay.replace("SEARCH_SYMBOL", stockSymbol);
            try {
                // Make API request and parse JSON response
                JSONObject stockData = getStockData(apiUrlWithKey);
                String suggestionsCrypto = "";
                String latestPrice = String.valueOf(stockData.getJSONObject(stockSymbol).getInt("eur"));
                suggestionsCrypto += "La plus récente valeur de la Crypto-Monnaie est : " + latestPrice +"€";
                if (suggestionsCrypto == ""){
                    rechercheInfoCryptoLabel.setText("Aucun résultat trouvé");
                } else {
                    rechercheInfoCryptoLabel.setText(suggestionsCrypto);
                }

            } catch (IOException e){
                e.printStackTrace();
                rechercheInfoCryptoLabel.setText("Erreur au moment de l'execution de la recherche.");

            }
            erreurLabel.setText("");
        } else if (valeurCheckBox.isSelected() && symboleCheckBox.isSelected()) {
            erreurLabel.setText("Veuillez selectionner un seul type de recherche.");
        } else{
            erreurLabel.setText("Veuillez selectionner le type de recherche.");
        }

    }
    @FXML
    protected void vendreButton(){}
    @FXML
    protected void acheterButton(ActionEvent e) throws IOException {
        AcheterCryptos_Controller.afficherAcheterCrypto();

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

    public static void afficherMainCryptos() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Crypto/F_mainCryptos.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Actions");
        secondStage.setScene(scene);
        secondStage.show();
    }
}

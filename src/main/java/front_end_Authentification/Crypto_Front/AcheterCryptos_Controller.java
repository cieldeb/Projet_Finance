package front_end_Authentification.Crypto_Front;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Crypto.Crypto;
import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Actions.AcheterActions_Controller;
import front_end_Authentification.Actions.Application_Action;
import front_end_Authentification.Actions.ConfirmerAchatController;
import front_end_Authentification.Actions.MainActionController;
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

import static front_end_Authentification.Accueil.F_Accueil_Controller.*;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class AcheterCryptos_Controller {
    protected static Portefeuille selectedWallet = getSelectedWallet();

    protected static Crypto newCrypto;
    public static float getValueSimulation;
    private static boolean simulationValid = false;
    private static String[] achatCrypto = new String[5];
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
    private Label simulationLabel;
    @FXML
    private TextField quantiteTextField;
    @FXML
    private TextField symboleTextField;
    @FXML
    private Label alertLabel;
    @FXML
    private void initialize(){
        mettreAjoursellectedWallet(getNomWallet());
        selectedWallet=getSelectedWallet();
    }
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
                getValueSimulation = stockData.getJSONObject(stockSymbol).getInt("eur");
                suggestionsCrypto += "La plus récente valeur de la Crypto-Monnaie est : " + latestPrice + "€";
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
    protected void simulerButton(){
        String stockSymbol = symboleTextField.getText();
        String apiUrlWithKey = API_URL_TimeSeriesIntraDay.replace("SEARCH_SYMBOL", stockSymbol);
        try {
            // Make API request and parse JSON response
            JSONObject stockData = getStockData(apiUrlWithKey);
            String cryptoValue = String.valueOf(stockData.getJSONObject(stockSymbol).getInt("eur"));

            if (cryptoValue == ""){
                simulationLabel.setText("Aucun résultat trouvé");
            } else {
                try{
                    float valueSimulation = parseFloat(cryptoValue) * parseFloat(quantiteTextField.getText());
                    simulationLabel.setText(Float.toString(valueSimulation));
                    achatCrypto[1] = stockSymbol;
                    achatCrypto[2] = cryptoValue;
                    achatCrypto[3] = quantiteTextField.getText();
                    achatCrypto[4] = Float.toString(valueSimulation); // A SORTIR POUR LA CONIRMATION
                    simulationValid = true;
                } catch (NumberFormatException e){
                    simulationLabel.setText("Veuillez renseignez un nombre pour le champ \"quantité\" ");
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
            ConfirmerAchatCrypto_Controller.afficherConfirmerAchatCryptos();
            Node button = (Node) e.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();


        } else{
            alertLabel.setText("Faites une simulation valide avant d'effectuer un achat.");
        }
    }
    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
        MainCryptoController.afficherMainCryptos();

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

    protected static void afficherAcheterCrypto() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Crypto/F_AcheterCrypto.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Acheter des cryptos");
        secondStage.setScene(scene);
        secondStage.show();
    }
    public static String[] getAchatCrypto() {
        return achatCrypto;
    }
    public static void setAchatCrypto(String achatCrypto) {
        AcheterCryptos_Controller.achatCrypto[0] = achatCrypto;
    }
}

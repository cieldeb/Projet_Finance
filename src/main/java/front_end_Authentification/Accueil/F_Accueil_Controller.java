package front_end_Authentification.Accueil;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Crypto.Crypto;
import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Application;
import front_end_Authentification.F_Authentification_Controller;
import front_end_Authentification.Portefeuilles.CreerPortefeuille_Controller;
import front_end_Authentification.Virement.F_gererCompte_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import static front_end_Authentification.Portefeuilles.GererPortefeuille_Controller.afficherGererPortefeuille;

public class F_Accueil_Controller {
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();

    public static Portefeuille getSelectedWallet() {
        return selectedWallet;
    }

    protected static Portefeuille selectedWallet;
    @FXML
    protected ComboBox<String> portefeuilleComboBoxAffiche;
    @FXML
    protected void portefeuilleComboBox(){
        String selectedWalletComboBox = portefeuilleComboBoxAffiche.getValue();
        if (selectedWalletComboBox != "Sélectionnez") {
            try {
                JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userObject = usersArray.getJSONObject(i);
                    if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                        JSONArray portefeuille = userObject.getJSONArray("PORTEFEUILLE");
                        for (int j = 0  ; j < portefeuille.length() ; j++){
                            if (selectedWalletComboBox.equals(portefeuille.getJSONObject(j).getString("LIBELLE"))){
                                LinkedList<Action> listActionWalletSelected = new LinkedList<>();
                                JSONArray actions = portefeuille.getJSONObject(j).getJSONArray("ACTIONS");
                                for (int k = 0 ; k<actions.length() ; k++){
                                    Action action = new Action(actions.getJSONObject(k).getString("Libellé"),actions.getJSONObject(k).getString("Symbole"),actions.getJSONObject(k).getFloat("Valeur initiale"),actions.getJSONObject(k).getFloat("Dernière valeur"),actions.getJSONObject(k).getInt("Quantité"),actions.getJSONObject(k).getFloat("Valeur totale à l'achat"),actions.getJSONObject(k).getFloat("Dernière valeur totale"));
                                    listActionWalletSelected.add(action);
                                }
                                LinkedList<Crypto> listCryptoWalletSelected = new LinkedList<>();
                                JSONArray cryptos = portefeuille.getJSONObject(j).getJSONArray("CRYPTOS");
                                for (int k = 0 ; k<cryptos.length() ; k++){
                                    Crypto crypto = new Crypto(cryptos.getJSONObject(k).getString("Libellé"),cryptos.getJSONObject(k).getString("Symbole"),cryptos.getJSONObject(k).getFloat("Valeur initiale"),cryptos.getJSONObject(k).getFloat("Dernière valeur"),cryptos.getJSONObject(k).getFloat("Quantité"),cryptos.getJSONObject(k).getFloat("Valeur totale à l'achat"),cryptos.getJSONObject(k).getFloat("Dernière valeur totale"));
                                    listCryptoWalletSelected.add(crypto);

                                }

                                selectedWallet = new Portefeuille(portefeuille.getJSONObject(j).getString("LIBELLE"),listActionWalletSelected,listCryptoWalletSelected);
                                afficherGererPortefeuille();
                                break;
                            }
                        }

                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void initialize() throws IOException{
        portefeuilleComboBoxAffiche.setVisibleRowCount(3);

        try{
            JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray portefeuilleArray = userObject.optJSONArray("PORTEFEUILLE");
                    if (portefeuilleArray != null){
                        for (int j = 0; j < portefeuilleArray.length(); j++) {
                            JSONObject libelle = portefeuilleArray.getJSONObject(j);
                            String libellePortefeuille = libelle.optString("LIBELLE");

                            portefeuilleComboBoxAffiche.getItems().add(libellePortefeuille);
                        }
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }


    }

    public static void afficher_F_Accueil() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/front_end_Authentification/F_Accueil.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();

        stage.setTitle("Accueil");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void creerPortefeuilleButton(ActionEvent e) throws IOException{
        CreerPortefeuille_Controller.afficherCreerPortefeuille();

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void btnGererComptes(ActionEvent e) throws IOException {
        F_gererCompte_Controller.afficher_F_gererCompte();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}

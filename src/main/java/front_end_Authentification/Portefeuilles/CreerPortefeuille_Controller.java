package front_end_Authentification.Portefeuilles;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Crypto.Crypto;
import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Actions.Application_Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class CreerPortefeuille_Controller {
    protected LinkedList<Action> listActionsTemporaire = new LinkedList<>();
    protected LinkedList<Crypto> listCryptoTemporaire = new LinkedList<>();
    @FXML
    private TextField libellePorteFeuilleTextField;
    @FXML
    private TextField libelleATextField;
    @FXML
    private TextField symboleATextField;
    @FXML
    private TextField valeurATextField;
    @FXML
    private TextField quantiteATextField;
    @FXML
    private TextField libelleCTextField;
    @FXML
    private TextField symboleCTextField;
    @FXML
    private TextField valeurCTextField;
    @FXML
    private TextField quantiteCTextField;

    public static void afficherCreerPortefeuille() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_PorteFeuille/F_CreerPorteFeuille.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("Créer un portefeuille");
        secondStage.setScene(scene);
        secondStage.show();
    }
    @FXML
    protected void ajouterActionButton(){
        listActionsTemporaire.add(new Action(libelleATextField.getText(), symboleATextField.getText(), parseFloat(valeurATextField.getText()), -1 , parseInt(quantiteATextField.getText()),-1,-1));

    }
    @FXML
    protected void ajouterCryptoButton(){
        listCryptoTemporaire.add(new Crypto(libelleCTextField.getText(), symboleCTextField.getText(), parseFloat(valeurCTextField.getText()), -1 , parseInt(quantiteCTextField.getText()),-1,-1));

    }

    @FXML
    protected void creerPortefeuilleButton(ActionEvent e) throws IOException{
        Portefeuille newWallet = new Portefeuille(libellePorteFeuilleTextField.getText(),listActionsTemporaire,listCryptoTemporaire);
        //Tout ajouter dans le JSON:
        newWallet.writeOnJSONnewWallet();

        front_end_Authentification.Accueil.F_Accueil_Controller.afficher_F_Accueil();
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

}

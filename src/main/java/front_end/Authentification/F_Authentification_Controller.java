package front_end.Authentification;

import com.example.projet_finance.back_end.Entite.Entite;
import front_end.Accueil.F_Accueil_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.projet_finance.back_end.Entite.Entite.current_authenticated;

public class F_Authentification_Controller {
    private int idCurrentUser;
    @FXML
    private TextField idField;
    @FXML
    private PasswordField mdpField;
    @FXML
    private Button btnValid;
    @FXML
    private void initialize(){
        btnValid.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                btnValid.setTextFill(Color.web("#12ab1f"));
            } else if (oldValue) {
                btnValid.setTextFill(Color.web("#000000"));
            }
        });
        idField.setTooltip(new Tooltip("Entrer l'identifiant"));
        mdpField.setTooltip(new Tooltip("Entrer le mot de passe"));
    }
    @FXML
    protected void btnValider(ActionEvent e) throws IOException {
        String id = idField.getText();
        String mdp = mdpField.getText();
        if (! verification(id,mdp) ){
           F_ErrAuthentification_Controller.afficherErr();
        }
        else{
            initialisation_CurrentEntite(getIdCurrentUser());
            System.out.println(current_authenticated.toString()); //on verifie que l'objet entité créée possède bien l'attribut qui sont dans le fichier listeInscrit.csv
            F_Accueil_Controller.afficher_F_Accueil();
        }
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void btnNoAccount(ActionEvent e) throws IOException {
        F_Inscription_Controller.afficherInscription();
    }
    protected boolean verification(String id, String mdp) throws FileNotFoundException {
        boolean result = false;
        Application.lectureCSV_Authentification();
        for (int i = 0; i < Application.lines.length ; i++){
            String[] line_n = Application.lines[i].split(";");
            if (line_n[0].equals(id) && line_n[3].equals(mdp)){
                result = true;
                setIdCurrentUser(i);
                System.out.println("idCurrentUser = " + i);
                break;
            }
        }
        return result;
    }
    protected void initialisation_CurrentEntite(int idCurrentUser){
        String[] lineUser = Application.lines[idCurrentUser].split(";");
        current_authenticated = new Entite(lineUser[0],lineUser[3], lineUser[2], lineUser[1]);
    }

    public int getIdCurrentUser() {
        return idCurrentUser;
    }

    public void setIdCurrentUser(int idCurrentUser) {
        this.idCurrentUser = idCurrentUser;
    }
}
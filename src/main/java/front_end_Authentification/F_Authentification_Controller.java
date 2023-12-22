package front_end_Authentification;

import com.example.projet_finance.back_end.Entite.Entite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.projet_finance.back_end.Entite.Entite.current_authentificated;
import static front_end_Authentification.Application.lines;

public class F_Authentification_Controller {
    int idCurrentUser;
    @FXML
    private TextField idField;
    @FXML
    private TextField mdpField;
    @FXML
    protected void btnValider(ActionEvent e) throws IOException {

        String id = idField.getText();
        String mdp = mdpField.getText();
        if (! verification(id,mdp) ){
        F_ErrAuthentification_Controller.afficherErr();
        }
        else{
            initialisation_CurrentEntite(idCurrentUser);
            //System.out.println(current_authentificated.toString()); //on verifie que l'objet entité créée possède bien l'attribu qui sont dans le fichier listeInscrit.csv
        }

    }
    @FXML
    protected void btnNoAcount(ActionEvent e) throws IOException {
        F_Inscription_Controller.afficherInscription();

    }

    protected boolean verification(String id, String mdp) throws FileNotFoundException {
        boolean result = false;
        Application.lectureCSV_Authentification();
        for (int i = 0 ; i < lines.length ; i++){
            String[] line_n = lines[i].split(";");
            if (line_n[0].equals(id) && line_n[3].equals(mdp)){
                result = true;
                idCurrentUser = i;
                break;
            }
        }
        return result;
    }
    protected void initialisation_CurrentEntite(int idCurrentUser){
        String[] lineUser = lines[idCurrentUser].split(";");
        current_authentificated = new Entite(lineUser[0],lineUser[3], lineUser[2], lineUser[1]);
    }
}

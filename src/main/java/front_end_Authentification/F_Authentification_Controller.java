package front_end_Authentification;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.io.IOException;

import static front_end_Authentification.Application.lines;

public class F_Authentification_Controller {
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
                break;
            }
        }
        return result;
    }
}

package front_end.Virement;

import front_end.Authentification.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class F_NewDestinataire_Controller {
    @FXML
    private TextField NPField;
    @FXML
    private TextField IBANField;
    @FXML
    private TextField BICField;
    private static final String DELIMITER = ";";
    private static final String SEPARATOR = "\n";
    private int compteAssocie = 5;

    public void start(){
    }
    public static void afficher_F_NewDestinataire() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/front_end_Virement/F_NewDestinataire.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Ajouter un destinataire");
        stage.setScene(scene);
        stage.show();
    }

    public void btnClear(ActionEvent actionEvent) {
        NPField.clear();
        IBANField.clear();
        BICField.clear();
    }
    @FXML
    public void btnNewDestOk(ActionEvent actionEvent) throws IOException {

        String NP = NPField.getText();
        String IBAN = IBANField.getText();
        String BIC = BICField.getText();
        String C_A = "salut";

        File fichier = new File("files/listeInscrits.csv");
        FileWriter file = new FileWriter(fichier,true);
        BufferedWriter bw = new BufferedWriter(file);
        bw.write(NP);
        bw.write(DELIMITER);
        bw.write(IBAN);
        bw.write(DELIMITER);
        bw.write(BIC);
        bw.write(DELIMITER);
        bw.write(SEPARATOR);
        bw.close();

        Node button = (Node) actionEvent.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
        F_Virement_Controller.afficher_F_Virement();
    }
}

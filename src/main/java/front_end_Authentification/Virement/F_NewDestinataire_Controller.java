package front_end_Authentification.Virement;

import front_end_Authentification.F_ErrAuthentification_Controller;
import front_end_Authentification.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class F_NewDestinataire_Controller {
    @FXML
    private TextField NPField;
    @FXML
    private TextField IBANField;
    @FXML
    private TextField BICField;
    private static final String DELIMITER = ";";
    private static final String SEPARATOR = "\n";
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

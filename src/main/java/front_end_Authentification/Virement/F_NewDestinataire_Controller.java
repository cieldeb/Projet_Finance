package front_end_Authentification.Virement;

import front_end_Authentification.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.io.FileReader;
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

        JSONObject newDestInfo = new JSONObject();
        newDestInfo.put("IBAN", IBANField.getText());
        newDestInfo.put("COMPTE_ASSOCIE", "salut");
        newDestInfo.put("PRENOM_NOM", NPField.getText());
        newDestInfo.put("BIC", BICField.getText());

        FileReader fileReader = null;
        FileWriter fileWriter = null;

        try {
            fileReader = new FileReader("files/destinataires.json");
            JSONTokener tokener = new JSONTokener(fileReader);
            JSONArray jsonArray = new JSONArray(tokener);

            jsonArray.put(newDestInfo);

            fileWriter = new FileWriter("files/destinataires.json");
            fileWriter.write(jsonArray.toString(4));
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /*File fichier = new File("files/listeInscrits.csv");
        FileWriter file = new FileWriter(fichier,true);
        BufferedWriter bw = new BufferedWriter(file);
        bw.write(NP);
        bw.write(DELIMITER);
        bw.write(IBAN);
        bw.write(DELIMITER);
        bw.write(BIC);
        bw.write(DELIMITER);
        bw.write(SEPARATOR);
        bw.close();*/

        Node button = (Node) actionEvent.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
        F_Virement_Controller.afficher_F_Virement();
    }
}

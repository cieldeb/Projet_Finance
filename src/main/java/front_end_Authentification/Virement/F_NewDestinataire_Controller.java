package front_end_Authentification.Virement;

import front_end_Authentification.Application;
import front_end_Authentification.F_Authentification_Controller;
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
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();
    @FXML
    private TextField NPField;
    @FXML
    private TextField IBANField;
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
    }
    @FXML
    public void btnNewDestOk(ActionEvent actionEvent) throws IOException {
        try {
            File jsonFile = new File("files/listeinscrits.json");
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
            JSONArray jsonArray = new JSONArray(jsonContent);
            boolean userFound = false;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray destArray = userObject.optJSONArray("DESTINATAIRES");
                    if (destArray == null) {
                        destArray = new JSONArray();
                        userObject.put("DESTINATAIRES", destArray);
                    }

                    JSONObject newDest = new JSONObject();
                    newDest.put("IBAN", Integer.parseInt(IBANField.getText()));
                    newDest.put("NOM", NPField.getText());
                    destArray.put(newDest);
                    userFound = true;
                }
            }
            if (userFound) {
                Files.write(Paths.get(jsonFile.getPath()), jsonArray.toString(4).getBytes());
            } else {
                System.err.println("User not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
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
    }
}

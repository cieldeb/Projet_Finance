package front_end_Authentification;

import com.example.projet_finance.back_end.Entite.Entite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class F_Inscription_Controller {
    @FXML
    private TextField nomField;
    @FXML
    private TextField telField;
    @FXML
    private TextField mailField;
    @FXML
    private TextField mdpField;
    @FXML
    private TextField mdp2Field;
    @FXML
    private Label errSetUpMdp;
    @FXML
    private Text auth_wrongchar;
    private static final String DELIMITER = ";";
    private static final String SEPARATOR = "\n";
    @FXML
    protected void btnInscription(ActionEvent e) throws IOException {
        String id = nomField.getText();
        String tel = telField.getText();
        String mail = mailField.getText();
        String mdp1 = mdpField.getText();
        String mdp2 = mdp2Field.getText();
        errSetUpMdp.setText(" ");

        if(mdp1.equals(mdp2)){ //ajouter une condition pour verifier que le compte n'existe pas deja.
            //Remplissage du fichier CSV permettant de faire l'authentification
            File fichier = new File("files/listeInscrits.csv");
            FileWriter file = new FileWriter(fichier,true);
            BufferedWriter bw = new BufferedWriter(file);
            bw.write(id);
            bw.write(DELIMITER);
            bw.write(tel);
            bw.write(DELIMITER);
            bw.write(mail);
            bw.write(DELIMITER);
            bw.write(mdp1);
            bw.write(SEPARATOR);
            bw.close();

            File jsonFile = new File("files/listeinscrits.json");
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
            JSONArray jsonArray = new JSONArray(jsonContent);

            JSONObject newUser = new JSONObject();
            newUser.put("IDENTIFIANT", id);
            newUser.put("TELEPHONE", tel);
            newUser.put("MAIL", mail);
            newUser.put("MOT DE PASSE", mdp1);
            newUser.put("PORTEFEUILLE",new JSONArray());
            JSONArray comptes = new JSONArray();
            newUser.put("COMPTES", comptes);
            newUser.put("DESTINATAIRES",new JSONArray());

            jsonArray.put(newUser);

            try (BufferedWriter jsonWriter = new BufferedWriter(new FileWriter(jsonFile))) {
                jsonWriter.write(jsonArray.toString(4));
            }

            Node button = (Node) e.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
        }
        else{
            errSetUpMdp.setText("Vous n'avez pas ré-écrit correctement votre mot de passe");
        }
    }
    public void start(){
        telField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("[a-zA-Z]")) {
                auth_wrongchar.setText("Entrez des chiffres");
            } else {
                auth_wrongchar.setText(" ");
            }
        });
    }
    @FXML
    protected void btnClear(){
        nomField.clear();
        telField.clear();
        mailField.clear();
        mdpField.clear();
        mdp2Field.clear();
    }
    protected static void afficherInscription() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("F_Inscription.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage secondStage = new Stage();

        secondStage.setTitle("S'inscrire");
        secondStage.setScene(scene);
        secondStage.show();
    }
}
package front_end_Authentification.Virement;
import front_end_Authentification.Application;

import front_end_Authentification.F_Authentification_Controller;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.*;
import java.lang.Math;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class F_Virement_Controller {
    Map<String, String[]> dataMap = new HashMap<>();
    int valNom;
    @FXML
    private ComboBox<String> vir_dest;
    @FXML
    private TextField vir_montant;
    @FXML
    private Text vir_account_montant;
    @FXML
    private ComboBox<String> vir_account;
    @FXML
    private Button btnValid;
    @FXML
    private void initialize() throws IOException, ParseException {

        F_Authentification_Controller authController = new F_Authentification_Controller();
        int currentUser = authController.getIdCurrentUser();
        System.out.println("ID d'entité récupérée: " + currentUser);

        vir_account.setTooltip(new Tooltip("Sélectionner un compte"));
        vir_dest.setTooltip(new Tooltip("Sélectionner un destinataire"));
        vir_montant.setTooltip(new Tooltip("Entrer un montant"));

        vir_account.setVisibleRowCount(3);
        vir_dest.setVisibleRowCount(3);

        btnValid.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                btnValid.setTextFill(Color.web("#12ab1f"));
            } else if (oldValue) {
                btnValid.setTextFill(Color.web("#000000"));
            }
        });
        /*try (BufferedReader br = new BufferedReader(new FileReader("files/listedestinataires.csv"))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                Map<String, String[]> dataMap = new HashMap<>();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";");
                    valNom = values.length;
                    if (values.length == 4) {
                        dataMap.put(values[0].trim(), values);
                    } else {
                        System.err.println("Skipping line: " + line);
                    }
                }
                for (Map.Entry<String, String[]> entry : dataMap.entrySet()) {
                    String[] values = entry.getValue();
                    StringBuilder displayValue = new StringBuilder(values[2] + " - ");
                    displayValue.append("IBAN: ");
                    displayValue.append(values[0].trim()).append(" ");
                    displayValue.append("BIC: ");
                    displayValue.append(values[3].trim()).append(" ");
                    vir_dest.getItems().add(displayValue.toString().trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            JSONParser destParser = new JSONParser();
            JSONArray destData = (JSONArray) destParser.parse(new FileReader("files/destinataires.json"));

            for (Object entryObj : destData) {
                if (entryObj instanceof JSONObject) {
                    JSONObject entry = (JSONObject) entryObj;
                    String c_a = (String) entry.get("COMPTE_ASSOCIE");

                    if (Objects.equals(currentUser, c_a)) {
                        String iban = (String) entry.get("IBAN");
                        String bic = (String) entry.get("BIC");
                        String np = (String) entry.get("PRENOM_NOM");

                        StringBuilder displayValue = new StringBuilder(np + " - ");
                        displayValue.append("IBAN: " + iban + " ");
                        displayValue.append("BIC: " + bic);
                        vir_dest.getItems().add(displayValue.toString().trim());
                        System.out.println(displayValue);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*try (BufferedReader br = new BufferedReader(new FileReader("files/listecomptes.csv"))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";");
                    if (values.length == 3) {
                        dataMap.put(values[0].trim(), values);
                    } else {
                        System.err.println("Skipping line: " + line);
                    }
                }
                int entree = 0;
                for (Map.Entry<String, String[]> entry : dataMap.entrySet()) {
                    entree = entree + 1;
                    String id = entry.getKey();
                    String[] values = entry.getValue();
                    StringBuilder displayValue = new StringBuilder("Compte " + entree + " - ");
                    int acc_type = Integer.parseInt(values[1]);
                    if (acc_type == 1){
                        displayValue.append("Compte Courant");
                    }else if (acc_type == 2){
                        displayValue.append("Compte Epargne");
                    }
                    vir_account.getItems().add(displayValue.toString().trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            JSONParser destParser = new JSONParser();
            JSONArray destData = (JSONArray) destParser.parse(new FileReader("files/comptes.json"));
            int entree = 0;
            for (Object entryObj : destData){
                if (entryObj instanceof JSONObject){
                    JSONObject entry = (JSONObject) entryObj;

                    String type = (String) entry.get("TYPE");
                    String solde = (String) entry.get("SOLDE");

                    int tip = Integer.parseInt(type);
                    StringBuilder displayValue = new StringBuilder("Compte " + entree + " ");
                    if (tip == 1){
                        displayValue.append("Courant");
                    }else if (tip == 2){
                        displayValue.append("Epargne");
                    }
                    vir_account.getItems().add(displayValue.toString().trim());
                }
                entree = entree + 1;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void vir_account_select() {
        String selectedAccount = vir_account.getValue();
        if (selectedAccount != null) {
            try {
                JSONParser parser = new JSONParser();
                JSONArray accountsArray = (JSONArray) parser.parse(new FileReader("files/comptes.json"));
                int accountIndex = Integer.parseInt(selectedAccount.split(" ")[1]);

                if (accountIndex < accountsArray.size()) {
                    JSONObject account = (JSONObject) accountsArray.get(accountIndex);
                    String solde = (String) account.get("SOLDE");

                    int montDispo = Integer.parseInt(solde);
                    if (montDispo > 0) {
                        vir_account_montant.setText("+" + montDispo + "€");
                        vir_account_montant.setFill(Color.web("#12ab1f"));
                    } else if (montDispo < 0) {
                        int montDispoAbs = Math.abs(montDispo);
                        vir_account_montant.setText("-" + montDispoAbs + "€");
                        vir_account_montant.setFill(Color.web("#df0000"));
                    } else {
                        vir_account_montant.setText("0€");
                        vir_account_montant.setFill(Color.web("#000000"));
                    }
                } else {
                    System.err.println("Selected account index is out of bounds.");
                }
            } catch (IOException | ParseException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void btnValider(ActionEvent e) throws IOException {
        String destinataire = vir_dest.getValue();
        String montantValue = vir_montant.getText();
        String compteDebite = vir_account.getValue();
        System.out.println(compteDebite);
    }
    public static void afficher_F_Virement() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/front_end_Virement/F_Virement.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Effectuer un virement");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void btnNewDestinataire(ActionEvent e) throws IOException {
        F_NewDestinataire_Controller.afficher_F_NewDestinataire();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void btnRetour(ActionEvent e) throws IOException{
    }
}
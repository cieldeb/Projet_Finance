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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONTokener;
import org.json.simple.*;
import java.lang.Math;

import org.json.JSONArray;
import org.json.JSONObject;


public class F_Virement_Controller {
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();
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
    private void initialize() throws IOException{

        F_Authentification_Controller authController = new F_Authentification_Controller();
        String currentUser = authController.getIdentifCurrentUser();
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
            JSONTokener destTokener = new JSONTokener(new FileReader("files/destinataires.json"));
            JSONArray destData = new JSONArray(destTokener);

            for (Object entryObj : destData) {
                if (entryObj instanceof JSONObject) {
                    JSONObject entry = (JSONObject) entryObj;
                    String c_a = entry.optString("COMPTE_ASSOCIE");

                    if (Objects.equals(currentUser, c_a)) {
                        String iban = entry.optString("IBAN");
                        String bic = entry.optString("BIC");
                        String np = entry.optString("PRENOM_NOM");

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
            File jsonFile = new File("files/listeinscrits.json");
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
            JSONArray jsonArray = new JSONArray(jsonContent);

            boolean userFound = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray comptesArray = userObject.optJSONArray("COMPTES");
                    if (comptesArray != null) {
                        for (int j = 0; j < comptesArray.length(); j++) {
                            JSONObject account = comptesArray.getJSONObject(j);
                            int type = account.optInt("TYPE");
                            System.out.println("Type : " + type);

                            StringBuilder displayValue = new StringBuilder("Compte " + j + " ");
                            if (type == 1){
                                displayValue.append(" - Courant");
                            } else if (type == 2){
                                displayValue.append(" - Epargne");
                            }
                            vir_account.getItems().add(displayValue.toString().trim());
                        }
                        userFound = true;
                    }
                    break;
                }
            }
            if (!userFound) {
                System.out.println("User not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void vir_account_select() {
        String selectedAccount = vir_account.getValue();
        if (selectedAccount != null) {
            try {
                JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userObject = usersArray.getJSONObject(i);
                    if (userObject.optString("IDENTIFIANT").equals(currentUser)) {

                        if (userObject.has("COMPTES")) {
                            JSONArray comptesArray = userObject.getJSONArray("COMPTES");
                            int accountIndex = Integer.parseInt(selectedAccount.split(" ")[1]);

                            if (accountIndex >= 0 && accountIndex < comptesArray.length()) {
                                JSONObject selectedCompte = comptesArray.getJSONObject(accountIndex);
                                String solde = selectedCompte.getString("SOLDE");
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
                                System.err.println("L'index de compte sélectionné n'existe pas pour cet utilisateur");
                            }
                        } else {
                            System.err.println("La clé 'COMPTES' n'existe pas dans l'objet JSON de l'utilisateur.");
                        }
                        break;
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No account selected"); // Debugging
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
    }
}
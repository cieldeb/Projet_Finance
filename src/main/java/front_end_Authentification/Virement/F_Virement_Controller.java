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

import org.json.JSONException;
import org.json.JSONTokener;
import org.json.simple.*;
import java.lang.Math;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Integer.sum;


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

        //Remplissage de la combobox des destinataires

        try {
            File jsonFile = new File("files/listeinscrits.json");
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
            JSONArray jsonArray = new JSONArray(jsonContent);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray destArray = userObject.optJSONArray("DESTINATAIRES");
                    JSONArray comptesArray = userObject.optJSONArray("COMPTES");

                    //Récupération des comptes de l'utilisateur

                    if (comptesArray != null){
                        for(int l = 0; l < comptesArray.length(); l++){
                            JSONObject entry = comptesArray.getJSONObject((l));
                            String iban = entry.optString("IBAN");

                            StringBuilder displayValue = new StringBuilder("Interne - ");
                            displayValue.append("IBAN: ").append(iban).append(" ");
                            vir_dest.getItems().add(displayValue.toString().trim());
                            System.out.println("Ajouté à la combobox des destinataires " + displayValue);
                        }
                    }

                    //Récupération des comptes de destinataires extérieurs

                    if (destArray != null) {
                        for (int j = 0; j < destArray.length(); j++) {
                            JSONObject entry = destArray.getJSONObject(j);
                            String iban = entry.optString("IBAN");
                            String id = entry.optString("NOM");

                            StringBuilder displayValue = new StringBuilder(id + " - ");
                            displayValue.append("IBAN: ").append(iban).append(" ");
                            vir_dest.getItems().add(displayValue.toString().trim());
                            System.out.println("Ajouté à la combobox des destinataires " + displayValue);
                        }
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

        //Remplissage de la combobox des comptes

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

                            StringBuilder displayValue = new StringBuilder("Compte ");
                            if (type == 1){
                                displayValue.append("Courant n° " + account.optInt("IBAN"));
                            } else if (type == 2){
                                displayValue.append("Epargne n° " + account.optInt("IBAN"));
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
                                int solde = selectedCompte.getInt("SOLDE");
                                if (solde > 0) {
                                    vir_account_montant.setText("+" + solde + "€");
                                    vir_account_montant.setFill(Color.web("#12ab1f"));
                                } else {
                                    vir_account_montant.setText("0€");
                                    vir_account_montant.setFill(Color.web("#00004d"));
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
            System.out.println("No account selected");
        }
    }
    @FXML
    protected void btnValider(ActionEvent e) throws IOException {

        String[] part = (vir_dest.getValue()).split(": ");
        int ibanDestinataire = Integer.parseInt(part[1]);

        String[] partDest = (vir_dest.getValue()).split("-");
        String idDestinataire = partDest[0];
        idDestinataire.replaceAll("\\s","");
        System.out.println("Identifiant du destinataire: " + idDestinataire);

        int montantValue = Integer.parseInt(vir_montant.getText());
        System.out.println("Montant de la transaction : " + montantValue);

        String compteDebite = vir_account.getValue();
        String[] parts = compteDebite.split("n° ");
        System.out.println("Compte débité: " + parts[1]);

        int emetteur = Integer.parseInt(parts[1]);
        int updatedSolde = 0;

        //Ajout de la transaction dans la partie TRANSACTIONS du récepteur dans transactions.json

        int newSoldeRecepteur = 0;
        try {
            JSONArray entryArray = new JSONArray(new JSONTokener(new FileReader("files/transactions.json")));
            for (int i = 0; i < entryArray.length(); i++) {
                JSONObject userObject = entryArray.getJSONObject(i);
                if (userObject.optInt("IBAN") == ibanDestinataire) {
                    JSONArray transacArray = userObject.has("TRANSACTIONS") ? userObject.getJSONArray("TRANSACTIONS") : new JSONArray();
                    int newID = getNextAvailableID(transacArray);

                    int indexMontantBase = transacArray.length();
                    Object montantBase = transacArray.toList().get(indexMontantBase - 1);
                    String montantBaseStr = montantBase.toString();
                    String[] part1 = montantBaseStr.split(", ");
                    String extractedAmount = "";
                    for (String part2 : part1) {
                        if (part2.startsWith("SOLDE=")) {
                            extractedAmount = part2.substring("SOLDE=".length());
                            break;
                        }
                    }

                    JSONObject newTransaction = new JSONObject();
                    newTransaction.put("ID", newID);
                    newTransaction.put("EMETTEUR", emetteur);
                    newTransaction.put("RECEPTEUR", ibanDestinataire);
                    newTransaction.put("MONTANT", montantValue);
                    System.out.println(extractedAmount);
                    System.out.println(montantValue);
                    newSoldeRecepteur = sum(Integer.parseInt(extractedAmount), montantValue);
                    newTransaction.put("SOLDE",  newSoldeRecepteur);

                    transacArray.put(newTransaction);

                    userObject.put("TRANSACTIONS", transacArray);
                    break;
                }
            }
            try (FileWriter file = new FileWriter("files/transactions.json")) {
                file.write(entryArray.toString(4));

            } catch (IOException f) {
                f.printStackTrace();
            }
        } catch (Exception j) {
            j.printStackTrace();
        }

        //Modification du solde de l'émetteur dans listeinscrits.json

        try {
            JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {

                    if (userObject.has("COMPTES")) {
                        JSONArray comptesArray = userObject.getJSONArray("COMPTES");
                        boolean containsIBAN = jsonArrayContainsKey(comptesArray, "IBAN");

                        if (containsIBAN) {
                            for (int j = 0; j < comptesArray.length(); j++) {
                                JSONObject compte = comptesArray.getJSONObject(j);
                                int ibanEnregistre = compte.optInt("IBAN");
                                if (emetteur == ibanEnregistre) {
                                    int currentSolde = compte.getInt("SOLDE");
                                    System.out.println("Solde du compte débité avant transaction: " + currentSolde);
                                    updatedSolde = currentSolde - montantValue;
                                    System.out.println("Solde du compte débité après transaction: " + updatedSolde);
                                    compte.put("SOLDE", updatedSolde);
                                    break;
                                }
                            }
                        }
                        try (FileWriter file = new FileWriter("files/listeinscrits.json")) {
                            file.write(usersArray.toString(4));
                            file.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.err.println("La clé 'COMPTES' n'existe pas dans l'objet JSON de l'utilisateur.");
                    }
                    break;
                }
            }

        } catch (IOException | NumberFormatException f) {
            f.printStackTrace();
        }

        //Ajout de la transaction dans la partie TRANSACTIONS de l'émetteur dans transactions.json

        try {
            JSONArray entryArray = new JSONArray(new JSONTokener(new FileReader("files/transactions.json")));
            for (int i = 0; i < entryArray.length(); i++) {
                JSONObject userObject = entryArray.getJSONObject(i);
                if (userObject.optInt("IBAN") == emetteur) {
                    JSONArray transacArray = userObject.has("TRANSACTIONS") ? userObject.getJSONArray("TRANSACTIONS") : new JSONArray();
                    int newID = getNextAvailableID(transacArray);

                    JSONObject newTransaction = new JSONObject();
                    newTransaction.put("ID", newID);
                    newTransaction.put("EMETTEUR", emetteur);
                    newTransaction.put("RECEPTEUR", ibanDestinataire);
                    newTransaction.put("MONTANT", montantValue);
                    newTransaction.put("SOLDE", updatedSolde);

                    transacArray.put(newTransaction);

                    userObject.put("TRANSACTIONS", transacArray);
                    break;
                }
            }
            try (FileWriter file = new FileWriter("files/transactions.json")) {
                file.write(entryArray.toString(4));
            } catch (IOException f) {
                f.printStackTrace();
            }
        } catch (Exception j) {
            j.printStackTrace();
        }

        //Modification du solde du récepteur dans listeinscrits.json

        try {
            JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                String idDest = idDestinataire.toString();
                //System.out.println("Comparing: '" + userObject.optString("IDENTIFIANT") + "' with '" + idDest + "'"); //Debug pour voir les comparaisons faites par la boucle suivante
                if (userObject.optString("IDENTIFIANT").trim().equals(idDest.trim())) {
                    if (userObject.has("COMPTES")) {
                        JSONArray comptesArray = userObject.getJSONArray("COMPTES");
                        boolean containsIBAN = jsonArrayContainsKey(comptesArray, "IBAN");

                        if (containsIBAN) {
                            for (int j = 0; j < comptesArray.length(); j++) {
                                JSONObject compte = comptesArray.getJSONObject(j);
                                int ibanEnregistre = compte.optInt("IBAN");

                                if (ibanDestinataire == ibanEnregistre) {
                                    int currentSoldeDest = compte.getInt("SOLDE");
                                    System.out.println("Solde du compte crédité avant transaction: " + currentSoldeDest);
                                    compte.put("SOLDE", newSoldeRecepteur);
                                    System.out.println("Solde du compte crédité après transaction: " + newSoldeRecepteur);
                                    break;
                                }
                            }
                        }

                        try (FileWriter file = new FileWriter("files/listeinscrits.json")) {
                            file.write(usersArray.toString(4));
                            file.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.err.println("La clé 'COMPTES' n'existe pas dans l'objet JSON de l'utilisateur.");
                    }
                    break;
                }
            }
        } catch (IOException | JSONException f) {
            f.printStackTrace();
        }

        F_gererCompte_Controller.afficher_F_gererCompte();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
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
    protected void retourButton(ActionEvent e) throws IOException {
        F_gererCompte_Controller.afficher_F_gererCompte();
        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    public boolean jsonArrayContainsKey(JSONArray jsonArray, String key) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has(key)) {
                return true;
            }
        }
        return false;
    }
    public static int getNextAvailableID(JSONArray transacArray) {
        int maxID = 0;
        for (int i = 0; i < transacArray.length(); i++) {
            JSONObject transaction = transacArray.getJSONObject(i);
            int currentID = transaction.optInt("ID");
            if (currentID > maxID) {
                maxID = currentID;
            }
        }
        return maxID + 1;
    }
}
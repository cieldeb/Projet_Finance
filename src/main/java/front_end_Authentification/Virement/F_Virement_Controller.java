package front_end_Authentification.Virement;

import front_end_Authentification.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class F_Virement_Controller {
    @FXML
    private ComboBox<String> vir_dest;
    @FXML
    private TextField vir_montant;
    @FXML
    private Text montant;
    @FXML
    private ComboBox<String> vir_account;
    Map<String, String[]> dataMap = new HashMap<>();
    @FXML
    private void initialize(){
        vir_account.setTooltip(new Tooltip("Sélectionner un compte"));
        vir_dest.setTooltip(new Tooltip("Sélectionner un destinataire"));
        vir_montant.setTooltip(new Tooltip("Entrer un montant"));

        vir_account.setVisibleRowCount(3);
        vir_dest.setVisibleRowCount(3);

        try (BufferedReader br = new BufferedReader(new FileReader("files/listedestinataires.csv"))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                Map<String, String[]> dataMap = new HashMap<>();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";");
                    if (values.length == 3) {
                        dataMap.put(values[0].trim(), values);
                    } else {
                        System.err.println("Skipping line: " + line);
                    }
                }
                for (Map.Entry<String, String[]> entry : dataMap.entrySet()) {
                    String id = entry.getKey();
                    String[] values = entry.getValue();
                    StringBuilder displayValue = new StringBuilder(id + " - ");
                    displayValue.append("IBAN: ");
                    displayValue.append(values[1].trim()).append(" ");
                    displayValue.append("BIC: ");
                    displayValue.append(values[2].trim()).append(" ");
                    vir_dest.getItems().add(displayValue.toString().trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader("files/listecomptes.csv"))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";");
                    if (values.length == 4) {
                        dataMap.put(values[0].trim(), values);
                    } else {
                        System.err.println("Skipping line: " + line);
                    }
                }
                for (Map.Entry<String, String[]> entry : dataMap.entrySet()) {
                    String id = entry.getKey();
                    String[] values = entry.getValue();
                    StringBuilder displayValue = new StringBuilder("Compte" + id + " - ");
                    int acc_type = Integer.parseInt(values[3]);
                    if (acc_type == 1){
                        displayValue.append("Compte Courant");
                    }else{
                        displayValue.append("Compte Epargne");
                    }
                    vir_account.getItems().add(displayValue.toString().trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    protected void vir_account_select(ActionEvent e) throws IOException{

    }
    @FXML
    protected void btnValider(ActionEvent e) throws IOException {
        String destinataire = vir_dest.getValue();
        String montantValue = vir_montant.getText();
        String compteDebite = vir_account.getValue();
        System.out.println(compteDebite);
        /*if (vir_montant > vir_account.toString()){
            prompt "erreur : Montant inscrit supérieur au montant disponible"
        }else{

        }*/
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
}
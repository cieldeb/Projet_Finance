package front_end_Authentification.Virement;

import front_end_Authentification.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
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
    private List<String> destinataires = new ArrayList<>();
    private final String ADD_NEW_RECEIVER = "Ajouter un nouveau destinataire";

    public F_Virement_Controller() {
    }
    @FXML
    private void initialize(){
        vir_account.setTooltip(new Tooltip("Sélectionner un compte"));
        vir_dest.setTooltip(new Tooltip("Sélectionner un destinataire"));
        vir_montant.setTooltip(new Tooltip("Entrer un montant"));

        vir_account.setVisibleRowCount(3);
        vir_dest.setVisibleRowCount(3);

        loadCSVIntoComboBox(vir_dest, "files/listedestinataires.csv");
        vir_dest.getItems().add(ADD_NEW_RECEIVER); // Add this line after loading the CSV

        loadCSVIntoComboBox(vir_account, "files/listecomptes.csv");

        try (Scanner scanner = new Scanner(new File("listedestinataires.csv"))) {
            while (scanner.hasNextLine()) {
                destinataires.addAll(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        vir_account.setItems(FXCollections.observableArrayList(destinataires));
    }

    private void start(Stage virement) {
        vir_dest.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (ADD_NEW_RECEIVER.equals(newValue)) {
                try {
                    newDestinataire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                vir_dest.getSelectionModel().clearSelection();
            }
        });
    }

    private void loadCSVIntoComboBox(ComboBox<String> comboBox, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\n");
                for (String value : values) {
                    comboBox.getItems().add(value.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newDestinataire() {
        try {
            F_NewDestinataire_Controller.afficher_F_NewDestinataire();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(";");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
    @FXML
    protected void btnValider(ActionEvent e) throws IOException {
        String destinataire = vir_dest.getValue();
        String montantValue = vir_montant.getText();
        String compteDebite = vir_account.getValue();
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
}
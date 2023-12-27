package front_end_Authentification;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class F_Virement_Controller {
    @FXML
    private ChoiceBox<String> vir_dest;
    @FXML
    private TextField vir_montant;
    @FXML
    private Text montant;
    @FXML
    private ChoiceBox<String> vir_account;
    private List<String> destinataires = new ArrayList<>();
    private final String ADD_NEW_RECEIVER = "Ajouter un nouveau destinataire";

    public F_Virement_Controller() {
        vir_account.setTooltip(new Tooltip("Sélectionner un compte"));
        vir_dest.setTooltip(new Tooltip("Sélectionner un destinataire"));
    }

    private void start(Stage virement) {
        loadCSVIntoChoiceBox(vir_dest, "files/listedestinataires.csv");
        loadCSVIntoChoiceBox(vir_account, "files/listecomptes.csv");

        vir_dest.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (ADD_NEW_RECEIVER.equals(newValue)) {
                promptForNewReceiver(vir_dest);
            }
        });
    }

    private void loadCSVIntoChoiceBox(ChoiceBox<String> choiceBox, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                for (String value : values) {
                    choiceBox.getItems().add(value.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void promptForNewReceiver(ChoiceBox<String> choiceBox) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un destinataire");
        dialog.setHeaderText("Ajouter un nouveau destinataire");
        dialog.setContentText("Entrez le nom du destinataire");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(receiverName -> {
            choiceBox.getItems().add(choiceBox.getItems().size() - 1, receiverName);
            choiceBox.getSelectionModel().select(receiverName);
        });
    }

    @FXML
    private void initialize() {
        try (Scanner scanner = new Scanner(new File("listedestinataires.csv"))) {
            while (scanner.hasNextLine()) {
                destinataires.addAll(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        vir_account.setItems(FXCollections.observableArrayList(destinataires));
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

    protected static void afficherVirement() throws IOException{
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
}
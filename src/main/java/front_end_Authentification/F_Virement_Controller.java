package front_end_Authentification;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class F_Virement_Controller {
    @FXML
    private TextField vir_nomDest;
    @FXML
    private TextField vir_montant;
    @FXML
    private Text montant;
    @FXML
    private ChoiceBox<String> vir_account;
    private List<String> destinataires = new ArrayList<>();

    public F_Virement_Controller() {
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

    @FXML
    protected void btnValider(ActionEvent e) throws IOException {
        String destinataire = vir_nomDest.getText();
        String montantValue = vir_montant.getText();

        // Other logic for button validation
    }
}

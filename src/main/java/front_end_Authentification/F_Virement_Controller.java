package front_end_Authentification;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class F_Virement_Controller {
    @FXML
    private MenuButton vir_account;
    @FXML
    private TextField vir_nomDest;
    @FXML
    private TextField vir_montant;
    @FXML
    private Text montant;
}

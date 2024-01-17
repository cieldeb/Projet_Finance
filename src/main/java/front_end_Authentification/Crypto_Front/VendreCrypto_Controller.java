package front_end_Authentification.Crypto_Front;

import com.example.projet_finance.back_end.Entite.Portefeuille;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Map;

import static front_end_Authentification.Accueil.F_Accueil_Controller.getSelectedWallet;

public class VendreCrypto_Controller {
    protected static Portefeuille selectedWallet = getSelectedWallet();
    @FXML
    private TableColumn nomTableColumn;
    @FXML
    private TableColumn symboleTableColumn;
    @FXML
    private TableColumn quantiteTableColumn;
    @FXML
    private TableColumn valeurInitialeTableColumn;
    @FXML
    private TableColumn derniereValeurTableColumn;
    @FXML
    private TableColumn valeurTTLInitialeTableColumn;
    @FXML
    private TableColumn derniereValeurTTLTableColumn;
    @FXML
    TableView<Map<String, Object>> cryptosTableView;
}

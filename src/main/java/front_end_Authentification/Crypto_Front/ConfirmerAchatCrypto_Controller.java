package front_end_Authentification.Crypto_Front;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Crypto.Crypto;
import com.example.projet_finance.back_end.Crypto.TransactionCrypto;
import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Actions.AcheterActions_Controller;
import front_end_Authentification.Actions.Application_Action;
import front_end_Authentification.Actions.ConfirmerAchatController;
import front_end_Authentification.F_Authentification_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.example.projet_finance.back_end.Crypto.Block.getTailleBlock;
import static front_end_Authentification.Accueil.F_Accueil_Controller.*;
import static front_end_Authentification.Crypto_Front.AcheterCryptos_Controller.*;
import static front_end_Authentification.Virement.F_Virement_Controller.getNextAvailableID;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.sum;

public class ConfirmerAchatCrypto_Controller {
    protected static Portefeuille selectedWallet = getSelectedWallet();
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();

    protected static String[] achatCrypto = getAchatCrypto();

    @FXML
    private Label recapLabel;
    @FXML
    private TextField libelleTextField;
    @FXML
    private ChoiceBox compteChoiceBox;

    @FXML
    private void initialize(){
        mettreAjoursellectedWallet(getNomWallet());
        selectedWallet=getSelectedWallet();
        recapLabel.setText("Vous vous apprêtez à effectuer l'achat de " + achatCrypto[3] +" "+ achatCrypto[1] + ". La valeur d'un coin étant : " + achatCrypto[2] + "euros, vous allez payer : " + achatCrypto[4] + "euros. Donnez un libellé à votre ensemble de crypto que vous vous apprêtez à acheter en complétant le champ suivant. Cliquez sur Confirmer pour finaliser l'achat, sinon sur retour.");
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
                                displayValue.append(" - Courant - n° " + account.optInt("IBAN"));
                            } else if (type == 2){
                                displayValue.append(" - Epargne - n° " + account.optInt("IBAN"));
                            }
                            compteChoiceBox.getItems().add(displayValue.toString().trim());
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
    protected void confirmerButton(ActionEvent e) throws IOException{
        setAchatCrypto(libelleTextField.getText());

        Crypto newCrypto = new Crypto(achatCrypto[0],achatCrypto[1],parseFloat(achatCrypto[2]),parseFloat(achatCrypto[2]),parseFloat(achatCrypto[3]),parseFloat(achatCrypto[4]),parseFloat(achatCrypto[4]));

        String compteDebite = (String) compteChoiceBox.getValue();
        String[] parts = compteDebite.split("n° ");
        int ibanDebite = parseInt(parts[1]);

        int prix = Math.round(parseFloat(achatCrypto[4]));

        TransactionCrypto transaction = new TransactionCrypto(selectedWallet,ibanDebite,newCrypto,prix,-1);

        try{
            JSONArray currentBlock = new JSONArray(new JSONTokener(new FileReader("files/currentBlock.json")));
            if (currentBlock.length() < getTailleBlock()){
                // transactionJSON = new JSONArray();
                JSONObject crypto = newCrypto.createJSONObject_Crypto();
                JSONObject transac = new JSONObject();
                transac.put("DATE",transaction.getDate());
                transac.put("PORTEFEUILLE",transaction.getPortefeuille().getName());
                transac.put("IBAN",transaction.getIban());
                transac.put("CRYPTO", crypto);
                transac.put("MONTANT",transaction.getValeur());
                transac.put("TYPE",transaction.getTypeTransaction());
                currentBlock.put(transac);
                //currentBlock.put(transactionJSON);

            } if (currentBlock.length() == getTailleBlock()) {


                for (int i = 0 ; i<getTailleBlock(); i++){
                    JSONObject cryptoTransactionJSON = currentBlock.getJSONObject(i).getJSONObject("CRYPTO");
                    Crypto cryptoTransaction = new Crypto(cryptoTransactionJSON.getString("Libellé"),cryptoTransactionJSON.getString("Symbole"),cryptoTransactionJSON.getFloat("Valeur initiale"),cryptoTransactionJSON.getFloat("Dernière valeur"),cryptoTransactionJSON.getFloat("Quantité"),cryptoTransactionJSON.getFloat("Valeur totale à l'achat"),cryptoTransactionJSON.getFloat("Dernière valeur totale"));
                    TransactionCrypto transactionToDo = new TransactionCrypto(selectedWallet,currentBlock.getJSONObject(i).getInt("IBAN"),cryptoTransaction,Math.round(cryptoTransaction.getValue()*cryptoTransaction.getQuantite()),transaction.getTypeTransaction());
                    transactionToDo.realiserTransactions(ibanDebite,Math.round(cryptoTransaction.getValue()*cryptoTransaction.getQuantite()));

                }
                try{
                    JSONArray blockChain = new JSONArray(new JSONTokener(new FileReader("files/blockChain.json")));
                    blockChain.put(currentBlock);
                    try (FileWriter file = new FileWriter("files/blockChain.json")) {
                        file.write(blockChain.toString(4));
                        file.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    /*for (int i = 0 ; i<getTailleBlock() ; i++){
                        currentBlock.remove(i);

                    }*/
                    currentBlock = new JSONArray();
                } catch (Exception j) {
                    j.printStackTrace();
                }

            }

            try (FileWriter file = new FileWriter("files/currentBlock.json")) {
                file.write(currentBlock.toString(4));
                file.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }  catch (IOException w) {
            w.printStackTrace();
        }

        AcheterCryptos_Controller.afficherAcheterCrypto();

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();

    }

    @FXML
    protected void retourButton(ActionEvent e) throws IOException {
        AcheterCryptos_Controller.afficherAcheterCrypto();

        Node button = (Node) e.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
    protected static void afficherConfirmerAchatCryptos() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Crypto/F_ConfirmerAchatCryptos.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage secondStage = new Stage();

        secondStage.setTitle("Confirmation d'achat de crypto-monnaie");
        secondStage.setScene(scene);
        secondStage.show();
    }
}

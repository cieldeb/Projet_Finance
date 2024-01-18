package com.example.projet_finance.back_end.Crypto;

import com.example.projet_finance.back_end.Banque.Compte;
import com.example.projet_finance.back_end.Entite.Portefeuille;
import front_end_Authentification.Crypto_Front.AcheterCryptos_Controller;
import front_end_Authentification.F_Authentification_Controller;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static front_end_Authentification.Accueil.F_Accueil_Controller.getSelectedWallet;
import static front_end_Authentification.Virement.F_Virement_Controller.getNextAvailableID;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.sum;

public class TransactionCrypto {
    protected static Portefeuille selectedWallet = getSelectedWallet();
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();

    protected String date;
    protected Portefeuille portefeuille;
    protected int iban;
    protected Crypto crypto;
    protected int valeur;

    protected int typeTransaction;

    public String getDate() {
        return date;
    }

    public Portefeuille getPortefeuille() {
        return portefeuille;
    }

    public int getIban() {
        return iban;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public int getValeur() {
        return valeur;
    }
    public int getTypeTransaction() {
        return typeTransaction;
    }

    public TransactionCrypto(Portefeuille portefeuille, int iban, Crypto crypto ,int valeur, int typeTransaction) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyy hh:mm:ss a");
        Date date = new Date();
        this.date = format.format(date);
        this.portefeuille = portefeuille;
        this.iban = iban;
        this.valeur = valeur;
        this.crypto = crypto;
        this.typeTransaction = typeTransaction;
    }

    public void realiserTransactions(int ibanDebite , int prix){
        //String[] parts = ibanCompteDebite.split("n° ");
        //int ibanDebite = Integer.parseInt(parts[1]);

        //Ajout de la transaction dans la partie TRANSACTIONS de l'émetteur dans transactions.json

        try {
            JSONArray entryArray = new JSONArray(new JSONTokener(new FileReader("files/transactions.json")));
            for (int i = 0; i < entryArray.length(); i++) {
                JSONObject userObject = entryArray.getJSONObject(i);
                if (userObject.optInt("IBAN") == ibanDebite) {
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
                    newTransaction.put("EMETTEUR", ibanDebite);
                    newTransaction.put("RECEPTEUR", 12345);
                    newTransaction.put("MONTANT", prix);
                    int newSoldeRecepteur = sum(Integer.parseInt(extractedAmount), prix);
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

        //Modification du solde de l'émetteur dans listeinscrits.json et écriture sur le fichier JSON des cryptos achetées

        try {
            JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray comptesArray = userObject.getJSONArray("COMPTES");
                    JSONArray portefeuilleArray = userObject.getJSONArray("PORTEFEUILLE");
                    for (int j = 0; j < comptesArray.length(); j++) {
                        JSONObject compte = comptesArray.getJSONObject(j);
                        if (this.getIban() == compte.optInt("IBAN")) {
                            int currentSolde = compte.getInt("SOLDE");
                            compte.put("SOLDE", currentSolde - Math.round(this.getValeur()));
                            break;
                        }
                    }

                    for(int j = 0; j < portefeuilleArray.length(); j++) {
                        JSONObject portefeuille = portefeuilleArray.getJSONObject(j);
                        if (portefeuille.getString("LIBELLE").equals(selectedWallet.getName())){
                            JSONArray listCrypto = portefeuille.getJSONArray("CRYPTOS");
                            listCrypto.put(this.getCrypto().createJSONObject_Crypto());
                            portefeuille.put("CRYPTOS",listCrypto);
                            portefeuilleArray.put(j,portefeuille);
                        }
                    }

                    try (FileWriter file = new FileWriter("files/listeinscrits.json")) {
                        file.write(usersArray.toString(4));
                        file.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }

        } catch (IOException | NumberFormatException f) {
            f.printStackTrace();
        }
    }
}

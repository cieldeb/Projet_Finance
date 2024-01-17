package com.example.projet_finance.back_end.Crypto;

import front_end_Authentification.F_Authentification_Controller;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Crypto {

    protected String name;
    protected String symbol;
    protected float initialValue;
    protected float value;
    protected float quantite;
    protected float valeurTotale;
    protected float actuelleValeurTotale;


    public Crypto(String name,String symbol,float initialValue,float value,float quantite,float valeurTotale,float actuelleValeurTtl) {
        this.name = name;
        this.symbol = symbol;
        this.initialValue = initialValue;
        this.value = value;
        this.quantite = quantite;
        this.valeurTotale = valeurTotale;
        this.actuelleValeurTotale = actuelleValeurTtl;
    }

    public JSONObject createJSONObject_Crypto(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Libellé" , this.name);
        jsonObject.put("Symbole" , this.symbol);
        jsonObject.put("Valeur initiale" , this.initialValue);
        jsonObject.put("Dernière valeur" , this.value);
        jsonObject.put("Quantité", this.quantite);
        jsonObject.put("Valeur totale à l'achat" , this.valeurTotale );
        jsonObject.put("Dernière valeur totale" , this.actuelleValeurTotale);
        return jsonObject ;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public float getInitialValue() {
        return initialValue;
    }

    public float getValue() {
        return value;
    }

    public float getQuantite() {
        return quantite;
    }

    public float getValeurTotale() {
        return valeurTotale;
    }

    public float getActuelleValeurTotale() {
        return actuelleValeurTotale;
    }
    public static void vendreCryptoJSON(String libelleCrypto, int ibanCompte, int valeurTransaction, String currentWallet){
        F_Authentification_Controller authController = new F_Authentification_Controller();
        String currentUser = authController.getIdentifCurrentUser();
        try {
            JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    //Suppression de la crypto.
                    JSONArray portefeuilleArray = userObject.getJSONArray("PORTEFEUILLE");
                    for (int k = 0 ; k<portefeuilleArray.length();k++){
                        if (currentWallet.equals(portefeuilleArray.getJSONObject(k).getString("LIBELLE"))){
                            JSONObject portefeuille = portefeuilleArray.getJSONObject(k);
                            JSONArray cryptoArray = portefeuille.getJSONArray("CRYPTOS");
                            for (int j = 0 ; j<cryptoArray.length() ; j++){
                                JSONObject crypto = cryptoArray.getJSONObject(j);
                                if (crypto.getString("Libellé").equals(libelleCrypto)){
                                    cryptoArray.remove(j);
                                }
                            }
                            portefeuille.put("CRYPTOS",cryptoArray);
                            portefeuilleArray.put(k,portefeuille);


                        }
                    }

                    //Mise à jour du solde du compte selectionné.
                    JSONArray compteArray = userObject.getJSONArray("COMPTES");
                    for (int j = 0 ; j<compteArray.length() ; j++){
                        JSONObject compte = compteArray.getJSONObject(j);
                        if (compte.getInt("IBAN") == ibanCompte){
                            int solde = compte.getInt("SOLDE");
                            compte.put("SOLDE", solde-valeurTransaction);
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


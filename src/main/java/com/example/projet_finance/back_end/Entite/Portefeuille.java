package com.example.projet_finance.back_end.Entite;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Banque.Compte;
import com.example.projet_finance.back_end.Crypto.Crypto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import front_end_Authentification.F_Authentification_Controller;

import static java.lang.Integer.sum;

public class Portefeuille {

    protected String name;
    protected LinkedList<Compte> listeComptes;

    protected LinkedList<Action> listActions;
    protected LinkedList<Crypto> listCrypto;
    F_Authentification_Controller authController = new F_Authentification_Controller();
    String currentUser = authController.getIdentifCurrentUser();

    //Constructeur:
    public Portefeuille(String name, LinkedList<Action> listActions, LinkedList<Crypto> listCryptos) {
        this.name = name;
        this.listActions = listActions;
        this.listCrypto = listCryptos;
    }

    public void writeOnJSONnewWallet(){
        //Création du JSON contenant les informations d'un portefeuille
        JSONObject newPortefeuilleJson = new JSONObject();
        newPortefeuilleJson.put("LIBELLE" , this.name);

        //newPortefeuilleJson.put("ACTIONS" , new JSONArray());
        //newPortefeuilleJson.put("CRYPTOS" , new JSONArray());

        try {
            JSONArray usersArray = new JSONArray(new JSONTokener(new FileReader("files/listeinscrits.json")));

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                if (userObject.optString("IDENTIFIANT").equals(currentUser)) {
                    JSONArray portefeuilleArray = userObject.getJSONArray("PORTEFEUILLE");

                    if ( !walletLabelTest(portefeuilleArray, "LIBELLE" , this.name) ) {
                        JSONArray actionObjet = new JSONArray();
                        JSONArray cryptoObjet = new JSONArray();
                        for (int k = 0 ; k<this.listCrypto.size() ; k++ ){
                            cryptoObjet.put(this.listCrypto.get(k).createJSONObject_Crypto());
                        }
                        for (int k = 0 ; k<this.listActions.size() ; k++ ){
                            actionObjet.put(this.listActions.get(k).createJSONObject_Action());
                        }
                        newPortefeuilleJson.put("CRYPTOS",cryptoObjet);
                        newPortefeuilleJson.put("ACTIONS",actionObjet);
                        portefeuilleArray.put(newPortefeuilleJson); // Ajout du nouveau portefeuille parmis les autres.
                        try (FileWriter file = new FileWriter("files/listeinscrits.json")) {
                            file.write(usersArray.toString(4));
                            file.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    } else {
                        System.out.println("Portefeuille ayant le même nom deja crée, changez le nom.");
                    }



                }
            }

        } catch (IOException | NumberFormatException f) {
            f.printStackTrace();
        }
    }
    public boolean walletLabelTest(JSONArray jsonArray, String key, String label) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString(key) == label) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }
    public LinkedList<Action> getListActions() {
        return listActions;
    }
}

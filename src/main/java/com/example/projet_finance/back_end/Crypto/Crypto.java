package com.example.projet_finance.back_end.Crypto;

import org.json.JSONObject;

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
}


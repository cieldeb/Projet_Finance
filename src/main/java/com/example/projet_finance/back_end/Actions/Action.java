package com.example.projet_finance.back_end.Actions;

import org.json.JSONObject;

public class Action {
    protected String name;
    protected String symbol;
    protected float initialValue;
    protected float value;
    protected int quantite;
    protected float valeurTotale;
    protected float actuelleValeurTotale;


    public Action(String name,String symbol,float initialValue,float value,int quantite,float valeurTotale,float actuelleValeurTtl) {
        this.name = name;
        this.symbol = symbol;
        this.initialValue = initialValue;
        this.value = value;
        this.quantite = quantite;
        this.valeurTotale = valeurTotale;
        this.actuelleValeurTotale = actuelleValeurTtl;
    }
    public JSONObject createJSONObject_Action(){
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
}


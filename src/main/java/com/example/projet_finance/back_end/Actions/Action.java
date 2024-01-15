package com.example.projet_finance.back_end.Actions;

public class Action {
    protected String name;
    protected String symbol;
    protected float initalValue;
    protected float value;
    protected int quantite;
    protected float valeurTotale;
    protected float actuelleValeurTotale;


    public Action(String name,String symbol,float initialValue,float value,int quantite,float valeurTotale,float actuelleValeurTtl) {
        this.name = name;
        this.symbol = symbol;
        this.initalValue = initialValue;
        this.value = value;
        this.quantite = quantite;
        this.valeurTotale = valeurTotale;
        this.actuelleValeurTotale = actuelleValeurTtl;
    }
}


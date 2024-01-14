package com.example.projet_finance.back_end.Actions;

public class Action {
    protected String name;
    protected String symbol;
    protected float value;
    protected int quantite;
    protected float valeurTotale;


    public Action(String name,String symbol,float value,int quantite,float valeurTotale) {
        this.name = name;
        this.symbol = symbol;
        this.value = value;
        this.quantite = quantite;
        this.valeurTotale = valeurTotale;
    }
}


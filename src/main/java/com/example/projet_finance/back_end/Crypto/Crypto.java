package com.example.projet_finance.back_end.Crypto;

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
}


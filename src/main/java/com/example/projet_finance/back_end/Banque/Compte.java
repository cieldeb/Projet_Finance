package com.example.projet_finance.back_end.Banque;

public class Compte {
    protected int iban;
    protected int solde;
    protected int type;


    public Compte(int iban, int solde, int type) {
        this.iban = iban;
        this.solde = solde;
        this.type = type;
    }
}

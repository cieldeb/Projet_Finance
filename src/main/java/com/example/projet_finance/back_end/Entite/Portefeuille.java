package com.example.projet_finance.back_end.Entite;

import com.example.projet_finance.back_end.Actions.Action;
import com.example.projet_finance.back_end.Banque.Compte;
import com.example.projet_finance.back_end.Crypto.Crypto;

import java.util.LinkedList;

public class Portefeuille {
    protected String name;
    protected LinkedList<Compte> listeComptes;
    protected LinkedList<Action> listActions;
    protected LinkedList<Crypto> listCrypto;

    //Construteur:
    public Portefeuille(String name, LinkedList<Action> listActions, LinkedList<Crypto> listCrypto) {
        this.name = name;
        this.listActions = listActions;
        this.listCrypto = listCrypto;
    }
}

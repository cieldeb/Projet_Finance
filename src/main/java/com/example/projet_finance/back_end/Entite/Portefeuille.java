package com.example.projet_finance.back_end.Entite;

import com.example.projet_finance.back_end.Action;
import com.example.projet_finance.back_end.Banque.CompteCourant;

abstract class Portefeuille {
    protected Action[] actions;
    protected  userBanque;
    protected List<CompteEpargne> userCompteEpargne;
    protected List<CompteCourant> useCompteCourant;
    protected List<Action> userAction;
}
protected void newPortefeuille(Action[] ; Banque[] ; CompteCourant[] ; CompteEpargne[]);

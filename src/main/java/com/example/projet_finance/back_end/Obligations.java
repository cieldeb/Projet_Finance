package com.example.projet_finance.back_end;

import java.util.ArrayDeque;

public class Obligations {
    protected String nextDateEcheance;
    protected float montant;
    protected ArrayDeque<String> recurrence;
    protected String nom;
}
protected void newObligation (String, float, ArrayDeque<String>);
protected String getNextDate (ArrayDeque<String>);



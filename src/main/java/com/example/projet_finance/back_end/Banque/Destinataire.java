package com.example.projet_finance.back_end.Banque;

public class Destinataire {
    protected String IBAN;
    protected String BIC;
    protected String nomDest;
    public String getIBAN(){
        return IBAN;
    }
    public void setIBAN(String newIBAN){
        this.IBAN = newIBAN;
    }
    public String getBIC(){
        return BIC;
    }
    public void setBIC(String newBIC){
        this.BIC = newBIC;
    }
    public String getNomDest(){
        return nomDest;
    }
    public void setNomDest(String newNomDest){
        this.nomDest = newNomDest;
    }
}

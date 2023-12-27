package com.example.projet_finance.back_end.Entite;

import java.util.LinkedList;

public class Entite {
    static public Entite current_authentificated;
    private String userName;
    private String passWord;
    private String adressMail;
    private String tel;

    public Entite(String userName , String passWord , String adressMail , String tel){
        this.userName = userName;
        this.passWord = passWord;
        this.adressMail = adressMail;
        this.tel = tel;
    }

    public String getUserName() {
        return this.userName;
    }

    //Pas utile en soit
    @Override
    public String toString() {
        return "Entite{" +
                "userName='" + userName + '\''+
                ", adressMail='" + adressMail + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}



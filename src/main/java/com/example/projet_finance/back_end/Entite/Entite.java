package com.example.projet_finance.back_end.Entite;

import java.util.LinkedList;

public class Entite {
    static public LinkedList<Entite> entites = new LinkedList<>();
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
}

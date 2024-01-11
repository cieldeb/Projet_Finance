package com.example.projet_finance.back_end.Entite;

public class Entite {
    static public Entite current_authenticated;
    private String userName;
    private String passWord;
    private String adresseMail;
    private String tel;

    public Entite(String userName , String passWord , String adressMail , String tel){
        this.userName = userName;
        this.passWord = passWord;
        this.adresseMail = adressMail;
        this.tel = tel;
    }

    public String getUserName() {
        return this.userName;
    }

    //Pas utile en soi
    @Override
    public String toString() {
        return "Entite{" +
                "userName='" + userName + '\''+
                ", adresseMail='" + adresseMail + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}



package com.example.projet_finance.back_end.Crypto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;

public class Block {
    public static int getTailleBlock() {
        return tailleBlock;
    }

    private static int tailleBlock = 2;
    protected String date;
    protected ArrayDeque<TransactionCrypto> fileTransaction;
    protected boolean valide;

    public Block(ArrayDeque<TransactionCrypto> fileTransaction) {
        this.fileTransaction = fileTransaction;
        this.valide = false;
    }

    public void addTransaction(TransactionCrypto newTransaction){
        this.fileTransaction.add(newTransaction);
        if (this.fileTransaction.size() == tailleBlock ){
            this.valide = true;
            DateFormat format = new SimpleDateFormat("dd-MM-yyy hh:mm:ss a");
            Date date = new Date();
            this.date = format.format(date);

        }
    }
}

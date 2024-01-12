package com.example.projet_finance.back_end.Crypto;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import javafx.application.Application;
import javafx.stage.Stage;

public class Application_crypto extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Config cfg = Config.builder()
                .key("P5LEJHFFCZKVAI88")
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);


    }
}

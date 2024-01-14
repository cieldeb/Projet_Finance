module com.example.projet_finance {
    requires javafx.controls;
    requires javafx.fxml;
    requires alphavantage.java;
    requires org.json;
    requires json.simple;

    opens front_end_Authentification to javafx.fxml;
    exports front_end_Authentification;

    opens front_end_Authentification.Accueil to javafx.fxml;
    exports front_end_Authentification.Accueil to javafx.fxml;

    opens front_end_Authentification.Virement to javafx.fxml;
    exports front_end_Authentification.Virement;

    opens front_end_Authentification.Actions to javafx.fxml;
    exports front_end_Authentification.Actions;


}
module com.example.projet_finance {
    requires javafx.controls;
    requires javafx.fxml;
    requires alphavantage.java;
    requires org.json;


    opens front_end to javafx.fxml;
    exports front_end;
    opens front_end.Accueil to javafx.fxml;
    exports front_end.Accueil to javafx.fxml;
    exports front_end.Virement;
    opens front_end.Virement to javafx.fxml;
    exports front_end.Authentification;
    opens front_end.Authentification to javafx.fxml;
}
module com.example.projet_finance {
    requires javafx.controls;
    requires javafx.fxml;


    opens front_end_Authentification to javafx.fxml;
    exports front_end_Authentification;
}
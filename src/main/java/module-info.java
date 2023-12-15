module com.example.projet_finance {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projet_finance to javafx.fxml;
    exports com.example.projet_finance;
}
package front_end.Actions_F;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MainActionController {
    private static String API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=SEARCH_SYMBOL&interval=1min&apikey=P5LEJHFFCZKVAI88" ;
    @FXML
    private TextField searchField;
}

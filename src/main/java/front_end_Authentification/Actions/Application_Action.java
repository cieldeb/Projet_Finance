package front_end_Authentification.Actions;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application_Action extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Config cfg = Config.builder()
                .key("P5LEJHFFCZKVAI88")
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);

        FXMLLoader fxmlLoader = new FXMLLoader(Application_Action.class.getResource("/front_end_Actions/F_mainActions.fxml"));
        Scene Scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Actions");
        primaryStage.setScene(Scene);
        primaryStage.show();

    }
}

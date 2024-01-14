package front_end_Authentification;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("F_Authentification.fxml"));
        Scene firstScene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Se connecter");
        primaryStage.setScene(firstScene);
        primaryStage.show();

    }

    static String[] lines; //Variable permettant de parcourir le fichier CSV avec tous les comptes "Entité".

    public static void lectureCSV_Authentification() throws FileNotFoundException {
        //Lecture du contenu du fichier csv
        Scanner scanner = new Scanner(new File("files/listeInscrits.csv"));
        //Stockage du contenu du fichier CSV
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()){
            stringBuilder.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        stringBuilder.delete(0, 40);
        String data = stringBuilder.toString();
        lines = data.split("\n");
    }

}

package com.example.projet_finance.back_end;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Stack;

public class SceneManager {
    private final Stack<Scene> sceneHistory = new Stack<>();
    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void switchScene(Scene newScene) {
        if (!sceneHistory.isEmpty()) {
            sceneHistory.push(stage.getScene());
        }
        stage.setScene(newScene);
    }

    public void goBack() {
        if (!sceneHistory.isEmpty()) {
            stage.setScene(sceneHistory.pop());
        }
        // Handle case where no previous scene exists, if needed
    }
}

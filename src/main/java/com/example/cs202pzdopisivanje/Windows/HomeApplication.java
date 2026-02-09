package com.example.cs202pzdopisivanje.Windows;

import Enums.SceneEnum;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        switchScene(SceneEnum.LOGIN);
        stage.show();
    }

    /** Switches scenes for the current stage.
     *
     * @param sceneEnum
     * Scene that will be switched to.
     */

    public static void switchScene(SceneEnum sceneEnum) {
        try {
            String fxmlFile = "";
            String title = "";

            switch (sceneEnum) {
                case LOGIN -> {
                    fxmlFile = "LogIn.fxml";
                    title = "Login";
                }
                case HOME -> {
                    fxmlFile = "home.fxml";
                    title = "Home";
                }
                case PROFILE -> {
                    fxmlFile = "Profile.fxml";
                    title = "User Profile";
                }
                case REGISTER -> {
                    fxmlFile = "Register.fxml";
                    title = "Register";
                }
            }

            FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource("/com/example/cs202pzdopisivanje/" + fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load scene: " + sceneEnum);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
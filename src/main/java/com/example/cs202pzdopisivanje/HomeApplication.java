package com.example.cs202pzdopisivanje;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.Network.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeApplication extends Application {

    public static Stage primaryStage;

    /** Starts the application and connects to the server. */
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Client.connect();
            primaryStage = stage;
            primaryStage.setTitle("Dopisivanje");
            switchScene(SceneEnum.LOGIN);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Stops the application, disconnects and stops the server. */
    @Override
    public void stop() throws Exception {
        Client.disconnect();
        super.stop();
    }

    /** Switches scenes for the current stage.
     *
     * @param sceneEnum
     * Scene that will be switched to.
     */
    public static void switchScene(SceneEnum sceneEnum) {
        try {
            String fxmlFile = "";

            switch (sceneEnum) {
                case LOGIN -> {
                    fxmlFile = "LogIn.fxml";
                }
                case HOME -> {
                    fxmlFile = "Home.fxml";
                }
                case PROFILE -> {
                    fxmlFile = "Profile.fxml";
                }
                case REGISTER -> {
                    fxmlFile = "Register.fxml";
                }
                case FRIENDS -> {
                    fxmlFile = "Friends.fxml";
                }
            }

            FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource("/com/example/cs202pzdopisivanje/" + fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());

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
package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.HomeApplication;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Requests.RegisterRequest;
import com.example.cs202pzdopisivanje.Requests.UsernameRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * RegisterController manages all the functions of the Register page in the application.
 * Is used for registering a new profile for the application and checks for the username to be unique.
 */
public class RegisterController {

    /**
     * The TextField input for the username.
     */
    @FXML
    private TextField usernameField;

    /**
     * The TextField input for the password.
     */
    @FXML
    private TextField passwordField;

    /**
     * The error label to display success or error.
     */
    @FXML
    private Label errorLabel;

    /**
     * Returns user to the login page.
     */
    @FXML
    public void OnCancelButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.LOGIN);
    }

    /**
     * Attempts to register a new user with given information and log in.
     * Checks if the username is taken.
     */
    @FXML
    public void OnRegisterButtonClick(ActionEvent actionEvent) throws IOException {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            showError("Please enter username and password.");
            return;
        }

        Client.getHandler().send(new UsernameRequest(username));
        UsernameRequest response = (UsernameRequest) Client.getHandler().tryReceive();

        if (response.getUsername() != null) {
            showError("Username already exists. Please try another username.");
            return;
        }

        Client.getHandler().send(new RegisterRequest(username, password));
        RegisterRequest response2 = (RegisterRequest) Client.getHandler().tryReceive();

        if (response2.getId() == -1) {
            showError("Unknown error.");
            return;
        }

        HomeApplication.currentUser.setPassword(password);
        HomeApplication.currentUser.setUsername(username);
        HomeApplication.switchScene(SceneEnum.HOME);
    }

    /**
     * showError displays an error.
     * @param s Text that will be displayed.
     */
    private void showError(String s) {
        if (errorLabel != null) {
            errorLabel.setText(s);
            errorLabel.setVisible(true);
        }
    }
}

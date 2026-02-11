package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.HomeApplication;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Requests.LoginRequest;
import com.example.cs202pzdopisivanje.Requests.RegisterRequest;
import com.example.cs202pzdopisivanje.Requests.UsernameRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void OnCancelButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.LOGIN);
    }

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

    /** Sets the text to display the error when logging in. */
    private void showError(String s) {
        if (errorLabel != null) {
            errorLabel.setText(s);
            errorLabel.setVisible(true);
        }
    }
}

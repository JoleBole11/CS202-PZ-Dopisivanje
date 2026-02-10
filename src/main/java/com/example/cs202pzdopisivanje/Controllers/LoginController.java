package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.HomeApplication;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Requests.LoginRequest;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    protected void OnLoginButtonClick() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            showError("Please enter username and password.");
            return;
        }

        Client.getHandler().send(new LoginRequest(username, password));
        LoginRequest response = (LoginRequest) Client.getHandler().tryReceive();

        if (response.getId() == -1) {
            showError("Invalid username or password.");
        }
        else {
            HomeApplication.switchScene(SceneEnum.HOME);
        }

        DbManager.setAccountID(response.getId());
    }

    /** Sets the text to display the error when logging in. */
    private void showError(String s) {
        if (errorLabel != null) {
            errorLabel.setText(s);
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void OnRegisterButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.REGISTER);
    }
}
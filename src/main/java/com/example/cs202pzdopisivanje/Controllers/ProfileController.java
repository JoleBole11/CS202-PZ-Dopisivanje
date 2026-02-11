package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.HomeApplication;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Requests.EditRequest;
import com.example.cs202pzdopisivanje.Requests.UsernameRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ProfileController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void OnExitButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.HOME);
    }

    public void initialize() {
        usernameField.setText(HomeApplication.currentUser.getUsername());
        passwordField.setText(HomeApplication.currentUser.getPassword());
    }

    @FXML
    public void OnSaveButtonClick(ActionEvent actionEvent) throws IOException {

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

        Client.getHandler().send(new EditRequest(usernameField.getText(), passwordField.getText()));
        EditRequest response2 = (EditRequest) Client.getHandler().tryReceive();

        if (response2 != null)
        {
            showSuccess("Profile edited successfully.");
        }
        else {
            showError("Unknown error.");
        }
    }

    /** Sets the text to display the error when logging in. */
    private void showError(String s) {
        if (errorLabel != null) {
            errorLabel.setText(s);
            errorLabel.setVisible(true);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    private void showSuccess(String s) {
        if (errorLabel != null) {
            errorLabel.setText(s);
            errorLabel.setVisible(true);
            errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        }
    }
}

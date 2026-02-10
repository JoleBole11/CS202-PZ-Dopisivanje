package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.HomeApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    protected void OnLoginButtonClick() {
        HomeApplication.switchScene(SceneEnum.HOME);
    }

    @FXML
    public void OnRegisterButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.REGISTER);
    }
}

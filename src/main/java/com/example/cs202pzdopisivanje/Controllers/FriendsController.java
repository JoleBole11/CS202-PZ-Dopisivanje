package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.HomeApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FriendsController {

    @FXML
    public void OnBackButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.HOME);
    }
}

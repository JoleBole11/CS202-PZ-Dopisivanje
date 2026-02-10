package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.HomeApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HomeController {

    @FXML
    private ListView<String> friendsList; // Renamed to match fx:id in Home.fxml

    @FXML
    private ListView<String> groupsList;  // Renamed to match fx:id in Home.fxml

    private final ObservableList<String> items = FXCollections.observableArrayList (
            "Strahinja", "Viktor", "Andrija", "Matija");

    private final ObservableList<String> items2 = FXCollections.observableArrayList (
            "Paradox", "Unreal", "Amogus", "Meow");

    @FXML
    public void initialize() {
        // This method is called automatically by FXMLLoader after the FXML is loaded
        if (friendsList != null) {
            friendsList.setItems(items);
            friendsList.toFront(); // Ensure friends are visible by default
        }
    }

    @FXML
    public void OnEditMenuClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.PROFILE);
    }

    @FXML
    public void OnLogOutMenuClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.LOGIN);
    }

    @FXML
    public void OnFriendsButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.FRIENDS);
    }

    @FXML
    public void OnFriendsMenuSelected(ActionEvent actionEvent) {
        if (friendsList != null) {
            friendsList.toFront();
            groupsList.toBack();
            groupsList.setDisable(true);
            friendsList.setDisable(false);
            friendsList.setItems(items);
        }
    }

    @FXML
    public void OnGroupsMenuSelected(ActionEvent actionEvent) {
        if (groupsList != null) {
            groupsList.toFront();
            friendsList.toBack();
            groupsList.setDisable(false);
            friendsList.setDisable(true);
            groupsList.setItems(items2);
        }
    }
}
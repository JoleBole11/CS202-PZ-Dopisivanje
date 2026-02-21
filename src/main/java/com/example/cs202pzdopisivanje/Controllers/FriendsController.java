package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.CellFactories.FriendRequestCell;
import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.HomeApplication;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Objects.FriendRequestObject;
import com.example.cs202pzdopisivanje.Requests.FriendReqRequest;
import com.example.cs202pzdopisivanje.Requests.FriendRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.Objects;

public class FriendsController {

    public ListView<String> friendsList;
    public ListView<FriendRequestObject> requestsList;
    public ListView<FriendRequestObject> sentList;

    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final ObservableList<FriendRequestObject> friendRequests = FXCollections.observableArrayList();
    private final ObservableList<FriendRequestObject> requests = FXCollections.observableArrayList();
    private final ObservableList<FriendRequestObject> sent = FXCollections.observableArrayList();

    @FXML
    public void OnBackButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.HOME);
    }

    public void initialize() {
        if (friendsList != null) {
            friendsList.setItems(friends);
            friendsList.toFront();
        }

        if (requestsList != null) {
            requestsList.setCellFactory(lv -> new FriendRequestCell());
        }

        if (sentList != null) {
            sentList.setCellFactory(lv -> new FriendRequestCell());
        }

        try {
            Client.getHandler().send(new FriendRequest(DbManager.getAccountID()));
            FriendRequest response = (FriendRequest) Client.getHandler().tryReceive();

            Client.getHandler().send(new FriendReqRequest(DbManager.getAccountID()));
            FriendReqRequest response2 = (FriendReqRequest) Client.getHandler().tryReceive();

            if (response != null && response.getFriends() != null) {
                friends.clear();
                friends.addAll(response.getFriends());
                System.out.println("Loaded " + friends.size() + " friends");
            } else {
                System.out.println("No friends received or response was null");
            }

            if (response2 != null && response2.getFriendsRequests() != null) {
                friendRequests.clear();
                friendRequests.addAll(response2.getFriendsRequests());
                requests.clear();
                sent.clear();
                for (FriendRequestObject fr : friendRequests) {
                    if (Objects.equals(fr.getType(), "Pending")) requests.add(fr);
                    else if (Objects.equals(fr.getType(), "Sent")) sent.add(fr);
                }
                System.out.println("Loaded " + requests.size() + " requests");
                System.out.println("Loaded " + sent.size() + " sent requests");
                requestsList.setItems(requests);
                sentList.setItems(sent);
            } else {
                System.out.println("No requests received or response was null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnFriendsButtonClick(ActionEvent actionEvent) {
        if (friendsList != null) {
            friendsList.toFront();
            sentList.toBack();
            requestsList.toBack();
            requestsList.setDisable(true);
            sentList.setDisable(true);
            friendsList.setDisable(false);
            friendsList.setItems(friends);
        }
    }

    public void OnRequestsButtonClick(ActionEvent actionEvent) {
        if (requestsList != null) {
            requestsList.toFront();
            friendsList.toBack();
            sentList.toBack();
            requestsList.setDisable(false);
            sentList.setDisable(true);
            friendsList.setDisable(true);
            requestsList.setItems(requests);
        }
    }

    public void OnSentButtonClick(ActionEvent actionEvent) {
        if (sentList != null) {
            sentList.toFront();
            requestsList.toBack();
            friendsList.toBack();
            sentList.setDisable(false);
            requestsList.setDisable(true);
            friendsList.setDisable(true);
            sentList.setItems(sent);
        }
    }

    public void OnAddFriendButtonClick(ActionEvent actionEvent) {
    }
}

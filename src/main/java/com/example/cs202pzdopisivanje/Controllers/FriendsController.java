package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.CellFactories.FriendRequestCell;
import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.HomeApplication;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Objects.FriendRequestObject;
import com.example.cs202pzdopisivanje.Requests.FriendReqRequest;
import com.example.cs202pzdopisivanje.Requests.FriendRequest;
import com.example.cs202pzdopisivanje.Requests.SendFriendRequest;
import com.example.cs202pzdopisivanje.Requests.UsernameRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
// Add these imports at the top
import com.example.cs202pzdopisivanje.CellFactories.FriendCell;
import com.example.cs202pzdopisivanje.Requests.RemoveFriendRequest;

import java.io.IOException;
import java.util.Objects;

public class FriendsController {

    public ListView<String> friendsList;
    public ListView<FriendRequestObject> requestsList;
    public ListView<FriendRequestObject> sentList;
    public VBox addFriendPage;

    @FXML
    private TextField friendUsernameField;
    @FXML
    private Label errorLabel;

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
            friendsList.setCellFactory(lv -> new FriendCell()); // Add this line
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
        try {
            Client.getHandler().send(new FriendRequest(DbManager.getAccountID()));
            FriendRequest response = (FriendRequest) Client.getHandler().tryReceive();

            if (response != null && response.getFriends() != null) {
                friends.clear();
                friends.addAll(response.getFriends());
                System.out.println("Loaded " + friends.size() + " friends");
            } else {
                System.out.println("No friends received or response was null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (friendsList != null) {
            friendsList.setCellFactory(lv -> new FriendCell()); // Add this line
            friendsList.toFront();
            addFriendPage.toBack();
            sentList.toBack();
            requestsList.toBack();
            friendsList.setDisable(false);
            requestsList.setDisable(true);
            addFriendPage.setDisable(true);
            sentList.setDisable(true);
            sentList.setVisible(false);
            friendsList.setVisible(true);
            requestsList.setVisible(false);
            addFriendPage.setVisible(false);
            friendsList.setItems(friends);
        }
    }

    public void OnRequestsButtonClick(ActionEvent actionEvent) {
        try {
            Client.getHandler().send(new FriendReqRequest(DbManager.getAccountID()));
            FriendReqRequest response2 = (FriendReqRequest) Client.getHandler().tryReceive();

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
        if (requestsList != null) {
            requestsList.toFront();
            addFriendPage.toBack();
            friendsList.toBack();
            sentList.toBack();
            requestsList.setDisable(false);
            addFriendPage.setDisable(true);
            sentList.setDisable(true);
            friendsList.setDisable(true);
            sentList.setVisible(false);
            friendsList.setVisible(false);
            addFriendPage.setVisible(false);
            requestsList.setVisible(true);
            requestsList.setItems(requests);
        }
    }

    public void OnSentButtonClick(ActionEvent actionEvent) {
        try {
            Client.getHandler().send(new FriendReqRequest(DbManager.getAccountID()));
            FriendReqRequest response2 = (FriendReqRequest) Client.getHandler().tryReceive();

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
        if (sentList != null) {
            sentList.toFront();
            addFriendPage.toBack();
            requestsList.toBack();
            friendsList.toBack();
            sentList.setDisable(false);
            addFriendPage.setDisable(true);
            requestsList.setDisable(true);
            friendsList.setDisable(true);
            sentList.setVisible(true);
            addFriendPage.setVisible(false);
            friendsList.setVisible(false);
            requestsList.setVisible(false);
            sentList.setItems(sent);
        }
    }

    public void OnAddFriendButtonClick(ActionEvent actionEvent) {
        addFriendPage.toFront();
        sentList.toBack();
        requestsList.toBack();
        friendsList.toBack();
        addFriendPage.setDisable(false);
        sentList.setDisable(true);
        requestsList.setDisable(true);
        friendsList.setDisable(true);
        addFriendPage.setVisible(true);
        sentList.setVisible(false);
        friendsList.setVisible(false);
        requestsList.setVisible(false);
    }

    public void onSendRequestButton(ActionEvent actionEvent) throws IOException {
        String username = friendUsernameField.getText();

        if (username == null || username.isBlank()) {
            showError("Please enter username");
            return;
        }

        Client.getHandler().send(new UsernameRequest(username));
        UsernameRequest response = (UsernameRequest) Client.getHandler().tryReceive();

        if (response.getUsername() == null) {
            showError("This user does not exist.");
            return;
        }

        Client.getHandler().send(new SendFriendRequest(username));
        SendFriendRequest response2 = (SendFriendRequest) Client.getHandler().tryReceive();

        if (Objects.equals(response2.getUsername(), "Fail")) {
            showError("Unknown error.");
            return;
        }
        if (Objects.equals(response2.getUsername(), "Exists")) {
            showError("Request already sent or friends with user.");
            return;
        }

        showSuccess("Friend request sent.");
    }

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
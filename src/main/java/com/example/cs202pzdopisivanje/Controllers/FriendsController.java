package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.CellFactories.FriendCell;
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
import com.example.cs202pzdopisivanje.Objects.Chat;

import java.io.IOException;
import java.util.Objects;

/**
 * FriendsController manages all the functions of the Friends page in the application.
 * Supports checking and removing friends, checking sent and received requests and sending requests.
 */
public class FriendsController {

    /**
     * The ListView of friends.
     */
    public ListView<Chat> friendsList;
    /**
     * The ListView of pending friend requests.
     */
    public ListView<FriendRequestObject> requestsList;
    /**
     * The ListView of sent friend requests.
     */
    public ListView<FriendRequestObject> sentList;
    /**
     * The VBox of the add friend page.
     */
    public VBox addFriendPage;

    /**
     * The TextField input of a friend to send a request to.
     */
    @FXML
    private TextField friendUsernameField;
    /**
     * The error label to display error or success.
     */
    @FXML
    private Label errorLabel;

    /**
     * The List of a user's friends.
     */
    private final ObservableList<Chat> friends = FXCollections.observableArrayList();
    /**
     * The List of a user's friend requests.
     */
    private final ObservableList<FriendRequestObject> friendRequests = FXCollections.observableArrayList();
    /**
     * The List of a user's pending friend requests.
     */
    private final ObservableList<FriendRequestObject> requests = FXCollections.observableArrayList();
    /**
     * The List of a user's sent friend requests.
     */
    private final ObservableList<FriendRequestObject> sent = FXCollections.observableArrayList();

    /**
     * Changes the scene when the back button is clicked.
     */
    @FXML
    public void OnBackButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.HOME);
    }

    /**
     * Is run on opening the friend's page.
     */
    @FXML
    public void initialize() {
        if (friendsList != null) {
            friendsList.setItems(friends);
            friendsList.setCellFactory(lv -> new FriendCell()); // Use ChatCell instead of FriendCell
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
                System.out.println("No friends received");
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
                System.out.println("No requests received");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes display to friends when the button is clicked.
     */
    public void OnFriendsButtonClick(ActionEvent actionEvent) {
        try {
            Client.getHandler().send(new FriendRequest(DbManager.getAccountID()));
            FriendRequest response = (FriendRequest) Client.getHandler().tryReceive();

            if (response != null && response.getFriends() != null) {
                friends.clear();
                friends.addAll(response.getFriends()); // Now List<Chat>
                System.out.println("Loaded " + friends.size() + " friends");
            } else {
                System.out.println("No friends received or response was null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (friendsList != null) {
            friendsList.setCellFactory(lv -> new FriendCell()); // Use ChatCell
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

    /**
     * Changes display to received requests when the button is clicked.
     */
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

    /**
     * Changes display to sent requests when the button is clicked.
     */
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

    /**
     * Changes display to send requests when the button is clicked.
     */
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

    /**
     * Sends a friend request.
     */
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

    /**
     * showError displays an error.
     * @param s Text that will be displayed.
     */
    private void showError(String s) {
        if (errorLabel != null) {
            errorLabel.setText(s);
            errorLabel.setVisible(true);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    /**
     * showError displays a success.
     * @param s Text that will be displayed.
     */
    private void showSuccess(String s) {
        if (errorLabel != null) {
            errorLabel.setText(s);
            errorLabel.setVisible(true);
            errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        }
    }
}
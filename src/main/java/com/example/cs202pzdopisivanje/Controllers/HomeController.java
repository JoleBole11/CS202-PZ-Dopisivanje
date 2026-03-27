package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.CellFactories.ChatCell;
import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.HomeApplication;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Objects.Chat;
import com.example.cs202pzdopisivanje.Requests.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML
    private ListView<String> friendsList;
    @FXML
    private ListView<Chat> groupsList;
    @FXML
    private Button createGroupButton;
    @FXML
    private VBox joinGroupVBox;
    @FXML
    private VBox createGroupVBox;
    @FXML
    private BorderPane chatBox;
    @FXML
    private Label errorLabelCreate;
    @FXML
    private Label errorLabelJoin;
    @FXML
    private TextField createGroupText;
    @FXML
    private TextField joinGroupText;
    @FXML
    private TextArea messageTextArea;

    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final ObservableList<Chat> groups = FXCollections.observableArrayList(); // Changed to Chat
    
    // Keep track of currently selected chat
    private Chat selectedChat = null;

    @FXML
    public void initialize() throws IOException {
        if (friendsList != null) {
            friendsList.setItems(friends);
            friendsList.toFront();
        }

        // Set up custom cell factory for groups ListView
        if (groupsList != null) {
            groupsList.setCellFactory(listView -> new ChatCell());
            
            // Add selection listener to track selected chat
            groupsList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedChat = newValue;
                    if (selectedChat != null) {
                        System.out.println("Selected chat: " + selectedChat.getChatName() + 
                                         " (ID: " + selectedChat.getChatId() + ")");
                    }
                }
            );
        }

        joinGroupVBox.setVisible(false);
        joinGroupVBox.setDisable(true);
        createGroupVBox.setVisible(false);
        createGroupVBox.setDisable(true);
        chatBox.setVisible(true);
        chatBox.setDisable(false);

        try {
            Client.getHandler().send(new GroupRequest(DbManager.getAccountID()));
            GroupRequest response = (GroupRequest) Client.getHandler().tryReceive();
            Client.getHandler().send(new FriendRequest(DbManager.getAccountID()));
            FriendRequest response2 = (FriendRequest) Client.getHandler().tryReceive();
            
            if (response != null && response.getGroups() != null) {
                groups.clear();
                List<Chat> chatList = response.getGroups();
                groups.addAll(chatList);
                System.out.println("Loaded " + groups.size() + " groups");
            } else {
                System.out.println("No groups received or response was null");
            }

            if (response2 != null && response2.getFriends() != null) {
                friends.clear();
                friends.addAll(response2.getFriends());
                System.out.println("Loaded " + friends.size() + " friends");
            } else {
                System.out.println("No friends received or response was null");
            }
        } catch (Exception e) {
            System.out.println("Error loading groups or friends: " + e.getMessage());
            e.printStackTrace();
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
            friendsList.setItems(friends);
            createGroupButton.setVisible(false);
            createGroupButton.setDisable(true);
            selectedChat = null; // Clear chat selection when switching to friends
        }
    }

    @FXML
    public void OnGroupsMenuSelected(ActionEvent actionEvent) {
        if (groupsList != null) {
            groupsList.toFront();
            friendsList.toBack();
            groupsList.setDisable(false);
            friendsList.setDisable(true);
            groupsList.setItems(groups);
            createGroupButton.setVisible(true);
            createGroupButton.setDisable(false);
        }
    }

    public void onCreateGroupClick(ActionEvent actionEvent) {
        String groupName = createGroupText.getText();
        try {
            if (groupName == null || groupName.trim().isEmpty()) {
                showError("Group name cannot be empty", errorLabelCreate);
                return;
            }

            if (groupName.length() < 3) {
                showError("Group name must be at least 3 characters long", errorLabelCreate);
                return;
            }

            if (groupName.length() > 20) {
                showError("Group name cannot exceed 20 characters", errorLabelCreate);
                return;
            }

            // Create request for group creation
            JoinGroupRequest joinRequest = new JoinGroupRequest(DbManager.getAccountID(), groupName, "member");
            
            // Send the request
            Client.getHandler().send(joinRequest);
            
            // Wait for response
            JoinGroupRequest response = (JoinGroupRequest) Client.getHandler().tryReceive();
            
            if (response != null) {
                showSuccess("Group '" + groupName + "' created successfully!", errorLabelCreate);
                
                // Add the new group to the local list as a Chat object
                // You'll need to get the chat ID from the response or make another request
                Chat newChat = new Chat(0, groupName); // Temporary ID, should be from response
                if (!groups.contains(newChat)) {
                    groups.add(newChat);
                }
                
                // Clear the text field
                createGroupText.clear();
                
                // Refresh the groups list view
                groupsList.setItems(groups);
                
            } else {
                showError("Error", errorLabelCreate);
            }
            
        } catch (Exception e) {
            System.out.println("Error creating group: " + e.getMessage());
            e.printStackTrace();
            showError("Error.", errorLabelCreate);
        }
    }

    public void onJoinGroupClick(ActionEvent actionEvent) {
        String groupName = joinGroupText.getText();
        try {
            if (groupName == null || groupName.trim().isEmpty()) {
                showError("Group name cannot be empty", errorLabelJoin);
                return;
            }

            if (groupName.length() < 3) {
                showError("Group name must be at least 3 characters long", errorLabelJoin);
                return;
            }

            if (groupName.length() > 20) {
                showError("Group name cannot exceed 20 characters", errorLabelJoin);
                return;
            }

            // Create request for group joining
            JoinGroupRequest joinRequest = new JoinGroupRequest(DbManager.getAccountID(), groupName, "member");

            // Send the request
            Client.getHandler().send(joinRequest);

            // Wait for response
            JoinGroupRequest response = (JoinGroupRequest) Client.getHandler().tryReceive();

            if (response != null) {
                showSuccess("Group '" + groupName + "' joined successfully!", errorLabelJoin);

                // Add the new group to the local list as a Chat object
                Chat newChat = new Chat(0, groupName); // Temporary ID, should be from response
                if (!groups.contains(newChat)) {
                    groups.add(newChat);
                }

                // Clear the text field
                joinGroupText.clear();

                // Refresh the groups list view
                groupsList.setItems(groups);

            } else {
                showError("Error", errorLabelJoin);
            }

        } catch (Exception e) {
            System.out.println("Error joining group: " + e.getMessage());
            e.printStackTrace();
            showError("Error.", errorLabelJoin);
        }
    }

    public void OnCreateGroupMenuClick(ActionEvent actionEvent) {
        joinGroupVBox.setVisible(false);
        joinGroupVBox.setDisable(true);
        createGroupVBox.setVisible(true);
        createGroupVBox.setDisable(false);
        chatBox.setVisible(false);
        chatBox.setDisable(true);
    }

    public void OnJoinGroupMenuClick(ActionEvent actionEvent) {
        joinGroupVBox.setVisible(true);
        joinGroupVBox.setDisable(false);
        createGroupVBox.setVisible(false);
        createGroupVBox.setDisable(true);
        chatBox.setVisible(false);
        chatBox.setDisable(true);
    }

    private void showError(String s, Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setText(s);
            errorLabel.setVisible(true);
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    private void showSuccess(String s, Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setText(s);
            errorLabel.setVisible(true);
            errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        }
    }

    public void OnSendButtonClick(ActionEvent actionEvent) throws IOException {
        String message = messageTextArea.getText();
        
        if (selectedChat == null) {
            System.out.println("No chat selected!");
            return;
        }
        
        if (message == null || message.isEmpty()) {
            System.out.println("Message is empty!");
            return;
        }

        // Now we can use the selectedChat.getChatId() for sending messages
        SendMessageRequest sendMessageRequest = new SendMessageRequest(DbManager.getAccountID(), selectedChat.getChatId(), message);
        
        Client.getHandler().send(sendMessageRequest);
        SendMessageRequest response = (SendMessageRequest) Client.getHandler().tryReceive();

        if (response != null) {
            System.out.println("Message sent to chat: " + selectedChat.getChatName());
        }

        messageTextArea.clear();
    }
}
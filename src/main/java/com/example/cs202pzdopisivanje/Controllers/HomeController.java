package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.CellFactories.ChatCell;
import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.HomeApplication;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Objects.Chat;
import com.example.cs202pzdopisivanje.Objects.Message;
import com.example.cs202pzdopisivanje.Requests.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HomeController {

    @FXML
    private ListView<Chat> friendsList;
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
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private TextFlow chatTextFlow;

    private final ObservableList<Chat> friends = FXCollections.observableArrayList();
    private final ObservableList<Chat> groups = FXCollections.observableArrayList();
    
    // Keep track of currently selected chat
    private Chat selectedChat = null;
    
    // Real-time message polling
    private ScheduledExecutorService messagePollingService;
    private ScheduledFuture<?> pollingTask;
    private volatile boolean isPollingActive = false;
    private int lastMessageCount = 0;

    @FXML
    public void initialize() throws IOException {
        if (friendsList != null) {
            friendsList.setItems(friends);
            friendsList.setCellFactory(listView -> new ChatCell());
            
            // Add selection listener for friends list
            friendsList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedChat = newValue;
                    if (selectedChat != null) {
                        // Clear groups selection when friends item is selected
                        groupsList.getSelectionModel().clearSelection();
                        System.out.println("Selected friend chat: " + selectedChat.getChatName() + 
                                         " (ID: " + selectedChat.getChatId() + ")");
                        onChatSelectionChanged(selectedChat);
                    }
                }
            );
            friendsList.toFront();
        }

        chatTextFlow.heightProperty().addListener((obs, oldHeight, newHeight) -> chatScrollPane.setVvalue(1.0));

        // Set up custom cell factory for groups ListView
        if (groupsList != null) {
            groupsList.setCellFactory(listView -> new ChatCell());
            
            // Add selection listener to track selected chat
            groupsList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedChat = newValue;
                    if (selectedChat != null) {
                        // Clear friends selection when groups item is selected
                        friendsList.getSelectionModel().clearSelection();
                        System.out.println("Selected group chat: " + selectedChat.getChatName() + 
                                         " (ID: " + selectedChat.getChatId() + ")");
                        onChatSelectionChanged(selectedChat);
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

        // Initialize message polling service
        messagePollingService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "MessagePolling");
            t.setDaemon(true);
            return t;
        });

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

    private void onChatSelectionChanged(Chat newValue) {
        if (newValue != null) {
            selectedChat = newValue;
            // When a new chat is selected, clear the old messages and fetch the new ones.
            Platform.runLater(() -> chatTextFlow.getChildren().clear());
            lastMessageCount = 0;
            fetchAndDisplayMessages(selectedChat.getChatId());
            
            // Start polling for new messages
            startMessagePolling();
        } else {
            // Stop polling when no chat is selected
            stopMessagePolling();
        }
    }

    private synchronized void startMessagePolling() {
        stopMessagePolling(); // Ensure any existing polling is stopped
        
        if (selectedChat == null) return;
        
        isPollingActive = true;
        final int currentChatId = selectedChat.getChatId();
        
        pollingTask = messagePollingService.scheduleAtFixedRate(() -> {
            if (isPollingActive && selectedChat != null && selectedChat.getChatId() == currentChatId) {
                try {
                    // Create a new handler connection for polling to avoid conflicts
                    synchronized (Client.getHandler()) {
                        Client.getHandler().send(new GetMessagesRequest(currentChatId));
                        Object response = Client.getHandler().tryReceive();
                        
                        if (response instanceof GetMessagesRequest) {
                            GetMessagesRequest messageResponse = (GetMessagesRequest) response;
                            if (messageResponse.getMessages() != null) {
                                List<Message> messages = messageResponse.getMessages();
                                
                                // Only update if there are new messages and we're still on the same chat
                                if (messages.size() > lastMessageCount && selectedChat != null && selectedChat.getChatId() == currentChatId) {
                                    lastMessageCount = messages.size();
                                    Platform.runLater(() -> {
                                        if (selectedChat != null && selectedChat.getChatId() == currentChatId) {
                                            updateChatView(messages);
                                        }
                                    });
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error polling for messages: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, 2, 3, TimeUnit.SECONDS); // Poll every 3 seconds, start after 2 seconds
    }

    private synchronized void stopMessagePolling() {
        isPollingActive = false;
        if (pollingTask != null && !pollingTask.isCancelled()) {
            pollingTask.cancel(true);
            pollingTask = null;
        }
    }

    private void fetchAndDisplayMessages(int chatId) {
        new Thread(() -> {
            try {
                synchronized (Client.getHandler()) {
                    Client.getHandler().send(new GetMessagesRequest(chatId));
                    Object response = Client.getHandler().tryReceive();

                    if (response instanceof GetMessagesRequest) {
                        GetMessagesRequest messageResponse = (GetMessagesRequest) response;
                        List<Message> messages = messageResponse.getMessages();
                        lastMessageCount = messages != null ? messages.size() : 0;
                        
                        // UI updates must run on the JavaFX Application Thread.
                        Platform.runLater(() -> {
                            if (selectedChat != null && selectedChat.getChatId() == chatId) {
                                updateChatView(messages);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching messages: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void updateChatView(List<Message> messages) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> updateChatView(messages));
            return;
        }
        
        chatTextFlow.getChildren().clear();
        if (messages != null) {
            for (Message msg : messages) {
                Text messageText = new Text(msg.getUsername() + ": " + msg.getMessage() + "\n");
                chatTextFlow.getChildren().add(messageText);
            }
        }
    }

    @FXML
    public void OnEditMenuClick(ActionEvent actionEvent) {
        cleanup();
        HomeApplication.switchScene(SceneEnum.PROFILE);
    }

    @FXML
    public void OnLogOutMenuClick(ActionEvent actionEvent) {
        cleanup();
        HomeApplication.switchScene(SceneEnum.LOGIN);
    }

    @FXML
    public void OnFriendsButtonClick(ActionEvent actionEvent) {
        cleanup();
        HomeApplication.switchScene(SceneEnum.FRIENDS);
    }

    @FXML
    public void OnFriendsMenuSelected(ActionEvent actionEvent) {
        if (friendsList != null) {
            // Clear selections when switching to friends
            groupsList.getSelectionModel().clearSelection();
            selectedChat = null;
            stopMessagePolling();
            
            friendsList.toFront();
            groupsList.toBack();
            groupsList.setDisable(true);
            friendsList.setDisable(false);
            friendsList.setItems(friends);
            createGroupButton.setVisible(false);
            createGroupButton.setDisable(true);
            
            // Clear chat view when switching
            Platform.runLater(() -> chatTextFlow.getChildren().clear());
        }
    }

    @FXML
    public void OnGroupsMenuSelected(ActionEvent actionEvent) {
        if (groupsList != null) {
            // Clear selections when switching to groups
            friendsList.getSelectionModel().clearSelection();
            selectedChat = null;
            stopMessagePolling();
            
            groupsList.toFront();
            friendsList.toBack();
            groupsList.setDisable(false);
            friendsList.setDisable(true);
            groupsList.setItems(groups);
            createGroupButton.setVisible(true);
            createGroupButton.setDisable(false);
            
            // Clear chat view when switching
            Platform.runLater(() -> chatTextFlow.getChildren().clear());
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
            CreateGroupRequest createRequest = new CreateGroupRequest(groupName, 1);
            
            // Send the request
            Client.getHandler().send(createRequest);
            
            // Wait for response
            CreateGroupRequest response = (CreateGroupRequest) Client.getHandler().tryReceive();
            
            if (response != null) {
                showSuccess("Group '" + groupName + "' created successfully!", errorLabelCreate);
                
                // Add the new group to the local list as a Chat object
                Chat newChat = new Chat(0, groupName);
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
            JoinGroupRequest joinRequest = new JoinGroupRequest(HomeApplication.currentUser.getUserId(), groupName, "member");

            // Send the request
            Client.getHandler().send(joinRequest);

            // Wait for response
            JoinGroupRequest response = (JoinGroupRequest) Client.getHandler().tryReceive();

            if (response != null) {
                showSuccess("Group '" + groupName + "' joined successfully!", errorLabelJoin);

                // Add the new group to the local list as a Chat object
                Chat newChat = new Chat(0, groupName);
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
        stopMessagePolling();
        joinGroupVBox.setVisible(false);
        joinGroupVBox.setDisable(true);
        createGroupVBox.setVisible(true);
        createGroupVBox.setDisable(false);
        chatBox.setVisible(false);
        chatBox.setDisable(true);
    }

    public void OnJoinGroupMenuClick(ActionEvent actionEvent) {
        stopMessagePolling();
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
        
        if (message == null || message.trim().isEmpty()) {
            System.out.println("Message is empty!");
            return;
        }

        // Send the message in a separate thread to avoid blocking UI
        new Thread(() -> {
            try {
                synchronized (Client.getHandler()) {
                    SendMessageRequest sendMessageRequest = new SendMessageRequest(DbManager.getAccountID(), selectedChat.getChatId(), message);
                    
                    Client.getHandler().send(sendMessageRequest);
                    Object response = Client.getHandler().tryReceive();

                    if (response instanceof SendMessageRequest) {
                        System.out.println("Message sent to chat: " + selectedChat.getChatName());
                        
                        // Trigger an immediate refresh for better user experience
                        Platform.runLater(() -> {
                            if (selectedChat != null) {
                                fetchAndDisplayMessages(selectedChat.getChatId());
                            }
                        });
                    }
                }
            } catch (Exception e) {
                System.err.println("Error sending message: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Clear the message field immediately for better UX
        messageTextArea.clear();
    }

    /**
     * Clean up resources when the controller is destroyed or scene is changed
     */
    public void cleanup() {
        stopMessagePolling();
        if (messagePollingService != null && !messagePollingService.isShutdown()) {
            messagePollingService.shutdown();
            try {
                if (!messagePollingService.awaitTermination(2, TimeUnit.SECONDS)) {
                    messagePollingService.shutdownNow();
                }
            } catch (InterruptedException e) {
                messagePollingService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
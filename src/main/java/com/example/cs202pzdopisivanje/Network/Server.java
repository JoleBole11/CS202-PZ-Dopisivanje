package com.example.cs202pzdopisivanje.Network;

import Enums.Constants;
import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Requests.*;
// Add this import at the top
import com.example.cs202pzdopisivanje.Requests.DenyFriendRequest;
// Add this import at the top
import com.example.cs202pzdopisivanje.Requests.RemoveFriendRequest;
// Add this import at the top
import com.example.cs202pzdopisivanje.Requests.CreateGroupRequest;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class manages the server-side functionality of the Chat App.
 */
public class Server {

    /**
     * The port number on which the server is listening for incoming connections.
     */
    public static final int PORT = 8080;

    /**
     * The IP address or hostname of the server.
     */
    public static final String IP = "localhost";

    /**
     * Indicates whether the server is up and running.
     */
    private static boolean up = false;

    /**
     * Checks if the server is up and running.
     *
     * @return True if the server is up, false otherwise.
     */
    public static boolean isUp() {
        return up;
    }

    /**
     * The server socket used for accepting incoming connections.
     */
    private static ServerSocket serverSocket;

    public static void start() throws Exception {
        if (isUp()) return;
        System.out.println(Constants.serverIsStarting);
        serverSocket = new ServerSocket(PORT);
        System.out.println(Constants.serverStarted);
        up = true;

        DbManager.connect();

        System.out.println(Constants.serverIsWaitingForConnection);
        final Socket socket = serverSocket.accept();
        System.out.println(Constants.clientConnected);
        try (final Handler handler = new Handler(socket, false)) {
            while (isUp() && socket.isConnected()) {
                System.out.println(Constants.serverIsWaitingForData);
                final Object received = handler.tryReceive();
                System.out.println(Constants.serverReceivedData);

                System.out.println(Constants.serverHandlingData);
                if (received == null) break;

                if (received instanceof UsernameRequest request) {
                    request.setUsername(DbManager.userService().checkIfUserExists(request.getUsername()));
                    handler.send(request);
                }
                else if (received instanceof LoginRequest request) {
                    request.setId(DbManager.userService().login(request.getUsername(), request.getPassword()));
                    handler.send(request);
                }
                else if (received instanceof RegisterRequest request) {
                    request.setId(DbManager.userService().register(request.getUsername(), request.getPassword()));
                    handler.send(request);
                }
                else if (received instanceof EditRequest request) {
                    DbManager.userService().editUser(DbManager.getAccountID(), request.getUsername(), request.getPassword());
                    handler.send(request);
                }
                else if (received instanceof GroupRequest request) {
                    request.setGroups(DbManager.GroupService().getUserGroups());
                    handler.send(request);
                }
                else if (received instanceof FriendRequest request) {
                    request.setFriends(DbManager.FriendService().getUserFriends());
                    handler.send(request);
                }
                else if (received instanceof FriendReqRequest request) {
                    request.setFriends(DbManager.FriendService().getUserRequests());
                    handler.send(request);
                }
                else if (received instanceof SendFriendRequest request) {
                    String result = DbManager.FriendService().sendFriendRequest(request.getUsername());
                    request.setUsername(result);
                    handler.send(request);
                }
                else if (received instanceof AcceptFriendRequest request) {
                    String result = DbManager.FriendService().acceptFriendRequest(request.getUsername());
                    request.setUsername(result);
                    handler.send(request);
                }
                else if (received instanceof DenyFriendRequest request) {
                    String result = DbManager.FriendService().denyFriendRequest(request.getUsername());
                    request.setUsername(result);
                    handler.send(request);
                }
                else if (received instanceof RemoveFriendRequest request) {
                    String result = DbManager.FriendService().removeFriend(request.getUsername());
                    request.setUsername(result);
                    handler.send(request);
                }
                else if (received instanceof CreateGroupRequest request) {
                    String result = DbManager.GroupService().createGroup(request.getGroupName(), request.getIsGroup());
                    request.setGroupName(result);
                    handler.send(request);
                }
                else if (received instanceof JoinGroupRequest request) {
                    String result = DbManager.GroupService().addUserToGroup(request.getGroupName(), request.getUserId(),request.getRole());
                    request.setGroupName(result);
                    handler.send(request);
                }
                else if (received instanceof SendMessageRequest request) {
                    int result = DbManager.MessageService().sendMessage(DbManager.getAccountID(), request.getChatId(), request.getMessageText());
                    request.setChatId(result);
                    handler.send(request);
                }
                else if (received instanceof GetMessagesRequest request) {
                    request.setMessages(DbManager.MessageService().getMessagesByChatId(request.getChatId()));
                    handler.send(request);
                }

            }
        }
        stop();
    }

    /**
     * Stops the server, closes the server socket, and disconnects from the database.
     *
     * @throws IOException If an I/O error occurs while stopping the server.
     */
    public static void stop() throws IOException {
        if (!isUp()) return;
        serverSocket.close();
        up = false;
        DbManager.disconnect();

        System.out.println("db disconnected");
    }

    /**
     * The main method to start the server.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        try {
            Server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
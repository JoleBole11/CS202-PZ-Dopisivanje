package com.example.cs202pzdopisivanje.Network;

import Enums.Constants;
import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Requests.*;
import com.example.cs202pzdopisivanje.Requests.DenyFriendRequest;
import com.example.cs202pzdopisivanje.Requests.RemoveFriendRequest;
import com.example.cs202pzdopisivanje.Requests.CreateGroupRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server class manages the server-side functionality of the Chat App.
 * Supports multiple concurrent client connections.
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
     * Thread pool for handling multiple client connections
     */
    private static ExecutorService clientThreadPool;

    /**
     * Map to keep track of connected clients
     */
    private static final ConcurrentHashMap<String, ClientHandler> connectedClients = new ConcurrentHashMap<>();

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

        clientThreadPool = Executors.newCachedThreadPool();

        DbManager.connect();

        System.out.println(Constants.serverIsWaitingForConnection);

        while (isUp()) {
            try {
                final Socket clientSocket = serverSocket.accept();
                System.out.println(Constants.clientConnected);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientThreadPool.submit(clientHandler);
                
            } catch (IOException e) {
                if (isUp()) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Removes a client from the connected clients map
     */
    public static void removeClient(String clientId) {
        connectedClients.remove(clientId);
        System.out.println("Client " + clientId + " disconnected. Active clients: " + connectedClients.size());
    }

    /**
     * Adds a client to the connected clients map
     */
    public static void addClient(String clientId, ClientHandler handler) {
        connectedClients.put(clientId, handler);
        System.out.println("Client " + clientId + " connected. Active clients: " + connectedClients.size());
    }

    /**
     * Inner class to handle individual client connections
     */
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private Handler handler;
        private String clientId;
        private Integer currentUserId = null;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.clientId = socket.getRemoteSocketAddress().toString();
        }

        @Override
        public void run() {
            try {
                handler = new Handler(socket, false);
                addClient(clientId, this);

                while (isUp() && socket.isConnected()) {
                    System.out.println("Server waiting for data from client: " + clientId + " (User ID: " + currentUserId + ")");
                    final Object received = handler.tryReceive();
                    
                    if (received == null) break;
                    
                    System.out.println("Server received data from client: " + clientId + " (User ID: " + currentUserId + ")");
                    handleRequest(received);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }

        private void handleRequest(Object received) throws Exception {
            System.out.println("Server handling data from client: " + clientId + " (User ID: " + currentUserId + ")");
            
            if (received instanceof UsernameRequest request) {
                request.setUsername(DbManager.userService().checkIfUserExists(request.getUsername()));
                handler.send(request);
            }
            else if (received instanceof LoginRequest request) {
                int loginResult = DbManager.userService().login(request.getUsername(), request.getPassword());
                request.setId(loginResult);
                if (loginResult > 0) {
                    currentUserId = loginResult;
                    System.out.println("Client " + clientId + " logged in as user ID: " + currentUserId);
                }
                handler.send(request);
            }
            else if (received instanceof RegisterRequest request) {
                int registerResult = DbManager.userService().register(request.getUsername(), request.getPassword());
                request.setId(registerResult);
                if (registerResult > 0) {
                    currentUserId = registerResult;
                    System.out.println("Client " + clientId + " registered as user ID: " + currentUserId);
                }
                handler.send(request);
            }
            else if (received instanceof EditRequest request) {
                if (currentUserId != null) {
                    DbManager.userService().editUser(currentUserId, request.getUsername(), request.getPassword());
                }
                handler.send(request);
            }
            else if (received instanceof GroupRequest request) {
                if (currentUserId != null) {
                    int originalAccountId = DbManager.getAccountID();
                    DbManager.setAccountID(currentUserId);
                    try {
                        request.setGroups(DbManager.GroupService().getUserGroups());
                    } finally {
                        DbManager.setAccountID(originalAccountId);
                    }
                }
                handler.send(request);
            }
            else if (received instanceof FriendRequest request) {
                if (currentUserId != null) {
                    int originalAccountId = DbManager.getAccountID();
                    DbManager.setAccountID(currentUserId);
                    try {
                        request.setFriends(DbManager.FriendService().getUserFriends());
                    } finally {
                        DbManager.setAccountID(originalAccountId);
                    }
                }
                handler.send(request);
            }
            else if (received instanceof FriendReqRequest request) {
                if (currentUserId != null) {
                    int originalAccountId = DbManager.getAccountID();
                    DbManager.setAccountID(currentUserId);
                    try {
                        request.setFriends(DbManager.FriendService().getUserRequests());
                    } finally {
                        DbManager.setAccountID(originalAccountId);
                    }
                }
                handler.send(request);
            }
            else if (received instanceof SendFriendRequest request) {
                if (currentUserId != null) {
                    int originalAccountId = DbManager.getAccountID();
                    DbManager.setAccountID(currentUserId);
                    try {
                        String result = DbManager.FriendService().sendFriendRequest(request.getUsername());
                        request.setUsername(result);
                    } finally {
                        DbManager.setAccountID(originalAccountId);
                    }
                }
                handler.send(request);
            }
            else if (received instanceof AcceptFriendRequest request) {
                if (currentUserId != null) {
                    int originalAccountId = DbManager.getAccountID();
                    DbManager.setAccountID(currentUserId);
                    try {
                        String result = DbManager.FriendService().acceptFriendRequest(request.getUsername());
                        request.setUsername(result);
                    } finally {
                        DbManager.setAccountID(originalAccountId);
                    }
                }
                handler.send(request);
            }
            else if (received instanceof DenyFriendRequest request) {
                if (currentUserId != null) {
                    int originalAccountId = DbManager.getAccountID();
                    DbManager.setAccountID(currentUserId);
                    try {
                        String result = DbManager.FriendService().denyFriendRequest(request.getUsername());
                        request.setUsername(result);
                    } finally {
                        DbManager.setAccountID(originalAccountId);
                    }
                }
                handler.send(request);
            }
            else if (received instanceof RemoveFriendRequest request) {
                if (currentUserId != null) {
                    int originalAccountId = DbManager.getAccountID();
                    DbManager.setAccountID(currentUserId);
                    try {
                        String result = DbManager.FriendService().removeFriend(request.getUsername());
                        request.setUsername(result);
                    } finally {
                        DbManager.setAccountID(originalAccountId);
                    }
                }
                handler.send(request);
            }
            else if (received instanceof CreateGroupRequest request) {
                if (currentUserId != null) {
                    int originalAccountId = DbManager.getAccountID();
                    DbManager.setAccountID(currentUserId);
                    try {
                        String result = DbManager.GroupService().createGroup(request.getGroupName(), request.getIsGroup());
                        request.setGroupName(result);
                    } finally {
                        DbManager.setAccountID(originalAccountId);
                    }
                }
                handler.send(request);
            }
            else if (received instanceof JoinGroupRequest request) {
                if (currentUserId != null) {
                    String result = DbManager.GroupService().addUserToGroup(request.getGroupName(), request.getUserId(), request.getRole());
                    request.setGroupName(result);
                }
                handler.send(request);
            }
            else if (received instanceof SendMessageRequest request) {
                if (currentUserId != null) {
                    int result = DbManager.MessageService().sendMessage(currentUserId, request.getChatId(), request.getMessageText());
                    request.setChatId(result);
                }
                handler.send(request);
            }
            else if (received instanceof GetMessagesRequest request) {
                request.setMessages(DbManager.MessageService().getMessagesByChatId(request.getChatId()));
                handler.send(request);
            }
        }

        public void disconnect() {
            try {
                if (currentUserId != null) {
                    System.out.println("User ID " + currentUserId + " disconnected from client " + clientId);
                }
                if (handler != null) {
                    handler.close();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                removeClient(clientId);
            } catch (Exception e) {
                System.err.println("Error disconnecting client " + clientId + ": " + e.getMessage());
            }
        }
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
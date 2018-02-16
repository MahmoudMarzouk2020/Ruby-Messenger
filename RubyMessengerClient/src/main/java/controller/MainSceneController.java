
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.FriendsList;
import model.User;
import view.FriendsListCellFactory;
import common.ClientInterface;
import common.ServerInterface;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.ChatRoom;

/**
 * FXML Controller class
 *
 * @author khaled
 */
public class MainSceneController implements Initializable, FriendsListCallback {

    private ServerInterface server;
    private ClientInterface client;
    
    // Ahmed St
    private ClientInterface receiver;
    private ChatRoom chatRoom;
    ArrayList<User> chatRoomUsers;
    private FXMLLoader loader;
    private Scene scene;
    private Parent root;
    private Stage mStage;
    // Ahmed En
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ListView friendsListView;
    @FXML
    private TextField usernameOrEmailField;
    @FXML
    private Button sendRequestBtn;

    public ServerInterface getServer() {
        return server;
    }

    public void setServer(ServerInterface server) {
        this.server = server;
    }

    public ClientInterface getClient() {
        return client;
    }

    public void setClient(ClientInterface client) {
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                populateFriendsList();
                sendRequestBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            client.sendFriendRequest(usernameOrEmailField.getText());
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void renderAnnouncement(String message) {
        System.out.println("Sever rejected your connection");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Announcement from the server");
        alert.setContentText("From server : " + message);
        alert.showAndWait();
    }

    // Mahmoud Marzouk
    private void populateFriendsList() {
        try {
            ObservableList<User> friends = FXCollections.observableArrayList(new FriendsList(client.getUser()).getFriends());
            FriendsListCellFactory friendsListFactory = new FriendsListCellFactory();
            friendsListFactory.setController(this);
            friendsListView.setCellFactory(friendsListFactory);
            friendsListView.setItems(friends);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCellDoubleClickedAction(User user) {
        
        
        
        
        try {
            chatRoom = new ChatRoom();

            chatRoomUsers.add(client.getUser());
            chatRoomUsers.add(user);
            chatRoom.setRoomClients(chatRoomUsers);
            
            
            root = loader.load(getClass().getResource("/fxml/ChatRoomScene.fxml").openStream());
            ChatRoomController chatRoomController = loader.<ChatRoomController>getController();

            chatRoomController.setServer(server);
            chatRoomController.setClient(client);
            chatRoomController.setChatRoom(chatRoom);
            


            scene = new Scene(root);
            mStage.setScene(scene);
            
        } catch (IOException ex) {
            Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    

    public void notifyNewFriendRequest(User u) {
        // System.out.println("you have a friendship request from " + u.getUsername());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("New friendship request");
                alert.setContentText("you have a friendship request from " + u.getUsername());
                alert.showAndWait();
            }
        });
    }

    // Ahmed Start 

    // Ahmed End


}


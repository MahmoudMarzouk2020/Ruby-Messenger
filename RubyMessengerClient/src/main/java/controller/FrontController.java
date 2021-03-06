package controller;
// abdelfata7 start

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import common.ClientInterface;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

// abdelfata7 end
// khaled start
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import common.ServerInterface;
import java.rmi.AccessException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import model.ClientImplementation;
import model.User;

//khaled end
public class FrontController implements Initializable {

    private Stage mStage;
    private double xOffset;
    private double yOffset;
    @FXML
    private AnchorPane loginRootPane;
    @FXML
    private JFXButton closeImgBtn;
    @FXML
    private JFXButton minimizeImgBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Label signupTxt;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    
    // khaled start
    private FXMLLoader loader;
    private Scene scene;
    private Parent root;
    private ServerInterface serverRef;
    //khaled end
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mahmoud Marzouk
        
                
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initController();
                
                // khaled start
                loader = new FXMLLoader();
                
                //khaled end
                
                loginBtn.requestFocus();
            }
        });
    }
    
    // khaled start
    @FXML
    public void signInAction() {
        try {
            String userName = this.usernameField.getText();
            String password = this.passwordField.getText();
            
                if(userName.trim().equals("") || password.trim().equals("") ){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("login error");
                    alert.setContentText("you must type your username and password to sign in");
                    alert.showAndWait();
                }else{
                    if(!serverRef.isThisUserLoggedIn(userName)){//function in server for login
                        User user = serverRef.signInUser(userName, password); //function in server for login
                        if(user != null){
                            root = loader.load(getClass().getResource("/fxml/UserMainScene.fxml").openStream());
                            MainSceneController mainController = loader.<MainSceneController>getController();
                            ClientInterface client = new ClientImplementation(mainController);
                            client.setUser(user);

                            user.setUserStatus("online");
                            new UserDao().updateUser(user);

                            // Esraa Hassan
                            this.serverRef.register(client); //function in server for login
                            // khaled
                            //send client object to contacts scene controller
                            mainController.setClient(client);
                            mainController.setServer(serverRef);
                            System.out.println(client.getUser().getUsername());

                            scene = new Scene(root);
                            scene.getStylesheets().add("styles/usermainscene.css");

                            mStage.close();
                            Stage mainSceneStage = new Stage(StageStyle.UNDECORATED);
                            mainSceneStage.setScene(scene);
                            mainSceneStage.show();
                        
                        }else{
                            
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("login error");
                            alert.setContentText("invalid userName or password");
                            alert.showAndWait();
                        
                        }
                
            }else{
                showUserAlreadyLoggedInError();
            }
       
           }
        } catch (RemoteException ex) {
            showServerError();
        } catch (IOException ex) {
            Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    


    @FXML
    public void signUpAction() {
            try {
                //change scene to sign-up scene
                root = loader.load(getClass().getResource("/fxml/Signup.fxml").openStream());
                scene = new Scene(root);
                SignupController sUpController = loader.<SignupController>getController();
                sUpController.setServer(serverRef);
                // Esraa Hassan start
                if(serverRef != null)
                    sUpController.populateCountriesInComboBox(); // I worte it here as I want the server to be initialized first
                // Esraa Hassan end
                
                scene.getStylesheets().add("styles/styles.css");
                mStage.setScene(scene);
            } catch (IOException ex) {
                Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private void showServerError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("server error");
        alert.setContentText("server is down !");
        alert.showAndWait();
    }
    //khaled end
    
    // Esraa Hassan start
    private void showUserAlreadyLoggedInError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Falied");
        alert.setContentText("User Already Logged in");
        alert.showAndWait();
    }
    // Esraa Hassan end

    // Mahmoud Marzouk
    private void initController() {
        mStage = (Stage) loginRootPane.getScene().getWindow();
        loginBtn.requestFocus();
         
        closeImgBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mStage.close();
                System.exit(0);
            }
        });

        minimizeImgBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mStage.setIconified(true);
                loginBtn.requestFocus();
            }
        });
        
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                signInAction();
            }
        });
        
        signupTxt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                signUpAction();
            }
        });
    }
    public void setServer(ServerInterface server){
        this.serverRef = server;
    }
    @FXML
    public void holdChatWindow(MouseEvent event) {
        xOffset = mStage.getX() - event.getScreenX();
        yOffset = mStage.getY() - event.getScreenY();
    }

    @FXML
    public void dragChatWindow(MouseEvent event) {
        mStage.setX(event.getScreenX() + xOffset);
        mStage.setY(event.getScreenY() + yOffset);
    }
}

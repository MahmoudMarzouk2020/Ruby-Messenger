
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import model.Country;
import model.Message;
import model.User;

/**
 *
 * @author toshiba
 */
public interface ServerInterface extends Remote{
    
    //Esraa Hassan
    public void register(ClientInterface client)throws RemoteException;
    //public boolean getAcceptedState()throws RemoteException;
    //public boolean getDecidedState() throws RemoteException;
    public void unregister(ClientInterface client)throws RemoteException;
    public boolean signup_user(User user)throws RemoteException;
    public User signInUser(String username, String password)throws RemoteException;
    public List<Country> retrieveAllCountries()throws RemoteException;
    public void sendAnnouncement(String message)throws RemoteException;
    // Ahmed
    public void sendMessageToUsers(ArrayList<ClientInterface> clients, Message msg );
    public ArrayList<ClientInterface> getOnlineClientsFromUserObjects(ArrayList<User> users);
    // Mahmoud Marzouk
    public void forwardFriendshipRequest(User fromUser, String usernameOrEmail) throws RemoteException;;
}
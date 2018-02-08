package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Country;
import model.User;
import util.UserControllerIntrface;

/**
 * @author Mahmoud.Marzouk
 * @since 08/02/2018
 */
public class UserDao implements UserControllerIntrface{
    
    private Connection dbConn;
    private ResultSet results;
    private PreparedStatement insStmt, updateStmt, delStmt;

    public UserDao() {
        dbConn = OracleDbConn.getDatabaseConnection();
    }
    
    @Override
    public User retrieveUser(long userId) {
        // we need to check the return value from this method to avoid null pointer exceptions
        User u = null;
        try {
            results = dbConn.createStatement().executeQuery("select * from users where user_id = " + userId);
            if (results.next()) {
                String userName = results.getString("USERNAME");
                String password = results.getString("PASSWORD");
                String email = results.getString("EMAIL");
                String firstName = results.getString("FIRST_NAME");
                String lastName = results.getString("LAST_NAME");
                String gender = results.getString("GENDER");
                Country country = new CountryDao().retrieveCountry(results.getLong("COUNTRY"));
                u = new User(userId, userName, password, email, firstName, lastName, gender, country);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return u;
    }

    public int insertUser(User u) {
        int rowsAffected = 0;
        try {
            insStmt = dbConn.prepareStatement("INSERT INTO USERS (USER_ID, USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, "
                    + "GENDER, COUNTRY, REGISTRATION_DATE, USER_STATUS, USER_MODE) " 
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insStmt.setLong(1, u.getUserId());
            insStmt.setString(2, u.getUserName());
            insStmt.setString(3, u.getPassword());
            insStmt.setString(4, u.getEmail());
            insStmt.setString(5, u.getFirstName());
            insStmt.setString(6, u.getLastName());
            insStmt.setString(7, u.getGender());
            insStmt.setLong(8, u.getCountry().getCountryId());
            insStmt.setDate(9, Date.valueOf(u.getRegistrationDate().toString()));
            insStmt.setString(10, u.getUserStatus());
            insStmt.setString(11, u.getUserMode());
            
            rowsAffected = insStmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rowsAffected;
    }

    public int updateUser(long userId, User u) {
        int rowsAffected = 0;
        return rowsAffected;
    }

    public int deleteUser(long userId) {
        int rowsAffected = 0;
        return rowsAffected;
    }
    
}
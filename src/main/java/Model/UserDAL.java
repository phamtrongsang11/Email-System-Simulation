/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author phamt
 */
public class UserDAL extends MyDatabaseManager {

    public UserDAL() {
        super();
        this.connectDB();
    }

    public boolean checkLogin(User user) {
        boolean result = false;
        String query = "SELECT UserID, FirstName, LastName FROM user WHERE Email = ? AND Password = ? ";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(query);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user.setId(rs.getInt("UserID"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setPassword("");
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean register(User user) {
        boolean result = false;
        String query = "INSERT INTO user (FirstName, LastName, Email, Password) VALUES(?,?,?,?)";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(query);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            if (ps.executeUpdate() != 0) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public boolean updateDomain(String email, int id) {
        boolean result = false;
        String query = "UPDATE user SET  email = ? WHERE UserID = ?";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(query);
            ps.setString(1, email);
            ps.setInt(2, id);
            if (ps.executeUpdate() != 0) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean checkEmailExist(String email) {
        try {
            String query = "SELECT * FROM user WHERE Email = '" + email + "'";
            ResultSet rs = this.doReadQuery(query);
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean checkIsAdmin(String email) {
        try {
            String query = "SELECT * FROM user WHERE Email = '" + email + "' AND IsAdmin = " + 1;
            ResultSet rs = this.doReadQuery(query);
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    
    
    public ArrayList<User> getAllUser() {
        ArrayList<User> uList = new ArrayList<>();
        try {
            String query = "SELECT * FROM user";
            ResultSet rs = this.doReadQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    User u = new User(rs.getInt("UserID"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Email"));
                    uList.add(u);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return uList;
    }

    public ArrayList<User> getLockedUser() {
        ArrayList<User> uList = new ArrayList<>();
        try {
            String query = "SELECT * FROM user WHERE IsLocked = " + 1;
            ResultSet rs = this.doReadQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    User u = new User(rs.getInt("UserID"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Email"));
                    uList.add(u);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return uList;
    }

    public int getId(String email) {
        int id = 0;
        try {
            String query = "SELECT UserID FROM user where Email = '" + email + "'";
            ResultSet rs = this.doReadQuery(query);
            if (rs.next()) {
                id = rs.getInt("UserID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public boolean lockOrUnlockUser(String email, int lock) {
        boolean result = false;
        try {
            String query = "UPDATE user SET IsLocked = ? WHERE Email = ?";
            PreparedStatement p = this.getConnection().prepareStatement(query);
            p.setInt(1, 1);
            p.setString(2, email);

            if (p.executeUpdate() != 0) {
                result = true;
            }

            System.out.println(p);

        } catch (Exception e) {
            e.getMessage();
        }

        return result;
    }

    public boolean checkIsLock(String email) {
        boolean result = false;
        try {
            String query = "SELECT IsLocked FROM user WHERE Email = '" + email + "'";
            ResultSet rs = this.doReadQuery(query);
            if (rs.next()) {
                result = rs.getBoolean("IsLocked");
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return result;
    }

    public static void main(String[] args) {
        UserDAL uDAL = new UserDAL();
        User user = new User();
        user.setEmail("john@smail.com");

//        System.out.println(uDAL.getId(user.getEmail()));
//        System.out.println(uDAL.getAllUser());
    }
}

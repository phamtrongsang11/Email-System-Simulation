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
                    u.setIsAdmin(rs.getBoolean("IsAdmin"));
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

    public boolean checkStorage(User user) {
        try {
            String query = "SELECT * FROM user WHERE HadUsed >= Storage AND UserID = " + user.getId();
            ResultSet rs = this.doReadQuery(query);
            if (rs != null && rs.next()) {
                return true;
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }

    public boolean updateHadUsed(User user, Double newUsed) {
        try {
            String query = "SELECT HadUsed FROM user WHERE UserID = " + user.getId();

            ResultSet rs = this.doReadQuery(query);
            if (rs != null && rs.next()) {
                Double hadUsed = rs.getDouble("HadUsed") + newUsed;
                String queryUpdate = "UPDATE user SET HadUsed = " + hadUsed + " WHERE UserID = " + user.getId();
                PreparedStatement p = this.getConnection().prepareStatement(queryUpdate);
                if (p.executeUpdate() != 0) {
                    return true;
                }
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }

    public Double getSizeQueryDb(User user) {
        try {
            String query = "SELECT *, user.UserID, user.FirstName, user.LastName, user.Email, mail_status.Name"
                    + " FROM mail"
                    + " INNER JOIN mail_received"
                    + " ON mail.MailID = mail_received.MailID"
                    + " INNER JOIN user"
                    + " ON mail.FromID = user.UserID"
                    //                    + " INNER JOIN mail_status"
                    //                    + " ON mail_received.StatusID = mail_status.StatusID"
                    + " WHERE mail_received.ReceiverID = " + user.getId()
                    //                    + " AND mail.StatusID = " + ObjectWrapper.INBOX_LIST
                    + " ORDER BY mail.Time DESC";

            String createTempTable = "CREATE TABLE email_system.inbox " + query;

            String getSize = "SELECT round((data_length / 1024 / 1024), 2) AS Size FROM information_schema.TABLES WHERE table_schema = 'email_system' AND table_name = 'inbox'";

            String dropTempTable = "DROP table email_system.inbox";

            PreparedStatement pCreate = this.getConnection().prepareStatement(createTempTable);

            if (pCreate.executeUpdate() != 0) {
                ResultSet rsSize = this.doReadQuery(getSize);

                if (rsSize != null && rsSize.next()) {
                    Double value = rsSize.getDouble("Size");
                    PreparedStatement pDelete = this.getConnection().prepareStatement(dropTempTable);

                    if (pDelete.executeUpdate() == 0) {
                        return value;
                    }
                }
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public boolean updateStorage(String email, Double value) {
        try {
            String queryUpdate = "UPDATE user SET Storage = " + value + " WHERE Email = '" + email + "'";
            PreparedStatement p = this.getConnection().prepareStatement(queryUpdate);
            if (p.executeUpdate() != 0) {
                return true;
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return false;
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

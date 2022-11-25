/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phamt
 */
public class MailDAL extends MyDatabaseManager {

    public MailDAL() {
        super();
        this.connectDB();
    }

    public int getLastID() throws SQLException {
        int id = 0;
        String query = "SELECT MailID FROM mail ORDER BY MailID DESC LIMIT 1";
        ResultSet rs = this.doReadQuery(query);
        if (rs.next()) {
            id = rs.getInt("MailID");
        }
        return id;
    }

    public boolean sendMail(Mail mail) {

        boolean result = false;
        String query;
        if (mail.getSchedule() == null && mail.isIsCC()) {
            query = "INSERT INTO mail(Title, Content, File, FromID, statusID, IsCC) VALUES (?, ?, ?, ?, ?, ?)";
        } else if (mail.getSchedule() == null) {
            query = "INSERT INTO mail(Title, Content, File, FromID, statusID) VALUES (?, ?, ?, ?, ?)";
        } else {
            query = "INSERT INTO mail(Title, Content, File, FromID, statusID, Schedule) VALUES (?, ?, ?, ?, ?, ?)";
        }

        try {
            PreparedStatement ps = this.getConnection().prepareStatement(query);
            ps.setString(1, mail.getTitle());
            ps.setString(2, mail.getContent());
            ps.setString(3, mail.getFile());
            ps.setInt(4, mail.getFormUser().getId());
            ps.setInt(5, mail.getStatus().getId());

            if (mail.isIsCC()) {
                ps.setBoolean(6, mail.isIsCC());
            }

            if (mail.getSchedule() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date parsedDate = dateFormat.parse(mail.getSchedule());
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                System.out.println(timestamp);
                ps.setTimestamp(6, timestamp);
            }

            System.out.println(ps);
            if (ps.executeUpdate() != 0) {
                int mailId = this.getLastID();

                String qr = "INSERT INTO mail_received VALUES (?,?)";

                for (User user : mail.getToUser()) {
//                    System.out.println(user);
                    PreparedStatement p = this.getConnection().prepareStatement(qr);
                    p.setInt(1, mailId);
                    p.setInt(2, user.getId());

                    //System.out.println(p);
                    if (p.executeUpdate() == 0) {
                        return false;
                    }
                }

                String qu = "INSERT INTO mail_replies VALUES (?,?)";

                for (Mail m : mail.getRepList()) {
                    PreparedStatement p = this.getConnection().prepareStatement(qu);
                    p.setInt(1, mailId);
                    p.setInt(2, m.getId());
                    if (p.executeUpdate() == 0) {
                        return false;
                    }
                }
                result = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

//    public boolean scheduleMail(Mail mail) {
//        boolean result = false;
//        String query = "INSERT INTO mail(Title, Content, FromID, statusID, Schedule) VALUES (?, ?, ?, ?, ?)";
//
//        try {
//            PreparedStatement ps = this.getConnection().prepareStatement(query);
//            ps.setString(1, mail.getTitle());
//            ps.setString(2, mail.getContent());
//            ps.setInt(3, mail.getFormUser().getId());
//            ps.setInt(4, mail.getStatus().getId());
//            ps.setString(5, mail.getSchedule());
//
//            System.out.println(ps);
//
//            if (ps.executeUpdate() != 0) {
//
//                int mailId = this.getLastID();
//
//                String qr = "INSERT INTO mail_received VALUES (?,?)";
//
//                for (User user : mail.getToUser()) {
////                    System.out.println(user);
//                    PreparedStatement p = this.getConnection().prepareStatement(qr);
//                    p.setInt(1, mailId);
//                    p.setInt(2, user.getId());
//
//                    //System.out.println(p);
//                    if (p.executeUpdate() == 0) {
//                        return false;
//                    }
//                }
//
//                if (!mail.getRepList().isEmpty()) {
//                    String qu = "INSERT INTO mail_replies VALUES (?,?)";
//
//                    for (Mail m : mail.getRepList()) {
//                        PreparedStatement p = this.getConnection().prepareStatement(qu);
//                        p.setInt(1, mailId);
//                        p.setInt(2, m.getId());
//                        if (p.executeUpdate() == 0) {
//                            return false;
//                        }
//                    }
//                }
//                result = true;
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//
//    }
    public ArrayList<Mail> getListMailByStatus(User user, int statusID) {
        ArrayList<Mail> mailList = new ArrayList<>();

        try {
            String query = "SELECT *, user.UserID, user.FirstName, user.LastName, user.Email, mail_status.Name"
                    + " FROM mail"
                    + " INNER JOIN mail_received"
                    + " ON mail.MailID = mail_received.MailID"
                    + " INNER JOIN user"
                    + " ON mail.FromID = user.UserID"
                    //                    + " INNER JOIN mail_replies"
                    //                    + " ON mail.MailID = mail_replies.MailID"
                    + " INNER JOIN mail_status"
                    + " ON mail.StatusID = mail_status.StatusID"
                    + " WHERE mail_received.ReceiverID = " + user.getId() + " AND "
                    + " mail.StatusID = " + statusID
                    + " ORDER BY mail.Time DESC";
            
            ResultSet rs = this.doReadQuery(query);

//            System.out.println(query);
            if (rs != null) {

                while (rs.next()) {

                    User fromUser = new User(rs.getInt("UserID"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Email"));
//                    fromUser.setId(rs.getInt("UserID"));
//                    fromUser.setFirstName(rs.getString("FirstName"));
//                    fromUser.setLastName(rs.getString("LastName"));
//                    fromUser.setEmail(rs.getString("Email"));

//                    Mail rep = new Mail();
//                    rep.setId(rs.getInt("RepID"));
//                    repList.add(rep);
                    Mail mail = new Mail(rs.getInt("MailID"), rs.getString("Title"), rs.getString("Content"), rs.getString("Time"), new Status(rs.getInt("StatusID"), rs.getString("name")), fromUser);
                    mail.setFile(rs.getString("File"));
                    mail.setIsCC(rs.getBoolean("IsCC"));

                    mailList.add(mail);
                }
                System.out.println("success");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mailList;
    }

    public Mail getReplies(Mail mail) {
        ArrayList<Mail> repList = new ArrayList<>();
        try {
            String query = "SELECT *"
                    + " FROM mail_replies"
                    + " INNER JOIN mail"
                    + " ON mail_replies.RepID = mail.MailID"
                    + " WHERE mail_replies.MailID = " + mail.getId();

            ResultSet rs = this.doReadQuery(query);
//            System.out.println(query);

            if (rs != null) {
                while (rs.next()) {
                    Mail m = new Mail();
                    m.setId(rs.getInt("RepID"));
                    m.setTitle(rs.getString("Title"));
                    repList.add(m);
                }
                mail.setRepList(repList);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mail;
    }

    public ArrayList<User> getUserSpamMailList(User user) {
        ArrayList<User> userList = new ArrayList<>();
        try {
            String query = "SELECT mail.FromID"
                    + " FROM mail"
                    + " INNER JOIN mail_received"
                    + " ON mail.MailID = mail_received.MailID"
                    + " WHERE mail_received.ReceiverID = " + user.getId() + " AND "
                    + " mail.StatusID = " + ObjectWrapper.SPAM_LIST
                    + " GROUP BY mail.FromID";

            ResultSet rs = this.doReadQuery(query);
//            System.out.println(query);
            if (rs != null) {
                while (rs.next()) {
                    User fromUser = new User();
                    fromUser.setId(rs.getInt("FromID"));

                    userList.add(fromUser);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userList;
    }

//    public Mail getReplyMail(Mail mail, int id) {
//        Mail replyMail = new Mail();
//        try {
//            String query = "SELECT MailID, Title FROM mail where MailID= " + id;
//            ResultSet rs = this.doReadQuery(query);
//            //System.out.println(query);
//            if (rs != null) {
//                while (rs.next()) {
//                    replyMail.setId(rs.getInt("MailID"));
//                    replyMail.setTitle(rs.getString("Title"));
//                }
//            }
//            mail.setReplyMail(replyMail);
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//
//        return mail;
//    }
    public int getTotalMailByStatus(User user, int statusID) {
        int total = 0;
        try {
            String query = "SELECT COUNT(mail.MailID) AS Total"
                    + " FROM mail"
                    + " INNER JOIN mail_received"
                    + " ON mail.MailID = mail_received.MailID"
                    + " INNER JOIN user"
                    + " ON mail.FromID = user.UserID"
                    + " INNER JOIN mail_status"
                    + " ON mail.StatusID = mail_status.StatusID"
                    + " WHERE mail_received.ReceiverID = " + user.getId() + " AND "
                    + " mail.StatusID = " + statusID
                    + " ORDER BY mail.Time DESC";

            ResultSet rs = this.doReadQuery(query);

            if (rs != null && rs.next()) {
                total = rs.getInt("Total");
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

        return total;

    }

    public synchronized int getTotalSendList(User user, int status) {
        int total = 0;
        try {
            String query;
            if (status == 0) {
                query = "SELECT COUNT(MailID) AS Total FROM mail WHERE FromID = " + user.getId();
            } else {
                query = "SELECT COUNT(MailID) AS Total FROM mail WHERE FromID = " + user.getId() + " AND StatusID = " + status;
            }
            ResultSet rs = this.doReadQuery(query);

            if (rs != null && rs.next()) {
                total = rs.getInt("Total");
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

        return total;

    }

    public boolean updateStatus(Mail mail, int status) {
        boolean result = false;
        try {
            String query = "UPDATE mail SET StatusID = ? WHERE MailID = ?";
            PreparedStatement p = this.getConnection().prepareStatement(query);
            p.setInt(1, status);
            p.setInt(2, mail.getId());

            if (p.executeUpdate() != 0) {
                result = true;
            }
            System.out.println(mail);
            System.out.println(p);

        } catch (Exception e) {
            e.getMessage();
        }

        return result;

    }

    public ArrayList<User> getToUser(Mail mail) {
        ArrayList<User> toUser = new ArrayList<>();
        try {
            String query = "SELECT user.UserID, user.Email"
                    + " FROM mail_received"
                    + " INNER JOIN user"
                    + " ON mail_received.ReceiverID = user.UserID"
                    + " WHERE mail_received.MailID = " + mail.getId();
            ResultSet rs = this.doReadQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    User to = new User();
                    to.setId(rs.getInt("UserID"));
                    to.setEmail(rs.getString("Email"));

                    toUser.add(to);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return toUser;
    }

    public ArrayList<Mail> getToUserFromSendMail(ArrayList<Mail> sendMailList) {
        try {

            for (Mail mail : sendMailList) {
                ArrayList<User> toUser = new ArrayList<>();
                String queryTo = "SELECT user.UserID, user.Email"
                        + " FROM mail_received"
                        + " INNER JOIN user"
                        + " ON mail_received.ReceiverID = user.UserID"
                        + " WHERE mail_received.MailID = " + mail.getId();
                ResultSet r = this.doReadQuery(queryTo);

                if (r != null) {
                    while (r.next()) {
                        User to = new User();
                        to.setId(r.getInt("UserID"));
                        to.setEmail(r.getString("Email"));

                        toUser.add(to);
                    }
                }

                mail.setToUser(toUser);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return sendMailList;
    }

    public ArrayList<Mail> getSendMail(User user, int status) {
        ArrayList<Mail> mailList = new ArrayList<>();
        try {
            String query;
            if (status == 0) {
                query = "SELECT *"
                        + " FROM mail"
                        + " WHERE mail.FromID = " + user.getId()
                        + " ORDER BY mail.Time DESC";
            } else {
                query = "SELECT *"
                        + " FROM mail"
                        + " WHERE mail.FromID = " + user.getId()
                        + " AND StatusID = " + status
                        + " ORDER BY mail.Time DESC";
            }
            ResultSet rs = this.doReadQuery(query);
//            System.out.println(query);

            if (rs != null) {

                while (rs.next()) {
                    Mail mail = new Mail(rs.getInt("MailID"), rs.getString("Title"), rs.getString("Content"), rs.getString("Time"));
                    mail.setFile(rs.getString("File"));
                    mail.setIsCC(rs.getBoolean("IsCC"));
                    mail.setSchedule(rs.getString("Schedule"));
                    mailList.add(mail);

                }
            }
            mailList = getToUserFromSendMail(mailList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mailList;
    }

    public boolean deleteSchedule(int id) {
        try {

            String queryTo = "DELETE FROM mail_received WHERE MailID = ?";
            PreparedStatement p = this.getConnection().prepareStatement(queryTo);
            p.setInt(1, id);

            if (p.executeUpdate() != 0) {
                String query = "DELETE FROM mail WHERE MailID = ?";
                PreparedStatement p1 = this.getConnection().prepareStatement(query);
                p1.setInt(1, id);

                if (p1.executeUpdate() != 0) {
                    return true;

                }
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean deleteMail(int id, int uId) {
        try {

            String queryTo = "DELETE FROM mail_received WHERE MailID = ?";
            PreparedStatement p = this.getConnection().prepareStatement(queryTo);
            p.setInt(1, id);

            if (p.executeUpdate() != 0) {
                return true;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        MailDAL mDAL = new MailDAL();
        User user = new User();
        user.setId(1);
//        user.setEmail("asley@smail.com");
//        User u = new User();
//        u.setId(5);
//        u.setEmail("john@smail.com");
//        ArrayList<User> userList = new ArrayList<User>();
//        userList.add(u);
//        Mail mail = new Mail("Hello", "It nice to meet you", user, userList);
//        System.out.println(mDAL.sendMail(mail));
//        System.out.println(mDAL.getInboxMail(user, 1));
//        System.out.println(mDAL.getSendMail(user));
//        System.out.println(mDAL.getTotalMailByStatus(user, 2));
        ArrayList<Mail> mailList = mDAL.getListMailByStatus(user, 1);
        for (Mail mail : mailList) {
            mail = mDAL.getReplies(mail);
        }
        System.out.println(mailList);
    }

}
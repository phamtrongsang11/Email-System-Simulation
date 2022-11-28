/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author phamt
 */
public class Mail implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String content;
    private String time;
    private String schedule;
    private String file;
    private Double size = 0.0;
    private boolean isCC = false;
    private User formUser;
    private Status status;
    private ArrayList<MailReceived> toUser = new ArrayList<>();
    private ArrayList<Mail> repList = new ArrayList<>();

    public Mail() {

    }
    
    public Mail(int id) {
        this.id = id;
    }

    public Mail(String title, String content, User formUser, ArrayList<MailReceived> toUser) {
        this.title = title;
        this.content = content;
        this.formUser = formUser;
        this.toUser = toUser;
    }
    
    public Mail(String title, String content, User formUser) {
        this.title = title;
        this.content = content;
        this.formUser = formUser;
    }

    public Mail(int id, String title, String content, String time, User formUser, ArrayList<Mail> repList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.formUser = formUser;
        this.repList = repList;

    }
    
    public Mail(int id, String title, String content, String time, User formUser) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.formUser = formUser;
    }

    public Mail(int id, String title, String content, String time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public User getFormUser() {
        return formUser;
    }

    public void setFormUser(User formUser) {
        this.formUser = formUser;
    }

    public ArrayList<Mail> getRepList() {
        return repList;
    }

    public void setRepList(ArrayList<Mail> repList) {
        this.repList = repList;
    }

    public ArrayList<MailReceived> getToUser() {
        return toUser;
    }

    public void setToUser(ArrayList<MailReceived> toUser) {
        this.toUser = toUser;
    }

    public boolean isIsCC() {
        return isCC;
    }

    public void setIsCC(boolean isCC) {
        this.isCC = isCC;
    }

    @Override
    public String toString() {
        return "Mail{" + "id=" + id + ", title=" + title + ", content=" + content + ", time=" + time + ", schedule=" + schedule + ", file=" + file + ", size=" + size + ", isCC=" + isCC + ", status=" + status + ", formUser=" + formUser + ", toUser=" + toUser + ", repList=" + repList + '}';
    }
    
/*
//    private static final long serialVersionUID = 20210811010L;
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String content;
    private String time;
    private String schedule;
    private String file;
    private Double size = 0.0;
    private boolean isCC = false;
    private Status status;
    private User formUser;
    private ArrayList<User> toUser = new ArrayList<>();
    private ArrayList<Mail> repList = new ArrayList<>();

    public Mail() {

    }
    
    public Mail(int id) {
        this.id = id;
    }

    public Mail(String title, String content, User formUser, ArrayList<User> toUser, Status status) {
        this.title = title;
        this.content = content;
        this.formUser = formUser;
        this.toUser = toUser;
        this.status = status;
    }
    
    public Mail(String title, String content, User formUser, Status status) {
        this.title = title;
        this.content = content;
        this.formUser = formUser;
        this.status = status;
    }

    public Mail(int id, String title, String content, String time, Status status, User formUser, ArrayList<Mail> repList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.status = status;
        this.formUser = formUser;
        this.repList = repList;

    }
    
    public Mail(int id, String title, String content, String time, Status status, User formUser) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.status = status;
        this.formUser = formUser;
    }

    public Mail(int id, String title, String content, String time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public User getFormUser() {
        return formUser;
    }

    public void setFormUser(User formUser) {
        this.formUser = formUser;
    }

    public ArrayList<Mail> getRepList() {
        return repList;
    }

    public void setRepList(ArrayList<Mail> repList) {
        this.repList = repList;
    }

    public ArrayList<User> getToUser() {
        return toUser;
    }

    public void setToUser(ArrayList<User> toUser) {
        this.toUser = toUser;
    }

    public boolean isIsCC() {
        return isCC;
    }

    public void setIsCC(boolean isCC) {
        this.isCC = isCC;
    }

    @Override
    public String toString() {
        return "Mail{" + "id=" + id + ", title=" + title + ", content=" + content + ", time=" + time + ", schedule=" + schedule + ", file=" + file + ", size=" + size + ", isCC=" + isCC + ", status=" + status + ", formUser=" + formUser + ", toUser=" + toUser + ", repList=" + repList + '}';
    }
    */
      

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author phamt
 */
public class MailReceived implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private User receiver;
    private Status status = new Status(ObjectWrapper.INBOX_LIST);

    public MailReceived() {

    }

    public MailReceived(User receiver, Status status) {
        this.receiver = receiver;
        this.status = status;
    }

    public MailReceived(User receiver) {
        this.receiver = receiver;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MailReceived{" + "receiver=" + receiver + ", status=" + status + '}';
    }

}

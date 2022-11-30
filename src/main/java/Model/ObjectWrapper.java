/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;

public class ObjectWrapper implements Serializable {

//    private static final long serialVersionUID = 20210811011L;
    private static final long serialVersionUID = 1L;

    public static final int INBOX_LIST = 1;
    public static final int READ_LIST = 2;
    public static final int SPAM_LIST = 3;
    public static final int DELETE_LIST = 4;
    public static final int SCHEDULE_LIST = 5;
    public static final int SEND_LIST = 6;

    public static final int SIGNIN_USER = 7;
    public static final int REPLY_SIGNIN_USER = 8;
    public static final int SIGNUP_USER = 9;
    public static final int REPLY_SIGNUP_USER = 10;
    public static final int SEND_MAIL = 11;
    public static final int REPLY_SEND_MAIL = 12;
    public static final int CHECK_RECEPIENT = 13;
    public static final int SERVER_INFORM_CLIENT_NUMBER = 14;
    public static final int GET_LIST_MAIL = 15;
    public static final int UPDATE_TO_READ_LIST = 16;
    public static final int UPDATE_TO_SPAM_LIST = 17;
    public static final int UPDATE_TO_DELETE_LIST = 18;
    public static final int GET_TOTAL_MAIL_LIST = 19;
    public static final int CHECK_RECEPIENTS = 20;
    public static final int REPLY_MAIL = 21;
    public static final int USER_EXIT = 22;
    public static final int BROARDCAST_INBOX = 23;
    public static final int BROARDCAST_SPAM = 24;
    public static final int SIGNOUT = 25;
    public static final int GET_DOMAIN_NAME = 26;
    public static final int CHECK_EXIST_EMAIL = 27;
    public static final int GET_TO_USER = 28;
    public static final int DELETE_MAIL = 29;
    public static final int DELETE_SCHEDULE = 30;
    public static final int PUBLIC_KEY = 31;
    public static final int KEY_AES = 32;
    public static final int INIT_SEND_FILE = 33;    
    public static final int FILE_NAME = 34;
    public static final int INIT_RECEIVED_FILE = 35;
    public static final int SCHEDULE_COMPLETE = 36;
    public static final int CHECK_STORAGE = 37;
    public static final int IS_FULL= 38;
    public static final int SCHEDULE_INIT= 39;
    private int performative;
    private Object data;

    public ObjectWrapper() {

    }

    public ObjectWrapper(int performative, Object data) {
        this.performative = performative;
        this.data = data;
    }

    public int getPerformative() {
        return performative;
    }

    public void setPerformative(int performative) {
        this.performative = performative;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObjectWrapper{" + "performative=" + performative + ", data=" + data + '}';
    }

}

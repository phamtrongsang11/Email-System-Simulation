/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author phamt
 */
public class Validation {

    public Validation() {

    }

    public static boolean validationMail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
//        Pattern pattern = Pattern.compile(regexPattern);
//        Matcher m = pattern.matcher(email);
//        return m.matches();
        return email.matches(regexPattern);
    }

    public static boolean firstName(String firstName) {
        return firstName.matches("[A-Z][a-z]*");
    }

    public static boolean lastName(String lastName) {
        return lastName.matches("[A-Z]+([ '-][a-zA-Z]+)*");
    }
}

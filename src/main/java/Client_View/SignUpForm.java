/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Client_View;

import Client_Controller.ClientController;
import Common.Validation;
import Model.ObjectWrapper;
import Model.User;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author phamt
 */
public class SignUpForm extends javax.swing.JFrame {

    private ClientController mySocket;
    private MainView view;
    private String domainName;

    /**
     * Creates new form RegisterForm
     */
    public SignUpForm() {
        initComponents();
    }

    public SignUpForm(ClientController socket, MainView view, String domainName) {
        mySocket = socket;
        initComponents();
        this.view = view;
        this.domainName = domainName;
        customInit();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtFname = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtLname = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtDomain = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jPanel16 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtConfirm = new javax.swing.JPasswordField();
        jPanel13 = new javax.swing.JPanel();
        btnRegister = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(34, 39, 54));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jPanel8.setBackground(new java.awt.Color(42, 48, 66));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.Y_AXIS));

        jPanel9.setBackground(new java.awt.Color(42, 48, 66));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("REGISTER FORM");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap())
        );

        jPanel8.add(jPanel9);

        jPanel10.setBackground(new java.awt.Color(42, 48, 66));
        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 10, 20, 10));
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));

        jPanel14.setBackground(new java.awt.Color(42, 48, 66));
        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("FirstName");
        jPanel14.add(jLabel6);

        txtFname.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtFname.setPreferredSize(new java.awt.Dimension(300, 30));
        txtFname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFnameFocusLost(evt);
            }
        });
        jPanel14.add(txtFname);

        jPanel10.add(jPanel14);

        jPanel15.setBackground(new java.awt.Color(42, 48, 66));
        jPanel15.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("LastName");
        jPanel15.add(jLabel7);

        txtLname.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtLname.setPreferredSize(new java.awt.Dimension(300, 30));
        txtLname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLnameFocusLost(evt);
            }
        });
        jPanel15.add(txtLname);

        jPanel10.add(jPanel15);

        jPanel12.setBackground(new java.awt.Color(42, 48, 66));
        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Email");
        jPanel12.add(jLabel1);

        txtEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtEmail.setPreferredSize(new java.awt.Dimension(200, 30));
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });
        jPanel12.add(txtEmail);

        txtDomain.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDomain.setText("@smail.com");
        txtDomain.setEnabled(false);
        txtDomain.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel12.add(txtDomain);

        jPanel10.add(jPanel12);

        jPanel11.setBackground(new java.awt.Color(42, 48, 66));
        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Password");
        jPanel11.add(jLabel2);

        txtPassword.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtPassword.setPreferredSize(new java.awt.Dimension(300, 30));
        jPanel11.add(txtPassword);

        jPanel10.add(jPanel11);

        jPanel16.setBackground(new java.awt.Color(42, 48, 66));
        jPanel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Confirm");
        jPanel16.add(jLabel8);

        txtConfirm.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtConfirm.setPreferredSize(new java.awt.Dimension(300, 30));
        txtConfirm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtConfirmFocusLost(evt);
            }
        });
        jPanel16.add(txtConfirm);

        jPanel10.add(jPanel16);

        jPanel8.add(jPanel10);

        jPanel13.setBackground(new java.awt.Color(42, 48, 66));
        jPanel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 10, 5));

        btnRegister.setBackground(new java.awt.Color(98, 110, 212));
        btnRegister.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnRegister.setForeground(new java.awt.Color(255, 255, 255));
        btnRegister.setText("REGISTER");
        btnRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRegister)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRegister)
                .addContainerGap())
        );

        jPanel8.add(jPanel13);

        jPanel3.add(jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
//        String password = new String(txtPassword.getPassword());
//        String confirm = new String(txtConfirm.getPassword());

        if (!checkEmpty()) {
            User user = new User();
            user.setEmail(txtEmail.getText() + txtDomain.getText());
            user.setPassword(new String(txtPassword.getPassword()));
            user.setFirstName(txtFname.getText());
            user.setLastName(txtLname.getText());
            mySocket.sendData(new ObjectWrapper(ObjectWrapper.SIGNUP_USER, user));
        } else {
            JOptionPane.showMessageDialog(this, "Please input all field!!!");
        }

    }//GEN-LAST:event_btnRegisterActionPerformed

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        if (!txtEmail.getText().isEmpty()) {
            String email = txtEmail.getText() + txtDomain.getText();

            if (!Validation.validationMail(email)) {
                JOptionPane.showMessageDialog(this, "This email is invalid");
                txtEmail.setText("");
            } else {
                mySocket.sendData(new ObjectWrapper(ObjectWrapper.CHECK_EXIST_EMAIL, email));
            }
        }
    }//GEN-LAST:event_txtEmailFocusLost

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        removeOutOfActiveUI();
    }//GEN-LAST:event_formWindowClosing

    private void txtFnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFnameFocusLost
        if (!txtFname.getText().isEmpty() && !Validation.validationName(txtFname.getText())) {
            JOptionPane.showMessageDialog(this, "This first name is invalid");
            txtFname.setText("");
        }
    }//GEN-LAST:event_txtFnameFocusLost

    private void txtLnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLnameFocusLost
        if (!txtLname.getText().isEmpty() && !Validation.validationName(txtLname.getText())) {
            JOptionPane.showMessageDialog(this, "This last name is invalid");
            txtLname.setText("");
        }
    }//GEN-LAST:event_txtLnameFocusLost

    private void txtConfirmFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtConfirmFocusLost
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());
        if(!password.equals(confirm)){
            JOptionPane.showMessageDialog(this, "Confirm password not match!");
            txtConfirm.setText("");
        }
    }//GEN-LAST:event_txtConfirmFocusLost

    public boolean checkEmpty() {
        if (txtFname.getText().isEmpty() || txtLname.getText().isEmpty() || txtEmail.getText().isEmpty() || new String(txtPassword.getPassword()).isEmpty() || new String(txtConfirm.getPassword()).isEmpty()) {
            return true;
        }
        return false;
    }

    public void receivedData(ObjectWrapper data) {

        if (!data.getData().equals("false")) {
            JOptionPane.showMessageDialog(this, "Signup succesfully!");
            removeOutOfActiveUI();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Signup fail!");
        }
    }

    public void removeOutOfActiveUI() {
        ArrayList<ObjectWrapper> activeFunc = mySocket.getActiveUI();
        for (int i = 0; i < activeFunc.size(); i++) {
            if (activeFunc.get(i).getData().equals(this)) {
                activeFunc.remove(i);
            }

        }
    }

    public void checkExistMail(ObjectWrapper data) {
        if (data.getData().equals(true)) {
            txtEmail.setText("");
            JOptionPane.showMessageDialog(this, "This email have aldready existed");
        }
    }

    public void customInit() {
        txtDomain.setText("@" + domainName);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SignUpForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignUpForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignUpForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignUpForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignUpForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField txtConfirm;
    private javax.swing.JTextField txtDomain;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFname;
    private javax.swing.JTextField txtLname;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}

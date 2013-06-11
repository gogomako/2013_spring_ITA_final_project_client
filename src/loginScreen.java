
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ashley
 */
public class loginScreen extends javax.swing.JFrame {

    /**
     * Creates new form loginScreen
     */
    ClientScreen cs;
    MsgSender sender;
    ClientList clientList;
    static ClientListener clientListener;
    int listingPort = 4444;
    int sendingPort = 5555;
    String ip = "192.168.1.7";
    static String userName = "";
    static boolean login = false;
    //CodeBookManager cbm;

    public loginScreen() {
        initComponents();
        clientListener = new ClientListener(listingPort);
        clientListener.start();
        //cbm = new CodeBookManager();
        sender = new MsgSender(ip, sendingPort);
        //cbm.initCodebook(ip, sender);        
        this.requestClientList();
        //this.setBak();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    public void setBak() {
        System.out.println("dhow");
        ((JPanel) this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon("bg1.png");
        JLabel background = new JLabel(img);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        userNameField = new javax.swing.JTextField();
        loginBut = new javax.swing.JButton();
        hintMsg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        jLabel1.setText("Chat Room Client");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel2.setText("User Name:");

        userNameField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        userNameField.setForeground(new java.awt.Color(51, 51, 51));
        userNameField.setText("Anonymous User");
        userNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                userNameFieldKeyPressed(evt);
            }
        });

        loginBut.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        loginBut.setText("Log In");
        loginBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButActionPerformed(evt);
            }
        });
        loginBut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                loginButKeyPressed(evt);
            }
        });

        hintMsg.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        hintMsg.setForeground(new java.awt.Color(255, 0, 0));
        hintMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(231, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(222, 222, 222))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(hintMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(293, 293, 293)
                .addComponent(loginBut, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(292, 292, 292)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addComponent(jLabel2)
                .addGap(30, 30, 30)
                .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hintMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loginBut, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButActionPerformed
        // TODO add your handling code here:
        this.doLogin();
    }//GEN-LAST:event_loginButActionPerformed

    public void requestClientList() {
        sender.sendSystemMsg(null, "request for client list");
    }

    public static String getUserName() {
        return userName;
    }

    public void doLogin() {
        clientList = new ClientList();
        this.hintMsg.setText("");
        userName = this.userNameField.getText();
        System.out.println("type:" + userName);
        System.out.println("list size=" + clientList.getListSize());
        String[] list = clientList.getList();
        System.out.println("list" + list[0]);
        if (clientList.checkIsNameInList(userName)) {
            this.hintMsg.setText("User name exist. Please try another one. Thank you!");
        } else {
            cs = new ClientScreen();
            this.setVisible(false);
            cs.setVisible(true);
            login = true;
            //cs.initCodebook();
            //cs.setClientList();            
        }
    }

    private void loginButKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loginButKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_loginButKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:       
    }//GEN-LAST:event_formKeyPressed

    private void userNameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userNameFieldKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            this.doLogin();
        }
    }//GEN-LAST:event_userNameFieldKeyPressed

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
                    javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
                    //com.jtattoo.plaf.hifi.HiFiLookAndFeel
                    //com.jtattoo.plaf.acryl.AcrylLookAndFeel
                    //com.jtattoo.plaf.noire.NoireLookAndFeel
                    //com.jtattoo.plaf.aluminium.AluminiumLookAndFeel
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(loginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(loginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(loginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(loginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new loginScreen().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel hintMsg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton loginBut;
    private javax.swing.JTextField userNameField;
    // End of variables declaration//GEN-END:variables
}

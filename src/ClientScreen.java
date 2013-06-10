
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ashley
 */
class Test extends Thread {
    
    JScrollBar vertical;
    
    Test() {
    }
    
    Test(JScrollBar b) {
        vertical = b;
    }
    
    public void run() {
        System.out.println("runing");
    }
    
    public void setScroll() {
        System.out.println("doing");
        vertical.setValue(vertical.getMaximum());
    }
}

public class ClientScreen extends javax.swing.JFrame implements KeyListener, PropertyChangeListener {

    /**
     * Creates new form ClientScreen
     */
    static int listingPort = 4444;
    static int sendingPort = 5555;
    static String ip = "127.0.0.1";
    static JTextPane _chatMsgPane;
    static JTextArea _codebookArea;
    static CodeBookManager cbm;
    static MsgSender msgSender;
    static ClientList clientList;
    static JComboBox _userListBox;
    static HuffmanEncoder encoder;
    static ArrayList<String> typeEncodeMsg;
    static JScrollBar vertical;
    static StringBuffer newMsg;
    static JEditorPane editorP;
    
    public ClientScreen() {
        initComponents();
        //editorP=this.chatMsgPane;
        newMsg = new StringBuffer();
        vertical = this.jScrollPane2.getVerticalScrollBar();
        _userListBox = this.userListBox;
        msgSender = new MsgSender(ip, sendingPort);
        _chatMsgPane = this.chatMsgPane;
        _codebookArea = this.codebookArea;
        _chatMsgPane.setEditable(false);
        this.encodeMsgArea.setEditable(false);
        msgArea.addKeyListener(this);
        clientList = new ClientList();
        encoder = new HuffmanEncoder();
        typeEncodeMsg = new ArrayList<String>() {
        };
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                Message leaveMsg = new Message(0, loginScreen.userName, "User Leaving");
                msgSender.send(leaveMsg);
                System.out.println("close");
            }
        });
        this.initCodebook();
        String m = cbm.getCodeword("a");
        System.out.println(m);
        this.setClientList();
        this.setupTab();
        this.chatMsgPane.addPropertyChangeListener("this", this);
        final DefaultCaret caret = (DefaultCaret) _chatMsgPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }
    
    public void setupTab() {
        JLabel lbl = new JLabel("Hello, World");
        Icon icon = new ImageIcon("faceIcon.gif");
        lbl.setIcon(icon);
        this.jTabbedPane1.setIconAt(0, icon);
    }
    
    public static void setScrollToButtom() {
        ClientScreen._chatMsgPane.setCaretPosition(_chatMsgPane.getDocument().getLength());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                vertical.setValue(vertical.getMaximum());
            }
        });
    }
    
    public void initCodebook() {
        cbm = new CodeBookManager();
        cbm.initCodebook(loginScreen.userName, msgSender);
    }
    
    public static void setClientList() {
        System.out.println("set clientList");
        System.out.println(clientList);
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        String[] users = clientList.getList();
        for (int i = 0; i < users.length; i++) {
            if (!(users[i].equals(loginScreen.userName))) {
                dcbm.addElement(users[i]);
            }
        }
        _userListBox.setModel(dcbm);
    }
    
    public void keyPressed(KeyEvent e) {     //get the key press event from msgArea
        int key = e.getKeyCode();
        Character c = e.getKeyChar();
        setScrollToButtom();
        if (key == KeyEvent.VK_ENTER) {    // if user press enter
            Message msg = this.getMessage();
            msgSender.send(msg);               //send msg
            msgArea.setText("");
            this.encodeMsgArea.setText("");
            typeEncodeMsg.clear();              //clear the list of codeword
            e.consume();
        } else if (key == KeyEvent.VK_BACK_SPACE) {   // if press backspace   
            typeEncodeMsg.remove(typeEncodeMsg.size() - 1);  //remove last codeword from list            
        } else {                                             //press other word
            String text = c.toString();
            text = encoder.encode(text);                //encode the word
            typeEncodeMsg.add(text);                    // add codeword to list
            System.out.println("push " + text);
        }
        StringBuffer showEncode = new StringBuffer();
        for (int i = 0; i < typeEncodeMsg.size(); i++) {    //print the list of codeword
            showEncode.append(typeEncodeMsg.get(i));
        }
        this.encodeMsgArea.setText(showEncode.toString());  //set the encode msg area

    }
    
    public Message getMessage() {
        Message m;
        String sender = loginScreen.userName;
        String receiver = (String) this.userListBox.getSelectedItem();
        String msg = encoder.encode(this.msgArea.getText());
        if (sender.equals("")) {
            sender = "Anonymou User";
        }
        if (receiver.equals("Not Selected")) {
            m = new Message(1, sender, msg);
            System.out.println("send a public msg");
        } else {
            m = new PrivateMessage(sender, receiver, msg);
            System.out.println("send a private msg");
        }
        return m;
    }
    
    public static void updateCodebookArea() {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : cbm.getContent().getEntrySet()) {
            buffer.append(entry.getKey() + "\t=>" + entry.getValue() + "\n");
        }
        ClientScreen._codebookArea.setText(buffer.toString());
        System.out.println("updateCodebookArea");
    }
    
    public static void updateMsgScreen(String msg) {
        
        newMsg.append(msg);
//        newMsg.append(msg);
//        _chatMsgPane.setText(newMsg.toString());
//        System.out.println("R"+_chatMsgPane.getText());
//        final DefaultCaret caret = (DefaultCaret) editorP.getCaret();
//        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);        
        _chatMsgPane.setText(newMsg.toString());
        System.out.println("updateMsgScreen");
        try {
            //        try {
            //            Thread.sleep(2000);
            //        } catch (InterruptedException ex) {
            //            Logger.getLogger(ClientScreen.class.getName()).log(Level.SEVERE, null, ex);
            //        }
            //        Test t = new Test(vertical);
            //        try {
            //            t.start();
            //            Test.sleep(500);
            //            t.setScroll();
            //        } catch (InterruptedException ex) {
            //            Logger.getLogger(ClientScreen.class.getName()).log(Level.SEVERE, null, ex);
            //        }
            Thread.sleep(450);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ClientScreen.setScrollToButtom();        
    }
    
    public void sendPictureMsg(String url) {
        url = encoder.encode(url);
        Message msg = new Message(1, loginScreen.userName, url);
        msgSender.send(msg);
    }
    
    public void setLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msgArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatMsgPane = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        codebookArea = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        encodeMsgArea = new javax.swing.JTextArea();
        userListBox = new javax.swing.JComboBox();
        jToggleButton1 = new javax.swing.JToggleButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        face1L = new javax.swing.JLabel();
        face2L = new javax.swing.JLabel();
        face3L = new javax.swing.JLabel();
        face4L = new javax.swing.JLabel();
        face5L = new javax.swing.JLabel();
        face6L = new javax.swing.JLabel();
        face7L = new javax.swing.JLabel();
        face8L = new javax.swing.JLabel();
        face9L = new javax.swing.JLabel();
        face10L = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        jLabel1.setText("Chat Room Client");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Message");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        msgArea.setColumns(20);
        msgArea.setLineWrap(true);
        msgArea.setRows(5);
        jScrollPane1.setViewportView(msgArea);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        chatMsgPane.setContentType("text/html"); // NOI18N
        jScrollPane2.setViewportView(chatMsgPane);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Private Message To:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("Encode Codebook");

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        codebookArea.setColumns(20);
        codebookArea.setRows(5);
        jScrollPane3.setViewportView(codebookArea);

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("Encode Message");

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        encodeMsgArea.setColumns(20);
        encodeMsgArea.setLineWrap(true);
        encodeMsgArea.setRows(5);
        jScrollPane4.setViewportView(encodeMsgArea);

        userListBox.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        userListBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not Selected", "Test", "Test" }));

        jToggleButton1.setText("jToggleButton1");

        jTabbedPane1.setForeground(new java.awt.Color(51, 51, 51));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setLayout(new java.awt.GridLayout(2, 0));

        face1L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face1.jpg"))); // NOI18N
        face1L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face1LMouseClicked(evt);
            }
        });
        jPanel1.add(face1L);

        face2L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face2.jpg"))); // NOI18N
        face2L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face2LMouseClicked(evt);
            }
        });
        jPanel1.add(face2L);

        face3L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face3.jpg"))); // NOI18N
        face3L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face3LMouseClicked(evt);
            }
        });
        jPanel1.add(face3L);

        face4L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face4.jpg"))); // NOI18N
        face4L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face4LMouseClicked(evt);
            }
        });
        jPanel1.add(face4L);

        face5L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face5.jpg"))); // NOI18N
        face5L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face5LMouseClicked(evt);
            }
        });
        jPanel1.add(face5L);

        face6L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face6.jpg"))); // NOI18N
        face6L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face6LMouseClicked(evt);
            }
        });
        jPanel1.add(face6L);

        face7L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face7.jpg"))); // NOI18N
        face7L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face7LMouseClicked(evt);
            }
        });
        jPanel1.add(face7L);

        face8L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face8.jpg"))); // NOI18N
        face8L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face8LMouseClicked(evt);
            }
        });
        jPanel1.add(face8L);

        face9L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face9.jpg"))); // NOI18N
        face9L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face9LMouseClicked(evt);
            }
        });
        jPanel1.add(face9L);

        face10L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/face10.jpg"))); // NOI18N
        face10L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                face10LMouseClicked(evt);
            }
        });
        jPanel1.add(face10L);

        jTabbedPane1.addTab("", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(370, 370, 370)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(42, 42, 42)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(userListBox, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addGap(62, 62, 62)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(43, 43, 43)
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(userListBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(jLabel6))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(8, 8, 8)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void face1LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face1LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=\"http://i.imgur.com/c4uH4JC.jpg\">";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face1LMouseClicked
    
    private void face2LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face2LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/9jTQfYy.jpg>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face2LMouseClicked
    
    private void face3LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face3LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/jN9zKav.jpg>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face3LMouseClicked
    
    private void face4LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face4LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/Tnqxc98.jpg>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face4LMouseClicked
    
    private void face5LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face5LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/xevjBws.jpg>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face5LMouseClicked
    
    private void face6LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face6LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/2vE0E8c.jpg>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face6LMouseClicked
    
    private void face7LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face7LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/bQoT7iA.jpg>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face7LMouseClicked
    
    private void face8LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face8LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/YxXlx9g.jpg>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face8LMouseClicked
    
    private void face9LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face9LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/glOCffA.jpg>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face9LMouseClicked
    
    private void face10LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_face10LMouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/aQtMtdS.jpg>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_face10LMouseClicked
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane chatMsgPane;
    private javax.swing.JTextArea codebookArea;
    private javax.swing.JTextArea encodeMsgArea;
    private javax.swing.JLabel face10L;
    private javax.swing.JLabel face1L;
    private javax.swing.JLabel face2L;
    private javax.swing.JLabel face3L;
    private javax.swing.JLabel face4L;
    private javax.swing.JLabel face5L;
    private javax.swing.JLabel face6L;
    private javax.swing.JLabel face7L;
    private javax.swing.JLabel face8L;
    private javax.swing.JLabel face9L;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextArea msgArea;
    private javax.swing.JComboBox userListBox;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setScrollToButtom();
    }
}


import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
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
public class ClientScreen extends javax.swing.JFrame implements KeyListener, PropertyChangeListener {

    /**
     * Creates new form ClientScreen
     */
    static int listingPort = 4444;
    static int sendingPort = 5555;
    static String ip = "192.168.1.7";
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
    static CodeBook codebook;
    static JLabel _rateL;
    static JTextArea _msgArea;
    static JTextArea _encodeMsgArea;
    static int mode=0;

    public ClientScreen() {
        initComponents();
        //editorP=this.chatMsgPane; 
        _encodeMsgArea = this.encodeMsgArea;
        _msgArea = this.msgArea;
        _rateL = this.rateL;
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
                msgSender.sendSystemMsg(loginScreen.userName, "User Leaving");
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
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }
    
    public static String getReceiver(){
        return (String) _userListBox.getSelectedItem();
    }

    public void setupTab() {
        JLabel lbl = new JLabel();
        Icon icon = new ImageIcon("faceIcon.gif");
        lbl.setIcon(icon);
        this.jTabbedPane1.setIconAt(0, icon);
        JLabel lb2 = new JLabel();
        Icon icon2 = new ImageIcon("catIcon.gif");
        lb2.setIcon(icon);
        this.jTabbedPane1.setIconAt(1, icon2);
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
        codebook = new CodeBook();
        cbm = new CodeBookManager(codebook);
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

    public static void updateRateLabel() {
        System.out.println("update Rate!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        double totalRate = cbm.getCompressRate();
        double nowRate = cbm.getCompressRate(_msgArea.getText(), _encodeMsgArea.getText());
        if (!(nowRate > 0)) {
            nowRate = 0.0;
        }
        BigDecimal bd = new BigDecimal(totalRate*100);
        bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal bd2 = new BigDecimal(nowRate*100);
        bd2 = bd2.setScale(4, BigDecimal.ROUND_HALF_UP);
        _rateL.setText("CodeBook Compress Rate= " + bd.doubleValue() + "%  Message Compress Rate=" + bd2.doubleValue()+"%");
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
            text = encoder.getEncodeString(text);                //encode the word
            typeEncodeMsg.add(text);                    // add codeword to list
            System.out.println("push " + text);
        }
        StringBuffer showEncode = new StringBuffer();
        for (int i = 0; i < typeEncodeMsg.size(); i++) {    //print the list of codeword
            showEncode.append(typeEncodeMsg.get(i));
        }
        this.encodeMsgArea.setText(showEncode.toString());  //set the encode msg area
        updateRateLabel();
    }

    public Message getMessage() {
        Message m;
        String sender = loginScreen.userName;
        String receiver = (String) this.userListBox.getSelectedItem();
        byte[] msg = encoder.encode(this.msgArea.getText());
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
            Thread.sleep(450);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClientScreen.setScrollToButtom();
    }

    public void sendPictureMsg(String url) {
        
        String sender = loginScreen.userName;
        String receiver = (String) this.userListBox.getSelectedItem();
        if(receiver.equals("Not Selected")){
            msgSender.send(1, loginScreen.userName, url);
        }else{
            PrivateMessage m = new PrivateMessage(sender, receiver,encoder.encode(url));
            msgSender.send(m);
        }
        
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

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        mode2But = new javax.swing.JToggleButton();
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
        jPanel2 = new javax.swing.JPanel();
        cat1 = new javax.swing.JLabel();
        cat2 = new javax.swing.JLabel();
        cat3 = new javax.swing.JLabel();
        cat4 = new javax.swing.JLabel();
        cat5 = new javax.swing.JLabel();
        cat6 = new javax.swing.JLabel();
        cat7 = new javax.swing.JLabel();
        cat8 = new javax.swing.JLabel();
        cat9 = new javax.swing.JLabel();
        cat10 = new javax.swing.JLabel();
        rateL = new javax.swing.JLabel();
        mode1But = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        jLabel1.setText("Chat Room Client");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, 330, 50));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Message");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(218, 466, 158, -1));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        msgArea.setColumns(20);
        msgArea.setLineWrap(true);
        msgArea.setRows(5);
        jScrollPane1.setViewportView(msgArea);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 490, 313, 69));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        chatMsgPane.setContentType("text/html"); // NOI18N
        jScrollPane2.setViewportView(chatMsgPane);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 75, 521, 373));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Private Message To:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 466, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("Encode Codebook");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 70, -1, -1));

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        codebookArea.setColumns(20);
        codebookArea.setRows(5);
        jScrollPane3.setViewportView(codebookArea);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 100, 247, 220));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("Encode Message");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, -1, -1));

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        encodeMsgArea.setColumns(20);
        encodeMsgArea.setLineWrap(true);
        encodeMsgArea.setRows(5);
        jScrollPane4.setViewportView(encodeMsgArea);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 521, 56));

        userListBox.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        userListBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not Selected", "Test", "Test" }));
        getContentPane().add(userListBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 498, 148, -1));

        buttonGroup1.add(mode2But);
        mode2But.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        mode2But.setText("IGNORING MODE");
        mode2But.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mode2ButMouseClicked(evt);
            }
        });
        mode2But.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                mode2ButPropertyChange(evt);
            }
        });
        getContentPane().add(mode2But, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 130, 180, 33));

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

        jPanel2.setLayout(new java.awt.GridLayout(2, 0));

        cat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat1.gif"))); // NOI18N
        cat1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat1MouseClicked(evt);
            }
        });
        jPanel2.add(cat1);

        cat2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat2.gif"))); // NOI18N
        cat2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat2MouseClicked(evt);
            }
        });
        jPanel2.add(cat2);

        cat3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat3.gif"))); // NOI18N
        cat3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat3MouseClicked(evt);
            }
        });
        jPanel2.add(cat3);

        cat4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat4.gif"))); // NOI18N
        cat4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat4MouseClicked(evt);
            }
        });
        jPanel2.add(cat4);

        cat5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat5.gif"))); // NOI18N
        cat5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat5MouseClicked(evt);
            }
        });
        jPanel2.add(cat5);

        cat6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat6.gif"))); // NOI18N
        cat6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat6MouseClicked(evt);
            }
        });
        jPanel2.add(cat6);

        cat7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat7.gif"))); // NOI18N
        cat7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat7MouseClicked(evt);
            }
        });
        jPanel2.add(cat7);

        cat8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat8.gif"))); // NOI18N
        cat8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat8MouseClicked(evt);
            }
        });
        jPanel2.add(cat8);

        cat9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat9.gif"))); // NOI18N
        cat9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat9MouseClicked(evt);
            }
        });
        jPanel2.add(cat9);

        cat10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resorce/cat10.gif"))); // NOI18N
        cat10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cat10MouseClicked(evt);
            }
        });
        jPanel2.add(cat10);

        jTabbedPane1.addTab("", jPanel2);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, 495, 298));

        rateL.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rateL.setForeground(new java.awt.Color(51, 51, 51));
        rateL.setText("jLabel2");
        getContentPane().add(rateL, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 640, 520, 30));

        buttonGroup1.add(mode1But);
        mode1But.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        mode1But.setSelected(true);
        mode1But.setText("GENERAL MODE");
        mode1But.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mode1ButMouseClicked(evt);
            }
        });
        mode1But.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                mode1ButPropertyChange(evt);
            }
        });
        getContentPane().add(mode1But, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 80, 180, 33));

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

    private void cat1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat1MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/Xv0aIlm.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat1MouseClicked

    private void mode1ButPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_mode1ButPropertyChange
        // TODO add your handling code here:
       
    }//GEN-LAST:event_mode1ButPropertyChange

    private void mode2ButPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_mode2ButPropertyChange
        // TODO add your handling code here:
        
    }//GEN-LAST:event_mode2ButPropertyChange

    private void mode2ButMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mode2ButMouseClicked
        // TODO add your handling code here:
        System.out.println("select change on mode2");
        if(this.mode2But.isSelected())mode=2;
    }//GEN-LAST:event_mode2ButMouseClicked

    private void mode1ButMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mode1ButMouseClicked
        // TODO add your handling code here:
         if(this.mode1But.isSelected())mode=1;
    }//GEN-LAST:event_mode1ButMouseClicked

    private void cat2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat2MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/HlgfnYf.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat2MouseClicked

    private void cat3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat3MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/DPePSLY.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat3MouseClicked

    private void cat4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat4MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/wo0PR1U.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat4MouseClicked

    private void cat5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat5MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/MVfudol.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat5MouseClicked

    private void cat6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat6MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/M85he9q.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat6MouseClicked

    private void cat7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat7MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/67TW9e9.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat7MouseClicked

    private void cat8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat8MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/2eQlN8k.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat8MouseClicked

    private void cat9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat9MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/fVGBaqA.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat9MouseClicked

    private void cat10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cat10MouseClicked
        // TODO add your handling code here:
        String url = "<br><img src=http://i.imgur.com/cZMASko.gif>";
        this.sendPictureMsg(url);
    }//GEN-LAST:event_cat10MouseClicked
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel cat1;
    private javax.swing.JLabel cat10;
    private javax.swing.JLabel cat2;
    private javax.swing.JLabel cat3;
    private javax.swing.JLabel cat4;
    private javax.swing.JLabel cat5;
    private javax.swing.JLabel cat6;
    private javax.swing.JLabel cat7;
    private javax.swing.JLabel cat8;
    private javax.swing.JLabel cat9;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton mode1But;
    private javax.swing.JToggleButton mode2But;
    private javax.swing.JTextArea msgArea;
    private javax.swing.JLabel rateL;
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

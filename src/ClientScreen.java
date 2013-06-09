
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ashley
 */
public class ClientScreen extends javax.swing.JFrame implements KeyListener {

    /**
     * Creates new form ClientScreen
     */
    static int listingPort = 4444;
    static int sendingPort = 5555;
    static String ip = "127.0.0.1";
    static JTextArea _chatScreenArea;
    static JTextArea _codebookArea;
    static CodeBookManager cbm;
    static MsgSender msgSender;
    static ClientList clientList;
    static JComboBox _userListBox;
    static HuffmanEncoder encoder;
    static ArrayList<String> typeEncodeMsg;
    static JScrollBar vertical;

    public ClientScreen() {
        initComponents();
        vertical = this.jScrollPane2.getVerticalScrollBar();
        _userListBox = this.userListBox;
        msgSender = new MsgSender(ip, sendingPort);
        _chatScreenArea = this.chatScreenArea;
        _codebookArea = this.codebookArea;
        this.chatScreenArea.setEditable(false);
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
    }

    public static void setScrollToButtom() {
        vertical.setValue(vertical.getMaximum());
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
        ClientScreen._chatScreenArea.append(msg);
        System.out.println("updateMsgScreen");
        ClientScreen.setScrollToButtom();
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
        chatScreenArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        codebookArea = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        encodeMsgArea = new javax.swing.JTextArea();
        userListBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel1.setText("Chat Roon Client");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Message");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        msgArea.setColumns(20);
        msgArea.setLineWrap(true);
        msgArea.setRows(5);
        jScrollPane1.setViewportView(msgArea);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        chatScreenArea.setColumns(20);
        chatScreenArea.setLineWrap(true);
        chatScreenArea.setRows(5);
        jScrollPane2.setViewportView(chatScreenArea);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(userListBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(35, 35, 35))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                                    .addComponent(jLabel6)
                                    .addComponent(jScrollPane4)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(userListBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatScreenArea;
    private javax.swing.JTextArea codebookArea;
    private javax.swing.JTextArea encodeMsgArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
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
}

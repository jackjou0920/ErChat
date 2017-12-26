package erchat;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class NewFriendFrame extends JFrame implements ActionListener, ItemListener {

    private JLabel jlbSearch = new JLabel(new ImageIcon(getClass().getResource("/resource/search.png")));
    private JTextField jtfSearch = new HintTextFieldUI("Search by Username");
    private JPanel jpnCenter = new JPanel();
    private JPanel jpnNo = new JPanel();
    private JLabel jlbNo = new JLabel("User not Found");
    private JButton jbtOK = new JButton("OK");
    private JLabel jlbHead = new JLabel(new ImageIcon(getClass().getResource("/resource/default-big.png")));
    private JLabel jlbUser = new JLabel();
    private JLabel jlbResult = new JLabel();
    private JButton jbtCancel = new JButton("Cancel");
    private JButton jbtAdd = new JButton("Add");
    private JPanel jpnUp = new JPanel();
    private JPanel jpnDown = new JPanel();
    private CheckboxGroup checkbox = new CheckboxGroup();
    private Checkbox checkboxName = new Checkbox("User Name");
    private Checkbox checkboxId = new Checkbox("User ID");
    
    private Font font = new Font("helvitica", Font.PLAIN, 15);
    //private DefaultListModel friendModel;
    //private ArrayList<FriendPanel> friendList;

    private ChatFrame chatFrame;
    private long id = 0;
    private String user = "";

    public NewFriendFrame(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;

        setTitle("Search for Friends");
        setSize(310, 540);
        setVisible(false);
        setResizable(false);
        setLocationRelativeTo(this);
        setLayout(new BorderLayout());
        add(jpnUp, BorderLayout.NORTH);
        add(jpnDown, BorderLayout.CENTER);

        jpnUp.setLayout(null);
        jpnUp.setPreferredSize(new Dimension(0, 90));
        // jpnUp.setBorder(BorderFactory.createEtchedBorder());
        jlbSearch.setBounds(15, 40, 25, 40);
        jtfSearch.setBounds(45, 40, 250, 40);
        // jtfSearch.requestFocusInWindow();
        jpnUp.add(jlbSearch);
        jpnUp.add(jtfSearch);
        jpnUp.add(checkboxName);
        jpnUp.add(checkboxId);
        jpnUp.setBackground(new Color(244, 248, 255));

        jpnDown.setLayout(null);
        jpnDown.setBackground(Color.white);
        jpnDown.add(getJpnCenter());
        jpnDown.add(getJpnNo());
        getJpnCenter().setBounds(45, 80, 225, 185);
        // jpnCenter.setBorder(BorderFactory.createEtchedBorder());
        getJpnCenter().setBackground(Color.white);
        getJpnCenter().setLayout(null);
        getJpnCenter().setVisible(false);

        jlbHead.setBounds(65, 0, 90, 90);
        getJlbUser().setBounds(60, 85, 110, 40);
        getJlbUser().setFont(font);
        getJlbResult().setBounds(13, 120, 300, 30);
        getJlbResult().setFont(new Font("helvitica", Font.PLAIN, 13));
        getJlbResult().setForeground(new Color(132, 132, 132));
        jbtCancel.setBounds(10, 150, 100, 37);
        getJbtAdd().setForeground(Color.white);
        getJbtAdd().setBackground(new Color(0, 205, 102));
        getJbtAdd().setOpaque(true);
        getJbtAdd().setBorderPainted(false);
        getJbtAdd().setBounds(110, 151, 100, 33);

        getJpnCenter().add(jlbHead);
        getJpnCenter().add(getJlbUser());
        getJpnCenter().add(getJlbResult());
        getJpnCenter().add(jbtCancel);
        getJpnCenter().add(getJbtAdd());

        jlbNo.setBounds(3, 0, 100, 30);
        jlbNo.setFont(new Font("helvitica", Font.BOLD, 12));
        jbtOK.setBounds(0, 40, 100, 37);
        getJpnNo().setBounds(100, 90, 100, 75);
        getJpnNo().setLayout(null);
        getJpnNo().setBackground(Color.white);
        getJpnNo().add(jlbNo);
        getJpnNo().add(jbtOK);
        getJpnNo().setVisible(false);

        checkboxName.setCheckboxGroup(checkbox);
        checkboxName.setBounds(15, 7, 100, 30);
        checkboxName.addItemListener(this);
        checkboxId.setCheckboxGroup(checkbox);
        checkboxId.setBounds(130, 7, 100, 30);
        checkboxId.addItemListener(this);
        checkbox.setSelectedCheckbox(checkboxName);

        jbtCancel.addActionListener(this);
        getJbtAdd().addActionListener(this);
        jbtOK.addActionListener(this);

        // Enter to send massage
        InputMap inputMap = jtfSearch.getInputMap();
        ActionMap actionMap = jtfSearch.getActionMap();
        Object transferTextActionKey = "TRANSFER_TEXT";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), transferTextActionKey);
        actionMap.put(transferTextActionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = jtfSearch.getText();
                if (msg == null || msg.equals("")) {
                    return;
                }
                try {
                    chatFrame.getBw().write("SEARCHFRIEND$" + chatFrame.getUser_id() + "$" + msg + "\n");
                    chatFrame.getBw().flush();
                } catch (IOException e1) {
                    System.out.println(e1.toString());
                }

            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbtCancel) {
            jtfSearch.setText("");
            jtfSearch.requestFocus();
            getJpnCenter().setVisible(false);
        } else if (e.getSource() == getJbtAdd()) {
            FriendPanel jpnFriend = new FriendPanel(getId(), getUser(), 2, 0);
            chatFrame.getFriendModel().addElement(jpnFriend);
            chatFrame.getFriendList().add(jpnFriend);
            setVisible(false);
            jtfSearch.setText("");
            getJpnCenter().setVisible(false);
            try {
                chatFrame.getBw().write("ADDFRIEND$" + chatFrame.getUser_id() + "$" + chatFrame.getUsername() + "$" + getId() + "\n");
                chatFrame.getBw().flush();
            } catch (IOException e1) {
                System.out.println(e1.toString());
            }
        } else if (e.getSource() == jbtOK) {
            jtfSearch.setText("");
            jtfSearch.requestFocus();
            getJpnNo().setVisible(false);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (checkbox.getSelectedCheckbox().getLabel().equals("User Name")) {
            jtfSearch.setText("Search by Username");
            System.out.println(checkbox.getSelectedCheckbox().getLabel());
        } else {
            jtfSearch.setText("Search by User ID");
            System.out.println(checkbox.getSelectedCheckbox().getLabel());
        }
    }
    
    public void compute(String content) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

        int textwidth = (int) (font.getStringBounds(getJlbUser().getText(), frc).getWidth());
        System.out.println(textwidth);
        
        getJlbUser().setBounds(155-(textwidth/2+47), 85, 110, 40);
    }

    public JPanel getJpnNo() {
        return jpnNo;
    }

    public void setJpnNo(JPanel jpnNo) {
        this.jpnNo = jpnNo;
    }

    public JPanel getJpnCenter() {
        return jpnCenter;
    }

    public void setJpnCenter(JPanel jpnCenter) {
        this.jpnCenter = jpnCenter;
    }

    public JLabel getJlbUser() {
        return jlbUser;
    }

    public void setJlbUser(JLabel jlbUser) {
        this.jlbUser = jlbUser;
    }

    public JLabel getJlbResult() {
        return jlbResult;
    }

    public void setJlbResult(JLabel jlbResult) {
        this.jlbResult = jlbResult;
    }

    public JButton getJbtAdd() {
        return jbtAdd;
    }

    public void setJbtAdd(JButton jbtAdd) {
        this.jbtAdd = jbtAdd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}

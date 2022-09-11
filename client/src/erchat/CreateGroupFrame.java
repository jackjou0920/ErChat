package erchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

public class CreateGroupFrame extends JFrame implements ActionListener {

    private JPanel jpnName = new JPanel(new BorderLayout());
    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default.png")));
    private JPanel jpnGroupName = new JPanel(new BorderLayout());
    private JTextField jtfName = new HintTextFieldUI("Group Name");
    private JPanel jpnCreate = new JPanel(new BorderLayout());
    private JPanel jpnUp = new JPanel();
    private JLabel jlbSearch = new JLabel(new ImageIcon(getClass().getResource("/resource/search-small.png")));
    private JTextField jtfSearch = new HintTextFieldUI("Search by Display Name");
    private DefaultListModel createGroupModel = new DefaultListModel();
    private JList jlsCreateGroup = new JList(createGroupModel);
    private JScrollPane jspCreateGroup = new JScrollPane(jlsCreateGroup);
    private JPanel jpnList = new JPanel(new BorderLayout());
    private JPanel jpnChoose = new JPanel(new FlowLayout(FlowLayout.LEFT));
    // private JScrollPane jspChoose = new JScrollPane(jpnChoose);
    private JLabel jlbNote = new JLabel("You have no friends to choose from.");
    private JPanel jpnSure = new JPanel();
    private JButton jbtCancel = new JButton("Cancel");
    private JButton jbtAdd = new JButton("Add");

    private ChatFrame chatFrame;
    private CreateGroupRenderer createGroupRendererrender = new CreateGroupRenderer();
    private ArrayList<CreateGroupPanel> createGroupList = new ArrayList<CreateGroupPanel>();
    private ArrayList<ChoosePanel> chooseList = new ArrayList<ChoosePanel>();

    public CreateGroupFrame(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;

        setLayout(new BorderLayout());
        setSize(642, 568);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("Create a Group");

        loadData();
        jlbHead.setBorder(new EmptyBorder(10, 10, 10, 10));
        jpnGroupName.setBorder(new EmptyBorder(20, 0, 20, 10));
        jpnGroupName.setBackground(new Color(244, 248, 255));
        jpnGroupName.add(jtfName, BorderLayout.CENTER);
        jpnName.setBackground(new Color(244, 248, 255));
        jpnName.add(jlbHead, BorderLayout.WEST);
        jpnName.add(jpnGroupName, BorderLayout.CENTER);

        jpnCreate.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
        jpnList.setBackground(Color.white);
        jpnList.setPreferredSize(new Dimension(235, 0));
        jpnChoose.setBackground(Color.white);
        jpnChoose.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
        // jspChoose.setBorder(null);
        jpnCreate.add(jpnList, BorderLayout.WEST);
        jpnCreate.add(jpnChoose, BorderLayout.CENTER);
        jtfSearch.setPreferredSize(new Dimension(203, 33));
        jpnUp.setBackground(Color.white);
        jpnUp.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        jpnUp.add(jlbSearch);
        jpnUp.add(jtfSearch);
        jspCreateGroup.setBorder(null);
        jlsCreateGroup.setCellRenderer(createGroupRendererrender);
        jpnList.add(jpnUp, BorderLayout.NORTH);
        jpnList.add(jspCreateGroup, BorderLayout.CENTER);
        jlbNote.setForeground(new Color(132, 132, 132));
        jpnChoose.add(jlbNote);

        jbtCancel.setPreferredSize(new Dimension(100, 37));
        jbtAdd.setPreferredSize(new Dimension(100, 33));
        jbtAdd.setForeground(Color.white);
        jbtAdd.setBackground(new Color(0, 205, 102));
        jbtAdd.setOpaque(true);
        jbtAdd.setBorderPainted(false);
        jbtAdd.setEnabled(false);
        jpnSure.setBackground(Color.white);
        jpnSure.add(jbtCancel);
        jpnSure.add(jbtAdd);
        add(jpnName, BorderLayout.NORTH);
        add(jpnCreate, BorderLayout.CENTER);
        add(jpnSure, BorderLayout.SOUTH);

        jlsCreateGroup.addMouseListener(createGroupRendererrender.getGroupHandler(jlsCreateGroup));
        jlsCreateGroup.addMouseMotionListener(createGroupRendererrender.getGroupHandler(jlsCreateGroup));

        jtfName.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (chooseList.size() > 0 && jtfName.getText() != "") {
                    jbtAdd.setEnabled(true);
                } else {
                    jbtAdd.setEnabled(false);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

        jbtCancel.addActionListener(this);
        jbtAdd.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbtCancel) {
            this.dispose();
        } else if (e.getSource() == jbtAdd) {
            long room_id = System.currentTimeMillis();
            FriendPanel jpnFriend = new FriendPanel(room_id, jtfName.getText(), 1, 1);
            chatFrame.getFriendModel().addElement(jpnFriend);
            chatFrame.getFriendList().add(jpnFriend);
            RoomPanel jpnRoom = new RoomPanel(room_id, jtfName.getText(), 1, 1, 0, "");
            chatFrame.getRoomModel().addElement(jpnRoom);
            chatFrame.getRoomList().add(jpnRoom);
            chatFrame.getGroupList().add(room_id);
            chatFrame.getJlsUser().repaint();
            chatFrame.getJlsChatroom().repaint();

            String msg = "NEWGROUP$" + room_id + "$" + jtfName.getText() + "$";
            for (int i = 0; i < chooseList.size(); i++) {
                msg += chooseList.get(i).name + "$";
            }
            try {
                chatFrame.getBw().write(msg + "\n");
                chatFrame.getBw().flush();
            } catch (IOException e1) {
                System.out.println(e1.toString());
            }
            this.dispose();
        }
    }

    public void loadData() {
        for (int i = 0; i < chatFrame.getFriendList().size(); i++) {
            if (chatFrame.getFriendList().get(i).count <= 2 && chatFrame.getFriendList().get(i).isgroup == 0) {
                CreateGroupPanel groupPanel = new CreateGroupPanel(chatFrame.getFriendList().get(i).getJlbName().getText());
                createGroupModel.addElement(groupPanel);
                createGroupList.add(groupPanel);
            }
        }
        jlsCreateGroup.repaint();
    }

    public class CreateGroupPanel extends JPanel {

        private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default-small.png")));
        private JLabel jlbName = new JLabel();
        private JRadioButton jrbSelect = new JRadioButton();
        private String name;

        public CreateGroupPanel(String name) {
            this.name = name;
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            setBackground(Color.white);
            setLayout(new BorderLayout());

            jlbHead.setBorder(new EmptyBorder(5, 5, 5, 10));
            jlbName.setText(name);
            jlbName.setFont(new Font("helvitica", Font.BOLD, 12));
            jrbSelect.setBorder(new EmptyBorder(0, 0, 0, 15));
            add(jlbHead, BorderLayout.WEST);
            add(jlbName, BorderLayout.CENTER);
            add(jrbSelect, BorderLayout.EAST);
        }
    }

    public class ChoosePanel extends JPanel implements ActionListener {

        private JLabel jlbName = new JLabel();
        private JButton jbtCancel = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/cancel.png")));
        private String name;

        public ChoosePanel(String name) {
            this.name = name;
            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(188, 210, 238)));
            setBackground(new Color(202, 225, 255));

            jlbName.setText(name);
            jlbName.setFont(new Font("helvitica", Font.BOLD, 11));

            jbtCancel.setBorderPainted(false);
            jbtCancel.setContentAreaFilled(false);
            add(jlbName);
            add(jbtCancel);

            jbtCancel.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == jbtCancel) {
                for (int i = 0; i < createGroupList.size(); i++) {
                    if (createGroupList.get(i).name.equals(name)) {
                        createGroupList.get(i).jrbSelect.setSelected(false);
                        break;
                    }
                }
                for (int i = 0; i < chooseList.size(); i++) {
                    if (chooseList.get(i).name.equals(name)) {
                        jpnChoose.remove(chooseList.get(i));
                        chooseList.remove(i);
                        break;
                    }
                }
                jlsCreateGroup.repaint();
                jpnChoose.revalidate();
                jpnChoose.repaint();
                if (chooseList.size() > 0 && jtfName.getText() != "") {
                    jbtAdd.setEnabled(true);
                } else {
                    jbtAdd.setEnabled(false);
                }
                if (chooseList.size() > 0) {
                    jpnChoose.remove(jlbNote);
                } else {
                    jpnChoose.add(jlbNote);
                }
            }
        }
    }

    public class CreateGroupRenderer implements ListCellRenderer {

        private MouseAdapter groupHandler;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JPanel renderer = (JPanel) value;

            return renderer;
        }

        public MouseAdapter getGroupHandler(JList list) {
            if (groupHandler == null) {
                groupHandler = new GroupHoverMouseHandler(list);
            }
            return groupHandler;
        }

        public class GroupHoverMouseHandler extends MouseAdapter {

            private final JList list;

            public GroupHoverMouseHandler(JList list) {
                this.list = list;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = list.getSelectedIndex();
                if (createGroupList.get(idx).jrbSelect.isSelected()) {
                    createGroupList.get(idx).jrbSelect.setSelected(false);
                    for (int i = 0; i < chooseList.size(); i++) {
                        if (createGroupList.get(idx).name.equals(chooseList.get(i).name)) {
                            jpnChoose.remove(chooseList.get(i));
                            chooseList.remove(i);
                            break;
                        }
                    }
                } else {
                    ChoosePanel choosePanel = new ChoosePanel(createGroupList.get(idx).name);
                    createGroupList.get(idx).jrbSelect.setSelected(true);
                    jpnChoose.add(choosePanel);
                    chooseList.add(choosePanel);
                }
                jlsCreateGroup.repaint();
                jpnChoose.revalidate();
                jpnChoose.repaint();
                if (chooseList.size() > 0 && jtfName.getText() != "") {
                    jbtAdd.setEnabled(true);
                } else {
                    jbtAdd.setEnabled(false);
                }
                if (chooseList.size() > 0) {
                    jpnChoose.remove(jlbNote);
                } else {
                    jpnChoose.add(jlbNote);
                }
            }
        }

    }
}

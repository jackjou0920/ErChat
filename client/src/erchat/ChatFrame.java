package erchat;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class ChatFrame extends JFrame implements ActionListener, MouseListener {

    private JPanel jpnLeft = new JPanel(new BorderLayout());
    private JPanel panelLeft = new JPanel();

    private JPanel jpnSetting = new JPanel(new BorderLayout());
    private JPanel jpnList = new JPanel();
    private JButton jbtFriend = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/man-user-click.png")));
    private JButton jbtChat = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/chat.png")));
    private JButton jbtNew = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/new-user.png")));
    private JButton jbtSetting = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/settings.png")));
    private JPopupMenu settingMenu = new JPopupMenu();
    private JMenuItem itGroup = new JMenuItem("Create a Group");
    private JMenuItem itSettings = new JMenuItem("Settings");
    private JMenuItem itLogout = new JMenuItem("Log out");
    private JMenuItem itExit = new JMenuItem("Quit");

    private JPanel jpnUser = new JPanel(new BorderLayout());
    private JPanel jpnUserSearch = new JPanel(new BorderLayout());
    private JLabel jlbUserSearch = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/search-small.png")));
    private JTextField jtfUserSearch = new HintTextFieldUI("Search by Display Name");
    private DefaultListModel friendModel = new DefaultListModel();
    private JList jlsUser = new JList(getFriendModel());
    private JScrollPane jspUser = new JScrollPane(getJlsUser());
    private JPanel jpnChatroom = new JPanel(new BorderLayout());
    private JPanel jpnChatSearch = new JPanel(new BorderLayout());
    private JLabel jlbChatSearch = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/search-small.png")));
    private JTextField jtfChatSearch = new HintTextFieldUI("Search for Chats and Messages");
    private DefaultListModel roomModel = new DefaultListModel();
    private JList jlsChatroom = new JList(getRoomModel());
    private JScrollPane jspChatroom = new JScrollPane(getJlsChatroom());

    private JPanel jpnNew = new JPanel(new BorderLayout());
    private JPanel jpnTwo = new JPanel(new GridLayout(2, 1));
    private JPanel jpnSearch = new JPanel(new BorderLayout());
    private JPanel jpnGroup = new JPanel(new BorderLayout());
    private JLabel jlbSearch = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/search-friend.png")));
    private JLabel jlbSearchFriend = new JLabel("Search for Friends");
    private JLabel jlbAdd = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/add-friend.png")));
    private JLabel jlbAddFriend = new JLabel("Create a Group");
    private DefaultListModel newModel = new DefaultListModel();
    private JList jlsNew = new JList(getNewModel());
    private JScrollPane jspNew = new JScrollPane(getJlsNew());

    private JPanel panelRight = new JPanel();
    private JPanel jpnRight = new JPanel(new BorderLayout());

    private JLabel jlbLogo = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/ErChat_start.png")));
    //private JLabel jlbStart = new JLabel("Start a new conversation !");
    private JPanel jpnStart = new JPanel();

    private JPanel jpnCreate = new JPanel();
    private JPanel jpnCreateCenter = new JPanel();
    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default-big.png")));
    private JLabel jlbUser = new JLabel();
    private JButton jbtCreate = new JButton("Chat");

    private JPanel jpnContent = new JPanel(new BorderLayout());
    private JPanel jpnTitle = new JPanel(new BorderLayout());
    private JPanel jpnChatTitle = new JPanel(new BorderLayout());
    private JLabel jlbChatTitle = new JLabel();
    private JPanel jpnNewGroup = new JPanel();
    private JButton jbtDecline = new JButton("Decline");
    private JButton jbtJoin = new JButton("Join");
    private DefaultListModel messageModel = new DefaultListModel();
    private JList jlsContent = new JList(messageModel);
    private JScrollPane jspContent = new JScrollPane(getJlsContent());

    private JPanel jpnMessage = new JPanel(new BorderLayout());
    private JPanel jpnFunction = new JPanel();
    private JButton jbtFile = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/folder.png")));
    private JButton jbtImage = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/picture.png")));
    private JButton jbtRecord = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/microphone.png")));
    private JPanel jpnChat = new JPanel();
    private JTextArea jtaMsg = new JTextArea();
    private JScrollPane jspMsg = new JScrollPane(jtaMsg);
    private JPanel jpnRecord = new JPanel();
    private JButton jbtStartRecord = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/mic_record.png")));
    private JFileChooser chooser = new JFileChooser();

    private JSplitPane jspRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jpnContent, jpnMessage);
    private JSplitPane jspCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpnLeft, getPanelRight());
    private CardLayout changePanel = new CardLayout();
    private CardLayout startPanel = new CardLayout();
    private CardLayout messagePanel = new CardLayout();
    private PanelRenderer render = new PanelRenderer(this);
    private NewFriendFrame newFriend = new NewFriendFrame(this);
    
    ArrayList<MessageList> messageList = new ArrayList<MessageList>();
    private ArrayList<Long> groupList = new ArrayList<Long>();
    private ArrayList<FriendPanel> friendList = new ArrayList<FriendPanel>();
    private ArrayList<RoomPanel> roomList = new ArrayList<RoomPanel>();
    private ArrayList<MessagePanel> nosendMessage = new ArrayList<MessagePanel>();
    private ArrayList<FilePanel> nofileMessage = new ArrayList<FilePanel>();
    private ArrayList<RecordPanel> norecordMessage = new ArrayList<RecordPanel>();
    private ArrayList<Long> noimageMessage = new ArrayList<Long>();

    private MessageThread messageThread;
    private ErChat login;
    private Socket socket;
    private long user_id;
    private String username;
    private String showname;
    private BufferedWriter bw;
    private BufferedReader br;
    private Connection con = null;
    private Statement stat = null;
    private ResultSet resu = null;
    private boolean isConnect = false;
    private long chatroom = 0;
    private String chatname = "";
    private boolean isGroup = false;
    private boolean isRecord = false;
    private boolean reload = false;
    private boolean isStart = false;
    private String startChat = "";
    private String imageInfo = "";

    //private Recorder recorder = new Recorder();
    private SoundRecorder recorder = new SoundRecorder();
    private AudioPlayer player = new AudioPlayer();
    private Thread playThread;
    private long recordId = 0;
    private String recordName = "";
    private RecordTimer timer;
    private boolean isPlaying = false;

    public ChatFrame(ErChat login, Socket socket, Connection con, Statement stat, ResultSet resu, long user_id,
            String username, String showname, BufferedWriter bw, BufferedReader br) {
        this.login = login;
        this.messageThread = messageThread;
        this.socket = socket;
        this.con = con;
        this.stat = stat;
        this.resu = resu;
        this.user_id = user_id;
        this.username = username;
        this.showname = showname;
        this.bw = bw;
        this.br = br;

        /*
         * ChatFrame GUI Start
         */
        setLayout(new BorderLayout());
        setSize(835, 745);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(false);
        add(jspCenter, BorderLayout.CENTER);

        jspCenter.setDividerSize(2);
        jspCenter.setDividerLocation(395);

        // Left Side
        // jpnSetting Panel
        jpnList.setLayout(new BoxLayout(jpnList, BoxLayout.Y_AXIS));
        jbtFriend.setBorderPainted(false);
        jbtFriend.setContentAreaFilled(false);
        jbtFriend.setBorder(new EmptyBorder(30, 15, 0, 0));
        jbtChat.setBorderPainted(false);
        jbtChat.setContentAreaFilled(false);
        jbtChat.setBorder(new EmptyBorder(30, 15, 0, 0));
        jbtNew.setBorderPainted(false);
        jbtNew.setContentAreaFilled(false);
        jbtNew.setBorder(new EmptyBorder(30, 15, 0, 0));
        jpnList.setBackground(new Color(16, 48, 80));
        jpnList.add(jbtFriend);
        jpnList.add(jbtChat);
        jpnList.add(jbtNew);
        jbtSetting.setBorderPainted(false);
        jbtSetting.setContentAreaFilled(false);
        jbtSetting.setBorder(new EmptyBorder(0, 0, 30, 0));
        jpnSetting.setPreferredSize(new Dimension(53, 740));
        jpnSetting.setBackground(new Color(16, 48, 80));
        jpnSetting.add(jbtSetting, BorderLayout.SOUTH);
        jpnSetting.add(jpnList, BorderLayout.NORTH);
        settingMenu.add(itGroup);
        settingMenu.add(itSettings);
        settingMenu.addSeparator();
        settingMenu.add(itLogout);
        settingMenu.add(itExit);

        jspUser.setBorder(null);
        jlsUser.setBorder(null);
        jtfUserSearch.setBorder(null);
        jlbUserSearch.setBorder(new EmptyBorder(0, 7, 0, 7));
        jpnUserSearch.add(jlbUserSearch, BorderLayout.WEST);
        jpnUserSearch.add(jtfUserSearch, BorderLayout.CENTER);
        jpnUserSearch.setPreferredSize(new Dimension(0, 45));
        jpnUserSearch.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        jpnUserSearch.setBackground(Color.white);
        jpnUser.add(jpnUserSearch, BorderLayout.NORTH);
        jpnUser.add(jspUser, BorderLayout.CENTER);
        jspChatroom.setBorder(null);
        jlsChatroom.setBorder(null);
        jtfChatSearch.setBorder(null);
        jlbChatSearch.setBorder(new EmptyBorder(0, 7, 0, 7));
        jpnChatSearch.add(jlbChatSearch, BorderLayout.WEST);
        jpnChatSearch.add(jtfChatSearch, BorderLayout.CENTER);
        jpnChatSearch.setPreferredSize(new Dimension(0, 45));
        jpnChatSearch.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        jpnChatSearch.setBackground(Color.white);
        jpnChatroom.add(jpnChatSearch, BorderLayout.NORTH);
        jpnChatroom.add(jspChatroom, BorderLayout.CENTER);

        jlbSearch.setBorder(new EmptyBorder(10, 10, 10, 10));
        jlbSearchFriend.setFont(new Font("helvitica", Font.BOLD, 13));
        jpnSearch.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
        jpnSearch.setBackground(new Color(244, 248, 255));
        jpnSearch.add(jlbSearch, BorderLayout.WEST);
        jpnSearch.add(jlbSearchFriend, BorderLayout.CENTER);
        jlbAdd.setBorder(new EmptyBorder(10, 10, 10, 10));
        jlbAddFriend.setFont(new Font("helvitica", Font.BOLD, 13));
        jpnGroup.setBackground(new Color(244, 248, 255));
        jpnGroup.add(jlbAdd, BorderLayout.WEST);
        jpnGroup.add(jlbAddFriend, BorderLayout.CENTER);
        jpnTwo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        jpnTwo.add(jpnSearch);
        jpnTwo.add(jpnGroup);
        jlsNew.setBorder(null);
        jspNew.setBorder(null);
        jpnNew.setBackground(Color.white);
        jpnNew.add(jpnTwo, BorderLayout.NORTH);
        jpnNew.add(jspNew, BorderLayout.CENTER);

        // panel Panel
        panelLeft.setLayout(changePanel);
        panelLeft.add(jpnUser, "user");
        panelLeft.add(jpnChatroom, "room");
        panelLeft.add(jpnNew, "new");
        changePanel.show(panelLeft, "user");

        jlsUser.setCellRenderer(render);
        jlsChatroom.setCellRenderer(render);
        jlsNew.setCellRenderer(render);
        jpnLeft.add(jpnSetting, BorderLayout.WEST);
        jpnLeft.add(panelLeft, BorderLayout.CENTER);

        // Right Side
        panelRight.setLayout(startPanel);
        panelRight.setBackground(Color.BLACK);
        panelRight.add(jpnRight, "chat");
        panelRight.add(jpnCreate, "create");
        panelRight.add(jpnStart, "start");
        startPanel.show(panelRight, "start");
        jpnRight.setBorder(null);

        jpnStart.setBackground(Color.white);
        //jpnStart.setLayout(new BorderLayout());
        jpnStart.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
        jlbLogo.setBorder(new EmptyBorder(270, 0, 0, 0));
        //jlbStart.setFont(new Font("helvitica", Font.BOLD, 15));
        //jlbStart.setBorder(new EmptyBorder(0, 110, 300, 0));
        jpnStart.add(jlbLogo);
        //jpnStart.add(jlbStart, BorderLayout.SOUTH);
        jpnCreate.setBackground(Color.white);
        jpnCreate.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
        jpnCreate.setLayout(null);
        jlbUser.setFont(new Font("helvitica", Font.BOLD, 13));
        jbtCreate.setForeground(Color.white);
        jbtCreate.setBackground(new Color(0, 205, 102));
        jbtCreate.setOpaque(true);
        jbtCreate.setBorderPainted(false);
        jbtCreate.setPreferredSize(new Dimension(150, 35));
        jbtCreate.setEnabled(false);
        jpnCreateCenter.setLayout(new BorderLayout());
        
        jpnCreateCenter.setBackground(Color.white);
        jlbUser.setBorder(new EmptyBorder(0, 25, 15, 0));
        jpnCreateCenter.add(jlbHead, BorderLayout.NORTH);
        
        jpnCreateCenter.add(jlbUser, BorderLayout.CENTER);
        jpnCreateCenter.add(jbtCreate, BorderLayout.SOUTH);
        jpnCreateCenter.setBounds(150, 250, 130, 160);
        jpnCreate.add(jpnCreateCenter);

        jspRight.setDividerSize(3);
        jspRight.setDividerLocation(570);

        // jpnContent Panel
        jlbChatTitle.setFont(new Font("helvitica", Font.BOLD, 13));
        jlbChatTitle.setBorder(new EmptyBorder(0, 10, 0, 0));
        jpnChatTitle.setPreferredSize(new Dimension(0, 43));
        jpnChatTitle.setBackground(new Color(244, 248, 255));
        jpnChatTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        jpnChatTitle.add(jlbChatTitle);
        jpnNewGroup.setPreferredSize(new Dimension(0, 53));
        jpnNewGroup.setBackground(new Color(247, 255, 255));
        jpnNewGroup.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        jpnNewGroup.add(jbtDecline);
        jpnNewGroup.add(jbtJoin);
        jbtDecline.setFont(new Font("helvitica", Font.BOLD, 13));
        jbtDecline.setForeground(new Color(115, 115, 115));
        jbtDecline.setPreferredSize(new Dimension(100, 42));
        jbtJoin.setFont(new Font("helvitica", Font.BOLD, 13));
        jbtJoin.setPreferredSize(new Dimension(100, 38));
        jbtJoin.setForeground(Color.white);
        jbtJoin.setBackground(new Color(0, 205, 102));
        jbtJoin.setOpaque(true);
        jbtJoin.setBorderPainted(false);
        jpnTitle.add(jpnChatTitle, BorderLayout.NORTH);

        jpnContent.setBackground(Color.white);
        jpnContent.add(jpnTitle, BorderLayout.NORTH);
        jpnContent.add(jspContent, BorderLayout.CENTER);
        jspContent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        jlsContent.setBackground(Color.white);
        jlsContent.setCellRenderer(render);
        jlsContent.setBorder(null);

        // jpnMessage Panel
        jpnFunction.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
        jpnFunction.setPreferredSize(new Dimension(440, 33));
        jpnFunction.setBackground(new Color(244, 244, 244));
        jpnFunction.setLayout(new BoxLayout(jpnFunction, BoxLayout.X_AXIS));
        jbtFile.setBorderPainted(false);
        jbtFile.setContentAreaFilled(false);
        jbtImage.setBorderPainted(false);
        jbtImage.setContentAreaFilled(false);
        jbtRecord.setBorderPainted(false);
        jbtRecord.setContentAreaFilled(false);
        jpnFunction.add(jbtFile);
        jpnFunction.add(jbtImage);
        jpnFunction.add(jbtRecord);

        jpnChat.setLayout(messagePanel);
        jpnChat.add(jtaMsg, "message");
        jpnChat.add(jpnRecord, "record");
        messagePanel.show(jpnChat, "message");
        
        jbtStartRecord.setBorderPainted(false);
        jbtStartRecord.setContentAreaFilled(false);
        jtaMsg.setLineWrap(true);
        jtaMsg.setWrapStyleWord(true);
        jspMsg.setBorder(null);
        jpnRecord.add(jbtStartRecord);
        jpnMessage.add(jpnFunction, BorderLayout.NORTH);
        jpnMessage.add(jpnChat, BorderLayout.CENTER);
        jpnRight.add(jspRight, BorderLayout.CENTER);
        /*
	 * ChatFrame GUI End
         */

        jlsUser.addMouseListener(render.getFriendHandler(jlsUser));
        jlsUser.addMouseMotionListener(render.getFriendHandler(jlsUser));
        jlsChatroom.addMouseListener(render.getRoomHandler(jlsChatroom));
        jlsChatroom.addMouseMotionListener(render.getRoomHandler(jlsChatroom));
        jlsNew.addMouseListener(render.getNewHandler(jlsNew));
        jlsNew.addMouseMotionListener(render.getNewHandler(jlsNew));
        jlsContent.addMouseListener(render.getMessageHandler(jlsContent));
        jlsContent.addMouseMotionListener(render.getMessageHandler(jlsContent));
        jbtFriend.addActionListener(this);
        jbtChat.addActionListener(this);
        jbtNew.addActionListener(this);
        jbtSetting.addActionListener(this);
        jbtCreate.addActionListener(this);
        jbtDecline.addActionListener(this);
        jbtJoin.addActionListener(this);
        jbtFile.addActionListener(this);
        jbtImage.addActionListener(this);
        jbtRecord.addActionListener(this);
        jbtStartRecord.addActionListener(this);
        itGroup.addActionListener(this);
        itSettings.addActionListener(this);
        itLogout.addActionListener(this);
        itExit.addActionListener(this);    

        jspCenter.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(jspCenter.getLeftComponent().getWidth());
                        for (int i = 0; i < roomList.size(); i++) {
                            roomList.get(i).setWidth(jspCenter.getLeftComponent().getWidth() - 53);
                            roomList.get(i).last();
                            jlsChatroom.repaint();
                        }
                    }
                });
            }
        });

        jpnSearch.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                newFriend.setVisible(true);
                newFriend.setLocationRelativeTo(null);
            }
        });

        jpnGroup.addMouseListener(this);

        jbtSetting.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                settingMenu.show(e.getComponent(), jbtSetting.getLocation().x + 38, jbtSetting.getLocation().y - 765);
            }
        });

        
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        KeyStroke newline = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK);
        InputMap inputMap = jtaMsg.getInputMap();
        inputMap.put(enter, "SEND");
        inputMap.put(newline, "NEWLINE");
        ActionMap actionMap = jtaMsg.getActionMap();

        actionMap.put("SEND", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = jtaMsg.getText();
                jtaMsg.setText("");
                if (msg == null || msg.equals("")) {
                    return;
                }
                long id = System.currentTimeMillis();
                boolean group = false;
                for (int i = 0; i < groupList.size(); i++) {
                    if (chatroom == groupList.get(i)) {
                        group = true;
                        break;
                    }
                }
                showMessage(true, true, group, getShowname(), id, 0, msg);
                try {
                    bw.write("MESSAGE$" + chatroom + "$" + chatname + "$" + id + "$" + msg + "\n");
                    bw.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                jtaMsg.requestFocus();
            }
        });

        actionMap.put("NEWLINE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jtaMsg.getText();
                jtaMsg.setText(str += "\n");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (getIsConnect()) {
                    try {
                        bw.write("LOGOUT\n");
                        bw.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    friendModel.clear();
                    friendList.clear();
                    roomModel.clear();
                    roomList.clear();
                    groupList.clear();
                    
                    try {
                        bw.write("CLOSE\n");
                        bw.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        setIsConnect(true);
        MessageThread messageThread = new MessageThread(this, login, con, stat, resu, br, bw);
        messageThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbtFriend) {
            jbtChat.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/chat.png")));
            jbtFriend.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/man-user-click.png")));
            jbtNew.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/new-user.png")));
            changePanel.show(panelLeft, "user");
        } else if (e.getSource() == jbtChat) {
            jbtFriend.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/man-user.png")));
            jbtChat.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/chat-click.png")));
            jbtNew.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/new-user.png")));
            changePanel.show(panelLeft, "room");
        } else if (e.getSource() == jbtNew) {
            jbtNew.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/new-user-click.png")));
            jbtFriend.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/man-user.png")));
            jbtChat.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/chat.png")));
            changePanel.show(panelLeft, "new");
        } else if (e.getSource() == jbtCreate) {
            messageModel.removeAllElements();
            long room_id = System.currentTimeMillis();
            RoomPanel jpnRoom = new RoomPanel(room_id, startChat, 2, 0, 0, "");
            roomModel.addElement(jpnRoom);
            roomList.add(jpnRoom);
            chatroom = room_id;
            chatname = startChat;
            jlbChatTitle.setText(chatname);
            startPanel.show(panelRight, "chat");
            try {
                bw.write("NEWROOM$" + room_id + "$" + startChat + "$" + user_id + "$0$0\n");
                bw.flush();
            } catch (IOException e1) {
                System.out.println(e1.toString());
            }

        } else if (e.getSource() == jbtDecline) {
            jpnTitle.remove(jpnNewGroup);
            jpnContent.revalidate();
            jpnContent.repaint();
            for (int i = 0; i < friendList.size(); i++) {
                if (friendList.get(i).id == chatroom) {
                    friendList.remove(i);
                    friendModel.remove(i);
                    break;
                }
            }
            for (int i = 0; i < roomList.size(); i++) {
                if (roomList.get(i).getId() == chatroom) {
                    roomList.remove(i);
                    roomModel.remove(i);
                    break;
                }
            }
            jlsUser.repaint();
            jlsChatroom.repaint();
            panelRight.add(jpnStart, "start");
        } else if (e.getSource() == jbtJoin) {
            jpnTitle.remove(jpnNewGroup);
            jpnContent.revalidate();
            jpnContent.repaint();
            for (int i = 0; i < roomList.size(); i++) {
                if (roomList.get(i).getId() == chatroom) {
                    roomList.get(i).setMessage("");
                    roomList.get(i).last();
                    roomList.get(i).getJpnRight().remove(roomList.get(i).getJpnNewGroup());
                    roomList.get(i).getJpnRight().add(roomList.get(i).getJpnNoRead(), BorderLayout.CENTER);
                    roomList.get(i).getJpnRight().revalidate();
                    roomList.get(i).getJpnRight().repaint();
                    break;
                }
            }
            jlsChatroom.repaint();
            try {
                bw.write("NEWROOM$" + chatroom + "$" + chatname + "$" + user_id + "$1$0\n");
                bw.flush();
            } catch (IOException e1) {
                System.out.println(e1.toString());
            }
        } else if (e.getSource() == jbtFile) {
            chooser.setMultiSelectionEnabled(false);
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = new File(chooser.getSelectedFile().getPath());
                long id = System.currentTimeMillis();
                String filepath = file.getAbsolutePath();
                String filename = file.getName();
                double fileSize = file.length();
                String filesize = "";

                DecimalFormat df = new DecimalFormat("##.00");
                if (fileSize >= 1024) {
                    fileSize = fileSize / 1024;
                    if (fileSize >= 1024) {
                        fileSize = fileSize / 1024;
                        if (fileSize >= 1024) {
                            fileSize = fileSize / 1024;
                            fileSize = Double.parseDouble(df.format(fileSize));
                            filesize = String.valueOf(fileSize) + "GB";
                        } else {
                            fileSize = Double.parseDouble(df.format(fileSize));
                            filesize = String.valueOf(fileSize) + "MB";
                        }
                    } else {
                        fileSize = Double.parseDouble(df.format(fileSize));
                        filesize = String.valueOf(fileSize) + "KB";
                    }
                } else {
                    fileSize = Double.parseDouble(df.format(fileSize));
                    filesize = String.valueOf(fileSize) + "BYTE";
                }
                System.out.println("filesize : " + filesize);

                boolean group = false;
                for (int i = 0; i < groupList.size(); i++) {
                    if (chatroom == groupList.get(i)) {
                        group = true;
                        break;
                    }
                }
                showMessage(true, true, group, getShowname(), id, 1, filename + "$" + filesize);
                try {
                    bw.write("SENDFILE$" + chatroom + "$" + chatname + "$" + id + "$1$" + filepath + "$" + filename
                            + "$" + filesize + "\n");
                    bw.flush();
                } catch (Exception e1) {
                    System.out.println(e1.toString());
                }
            }
        } else if (e.getSource() == jbtImage) {
            chooser.setMultiSelectionEnabled(false);
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = new File(chooser.getSelectedFile().getPath());
                long id = System.currentTimeMillis();
                String filepath = file.getAbsolutePath();
                String filename = file.getName();
                noimageMessage.add(id);

                try {
                    bw.write("SENDIMAGE$" + chatroom + "$" + chatname + "$" + id + "$" + filepath + "$" + filename + "\n");
                    bw.flush();
                } catch (Exception e1) {
                    System.out.println(e1.toString());
                }
            }
        } else if (e.getSource() == jbtRecord) {
            if (isRecord) {
                messagePanel.show(jpnChat, "message");
                isRecord = false;
            } else {
                messagePanel.show(jpnChat, "record");
                isRecord = true;
            }
        } else if (e.getSource() == jbtStartRecord) {
            if (isStart) {
                isStart = false;
                jbtStartRecord.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/mic_record.png")));
                recorder.finish();
                timer.cancel();

                String filepath = "./File/" + recordName;
                String spendTime = timer.getSpendTime();
                System.out.println("spendTime : " + spendTime);

                showMessage(true, true, isGroup, showname, recordId, 3, recordName + "$" + spendTime);
                try {
                    bw.write("SENDFILE$" + chatroom + "$" + chatname + "$" + recordId + "$3$" + filepath + "$"
                            + recordName + "$" + spendTime + "\n");
                    bw.flush();
                } catch (Exception e1) {
                    System.out.println(e1.toString());
                }
                messagePanel.show(jpnChat, "message");
            } else {
                isStart = true;
                jbtStartRecord.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/mic_record_click.png")));
                recordId = System.currentTimeMillis();
                recordName = recordId + ".wav";
                startRecording(recordName);
            }
        } else if (e.getSource() == itGroup) {
            new CreateGroupFrame(this);
        } else if (e.getSource() == itSettings) {
            new SettingsFrame(getUsername(), getShowname());
        } else if (e.getSource() == itLogout) {
            try {
                bw.write("LOGOUT\n");
                bw.flush();
            } catch (IOException e1) {
                System.out.println(e1.toString());
            }

            friendModel.clear();
            friendList.clear();
            roomModel.clear();
            roomList.clear();
            groupList.clear();
            changePanel.show(panelLeft, "user");
            startPanel.show(panelRight, "start");

            login.getJtfUsername().setText(login.lastLogin());
            login.getJpfPassword().setText("");
            login.setVisible(true);
            setVisible(false);
        } else if (e.getSource() == itExit) {
            if (getIsConnect()) {
                try {
                    bw.write("LOGOUT\n");
                    bw.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                friendModel.clear();
                friendList.clear();
                roomModel.clear();
                roomList.clear();
                groupList.clear();

                try {
                    bw.write("CLOSE\n");
                    bw.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.exit(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        new CreateGroupFrame(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void loadRecord(long user_id, long last) {
        try {
            getBw().write("LOADRECORD$" + user_id + "$" + last + "\n");
            getBw().flush();

            String msg = br.readLine();
            System.out.println("[Client] LOADRECORD...");
            StringTokenizer st = new StringTokenizer(msg, "$");

            while (st.hasMoreTokens()) {
                long room_id = Long.parseLong(st.nextToken());
                String sender = st.nextToken();
                long send_time = Long.parseLong(st.nextToken());
                int type = Integer.parseInt(st.nextToken());
                String message = st.nextToken();

                if (type == 1) {
                    String filename = message;
                    String filesize = st.nextToken();
                    message = filename + "$" + filesize;
                } else if (type == 2) {
                    String filename = "archive" + message;
                    String filepath = "./File/" + filename;
                    FileSocket fileSocket = new FileSocket(this, "RECEIVEFILE", filepath, filename, 1);
                    fileSocket.start();
                } else if (type == 3) {
                    String filename = message;
                    String filepath = "./File/" + filename;
                    String spendTime = st.nextToken();
                    message = filename + "$" + spendTime;
                    FileSocket fileSocket = new FileSocket(this, "RECEIVEFILE", filepath, filename, 1);
                    fileSocket.start();
                }
                stat = con.createStatement();
                stat.executeUpdate("INSERT INTO CLIENT(room_id, sender, time, type, message) " 
                            + "VALUES('" + room_id + "','" + sender + "','" + send_time + "','" + type + "','" + message
                            + "')");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
                if (resu != null) {
                    resu.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    public void loadRoom(long user_id) {
        try {
            getBw().write("LOADROOM$" + user_id + "\n");
            getBw().flush();

            String msg = br.readLine();
            System.out.println("[Client] LOADROOM...");
            StringTokenizer st = new StringTokenizer(msg, "$");

            while (st.hasMoreTokens()) {
                long room_id = Long.parseLong(st.nextToken());
                String name = st.nextToken();
                int count = Integer.parseInt(st.nextToken());
                int isgroup = Integer.parseInt(st.nextToken());
                System.out.print(">>> ");
                System.out.print("Id:" + room_id + " Name:" + name + " ");
                if (isgroup == 1) {
                    FriendPanel jpnFriend = new FriendPanel(room_id, name, count, isgroup);
                    getFriendModel().addElement(jpnFriend);
                    getFriendList().add(jpnFriend);
                    getGroupList().add(room_id);
                }

                stat = con.createStatement();
                resu = stat.executeQuery("SELECT sender, time, type, message FROM CLIENT WHERE room_id = " + room_id
                        + " ORDER BY id DESC LIMIT 1");
                long last = 0;
                String lastMsg = "";
                int type = -1;
                while (resu.next()) {
                    last = resu.getLong("time");
                    lastMsg = resu.getString("message");
                    type = resu.getInt("type");
                }
                if (type == 1) {
                    StringTokenizer token = new StringTokenizer(lastMsg, "$");
                    String filename = token.nextToken();
                    lastMsg = filename;
                } else if (type == 3) {
                    StringTokenizer token = new StringTokenizer(lastMsg, "$");
                    String filename = token.nextToken();
                    String spendTime = token.nextToken();
                    lastMsg = spendTime;
                }
                System.out.println("Last time:" + last + " Last msg:" + lastMsg);
                RoomPanel jpnRoom = new RoomPanel(room_id, name, count, isgroup, last, lastMsg);
                getRoomModel().addElement(jpnRoom);
                getRoomList().add(jpnRoom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
                if (resu != null) {
                    resu.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void loadFriend(long user_id) {
        try {
            getBw().write("LOADFRIEND$" + user_id + "\n");
            getBw().flush();

            String msg = br.readLine();
            StringTokenizer st = new StringTokenizer(msg, "$");
            System.out.println("[Client] LOADFRIEND...");
            while (st.hasMoreTokens()) {
                long id = Long.parseLong(st.nextToken());
                String name = st.nextToken();
                System.out.print(">>> ");
                System.out.println("Id:" + id + " Name:" + name + " ");
                FriendPanel jpnFriend = new FriendPanel(id, name, 2, 0);
                getFriendModel().addElement(jpnFriend);
                getFriendList().add(jpnFriend);
            }
            jbtChat.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/chat.png")));
            jbtFriend.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/man-user-click.png")));
            jbtNew.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/new-user.png")));
            changePanel.show(panelLeft, "user");
        } catch (Exception e1) {
            System.out.println(e1.toString());
        }
    }

    public void loadAddFriend(long user_id) {
        try {
            getBw().write("LOADADDFRIEND$" + user_id + "\n");
            getBw().flush();

            String msg = br.readLine();
            System.out.println("[Client] LOADADDFRIEND...");
            StringTokenizer st = new StringTokenizer(msg, "$");
            while (st.hasMoreTokens()) {
                long request_id = Long.parseLong(st.nextToken());
                String request_name = st.nextToken();
                System.out.print(">>> ");
                System.out.println("Id:" + request_id + " Name:" + request_name + " ");
                NewFriendPanel newfriend = new NewFriendPanel(request_id, request_name);
                getNewModel().addElement(newfriend);
                getJlsNew().repaint();
            }
        } catch (Exception e1) {
            System.out.println(e1.toString());
        }
    }

    public void loadMessage(long room_id) {
        messageModel.removeAllElements();
        messageList.clear();
        try {
            stat = con.createStatement();
            resu = stat.executeQuery("SELECT * FROM CLIENT WHERE room_id = " + room_id + " ORDER BY time");
            while (resu.next()) {
                boolean me = false;
                if (resu.getString("sender").equals(getShowname())) {
                    me = true;
                }
                showMessage(false, me, getIsGroup(), resu.getString("sender"), resu.getLong("time"), resu.getInt("type"),
                        resu.getString("message"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
                if (resu != null) {
                    resu.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void showMessage(boolean send, boolean me, boolean group, String sender, long time, int type, String message) {
        if (type == 0) {
            MessagePanel msgPanel = new MessagePanel(send, me, group, getJlsContent(), getJlsContent().getWidth(), sender, time, message);
            messageModel.addElement(msgPanel);
            MessageList msgList = new MessageList(type, time, message);
            messageList.add(msgList);
            if (send) {
                getNosendMessage().add(msgPanel);
            }
        } else if (type == 1) {
            StringTokenizer token = new StringTokenizer(message, "$");
            String filename = token.nextToken();
            String filesize = token.nextToken();
            FilePanel filePanel = new FilePanel(send, me, group, getJlsContent(), getJlsContent().getWidth(), sender, time, filename, filesize);
            messageModel.addElement(filePanel);
            MessageList msgList = new MessageList(type, time, filename);
            messageList.add(msgList);
            if (send) {
                getNofileMessage().add(filePanel);
            }
        } else if (type == 2) {
            ImagePanel imagePanel = new ImagePanel(this, me, group, sender, message, "archive" + message, time);
            messageModel.addElement(imagePanel);
            MessageList msgList = new MessageList(type, time, message);
            messageList.add(msgList);
        } else if (type == 3) {
            StringTokenizer token = new StringTokenizer(message, "$");
            String filename = token.nextToken();
            String spendTime = token.nextToken();
            RecordPanel recordPanel = new RecordPanel(send, me, group, getJlsContent(), sender, time, filename, spendTime);
            messageModel.addElement(recordPanel);
            MessageList msgList = new MessageList(type, time, filename);
            messageList.add(msgList);
            if (send) {
                getNorecordMessage().add(recordPanel);
            }
        }

        getJlsContent().revalidate();
        getJlsContent().repaint();
        getJlsContent().scrollRectToVisible(new Rectangle(0, getJlsContent().getHeight() * 2, 0, 0));
    }
    
    public void removeMessage(long time, int type, String message) {
        try {
            if (type == 1) {
                stat = con.createStatement();
                resu = stat.executeQuery("SELECT message FROM CLIENT WHERE room_id = '" + getChatroom()
                        + "' AND sender = '" + getShowname() + "' AND time = '" + time + "' AND type = '" + type + "'");
                while (resu.next()) {
                    String msg = resu.getString("message");
                    StringTokenizer token = new StringTokenizer(msg, "$");
                    String filename = token.nextToken();
                    String filesize = token.nextToken();
                    if (message.equals(filename)) {
                        message = filename + "$" + filesize;
                    }
                }
            }                       
            getBw().write("REMOVE$" + getChatroom() + "$" + getShowname() + "$" + time + "$" + type + "$" + message + "\n");
            getBw().flush();
            //System.out.println("REMOVE$" + getChatroom() + "$" + getShowname() + "$" + time + "$" + type + "$" + message);
            stat = con.createStatement();
            stat.executeUpdate("DELETE FROM CLIENT WHERE room_id = '" + getChatroom() + "' AND sender = '" 
                    + getShowname() + "' AND time = '" + time + "' AND type = '" + type + "' AND message = '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eraseMessage(long time, String message) {
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).getTime() == time && messageList.get(i).getContent().equals(message)) {
                messageList.remove(i);
                messageModel.removeElementAt(i);
                break;
            }            
        }
        getJlsContent().revalidate();
        getJlsContent().repaint();
        getJlsContent().scrollRectToVisible(new Rectangle(0, getJlsContent().getHeight() * 2, 0, 0));
    }
    
    public void newGroup(long room_id, String room_name, String sender) {
        FriendPanel jpnFriend = new FriendPanel(room_id, room_name, 0, 1);
        getFriendModel().addElement(jpnFriend);
        getFriendList().add(jpnFriend);
        RoomPanel jpnRoom = new RoomPanel(room_id, room_name, 0, 1, 0, sender + " invited you to a group.");
        getRoomModel().addElement(jpnRoom);
        getRoomList().add(jpnRoom);
        getGroupList().add(room_id);
        getJlsUser().repaint();
        getJlsChatroom().repaint();
    }

    public void startRecording(String filename) {
        Thread recordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                recorder.start(filename);
            }
        });
        recordThread.start();
        timer = new RecordTimer();
        timer.start();
    }
    
    public void play() {
        try {
            FileInputStream fileInputStream = new FileInputStream("./src/resource/iphone.mp3");
            Player player = new Player(fileInputStream);
            System.out.println("Song is playing...");
            player.play();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JavaLayerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void playRecording(RecordPanel record) {
        setIsPlaying(true);
        playThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getPlayer().play("./File/" + record.getFilename());
                    setIsPlaying(false);
                    record.getChat().setImage(ImageIO.read(ChatFrame.class.getResource("/resource/play.png")));
                    record.getChat().repaint();
                    getJlsContent().repaint();
                } catch (UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        playThread.start();
    }

    public void setJlbUserLocation(String user) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        Font font = new Font("helvitica", Font.PLAIN, 13);
        
        int textwidth = (int) (font.getStringBounds(user, frc).getWidth());
        jlbUser.setBorder(new EmptyBorder(0, (jpnCreateCenter.getWidth()/2)-((textwidth/2)+2), 15, 0));
    }
    
    
    //Get and Set
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public boolean getIsConnect() {
        return isConnect;
    }

    public void setIsConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }

    public DefaultListModel getFriendModel() {
        return friendModel;
    }

    public void setFriendModel(DefaultListModel friendModel) {
        this.friendModel = friendModel;
    }

    public ArrayList<FriendPanel> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<FriendPanel> friendList) {
        this.friendList = friendList;
    }

    public DefaultListModel getRoomModel() {
        return roomModel;
    }

    public void setRoomModel(DefaultListModel roomModel) {
        this.roomModel = roomModel;
    }

    public ArrayList<RoomPanel> getRoomList() {
        return roomList;
    }

    public void setRoomList(ArrayList<RoomPanel> roomList) {
        this.roomList = roomList;
    }

    public ArrayList<Long> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<Long> groupList) {
        this.groupList = groupList;
    }

    public JList getJlsUser() {
        return jlsUser;
    }

    public void setJlsUser(JList jlsUser) {
        this.jlsUser = jlsUser;
    }

    public JList getJlsChatroom() {
        return jlsChatroom;
    }

    public void setJlsChatroom(JList jlsChatroom) {
        this.jlsChatroom = jlsChatroom;
    }

    public long getChatroom() {
        return chatroom;
    }

    public void setChatroom(long chatroom) {
        this.chatroom = chatroom;
    }

    public String getChatname() {
        return chatname;
    }
    
    public void setImageInfo(String imageInfo) {
        this.imageInfo = imageInfo;
    }

    public String getImageInfo() {
        return imageInfo;
    }

    public void setChatname(String chatname) {
        this.chatname = chatname;
    }

    public boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }
    
    public boolean getReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public JLabel getJlbChatTitle() {
        return jlbChatTitle;
    }

    public void setJlbChatTitle(JLabel jlbChatTitle) {
        this.jlbChatTitle = jlbChatTitle;
    }

    public JPanel getJpnTitle() {
        return jpnTitle;
    }

    public void setJpnTitle(JPanel jpnTitle) {
        this.jpnTitle = jpnTitle;
    }

    public JPanel getJpnNewGroup() {
        return jpnNewGroup;
    }

    public void setJpnNewGroup(JPanel jpnNewGroup) {
        this.jpnNewGroup = jpnNewGroup;
    }

    public CardLayout getStartPanel() {
        return startPanel;
    }

    public void setStartPanel(CardLayout startPanel) {
        this.startPanel = startPanel;
    }

    public JPanel getPanelRight() {
        return panelRight;
    }

    public void setPanelRight(JPanel panelRight) {
        this.panelRight = panelRight;
    }

    public JLabel getJlbUser() {
        return jlbUser;
    }

    public void setJlbUser(JLabel jlbUser) {
        this.jlbUser = jlbUser;
    }

    public String getStartChat() {
        return startChat;
    }

    public void setStartChat(String startChat) {
        this.startChat = startChat;
    }

    public JList getJlsContent() {
        return jlsContent;
    }

    public void setJlsContent(JList jlsContent) {
        this.jlsContent = jlsContent;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    public JList getJlsNew() {
        return jlsNew;
    }

    public void setJlsNew(JList jlsNew) {
        this.jlsNew = jlsNew;
    }

    public DefaultListModel getNewModel() {
        return newModel;
    }

    public void setNewModel(DefaultListModel newModel) {
        this.newModel = newModel;
    }
    
    public DefaultListModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(DefaultListModel messageModel) {
        this.messageModel = messageModel;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public void setPlayer(AudioPlayer player) {
        this.player = player;
    }
    
    public ArrayList<MessageList> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<MessageList> messageList) {
        this.messageList = messageList;
    }

    public ArrayList<MessagePanel> getNosendMessage() {
        return nosendMessage;
    }

    public void setNosendMessage(ArrayList<MessagePanel> nosendMessage) {
        this.nosendMessage = nosendMessage;
    }

    public ArrayList<FilePanel> getNofileMessage() {
        return nofileMessage;
    }

    public void setNofileMessage(ArrayList<FilePanel> nofileMessage) {
        this.nofileMessage = nofileMessage;
    }

    public ArrayList<Long> getNoimageMessage() {
        return noimageMessage;
    }

    public void setNoimageMessage(ArrayList<Long> noimageMessage) {
        this.noimageMessage = noimageMessage;
    }

    public ArrayList<RecordPanel> getNorecordMessage() {
        return norecordMessage;
    }

    public void setNorecordMessage(ArrayList<RecordPanel> norecordMessage) {
        this.norecordMessage = norecordMessage;
    }

    public JButton getJbtCreate() {
        return jbtCreate;
    }

    public void setJbtCreate(JButton jbtCreate) {
        this.jbtCreate = jbtCreate;
    }

    public ErChat getLogin() {
        return login;
    }

    public void setLogin(ErChat login) {
        this.login = login;
    }

    public NewFriendFrame getNewFriend() {
        return newFriend;
    }

    public void setNewFriend(NewFriendFrame newFriend) {
        this.newFriend = newFriend;
    }
    
}

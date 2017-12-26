package erchat;

import java.io.*;
import java.net.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;

public class ErChat extends JFrame implements ActionListener{
    
    private JPanel panel = new JPanel();
    private JPanel jpnLogin = new JPanel();
    private JPanel jpnSignup = new JPanel();
    private JLabel jlbTitle = new JLabel("ErChat");
    private JLabel jlbSignup = new JLabel("Sign Up");
    private JTextField jtfUsername = new JTextField();
    private JPasswordField jpfPassword = new JPasswordField();
    private JTextField jtfName = new HintTextFieldUI("Enter your username");
    private JTextField jtfPass = new HintTextFieldUI("Enter your password");
    private JTextArea jtaError = new JTextArea();
    private JButton jbtLogin = new JButton("Login");
    private JButton jbtChange = new JButton("Sign up");
    private JButton jbtSignup = new JButton("Sign up");
    private JButton jbtCancel = new JButton("Cancel");
    private CardLayout changePanel = new CardLayout();

    private ChatFrame chatFrame;
    private Socket socket;
    private BufferedWriter bw;
    private BufferedReader br;
    private Connection con = null;
    private Statement stat = null;
    private ResultSet resu = null;

    private long user_id = 0;
    private String username = "";
    private String showname = "";
    //private String IP = "127.0.0.1";
    private String IP = "163.21.245.147";
    //private String IP = "118.150.129.133";
    private int PORT = 6666;
    
    public ErChat() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection("jdbc:sqlite:./ErChat.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("[Client] Open database successfully...");
        createTable();

        /*
         * Client GUI start
         */
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        panel.setLayout(changePanel);
        panel.add(jpnLogin, "login");
        panel.add(jpnSignup, "signup");
        changePanel.show(panel, "login");

        // jpnLogin Panel
        jpnLogin.setLayout(null);
        jlbTitle.setFont(new Font("helvitica", Font.BOLD, 40));
        jlbTitle.setBounds(75, 50, 150, 50);
        jlbTitle.setForeground(new Color(0, 61, 122));
        getJtfUsername().setBounds(25, 145, 250, 40);
        getJpfPassword().setBounds(25, 178, 250, 40);
        jbtLogin.setBounds(25, 220, 250, 45);
        jbtChange.setBounds(25, 375, 250, 45);
        jtaError.setBounds(30, 335, 240, 35);
        jtaError.setEditable(false);
        jtaError.setForeground(Color.red);
        jtaError.setBackground(null);
        jpnLogin.add(jlbTitle);
        jpnLogin.add(getJtfUsername());
        jpnLogin.add(getJpfPassword());
        jpnLogin.add(jtaError);
        jpnLogin.add(jbtLogin);
        jpnLogin.add(jbtChange);
        jbtLogin.addActionListener(this);
        jbtChange.addActionListener(this);

        // jpnRegister Panel
        jpnSignup.setLayout(null);
        jlbSignup.setFont(new Font("helvitica", Font.BOLD, 32));
        jlbSignup.setBounds(80, 50, 150, 50);
        jlbSignup.setForeground(new Color(0, 61, 122));
        jtfName.setBounds(25, 145, 250, 40);
        jtfPass.setBounds(25, 178, 250, 40);
        jbtSignup.setBounds(175, 375, 100, 45);
        jbtCancel.setBounds(25, 375, 100, 45);
        jpnSignup.add(jlbSignup);
        jpnSignup.add(jtfName);
        jpnSignup.add(jtfPass);
        jpnSignup.add(jbtSignup);
        jpnSignup.add(jbtCancel);
        jbtSignup.addActionListener(this);
        jbtCancel.addActionListener(this);

        getJtfUsername().setText(lastLogin());
        /*
         * Client GUI end
         */

        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        InputMap inputMap = jpfPassword.getInputMap();
        inputMap.put(enter, "GO");
        ActionMap actionMap = jpfPassword.getActionMap();
        actionMap.put("GO", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String passText = new String(getJpfPassword().getPassword());
                if (getJtfUsername().getText() == "" || passText.equals("") || getJtfUsername().getText().length() > 30 || passText.length() > 30) {
                    return;
                }
                setUsername(getJtfUsername().getText());

                try {
                    bw.write("USERPASS$" + getUsername() + "$" + passText + "\n");
                    bw.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (chatFrame.getIsConnect()) {
                    try {
                        bw.write("CLOSE\n");
                        bw.flush();
                    } catch (IOException e1) {
                        System.out.println(e1.toString());
                    }
                }
            }
        });

        openConnect();
        chatFrame = new ChatFrame(this, socket, con, stat, resu, getUser_id(), getUsername(), getShowname(), bw, br);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbtLogin) {
            String passText = new String(getJpfPassword().getPassword());
            if (getJtfUsername().getText() == "" || passText.equals("") || getJtfUsername().getText().length() > 30 || passText.length() > 30) {
                return;
            }
            setUsername(getJtfUsername().getText());

            try {
                bw.write("USERPASS$" + getUsername() + "$" + passText + "\n");
                bw.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource() == jbtChange) {
            changePanel.show(panel, "signup");
        } else if (e.getSource() == jbtSignup) {
            if (!getUsername().equals("")) {
                return;
            }
            if (jtfName.getText() != "" && jtfPass.getText() != "") {
                setUsername(jtfName.getText());
                try {
                    bw.write("SIGNUP$" + System.currentTimeMillis() + "$" + getUsername() + "$" + jtfPass.getText() + "\n");
                    bw.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } else if (e.getSource() == jbtCancel) {
            changePanel.show(panel, "login");
        }
    }

    
    public void createTable() {
        try {
            stat = con.createStatement();
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS USER ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "user_id INTEGER DEFAULT NULL, "
                    + "username TEXT DEFAULT NULL, "
                    + "showname TEXT DEFAULT NULL"
                    + ");");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS CLIENT ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "room_id INTEGER DEFAULT NULL, "
                    + "sender TEXT DEFAULT NULL, "
                    + "time INTEGER DEFAULT NULL, "
                    + "type INTEGER DEFAULT NULL, "
                    + "message TEXT DEFAULT NULL"
                    + ");");
        } catch (SQLException e) {
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
                e.printStackTrace();
            }
        }
    }

    public void openConnect() {
        try {
            socket = new Socket(IP, PORT);
            System.out.println("[Client] " + socket.getInetAddress() + " has connected.");
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            //System.exit(0);
        }
    }

    public void closeConnect() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("con error");
            System.out.println(e.toString());
        }
        try {
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Client] Program Close...");
        System.exit(0);
    }

    public String lastLogin() {
        String last = "";
        try {
            stat = con.createStatement();
            resu = stat.executeQuery("SELECT username FROM USER");
            while (resu.next()) {
                last = resu.getString("username");
            }
        } catch (SQLException e) {
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
                e.printStackTrace();
            }
        }
        return last;
    }

    public long checkUser() {
        try {
            long uid = 0;
            long lastMsgTime = 0;
            stat = con.createStatement();
            resu = stat.executeQuery("SELECT user_id FROM USER");
            while (resu.next()) {
                uid = resu.getLong("user_id");
            }
            if (uid != getUser_id()) {
                stat.executeUpdate("DROP TABLE USER");
                stat.executeUpdate("DROP TABLE CLIENT");
                System.out.println("[Client] Change User...");
                createTable();
                stat.executeUpdate("INSERT INTO USER(user_id, username, showname) " + "VALUES('"
                        + getUser_id() + "','" + getUsername() + "','" + getShowname() + "')");
            } else {
                resu = stat.executeQuery("SELECT time FROM CLIENT ORDER BY id DESC LIMIT 1");
                while (resu.next()) {
                    lastMsgTime = resu.getLong("time");
                }
            }
            return lastMsgTime;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return 0;
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
    
    public void login() {    
        long last = checkUser();
        chatFrame.loadRecord(getUser_id(), last);
        chatFrame.loadRoom(getUser_id());
        chatFrame.loadFriend(getUser_id());
        chatFrame.loadAddFriend(getUser_id());
        
        try {
            bw.write("LOGIN\n");
            bw.flush();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        jtaError.setText("");
        chatFrame.setVisible(true);
        setVisible(false);
    }

    public void signup() {
        try {
            stat = con.createStatement();
            stat.executeUpdate("INSERT INTO USER(user_id, username, showname) "
                    + "VALUES('" + getUser_id() + "','" + getUsername() + "','" + getUsername() + "')");
            getJtfUsername().setText(getUsername());
            changePanel.show(panel, "login");
        } catch (Exception e1) {
            System.out.println(e1.toString());
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

    

    /*
     * GET & SET
     */
    public JPasswordField getJpfPassword() {
        return jpfPassword;
    }

    public JTextField getJtfUsername() {
        return jtfUsername;
    }

    public void setJtfUsername(JTextField jtfUsername) {
        this.jtfUsername = jtfUsername;
    }

    public void setJpfPassword(JPasswordField jpfPassword) {
        this.jpfPassword = jpfPassword;
    }
    
    public JTextArea getJtaError() {
        return jtaError;
    }

    public void setJtaError(JTextArea jtaError) {
        this.jtaError = jtaError;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

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

    public static void main(String[] args) {
        JFrame frame = new ErChat();
        frame.setSize(300, 485);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}

package erchat;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class MessageThread extends Thread {

    private ChatFrame chatFrame;
    private ErChat login;
    private Connection con;
    private Statement stat;
    private ResultSet resu;
    private BufferedReader br;
    private BufferedWriter bw;

    public MessageThread(ChatFrame chatFrame, ErChat login, Connection con, Statement stat, ResultSet resu, 
                BufferedReader br, BufferedWriter bw) {
        this.chatFrame = chatFrame;
        this.login = login;
        this.con = con;
        this.stat = stat;
	this.resu = resu;
        this.br = br;
        this.bw = bw;
        System.out.println("MessageThread Start...");
    }

    public void run() {
        String msg = "";
        
        try {
            while ((msg = br.readLine()) != null && chatFrame.getIsConnect()) {
                System.out.println(msg);
                StringTokenizer st = new StringTokenizer(msg, "$");
                String state = st.nextToken();
                System.out.println("[Server] Message : " + state);

                if (state.equals("MESSAGE")) {
                    String sender = st.nextToken();
                    long sender_id = Long.parseLong(st.nextToken());
                    long room_id = Long.parseLong(st.nextToken());
                    long send_time = Long.parseLong(st.nextToken());
                    int type = Integer.parseInt(st.nextToken());
                    String message = st.nextToken();
                    String filename = "";
                    String another = "";
                    if (type == 1 || type == 3) {
                        filename = message;
                        another = st.nextToken();
                        message = message + "$" + another;
                    }
                    System.out.println(">>> " + sender + " " + sender_id + " " + room_id + " " + message);
                    chatFrame.play();
                    
                    if (room_id == chatFrame.getChatroom()) {
                        boolean group = false, me = false;
                        chatFrame.setReload(true);
                        for (int i = 0; i < chatFrame.getGroupList().size(); i++) {
                            if (room_id == chatFrame.getGroupList().get(i)) {
                                group = true;
                                break;
                            }
                        }
                        if (sender.equals(chatFrame.getShowname())) {
                            me = true;
                        }
                        if (type != 2) {
                            chatFrame.showMessage(false, me, group, sender, send_time, type, message);
                        }
                        chatFrame.getJlsContent().scrollRectToVisible(new Rectangle(0, chatFrame.getJlsContent().getHeight() * 2, 0, 0));
                        chatFrame.getJlsContent().revalidate();
                        chatFrame.getJlsContent().repaint();
                        
                    }

                    boolean have = false;
                    for (int i = 0; i < chatFrame.getRoomList().size(); i++) {
                        if (chatFrame.getRoomList().get(i).getId() == room_id) {
                            have = true;
                            Date date = new Date(send_time);
                            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                            chatFrame.getRoomList().get(i).getJlbTime().setText(df.format(date));
                            if (type == 0) {
                                chatFrame.getRoomList().get(i).setMessage(message);
                            } else if (type == 1) {
                                chatFrame.getRoomList().get(i).setMessage(filename);
                            } else if (type == 3) {
                                chatFrame.getRoomList().get(i).setMessage(another);
                            }
                            chatFrame.getRoomList().get(i).last();
                            if (room_id != chatFrame.getChatroom()) {
                                chatFrame.getRoomList().get(i).getJpnNoRead().noRead++;
                                chatFrame.getRoomList().get(i).getJpnNoRead().repaint();
                            }
                            chatFrame.getJlsChatroom().repaint();
                            break;
                        }
                    }
                    
                    if (have) {
                        bw.write("RESPONSE$" + room_id + "$" + sender_id + "$" + send_time + "$" + type + "$" + message + "\n");
                        bw.flush();
                        stat = con.createStatement();
                        stat.executeUpdate("INSERT INTO CLIENT(room_id, sender, time, type, message) " + "VALUES('"
                                + room_id + "','" + sender + "','" + send_time + "','" + type + "','" + message + "')");
                        if (type == 3) {
                            //bw.write("SAVEFILE$./File/" + filename + "$" + filename + "\n");
                            //bw.flush();
                        }
                    } else {
                        chatFrame.newGroup(room_id, message, sender);
                        bw.write("RESPONSE$" + room_id + "$" + sender_id + "$" + send_time + "$" + type + "$NEWGROUP\n");
                        bw.flush();
                    }
                } else if (state.equals("RESPONSE")) {
                    long room_id = Long.parseLong(st.nextToken());
                    long id = Long.parseLong(st.nextToken());
                    long send_time = Long.parseLong(st.nextToken());
                    int type = Integer.parseInt(st.nextToken());
                    String message = st.nextToken();
                    String filename = "";
                    String filesize = "";
                    String spendTime = "";

                    Date date = new Date(send_time);
                    SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
                    if (type == 0) {
                        for (int i = 0; i < chatFrame.getNosendMessage().size(); i++) {
                            if (chatFrame.getNosendMessage().get(i).getTime() == id) {
                                chatFrame.getNosendMessage().get(i).setTime(send_time);
                                chatFrame.getNosendMessage().get(i).getJlbTime().setText(df.format(date));
                                chatFrame.getNosendMessage().remove(i);
                                break;
                            }
                        }
                        for (int i = 0; i < chatFrame.getMessageList().size(); i++) {
                            if (chatFrame.getMessageList().get(i).getTime() == id) {
                                chatFrame.getMessageList().get(i).setTime(send_time);
                                break;
                            }
                        }
                    } else if (type == 1) {
                        filename = message;
                        filesize = st.nextToken();
                        for (int i = 0; i < chatFrame.getNofileMessage().size(); i++) {
                            if (chatFrame.getNofileMessage().get(i).getTime() == id) {
                                chatFrame.getNofileMessage().get(i).setTime(send_time);
                                chatFrame.getNofileMessage().get(i).getJlbTime().setText(df.format(date));
                                chatFrame.getNofileMessage().remove(i);
                                break;
                            }
                        }
                        for (int i = 0; i < chatFrame.getMessageList().size(); i++) {
                            if (chatFrame.getMessageList().get(i).getTime() == id) {
                                chatFrame.getMessageList().get(i).setTime(send_time);
                                break;
                            }
                        }
                    } else if (type == 2) {
                        for (int i = 0; i < chatFrame.getNoimageMessage().size(); i++) {
                            if (chatFrame.getNoimageMessage().get(i) == id) {
                                chatFrame.getNoimageMessage().remove(i);
                                break;
                            }
                        }
                    } else if (type == 3) {
                        filename = message;
                        spendTime = st.nextToken();
                        for (int i = 0; i < chatFrame.getNorecordMessage().size(); i++) {
                            if (chatFrame.getNorecordMessage().get(i).getTime() == id) {
                                chatFrame.getNorecordMessage().get(i).setTime(send_time);
                                chatFrame.getNorecordMessage().get(i).getJlbTime().setText(df.format(date));
                                chatFrame.getNorecordMessage().remove(i);
                                break;
                            }
                        }
                        for (int i = 0; i < chatFrame.getMessageList().size(); i++) {
                            if (chatFrame.getMessageList().get(i).getTime() == id) {
                                chatFrame.getMessageList().get(i).setTime(send_time);
                                break;
                            }
                        }
                    }
                    chatFrame.getJlsContent().revalidate();
                    chatFrame.getJlsContent().repaint();

                    for (int i = 0; i < chatFrame.getRoomList().size(); i++) {
                        if (chatFrame.getRoomList().get(i).getId() == room_id) {
                            SimpleDateFormat dff = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                            chatFrame.getRoomList().get(i).getJlbTime().setText(dff.format(date));
                            if (type == 0 || type == 2) {
                                chatFrame.getRoomList().get(i).setMessage(message);
                            } else if (type == 1) {
                                chatFrame.getRoomList().get(i).setMessage(filename);
                                message = filename + "$" + filesize;
                            } else if (type == 3) {
                                chatFrame.getRoomList().get(i).setMessage(spendTime);
                                message = filename + "$" + spendTime;
                            }
                            chatFrame.getRoomList().get(i).last();
                            if (room_id != chatFrame.getChatroom()) {
                                chatFrame.getRoomList().get(i).getJpnNoRead().noRead++;
                                chatFrame.getRoomList().get(i).getJpnNoRead().repaint();
                            }
                            chatFrame.getJlsChatroom().repaint();
                            break;
                        }
                    }
                    if (type != 2) {
                        stat = con.createStatement();
                        stat.executeUpdate("INSERT INTO CLIENT(room_id, sender, time, type, message) " + "VALUES('"
                                + room_id + "','" + chatFrame.getShowname() + "','" + send_time + "','" + type + "','"
                                + message + "')");
                    }
                } else if (state.equals("LOGIN")) {
                    String result = st.nextToken();
                    System.out.println(">>> " + result);
                    if (result.equals("correct")) {
                        long id = Long.parseLong(st.nextToken());
                        String uname = st.nextToken();
                        String sname = st.nextToken();
                        System.out.println(">>> " + id + " " + uname + " " + sname);

                        //set Client info
                        login.setUser_id(id);
                        login.setUsername(uname);
                        login.setShowname(sname);

                        //set ChatFrame info
                        chatFrame.setUser_id(id);
                        chatFrame.setUsername(uname);
                        chatFrame.setShowname(sname);
                        chatFrame.setChatroom(0);
                        chatFrame.setChatname("");
                        chatFrame.setIsGroup(false);
                        chatFrame.setStartChat("");

                        login.login();
                    } else if (result.equals("incorrect")) {
                        login.getJpfPassword().setText("");
                        login.getJtaError().setText("Your password is incorrect, or you are \nnot signed up.");
                    } else {
                        login.getJpfPassword().setText("");
                        login.getJtaError().setText("Your username is not existing, please sign up.");
                    }
                } else if (state.equals("SIGNUP")) {
                    String result = st.nextToken();
                    System.out.println(">>> " + result);
                    if (result.equals("finish")) {
                        login.signup();
                    } else {
                        // System.out.println("error");
                    }
                } else if (state.equals("SENDFILE")) {
                    String filepath = st.nextToken();
                    String filename = st.nextToken();
                    int type = Integer.parseInt(st.nextToken());
                    
                    FileSocket filesocket = new FileSocket(chatFrame, "SENDFILE", filepath, filename, type);
                    filesocket.start();
                } else if (state.equals("RECEIVEFILE")) {
                    String filepath = st.nextToken();
                    String filename = st.nextToken();
                    int type = Integer.parseInt(st.nextToken());
                    
                    FileSocket filesocket = new FileSocket(chatFrame, "RECEIVEFILE", filepath, filename, type);
                    filesocket.start();
                } else if (state.equals("RECEIVEIMAGE")) {
                    long room_id = Long.parseLong(st.nextToken());
                    String sender = st.nextToken();
                    long time = Long.parseLong(st.nextToken());
                    String filepath = st.nextToken();
                    String filename = st.nextToken();
                    int type = Integer.parseInt(st.nextToken());
                    
                    boolean me = false, group = false;
                    if (sender.equals(chatFrame.getShowname())) {
                        me = true;
                    }
                    for (int i = 0; i < chatFrame.getGroupList().size(); i++) {
                        if (room_id == chatFrame.getGroupList().get(i)) {
                            group = true;
                            break;
                        }
                    }
                    String image = me + "%" + group + "%" + sender + "%" + filename + "%archive" + filename + "%" + time;
                    chatFrame.setImageInfo(image);
                    FileSocket filesocket = new FileSocket(chatFrame, "RECEIVEFILE", filepath, "archive" + filename, type);
                    filesocket.start();
                } else if (state.equals("REMOVE")) {
                    long room_id = Long.parseLong(st.nextToken());
                    String sender = st.nextToken();
                    long sender_id = Long.parseLong(st.nextToken());
                    long time = Long.parseLong(st.nextToken());
                    int type = Integer.parseInt(st.nextToken());
                    String message = st.nextToken();
                    String filename = "";
                    String filesize = "";
                    if (type == 1) {
                        filename = message;
                        filesize = st.nextToken();
                        message = message + "$" + filesize;
                    }
                    bw.write("RESPONSE$" + room_id + "$" + sender_id + "$" + time + "$" + type + "$REMOVE\n");
                    bw.flush();
                    
                    if (room_id == chatFrame.getChatroom()) {
                        if (type == 1) {
                            chatFrame.eraseMessage(time, filename);
                        } else {
                            chatFrame.eraseMessage(time, message);
                        }                                              
                    }
                    stat = con.createStatement();
                    stat.executeUpdate("DELETE FROM CLIENT WHERE room_id = '" + room_id + "' AND sender = '" + sender 
                            + "' AND time = '" + time + "' AND type = '" + type + "' AND message = '" + message + "'");
                } else if (state.equals("SEARCHFRIEND")) {
                    chatFrame.getNewFriend().getJpnNo().setVisible(false);
                    chatFrame.getNewFriend().getJpnCenter().setVisible(false);
                    String result = st.nextToken();
                    if (result.equals("NOTEXIST")) {
                        chatFrame.getNewFriend().getJpnNo().setVisible(true);
                    } else if (result.equals("SELF")) {
                        String user = st.nextToken();
                        chatFrame.getNewFriend().getJlbUser().setText(user);
                        chatFrame.getNewFriend().getJlbResult().setBounds(0, 120, 300, 30);
                        chatFrame.getNewFriend().getJlbResult().setText("You cannot add yourself as a friend");
                        chatFrame.getNewFriend().compute(user);
                        chatFrame.getNewFriend().getJbtAdd().setEnabled(false);
                        chatFrame.getNewFriend().getJpnCenter().setVisible(true);
                    } else if (result.equals("HAVE")) {
                        String user = st.nextToken();
                        chatFrame.getNewFriend().getJlbUser().setText(user);
                        chatFrame.getNewFriend().getJlbResult().setBounds(13, 120, 300, 30);
                        chatFrame.getNewFriend().getJlbResult().setText("This user is already your friend");
                        chatFrame.getNewFriend().compute(user);
                        chatFrame.getNewFriend().getJbtAdd().setEnabled(false);
                        chatFrame.getNewFriend().getJpnCenter().setVisible(true);
                    } else if (result.equals("NEW")) {
                        long id = Long.parseLong(st.nextToken());
                        String user = st.nextToken();
                        chatFrame.getNewFriend().getJlbUser().setText(user);
                        chatFrame.getNewFriend().setId(id);
                        chatFrame.getNewFriend().setUser(user);
                        chatFrame.getNewFriend().compute(user);
                        chatFrame.getNewFriend().getJpnCenter().setVisible(true);
                    }
                } else if (state.equals("FRIENDREQUEST")) {
                    long request_id = Long.parseLong(st.nextToken());
                    String request_name = st.nextToken();
                    System.out.println(">>> " + request_id + " " + request_name);
                    NewFriendPanel newfriend = new NewFriendPanel(request_id, request_name);
                    chatFrame.getNewModel().addElement(newfriend);
                    chatFrame.getJlsNew().repaint();
                } else if (state.equals("ISFRIEND")) {
                    boolean isFriend = Boolean.parseBoolean(st.nextToken());
                    System.out.println(">>> " + isFriend);
                    if (isFriend) {
                        chatFrame.getJbtCreate().setEnabled(true);
                    } else {
                        chatFrame.getJbtCreate().setEnabled(false);
                    }
                    chatFrame.getStartPanel().show(chatFrame.getPanelRight(), "create");
                } else if (state.equals("NEWROOM")) {
                    long room_id = Long.parseLong(st.nextToken());
                    String room_name = st.nextToken();
                    RoomPanel jpnRoom = new RoomPanel(room_id, room_name, 2, 0, 0, "");
                    chatFrame.getRoomModel().addElement(jpnRoom);
                    chatFrame.getRoomList().add(jpnRoom);
                    chatFrame.getJlsChatroom().repaint();
                } else if (state.equals("NEWGROUP")) {
                    long room_id = Long.parseLong(st.nextToken());
                    String room_name = st.nextToken();
                    String sender = st.nextToken();
                    chatFrame.newGroup(room_id, room_name, sender);
                } else if (state.equals("GROUPCOUNT")) {
                    long room_id = Long.parseLong(st.nextToken());
                    int count = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < chatFrame.getFriendList().size(); i++) {
                        if (chatFrame.getFriendList().get(i).id == room_id) {
                            chatFrame.getFriendList().get(i).count = count;
                            chatFrame.getFriendList().get(i).getJlbName().setText(chatFrame.getFriendList().get(i).name + "(" + count + ")");
                            break;
                        }
                    }
                    for (int i = 0; i < chatFrame.getRoomList().size(); i++) {
                        if (chatFrame.getRoomList().get(i).getId() == room_id) {
                            chatFrame.getRoomList().get(i).setCount(count);
                            chatFrame.getRoomList().get(i).getJlbName().setText(chatFrame.getRoomList().get(i).getName() + "(" + count + ")");
                            break;
                        }
                    }
                    chatFrame.getJlsUser().repaint();
                    chatFrame.getJlsChatroom().repaint();
                } else if (state.equals("CLOSE")) {
                    chatFrame.setIsConnect(false);
                    break;
                }
            }
            login.closeConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

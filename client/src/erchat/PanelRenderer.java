package erchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class PanelRenderer implements ListCellRenderer {
    
    private JPopupMenu messageMenu = new JPopupMenu();
    private JMenuItem msgDelete = new JMenuItem("Delete");
    private JPopupMenu fileMenu = new JPopupMenu();
    private JMenuItem fileDownload = new JMenuItem("Download");
    private JMenuItem fileDelete = new JMenuItem("Delete");
    private JPopupMenu imageMenu = new JPopupMenu();
    private JMenuItem imgSave = new JMenuItem("Save");
    private JMenuItem imgDelete = new JMenuItem("Delete");
    
    private MouseAdapter friendHandler, roomHandler, newHandler, messageHandler;
    private int friendIndex = -1, roomIndex = -1, newIndex = -1, messageIndex = -1, isClick = -1, isDownload = -1, isChoose = -1;
    private boolean fileClick = false, imageClick = false, messageClick = false;
    private ChatFrame chatFrame;

    public PanelRenderer(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
        
        messageMenu.add(msgDelete);        
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof FriendPanel) {
            FriendPanel friend = (FriendPanel) value;
            if (isSelected) {
                if (index == friendIndex) {
                    friend.setBackground(new Color(188, 210, 238));
                    boolean select = false;
                    for (int i = 0; i < chatFrame.getRoomList().size(); i++) {
                        if (friend.name.equals(chatFrame.getRoomList().get(i).getName())) {
                            if (chatFrame.getRoomList().get(i).getId() != chatFrame.getChatroom()) {
                                chatFrame.setChatroom(chatFrame.getRoomList().get(i).getId());
                                chatFrame.setChatname(chatFrame.getRoomList().get(i).getName());
                                for (int k = 0; k < chatFrame.getGroupList().size(); k++) {
                                    if (chatFrame.getChatroom() == chatFrame.getGroupList().get(k)) {
                                        chatFrame.setIsGroup(true);
                                        break;
                                    } else {
                                        chatFrame.setIsGroup(false);
                                    }
                                }
                                chatFrame.getJlbChatTitle().setText(chatFrame.getChatname());
                                if (chatFrame.getRoomList().get(i).getCount() == 0) {
                                    chatFrame.getJpnTitle().add(chatFrame.getJpnNewGroup(), BorderLayout.CENTER);
                                }
                                chatFrame.loadMessage(chatFrame.getChatroom());
                            }
                            chatFrame.getStartPanel().show(chatFrame.getPanelRight(), "chat");
                            chatFrame.getJlsChatroom().setSelectedIndex(i);
                            select = true;
                            break;
                        }
                    }
                    if (select == false) {
                        if (friendIndex != isChoose) {
                            isChoose = friendIndex;
                            chatFrame.getJlsChatroom().clearSelection();
                            chatFrame.getJlbUser().setText(friend.getJlbName().getText());
                            chatFrame.setJlbUserLocation(friend.getJlbName().getText());
                            chatFrame.setStartChat(friend.getJlbName().getText());
                            try {
                                chatFrame.getBw().write("ISFRIEND$" + chatFrame.getUser_id() + "$" + chatFrame.getStartChat() + "\n");
                                chatFrame.getBw().flush();
                            } catch (IOException e) {
                                System.out.println(e.toString());
                            }
                        }
                    }
                } else {
                    isChoose = -1;
                    friend.setBackground(new Color(176, 196, 222));
                }
            } else {
                friend.setBackground(index == friendIndex ? new Color(240, 248, 255) : list.getBackground());
            }
            return friend;
        } else if (value instanceof RoomPanel) {
            RoomPanel room = (RoomPanel) value;
            if (isSelected) {
                if (index == roomIndex) {
                    room.setBackground(new Color(188, 210, 238));
                    room.getJpnCenter().setBackground(new Color(188, 210, 238));
                    room.getJpnRight().setBackground(new Color(188, 210, 238));
                    room.getJpnNoRead().setBackground(new Color(188, 210, 238));
                    room.getJpnNewGroup().setBackground(new Color(188, 210, 238));
                    if (room.getId() != chatFrame.getChatroom()) {
                        chatFrame.setChatroom(room.getId());
                        chatFrame.setChatname(room.getName());
                        for (int k = 0; k < chatFrame.getGroupList().size(); k++) {
                            if (chatFrame.getChatroom() == chatFrame.getGroupList().get(k)) {
                                chatFrame.setIsGroup(true);
                                break;
                            } else {
                                chatFrame.setIsGroup(false);
                            }
                        }
                        chatFrame.getJlbChatTitle().setText(chatFrame.getChatname());
                        if (room.getCount() == 0) {
                            chatFrame.getJpnTitle().add(chatFrame.getJpnNewGroup(), BorderLayout.CENTER);
                        }
                        chatFrame.loadMessage(chatFrame.getChatroom());
                        room.getJpnNoRead().noRead = 0;
                        room.getJpnNoRead().repaint();
                    }

                    chatFrame.getStartPanel().show(chatFrame.getPanelRight(), "chat");
                    for (int i = 0; i < chatFrame.getFriendList().size(); i++) {
                        if (room.getName().equals(chatFrame.getFriendList().get(i).getName())) {
                            chatFrame.getJlsUser().setSelectedIndex(i);
                            break;
                        }
                    }
                } else {
                    room.setBackground(new Color(176, 196, 222));
                    room.getJpnCenter().setBackground(new Color(176, 196, 222));
                    room.getJpnRight().setBackground(new Color(176, 196, 222));
                    room.getJpnNoRead().setBackground(new Color(176, 196, 222));
                    room.getJpnNewGroup().setBackground(new Color(176, 196, 222));
                }
            } else {
                room.setBackground(index == roomIndex ? new Color(240, 248, 255) : list.getBackground());
                room.getJpnCenter().setBackground(index == roomIndex ? new Color(240, 248, 255) : list.getBackground());
                room.getJpnRight().setBackground(index == roomIndex ? new Color(240, 248, 255) : list.getBackground());
                room.getJpnNoRead().setBackground(index == roomIndex ? new Color(240, 248, 255) : list.getBackground());
                room.getJpnNewGroup().setBackground(index == roomIndex ? new Color(240, 248, 255) : list.getBackground());
            }
            return room;
        } else if (value instanceof NewFriendPanel) {
            NewFriendPanel newfriend = (NewFriendPanel) value;
            if (isSelected) {
                if (index == newIndex) {
                    newfriend.setBackground(new Color(188, 210, 238));
                    if (newIndex != isClick) {
                        isClick = newIndex;
                        int result = JOptionPane.showConfirmDialog(null,
                                "Are you sure you want to add " + newfriend.getJlbName().getText() + "\nas your friend ?",
                                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
                            FriendPanel jpnFriend = new FriendPanel(newfriend.getId(), newfriend.getJlbName().getText(), 2, 0);
                            chatFrame.getFriendModel().addElement(jpnFriend);
                            chatFrame.getFriendList().add(jpnFriend);
                            chatFrame.getJlsUser().repaint();
                            chatFrame.getNewModel().removeElementAt(newIndex);
                            try {
                                chatFrame.getBw().write("CONFIRMFRIEND$" + newfriend.getId() + "$" + chatFrame.getUser_id() + "\n");
                                chatFrame.getBw().flush();
                            } catch (IOException e) {
                                System.out.println(e.toString());
                            }
                        }
                        isClick = -1;
                        newIndex = -1;
                        chatFrame.getJlsNew().repaint();
                        chatFrame.getJlsNew().removeSelectionInterval(index, index);
                    }
                } else {
                    newfriend.setBackground(new Color(176, 196, 222));
                }
            } else {
                newfriend.setBackground(index == newIndex ? new Color(240, 248, 255) : list.getBackground());
            }
            return newfriend;
        } else if (value instanceof FilePanel) {
            FilePanel savefile = (FilePanel) value;

            if (isSelected) {
                if (index == messageIndex) {
                    if (messageIndex != isDownload) {
                        //System.out.println(isSelected + " " + index + " " + messageIndex);
                        isDownload = messageIndex;

                        fileMenu.add(fileDownload);
                        if (savefile.getMe()) {
                            fileMenu.add(fileDelete);
                        }
                        fileClick = true;
                        fileMenu.show(chatFrame, chatFrame.getMousePosition().x, chatFrame.getMousePosition().y);

                        fileDownload.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                if (fileClick) {
                                    fileClick = false;
                                    JFrame saveFrame = new JFrame();
                                    FileDialog saveDialog = new FileDialog(saveFrame, "", FileDialog.SAVE);
                                    saveDialog.setFile(savefile.getFilename());
                                    saveDialog.setVisible(true);
                                    String fn = saveDialog.getFile();
                                    String filename = savefile.getFilename();
                                    String dir = saveDialog.getDirectory();
                                    String filepath = dir + saveDialog.getFile();
                                    if (fn == null) {
                                        System.out.println("[Client] You cancelled the choice");
                                    } else {
                                        System.out.println("[Client] You chose " + filename);
                                        FileSocket fileSocket = new FileSocket(chatFrame, "RECEIVEFILE", filepath, filename, 1);
                                        fileSocket.start();
                                    }
                                }
                            }
                        });
                        fileDelete.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                if (fileClick) {
                                    fileClick = false;
                                    chatFrame.getMessageModel().removeElementAt(index);
                                    chatFrame.getJlsContent().revalidate();
                                    chatFrame.getJlsContent().repaint();
                                    chatFrame.removeMessage(savefile.getTime(), 1, savefile.getFilename());  
                                }
                            }
                        });

                        isDownload = -1;
                        messageIndex = -1;
                        chatFrame.getJlsContent().removeSelectionInterval(index, index);
                    }
                }
            }

            return savefile;
        } else if (value instanceof ImagePanel) {
            ImagePanel image = (ImagePanel) value;
            if (isSelected) {
                if (index == messageIndex) {
                    if (messageIndex != isDownload) {
                        isDownload = messageIndex;
                        
                        imageMenu.add(imgSave);
                        if (image.getMe()) {
                            imageMenu.add(imgDelete);
                        }
                        imageClick = true;
                        imageMenu.show(chatFrame, chatFrame.getMousePosition().x, chatFrame.getMousePosition().y);
        
                        imgSave.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                if (imageClick) {
                                    imageClick = false;
                                    JFrame saveFrame = new JFrame();
                                    FileDialog saveDialog = new FileDialog(saveFrame, "", FileDialog.SAVE);
                                    saveDialog.setFile(image.getFullname());
                                    saveDialog.setVisible(true);
                                    String fn = saveDialog.getFile();
                                    String filename = image.getFullname();
                                    String dir = saveDialog.getDirectory();
                                    String filepath = dir + saveDialog.getFile();
                                    if (fn == null) {
                                        System.out.println("[Client] You cancelled the choice");
                                    } else {
                                        System.out.println("[Client] You chose " + filename);
                                        FileSocket fileSocket = new FileSocket(chatFrame, "RECEIVEFILE", filepath, filename, 1);
                                        fileSocket.start();
                                    }
                                }
                            }
                        });
                        imgDelete.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                if (imageClick) {
                                    imageClick = false;
                                    chatFrame.getMessageModel().removeElementAt(index);
                                    chatFrame.getJlsContent().revalidate();
                                    chatFrame.getJlsContent().repaint();
                                    chatFrame.removeMessage(image.getTime(), 2, image.getFullname());  
                                }
                            }
                        });
                        
                        isDownload = -1;
                        messageIndex = -1;
                        chatFrame.getJlsContent().removeSelectionInterval(index, index);
                    }
                }
            }
            return image;
        } else if (value instanceof RecordPanel) {
            RecordPanel record = (RecordPanel) value;
            if (isSelected) {
                if (index == messageIndex) {
                    if (messageIndex != isDownload) {
                        isDownload = messageIndex;

                        try {
                            if (!chatFrame.getIsPlaying()) {
                                record.getChat().image = ImageIO.read(ChatFrame.class.getResource("/resource/stop.png"));
                                record.getChat().repaint();
                                chatFrame.playRecording(record);
                            } else {
                                chatFrame.getPlayer().playCompleted = true;
                                chatFrame.getPlayer().audioClip.stop();
                                chatFrame.setIsPlaying(false);
                                record.getChat().image = ImageIO.read(ChatFrame.class.getResource("/resource/play.png"));
                                record.getChat().repaint();
                                chatFrame.getJlsContent().repaint();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        isDownload = -1;
                        messageIndex = -1;
                        chatFrame.getJlsContent().removeSelectionInterval(index, index);
                    }
                }
            }
            return record;
        } else if (value instanceof MessagePanel) {
            MessagePanel message = (MessagePanel) value;
            if (isSelected) {
                if (index == messageIndex) {
                    if ((messageIndex != isDownload) && message.getMe()) {
                        isDownload = messageIndex;
                        
                        messageClick = true;
                        messageMenu.show(chatFrame, chatFrame.getMousePosition().x, chatFrame.getMousePosition().y);
                        
                        msgDelete.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                if (messageClick) {
                                    messageClick = false;
                                    chatFrame.getMessageModel().removeElementAt(index);
                                    chatFrame.getJlsContent().revalidate();
                                    chatFrame.getJlsContent().repaint();
                                    chatFrame.removeMessage(message.getTime(), 0, message.getContent());
                                }
                            }
                        });
                        
                        isDownload = -1;
                        messageIndex = -1;
                        chatFrame.getJlsContent().removeSelectionInterval(index, index);
                    }
                }
            }
                
            return message;
        } else {
            JPanel renderer = (JPanel) value;
            return renderer;
        }
    }

    public MouseAdapter getFriendHandler(JList list) {
        if (friendHandler == null) {
            friendHandler = new FriendHoverMouseHandler(list);
        }
        return friendHandler;
    }

    public MouseAdapter getRoomHandler(JList list) {
        if (roomHandler == null) {
            roomHandler = new RoomHoverMouseHandler(list);
        }
        return roomHandler;
    }

    public MouseAdapter getNewHandler(JList list) {
        if (newHandler == null) {
            newHandler = new NewHoverMouseHandler(list);
        }
        return newHandler;
    }

    public MouseAdapter getMessageHandler(JList list) {
        if (messageHandler == null) {
            messageHandler = new MessageHoverMouseHandler(list);
        }
        return messageHandler;
    }

    public class FriendHoverMouseHandler extends MouseAdapter {

        private final JList list;

        public FriendHoverMouseHandler(JList list) {
            this.list = list;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            friendIndex = -1;
            list.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (list.getModel().getSize() > 0) {
                int index = list.locationToIndex(e.getPoint());
                if (list.getCellBounds(index, index).contains(e.getPoint())) {
                    friendIndex = index;
                    list.repaint();
                }
            }
        }
    }

    public class RoomHoverMouseHandler extends MouseAdapter {

        private final JList list;

        public RoomHoverMouseHandler(JList list) {
            this.list = list;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            roomIndex = -1;
            list.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (list.getModel().getSize() > 0) {
                int index = list.locationToIndex(e.getPoint());
                if (list.getCellBounds(index, index).contains(e.getPoint())) {
                    roomIndex = index;
                    list.repaint();
                }
            }
        }
    }

    public class NewHoverMouseHandler extends MouseAdapter {

        private final JList list;

        public NewHoverMouseHandler(JList list) {
            this.list = list;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            newIndex = -1;
            list.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (list.getModel().getSize() > 0) {
                int index = list.locationToIndex(e.getPoint());
                if (list.getCellBounds(index, index).contains(e.getPoint())) {
                    newIndex = index;
                    list.repaint();
                }
            }
        }
    }

    public class MessageHoverMouseHandler extends MouseAdapter {

        private final JList list;

        public MessageHoverMouseHandler(JList list) {
            this.list = list;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            messageIndex = -1;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (list.getModel().getSize() > 0) {
                int index = list.locationToIndex(e.getPoint());
                if (list.getCellBounds(index, index).contains(e.getPoint())) {
                    messageIndex = index;
                    list.repaint();
                }
            }
        }
    }
}

package erchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class RecordPanel extends JPanel {

    private JLabel jlbTime = new JLabel("");
    private JPanel jpnMessage = new JPanel(new BorderLayout());
    private JPanel jpnDownload = new JPanel();
    private JLabel jlbForward = new JLabel("Forward");
    private JLabel jlbDownload = new JLabel("Download");
    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default-small.png")));
    private JLabel jlbSender = new JLabel();
    private ChatPanel chat;
    private long time;
    private String filename;
    private String spendTime;

    public RecordPanel(boolean send, boolean me, boolean group, JList jlsContent, String sender, long time, String filename,
            String spendTime) {
        this.setTime(time);
        this.setFilename(filename);
        this.spendTime = spendTime;

        Image image = null;
        try {
            image = ImageIO.read(ChatFrame.class.getResource("/resource/play.png"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        setChat(new ChatPanel(me, spendTime, image));
        getChat().setBorder(new EmptyBorder(22, 0, 22, 0));
        getChat().setPreferredSize(new Dimension(138, 58));
        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

        getJlbTime().setFont(new Font("helvitica", Font.BOLD, 11));
        getJlbTime().setForeground(new Color(170, 170, 170));
        jpnDownload.setBackground(Color.white);
        jlbForward.setForeground(Color.GRAY);
        jlbForward.setFont(new Font("helvitica", Font.BOLD, 11));
        jlbDownload.setForeground(Color.GRAY);
        jlbDownload.setFont(new Font("helvitica", Font.BOLD, 11));
        jpnMessage.setBackground(Color.white);
        setBackground(Color.white);
        if (me) {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            add(getJlbTime());
            add(jpnMessage);
            getJlbTime().setBorder(new EmptyBorder(20, 0, 0, 0));
            if (send) {
                getJlbTime().setText("");
            } else {
                getJlbTime().setText(df.format(date));
            }
            jpnDownload.setLayout(new FlowLayout(FlowLayout.RIGHT));
            jpnDownload.add(jlbForward);
            jpnDownload.add(jlbDownload);
            jpnMessage.add(getChat(), BorderLayout.CENTER);
            jpnMessage.add(jpnDownload, BorderLayout.SOUTH);
        } else {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            getJlbTime().setText(df.format(date));

            if (group) {
                getJlbTime().setBorder(new EmptyBorder(35, 0, 0, 0));
                jlbHead.setBorder(new EmptyBorder(0, 0, 48, 0));
                jlbSender.setText(sender);
                jlbSender.setFont(new Font("helvitica", Font.BOLD, 14));
                jlbSender.setForeground(new Color(132, 132, 132));
                jpnMessage.add(jlbSender, BorderLayout.NORTH);
                add(jlbHead);
            } else {
                getJlbTime().setBorder(new EmptyBorder(20, 0, 0, 0));
            }
            add(jpnMessage);
            add(getJlbTime());
            jpnDownload.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpnDownload.add(jlbDownload);
            jpnDownload.add(jlbForward);
            jpnMessage.add(getChat(), BorderLayout.CENTER);
            jpnMessage.add(jpnDownload, BorderLayout.SOUTH);
        }
        jlsContent.scrollRectToVisible(new Rectangle(0, jlsContent.getHeight() * 2, 0, 0));
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ChatPanel getChat() {
        return chat;
    }

    public void setChat(ChatPanel chat) {
        this.chat = chat;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public JLabel getJlbTime() {
        return jlbTime;
    }

    public void setJlbTime(JLabel jlbTime) {
        this.jlbTime = jlbTime;
    }

    public class ChatPanel extends JPanel {

        private JLabel jbtPlay = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/play.png")));
        private String spendTime;
        private int width;
        private boolean me;
        Image image;

        public ChatPanel(boolean me, String spendTime, Image image) {
            this.spendTime = spendTime;
            this.me = me;
            this.setImage(image);
            setBackground(Color.white);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (me) {
                g.setColor(new Color(167, 231, 151));
            } else {
                g.setColor(new Color(227, 227, 227));
            }

            g.fillRoundRect(0, 3, 135, 53, 27, 27);

            g.drawImage(getImage(), 18, 19, null);
            g.setColor(Color.black);
            g.setFont(new Font("helvitica", Font.PLAIN, 13));
            g.drawString(spendTime, 60, 35);
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }
    }
}

package erchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ImagePanel extends JPanel {

    private JLabel jlbTime = new JLabel("");
    private JPanel jpnMessage = new JPanel(new BorderLayout());
    private JPanel jpnDownload = new JPanel();
    private JLabel jlbForward = new JLabel("Forward");
    private JLabel jlbDownload = new JLabel("Download");
    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default-small.png")));
    private JLabel jlbSender = new JLabel();
    private Image img;
    private String fullname;
    private long time;
    private boolean me;
    
    public ImagePanel(ChatFrame chatFrame, boolean me, boolean group, String sender, String fullname, String filename, long time) {
        this.setFullname(fullname);
        this.setTime(time);
        this.setMe(me);
        
        setBackground(Color.white);
        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        JPanel picPanel = new PicturePanel();

        if (filename != null) {
            try {
                img = ImageIO.read(new File("./File/" + filename));
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            if (img.getWidth(this) > img.getHeight(this)) {
                img = img.getScaledInstance(250, -1, Image.SCALE_SMOOTH);
            } else {
                img = img.getScaledInstance(-1, 250, Image.SCALE_SMOOTH);
            }
            picPanel.setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
            this.repaint();
        }

        jpnMessage.setBackground(Color.white);
        jlbTime.setText(df.format(date));
        jlbTime.setFont(new Font("helvitica", Font.BOLD, 11));
        jlbTime.setForeground(new Color(170, 170, 170));
        jpnDownload.setBackground(Color.white);
        jlbForward.setForeground(Color.GRAY);
        jlbForward.setFont(new Font("helvitica", Font.BOLD, 11));
        jlbDownload.setForeground(Color.GRAY);
        jlbDownload.setFont(new Font("helvitica", Font.BOLD, 11));
        jpnMessage.setBackground(Color.white);
        jpnMessage.setBorder(new EmptyBorder(7, 5, 0, 0));
        if (me) {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            add(jlbTime);
            add(jpnMessage);
            jlbTime.setBorder(new EmptyBorder(img.getHeight(this) - 40, 0, 0, 0));
            jpnMessage.setBorder(new EmptyBorder(7, 0, 0, 5));
            jpnMessage.add(picPanel, BorderLayout.CENTER);
            jpnMessage.add(jpnDownload, BorderLayout.SOUTH);
            jpnDownload.setLayout(new FlowLayout(FlowLayout.RIGHT));
            jpnDownload.add(jlbForward);
            jpnDownload.add(jlbDownload);
        } else {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            if (group) {
                jlbTime.setBorder(new EmptyBorder(img.getHeight(this) - 10, 0, 0, 0));
                jlbHead.setBorder(new EmptyBorder(0, 0, img.getHeight(this) - 20, 0));
                jlbSender.setText(sender);
                jlbSender.setFont(new Font("helvitica", Font.BOLD, 14));
                jlbSender.setForeground(new Color(132, 132, 132));
                jpnMessage.add(jlbSender, BorderLayout.NORTH);
                add(jlbHead);
            } else {
                jlbTime.setBorder(new EmptyBorder(img.getHeight(this) - 40, 0, 0, 0));
            }
            add(jpnMessage);
            add(jlbTime);
            jpnMessage.add(picPanel, BorderLayout.CENTER);
            jpnMessage.add(jpnDownload, BorderLayout.SOUTH);
            jpnDownload.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpnDownload.add(jlbDownload);
            jpnDownload.add(jlbForward);
        }
        chatFrame.getJlsContent().scrollRectToVisible(new Rectangle(0, chatFrame.getJlsContent().getHeight() * 2, 0, 0));
    }
    
    public boolean getMe() {
        return me;
    }
    
    public void setMe(boolean me) {
        this.me = me;
    }
    
    public long getTime() {
        return time;
    }
    
    public void setTime(long time) {
        this.time = time;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public class PicturePanel extends JPanel {

        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, this);
        }
    }
}

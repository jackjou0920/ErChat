package erchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FilePanel extends JPanel {

    private JLabel jlbTime = new JLabel("");
    private JPanel jpnMessage = new JPanel(new BorderLayout());
    private JPanel jpnDownload = new JPanel();
    private JLabel jlbForward = new JLabel("Forward");
    private JLabel jlbDownload = new JLabel("Download");
    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default-small.png")));
    private JLabel jlbSender = new JLabel();
    // private JPanel jpnCenter = new JPanel(new BorderLayout());
    private Font font = new Font("helvitica", Font.PLAIN, 13);
    private ArrayList<String> array = new ArrayList<String>();
    private long time;
    private String filename;
    private boolean me;

    public FilePanel(boolean send, boolean me, boolean group, JList jlsContent, int jpnContentW, String sender, long time,
            String filename, String filesize) {
        this.setMe(me);
        this.setTime(time);
        this.setFilename(filename);

        setBackground(Color.white);
        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        int sizewidth = compute("Size : " + filesize, jpnContentW - 100);
        int width = compute(filename, jpnContentW - 100);
        if (sizewidth > width) {
            width = sizewidth;
        }
        ChatPanel chat = new ChatPanel(me, filename, filesize, width);
        chat.setPreferredSize(new Dimension(width + 28, 60 + ((array.size() - 1) * 20)));
        chat.setBorder(new EmptyBorder(22, 0, 22, 0));
        getJlbTime().setFont(new Font("helvitica", Font.BOLD, 11));
        getJlbTime().setForeground(new Color(170, 170, 170));
        jpnDownload.setBackground(Color.white);
        jlbForward.setForeground(Color.GRAY);
        jlbForward.setFont(new Font("helvitica", Font.BOLD, 11));
        jlbDownload.setForeground(Color.GRAY);
        jlbDownload.setFont(new Font("helvitica", Font.BOLD, 11));
        jpnMessage.setBackground(Color.white);
        if (me) {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            getJlbTime().setBorder(new EmptyBorder(20 * array.size(), 0, 0, 0));
            jpnMessage.add(chat, BorderLayout.CENTER);
            jpnMessage.add(jpnDownload, BorderLayout.SOUTH);
            if (send) {
                getJlbTime().setText("");
            } else {
                getJlbTime().setText(df.format(date));
            }

            add(getJlbTime());
            add(jpnMessage);
            jpnDownload.setLayout(new FlowLayout(FlowLayout.RIGHT));
            jpnDownload.add(jlbForward);
            jpnDownload.add(jlbDownload);
        } else {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            getJlbTime().setText(df.format(date));
            if (group) {
                jlbHead.setBorder(new EmptyBorder(0, 0, 50 + 18 * (array.size() - 1), 0));
                getJlbTime().setBorder(new EmptyBorder(38 + 18 * (array.size() - 1), 0, 0, 0));
                jlbSender.setText(sender);
                jlbSender.setFont(new Font("helvitica", Font.BOLD, 14));
                jlbSender.setForeground(new Color(132, 132, 132));
                jpnMessage.add(jlbSender, BorderLayout.NORTH);
                add(jlbHead);
            } else {
                getJlbTime().setBorder(new EmptyBorder(20 * array.size(), 0, 0, 0));
            }
            add(jpnMessage);
            add(getJlbTime());
            jpnMessage.add(chat, BorderLayout.CENTER);
            jpnMessage.add(jpnDownload, BorderLayout.SOUTH);
            jpnDownload.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpnDownload.add(jlbDownload);
            jpnDownload.add(jlbForward);
        }
        jlsContent.scrollRectToVisible(new Rectangle(0, jlsContent.getHeight() * 2, 0, 0));
    }

    public int compute(String content, int jpnContentW) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

        int textwidth = 0, width = 0;
        String str = "";
        array.clear();
        for (int i = 0; i < content.length(); i++) {
            textwidth = (int) (font.getStringBounds(str + content.charAt(i), frc).getWidth());
            if (textwidth + 26 > jpnContentW) {
                if (textwidth > width) {
                    width = textwidth;
                }
                array.add(str);
                str = "";
            }
            str += content.charAt(i);
        }
        array.add(str);
        if (width == 0) {
            width = (int) (font.getStringBounds(str, frc).getWidth());
        }

        return width;
    }
    
    public boolean getMe() {
        return me;
    }
    
    public void setMe(boolean me) {
        this.me = me;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

        private String filename;
        private String filesize;
        private int width;
        private boolean me;

        public ChatPanel(boolean me, String filename, String filesize, int width) {
            this.filename = filename;
            this.filesize = filesize;
            this.width = width;
            this.me = me;
            setBackground(Color.white);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (me) {
                g.setColor(new Color(167, 231, 151));
            } else {
                g.setColor(new Color(227, 227, 227));
            }
            g.fillRoundRect(0, 3, width + 26, 55 + ((array.size() - 1) * 20), 27, 27);
            g.setColor(Color.black);
            g.setFont(font);
            for (int i = 0; i < array.size(); i++) {
                g.drawString(array.get(i), 13, 25 + (i * 20));
            }
            g.setColor(Color.GRAY);
            g.drawString("Size : " + filesize, 13, 45 + ((array.size() - 1) * 20));
        }
    }
}

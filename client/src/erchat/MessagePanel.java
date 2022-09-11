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

public class MessagePanel extends JPanel {

    private JLabel jlbTime = new JLabel("");
    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default-small.png")));
    private JLabel jlbSender = new JLabel();
    private JPanel jpnCenter = new JPanel(new BorderLayout());
    private Font font = new Font("helvitica", Font.PLAIN, 13);
    private ArrayList<String> array = new ArrayList<String>();
    private long time;
    private boolean me;
    private String content;

    public MessagePanel(boolean send, boolean me, boolean group, JList jlsContent, int jpnContentW, String sender, long time,
            String content) {
        this.setTime(time);
        this.setMe(me);
        this.setContent(content);

        setBackground(Color.white);
        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

        getJlbTime().setFont(new Font("helvitica", Font.BOLD, 11));
        getJlbTime().setForeground(new Color(170, 170, 170));
        if (me) {
            int width = compute(content, jpnContentW - 100);
            ChatPanel chat = new ChatPanel(me, content, width);
            chat.setBorder(new EmptyBorder(22, 0, 22, 0));
            chat.setPreferredSize(new Dimension(width + 28, 40 + ((array.size() - 1) * 20)));
            getJlbTime().setBorder(new EmptyBorder(20 * array.size(), 0, 0, 0));
            if (send) {
                getJlbTime().setText("");
            } else {
                getJlbTime().setText(df.format(date));
            }
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            add(getJlbTime());
            add(chat);
        } else {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            getJlbTime().setText(df.format(date));

            if (group) {
                int width = compute(content, jpnContentW - 135);
                ChatPanel chat = new ChatPanel(me, content, width);
                chat.setBorder(new EmptyBorder(22, 0, 22, 0));
                chat.setPreferredSize(new Dimension(width + 28, 40 + ((array.size() - 1) * 20)));

                jlbSender.setText(sender);
                jlbSender.setFont(new Font("helvitica", Font.BOLD, 14));
                jlbSender.setForeground(new Color(132, 132, 132));
                jpnCenter.setBackground(Color.white);
                jpnCenter.add(jlbSender, BorderLayout.NORTH);
                jpnCenter.add(chat, BorderLayout.CENTER);
                jlbHead.setBorder(new EmptyBorder(0, 0, 8 + 18 * (array.size() - 1), 0));
                getJlbTime().setBorder(new EmptyBorder(38 + 18 * (array.size() - 1), 0, 0, 0));

                add(jlbHead);
                add(jpnCenter);
                add(getJlbTime());
            } else {
                int width = compute(content, jpnContentW - 100);
                ChatPanel chat = new ChatPanel(me, content, width);
                chat.setBorder(new EmptyBorder(22, 0, 22, 0));
                chat.setPreferredSize(new Dimension(width + 28, 40 + ((array.size() - 1) * 20)));
                getJlbTime().setBorder(new EmptyBorder(20 * array.size(), 0, 0, 0));
                add(chat);
                add(getJlbTime());
            }
        }
        jlsContent.scrollRectToVisible(new Rectangle(0, jlsContent.getHeight() * 2, 0, 0));
    }

    public int compute(String content, int jpnContentW) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

        int textwidth = 0, width = 0;
        String str = "";
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


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public JLabel getJlbTime() {
        return jlbTime;
    }

    public void setJlbTime(JLabel jlbTime) {
        this.jlbTime = jlbTime;
    }

    public class ChatPanel extends JPanel {

        private String content;
        private int width;
        private boolean me;

        public ChatPanel(boolean me, String content, int width) {
            this.content = content;
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
            g.fillRoundRect(0, 3, width + 26, 35 + ((array.size() - 1) * 20), 27, 27);
            g.setColor(Color.black);
            g.setFont(font);
            for (int i = 0; i < array.size(); i++) {
                g.drawString(array.get(i), 13, 25 + (i * 20));
            }
        }
    }

}

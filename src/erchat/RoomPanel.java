package erchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RoomPanel extends JPanel {

    private JPanel jpnCenter = new JPanel(new BorderLayout());
    private JPanel jpnRight = new JPanel(new BorderLayout());
    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default.png")));
    private JLabel jlbName = new JLabel();
    private JTextField jtfNoRead = new JTextField(2);
    private JLabel jlbLast = new JLabel("Empty record");
    private JLabel jlbLastNext = new JLabel("");
    private JLabel jlbTime = new JLabel("Jun 25, 2017");
    private Font font = new Font("helvitica", Font.PLAIN, 14);
    private long id;
    private int count;
    private int isgroup;
    private String name;
    private String message;
    private int width = 342;
    private NoReadPanel jpnNoRead = new NoReadPanel();
    private NewGroupPanel jpnNewGroup = new NewGroupPanel();

    public RoomPanel(long id, String name, int count, int isgroup, long time, String message) {
        this.setId(id);
        this.name = name;
        this.setCount(count);
        this.isgroup = isgroup;
        this.setMessage(message);
        setLayout(new BorderLayout());
        setBackground(Color.white);

        getJpnCenter().setBackground(Color.white);
        getJpnRight().setBackground(Color.white);
        // jpnRight.setBorder(BorderFactory.createEtchedBorder());
        jlbHead.setBorder(new EmptyBorder(0, 5, 0, 0));

        getJlbName().setText(isgroup == 1 ? name + "(" + count + ")" : name);
        getJlbName().setFont(new Font("helvitica", Font.BOLD, 13));
        getJlbName().setBorder(new EmptyBorder(8, 10, 5, 0));

        last();
        jlbLast.setForeground(new Color(132, 132, 132));
        jlbLast.setFont(font);
        jlbLastNext.setForeground(new Color(132, 132, 132));
        jlbLastNext.setFont(font);

        if (time == 0) {
            getJlbTime().setText("");
        } else {
            Date date = new Date(time);
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            getJlbTime().setText(df.format(date));
        }
        getJlbTime().setForeground(new Color(170, 170, 170));
        getJlbTime().setBorder(new EmptyBorder(8, 0, 0, 4));
        getJlbTime().setFont(new Font("helvitica", Font.PLAIN, 12));

        // jpnNoRead.setVisible(false);
        getJpnCenter().add(getJlbName(), BorderLayout.NORTH);
        getJpnCenter().add(jlbLast, BorderLayout.CENTER);
        getJpnCenter().add(jlbLastNext, BorderLayout.SOUTH);
        getJpnRight().add(getJlbTime(), BorderLayout.NORTH);
        if (count == 0 && isgroup == 1) {
            getJpnRight().add(getJpnNewGroup(), BorderLayout.CENTER);
        } else {
            getJpnRight().add(getJpnNoRead(), BorderLayout.CENTER);
        }
        add(jlbHead, BorderLayout.WEST);
        add(getJpnCenter(), BorderLayout.CENTER);
        add(getJpnRight(), BorderLayout.EAST);
    }

    public void last() {
        if (getMessage() != "") {
            AffineTransform affinetransform = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
            String str = "";
            int count = 0;
            for (int i = 0; i < getMessage().length(); i++) {
                int textwidth = (int) (font.getStringBounds(str + getMessage().charAt(i), frc).getWidth());
                if (textwidth > getWidth() - 170) {
                    jlbLast.setText(str);
                    str = "";
                    count = i;
                    break;
                }
                str += getMessage().charAt(i);
            }
            if (count != 0) {
                for (int i = count; i < getMessage().length(); i++) {
                    int textwidth = (int) (font.getStringBounds(str + getMessage().charAt(i), frc).getWidth());
                    if (textwidth > getWidth() - 185) {
                        str += "...";
                        break;
                    }
                    str += getMessage().charAt(i);
                }
                jlbLastNext.setText(str);
            } else {
                jlbLast.setText(str);
                jlbLastNext.setText("");
            }
        }
        if (jlbLastNext.getText().equals("")) {
            jlbLast.setBorder(new EmptyBorder(0, 10, 25, 0));
        } else {
            jlbLast.setBorder(new EmptyBorder(0, 10, 0, 0));
            jlbLastNext.setBorder(new EmptyBorder(0, 10, 10, 0));
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public JPanel getJpnCenter() {
        return jpnCenter;
    }

    public void setJpnCenter(JPanel jpnCenter) {
        this.jpnCenter = jpnCenter;
    }

    public JPanel getJpnRight() {
        return jpnRight;
    }

    public void setJpnRight(JPanel jpnRight) {
        this.jpnRight = jpnRight;
    }

    public NoReadPanel getJpnNoRead() {
        return jpnNoRead;
    }

    public void setJpnNoRead(NoReadPanel jpnNoRead) {
        this.jpnNoRead = jpnNoRead;
    }

    public NewGroupPanel getJpnNewGroup() {
        return jpnNewGroup;
    }

    public void setJpnNewGroup(NewGroupPanel jpnNewGroup) {
        this.jpnNewGroup = jpnNewGroup;
    }

    public JLabel getJlbTime() {
        return jlbTime;
    }

    public void setJlbTime(JLabel jlbTime) {
        this.jlbTime = jlbTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JLabel getJlbName() {
        return jlbName;
    }

    public void setJlbName(JLabel jlbName) {
        this.jlbName = jlbName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public class NoReadPanel extends JPanel {

        int noRead = 0;

        public NoReadPanel() {
            setBackground(Color.white);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (noRead == 0) {
                return;
            }

            String num = String.valueOf(noRead);
            if (num.length() > 3) {
                num = "999+";
            }
            int x[] = {40, 36, 33, 31};
            int w[] = {29, 35, 41, 47};
            int sx[] = {51, 46, 42, 39};

            g.setColor(new Color(0, 205, 102));
            g.fillRoundRect(x[num.length() - 1], 10, w[num.length() - 1], 25, 23, 23);
            g.setColor(Color.white);
            g.setFont(new Font("helvitica", Font.BOLD, 12));
            g.drawString(num, sx[num.length() - 1], 27);
        }
    }

    public class NewGroupPanel extends JPanel {

        public NewGroupPanel() {
            setPreferredSize(new Dimension(80, 0));
            setBackground(Color.white);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.red);
            g.fillRoundRect(40, 10, 25, 25, 23, 23);
            g.setColor(Color.white);
            g.setFont(new Font("helvitica", Font.BOLD, 12));
            g.drawString("N", 48, 27);
        }
    }
}
package erchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FriendPanel extends JPanel {

    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default.png")));
    private JLabel jlbName = new JLabel();
    long id;
    String name;
    int count;
    int isgroup;

    public FriendPanel(long id, String name, int count, int isgroup) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.isgroup = isgroup;
        setLayout(new BorderLayout());
        setBackground(Color.white);

        jlbHead.setBorder(new EmptyBorder(0, 5, 0, 0));
        getJlbName().setText(isgroup == 1 ? name + "(" + count + ")" : name);
        getJlbName().setFont(new Font("helvitica", Font.BOLD, 14));
        getJlbName().setBorder(new EmptyBorder(27, 10, 27, 0));

        add(jlbHead, BorderLayout.WEST);
        add(getJlbName(), BorderLayout.CENTER);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JLabel getJlbName() {
        return jlbName;
    }

    public void setJlbName(JLabel jlbName) {
        this.jlbName = jlbName;
    }
}

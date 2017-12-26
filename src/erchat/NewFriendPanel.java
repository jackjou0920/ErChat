package erchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class NewFriendPanel extends JPanel {

    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default-small.png")));
    private JLabel jlbName = new JLabel();
    private JButton jbtNew = new JButton(new ImageIcon(ChatFrame.class.getResource("/resource/new-friend.png")));
    private long id;
    private String name;

    public NewFriendPanel(long id, String name) {
        this.setId(id);
        this.name = name;
        setLayout(new BorderLayout());
        setBackground(Color.white);

        jlbHead.setBorder(new EmptyBorder(0, 5, 0, 0));
        getJlbName().setText(name);
        getJlbName().setFont(new Font("helvitica", Font.BOLD, 13));
        getJlbName().setBorder(new EmptyBorder(22, 10, 22, 0));
        jbtNew.setBorderPainted(false);
        jbtNew.setContentAreaFilled(false);

        add(jlbHead, BorderLayout.WEST);
        add(getJlbName(), BorderLayout.CENTER);
        add(jbtNew, BorderLayout.EAST);
    }

    public JLabel getJlbName() {
        return jlbName;
    }

    public void setJlbName(JLabel jlbName) {
        this.jlbName = jlbName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}

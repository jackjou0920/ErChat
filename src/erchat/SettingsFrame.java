package erchat;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SettingsFrame extends JFrame implements ActionListener {

    private JPanel jpnList = new JPanel();
    private DefaultListModel settingsModel = new DefaultListModel();
    private JList jlsSettings = new JList(settingsModel);
    private JPanel contentPanel = new JPanel();
    private JPanel jpnContent0 = new JPanel();
    private JLabel jlbHead = new JLabel(new ImageIcon(ChatFrame.class.getResource("/resource/default-big.png")));
    private JLabel jlbName = new JLabel("Name");
    private JButton jbtName = new JButton();
    private JLabel jlbUserID = new JLabel("User ID");
    private JLabel jlbID = new JLabel();
    private JPanel jpnContent1 = new JPanel();

    private CardLayout content = new CardLayout();
    private SettingsRenderer settingsRendererrender = new SettingsRenderer();
    private SettingsPanel[] settingsArr = new SettingsPanel[2];
    private Font font = new Font("helvitica", Font.BOLD, 13);

    public SettingsFrame(String username, String showname) {
        setSize(657, 495);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setTitle("Settings");

        SettingsPanel panel0 = new SettingsPanel("Basic");
        settingsModel.addElement(panel0);
        settingsArr[0] = panel0;
        settingsArr[0].setBackground(new Color(240, 248, 255));
        settingsArr[0].jlbList.setForeground(new Color(16, 48, 80));
        SettingsPanel panel1 = new SettingsPanel("Friends");
        settingsModel.addElement(panel1);
        settingsArr[1] = panel1;

        jlsSettings.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        jlsSettings.setCellRenderer(settingsRendererrender);
        jlsSettings.setSelectedIndex(0);
        jpnList.setLayout(new BorderLayout());
        jpnList.setBackground(Color.white);
        jpnList.setPreferredSize(new Dimension(160, 0));
        jpnList.add(jlsSettings, BorderLayout.CENTER);

        jlbHead.setBounds(20, 20, 90, 90);
        jlbName.setBounds(130, 20, 40, 30);
        jlbName.setFont(font);
        jbtName.setIcon(new ImageIcon(ChatFrame.class.getResource("/resource/edit.png")));
        jlbID.setText(username);
        jbtName.setText(showname);
        jbtName.setHorizontalTextPosition(SwingConstants.LEFT);
        jbtName.setHorizontalAlignment(SwingConstants.LEFT);
        jbtName.setFont(font);
        jbtName.setBounds(245, 17, compute(showname) + 35, 35);
        jlbUserID.setBounds(130, 50, 50, 40);
        jlbUserID.setFont(font);
        jlbID.setBounds(253, 50, compute(username) + 5, 35);
        jlbID.setFont(font);
        jlbID.setForeground(new Color(115, 115, 115));
        jpnContent0.setLayout(null);
        jpnContent0.setBackground(Color.white);
        jpnContent0.add(jlbHead);
        jpnContent0.add(jlbName);
        jpnContent0.add(jbtName);
        jpnContent0.add(jlbUserID);
        jpnContent0.add(jlbID);

        jpnContent1.setBackground(Color.white);

        contentPanel.setLayout(content);
        contentPanel.add(jpnContent0, "Basic");
        contentPanel.add(jpnContent1, "Friends");
        content.show(contentPanel, "Basic");

        add(jpnList, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        jlsSettings.addMouseListener(settingsRendererrender.getSettingsHandler(jlsSettings));
        jlsSettings.addMouseMotionListener(settingsRendererrender.getSettingsHandler(jlsSettings));
        jbtName.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public int compute(String text) {
        System.out.println(text);
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

        int textwidth = (int) (font.getStringBounds(text, frc).getWidth());
        System.out.println(textwidth);
        return textwidth;
    }

    public class SettingsPanel extends JPanel {

        private JLabel jlbList = new JLabel();
        private String name;
        private boolean isClick = false;

        public SettingsPanel(String name) {
            this.name = name;

            setLayout(new BorderLayout());
            setBackground(Color.white);

            jlbList.setBorder(new EmptyBorder(15, 15, 15, 0));
            jlbList.setFont(new Font("helvitica", Font.BOLD, 13));
            jlbList.setForeground(new Color(132, 132, 132));
            jlbList.setText(name);

            add(jlbList, BorderLayout.CENTER);
        }
    }

    public class SettingsRenderer implements ListCellRenderer {

        private MouseAdapter SettingHandler;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JPanel renderer = (JPanel) value;

            return renderer;
        }

        public MouseAdapter getSettingsHandler(JList list) {
            if (SettingHandler == null) {
                SettingHandler = new SettingsHoverMouseHandler(list);
            }
            return SettingHandler;
        }

        public class SettingsHoverMouseHandler extends MouseAdapter {

            private final JList list;

            public SettingsHoverMouseHandler(JList list) {
                this.list = list;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = list.getSelectedIndex();
                for (int i = 0; i < settingsArr.length; i++) {
                    settingsArr[i].setBackground(Color.white);
                    settingsArr[i].jlbList.setForeground(new Color(132, 132, 132));
                }
                settingsArr[idx].setBackground(new Color(240, 248, 255));
                settingsArr[idx].jlbList.setForeground(new Color(16, 48, 80));
                content.show(contentPanel, settingsArr[idx].name);
            }
        }

    }

}

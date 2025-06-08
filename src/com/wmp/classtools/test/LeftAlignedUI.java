import javax.swing.*;
import java.awt.*;

public class LeftAlignedUI extends JFrame {

    public LeftAlignedUI() {
        super("左对齐界面设计");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 创建主面板 - 使用BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建顶部按钮面板 - 左对齐
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // 创建内容区域 - 左对齐
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 添加状态栏
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    // 创建左对齐按钮面板
    private JPanel createButtonPanel() {
        // 使用FlowLayout左对齐
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 210)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        panel.setBackground(new Color(240, 242, 245));

        // 创建按钮
        String[] buttonLabels = {"新建", "打开", "保存", "打印", "剪切", "复制", "粘贴", "帮助"};
        for (String label : buttonLabels) {
            JButton button = createFlatButton(label);
            panel.add(button);
        }

        return panel;
    }

    // 创建扁平化风格按钮
    private JButton createFlatButton(String text) {
        JButton button = new JButton(text);

        // 设置按钮样式
        button.setBackground(new Color(70, 130, 200));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));

        // 添加悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 150, 220));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 200));
            }
        });

        return button;
    }

    // 创建左对齐内容面板
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // 添加标题
        JLabel titleLabel = new JLabel("项目概览");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel);

        // 添加左对齐表单
        panel.add(createLeftAlignedForm());

        // 添加左对齐卡片
        panel.add(createCardPanel());

        return panel;
    }

    // 创建左对齐表单
    private JPanel createLeftAlignedForm() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 240)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 8, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // 表单字段
        String[] labels = {"项目名称:", "项目类型:", "负责人:", "开始日期:", "结束日期:", "项目状态:"};
        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            formPanel.add(lbl, gbc);

            JTextField textField = new JTextField(20);
            textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 210)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            gbc.gridx = 1;
            formPanel.add(textField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
        }

        // 提交按钮
        JButton submitButton = new JButton("提交信息");
        submitButton.setBackground(new Color(85, 170, 85));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 10, 5, 0);
        formPanel.add(submitButton, gbc);

        return formPanel;
    }

    // 创建左对齐卡片面板
    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JLabel sectionLabel = new JLabel("项目统计");
        sectionLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        cardPanel.add(sectionLabel);

        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        cardsContainer.setBackground(Color.WHITE);
        cardsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 添加统计卡片
        cardsContainer.add(createStatCard("任务总数", "128", new Color(70, 130, 200)));
        cardsContainer.add(createStatCard("已完成", "94", new Color(85, 170, 85)));
        cardsContainer.add(createStatCard("进行中", "24", new Color(220, 170, 50)));
        cardsContainer.add(createStatCard("未开始", "10", new Color(200, 90, 80)));

        cardPanel.add(cardsContainer);
        return cardPanel;
    }

    // 创建统计卡片
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 240)),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setForeground(new Color(120, 120, 130));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setForeground(color);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);

        return card;
    }

    // 创建状态栏
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(240, 242, 245));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 210)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        JLabel leftLabel = new JLabel("就绪");
        leftLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        leftLabel.setForeground(new Color(100, 100, 110));

        JLabel rightLabel = new JLabel("© 2023 企业管理系统");
        rightLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        rightLabel.setForeground(new Color(100, 100, 110));

        statusBar.add(leftLabel, BorderLayout.WEST);
        statusBar.add(rightLabel, BorderLayout.EAST);

        return statusBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置系统风格
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // 创建并显示界面
                new LeftAlignedUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
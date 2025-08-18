package com.wmp.classTools.CTComponent;

import com.wmp.Main;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.UITools.GetIcon;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class CTOptionPane {
    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;

    public static final int ERROR_MESSAGE = 0;
    public static final int INFORMATION_MESSAGE = 1;
    public static final int WARNING_MESSAGE = 2;
    protected static final Border BASIC_LINE_BORDER = BorderFactory.createLineBorder(new Color(200, 200, 200), 2);
    protected static final Border FOCUS_GAINTED_BORDER = BorderFactory.createLineBorder(new Color(112, 112, 112), 2);
    private static final int YES_NO_BUTTONS = 0;
    private static final int YES_BUTTONS = 1;
    private static final int MESSAGE_TEXT = 0;
    private static final int MESSAGE_INPUT = 1;

    /**
     * 显示消息对话框
     *
     * @param owner         对话框的父组件
     * @param title         对话框标题
     * @param message       显示的消息
     * @param icon          对话框图标
     * @param iconType      对话框图标类型
     * @param isAlwaysOnTop 是否始终置顶
     */
    public static void showMessageDialog(Component owner, String title, String message, Icon icon, int iconType, boolean isAlwaysOnTop) {

        showDefaultDialog(owner, title, message, icon, iconType, YES_BUTTONS, MESSAGE_TEXT, isAlwaysOnTop);

    }

    /**
     * 显示确认对话框
     *
     * @param owner         对话框的父组件
     * @param title         对话框标题
     * @param message       显示的消息
     * @param icon          对话框图标
     * @param isAlwaysOnTop 是否始终置顶
     * @return 用户选择的选项 YES_OPTION, NO_OPTION, 按取消返回-1
     */
    public static int showConfirmDialog(Container owner, String title, String message, Icon icon, boolean isAlwaysOnTop) {
        Object o = showDefaultDialog(owner, title, message, icon, INFORMATION_MESSAGE, YES_NO_BUTTONS, MESSAGE_TEXT, isAlwaysOnTop);
        if (o != null) {
            return (int) o;
        }
        return NO_OPTION;
    }

    /**
     * 显示确认对话框
     *
     * @param owner         对话框的父组件
     * @param title         对话框标题
     * @param message       显示的消息
     * @param icon          对话框图标
     * @param isAlwaysOnTop 是否始终置顶
     * @param choices       显示的选项
     * @return 用户选择的选项 , 按取消返回"NULL"
     */
    public static String showConfirmDialog(Container owner, String title, String message, Icon icon, boolean isAlwaysOnTop, String... choices) {
        Object[] o = showDefaultDialog(owner, title, message, icon, INFORMATION_MESSAGE, YES_NO_BUTTONS, MESSAGE_TEXT, isAlwaysOnTop, choices);
        if (o != null && o.length > 0 && o[0] != null) {
            return o[0].toString();
        }
        return "NULL";
    }

    /**
     * 显示输入对话框
     *
     * @param owner         对话框的父组件
     * @param title         对话框标题
     * @param message       显示的消息
     * @param icon          对话框图标
     * @param isAlwaysOnTop 是否始终置顶
     * @return 用户输入的字符串
     */
    public static String showInputDialog(Container owner, String title, String message, Icon icon, boolean isAlwaysOnTop) {
        Object o = showDefaultDialog(owner, title, message, icon, INFORMATION_MESSAGE, YES_NO_BUTTONS, MESSAGE_INPUT, isAlwaysOnTop);
        if (o != null && !o.toString().isEmpty()) {
            return o.toString();
        }
        return null;
    }

    /**
     * 显示选择输入对话框
     *
     * @param owner         对话框的父组件
     * @param title         对话框标题
     * @param message       显示的消息
     * @param icon          对话框图标
     * @param isAlwaysOnTop 是否始终置顶
     * @param choices       显示的选项
     * @return 0-选择的选项  1-用户输入的字符串
     */
    public static String[] showConfirmInputDialog(Container owner, String title, String message, Icon icon, boolean isAlwaysOnTop, String... choices) {
        Object[] o = showDefaultDialog(owner, title, message, icon, INFORMATION_MESSAGE, YES_NO_BUTTONS, MESSAGE_INPUT, isAlwaysOnTop, choices);
        if (o != null) {
            String[] ss = new String[o.length];
            for (int i = 0; i < o.length; i++) {


                String string = o[i].toString();
                ss[i] = string;
            }
            return ss;
        }
        return new String[]{"NULL", ""};
    }

    /**
     * @param owner         对话框的父组件
     * @param title         对话框标题
     * @param message       显示的消息
     * @param icon          对话框图标
     * @param iconType      对话框图标类型
     * @param optionType    对话框选项类型
     * @param messageType   对话框消息类型
     * @param isAlwaysOnTop 是否始终置顶
     * @return 根据showComponent的类型返回不同的对象
     */
    private static Object showDefaultDialog(Component owner, String title, String message, Icon icon, int iconType, int optionType, int messageType, boolean isAlwaysOnTop) {

        Object[] objects = showDefaultDialog(owner, title, message, icon, iconType, optionType, messageType, isAlwaysOnTop, "");
        if (objects != null) {
            return objects[0];
        }
        return null;
    }

    /**
     * @param owner         对话框的父组件
     * @param title         对话框标题
     * @param message       显示的消息
     * @param icon          对话框图标
     * @param iconType      对话框图标类型
     * @param optionType    对话框选项类型
     * @param messageType   对话框消息类型
     * @param isAlwaysOnTop 是否始终置顶
     * @return 根据showComponent的类型返回不同的对象
     */
    private static Object[] showDefaultDialog(Component owner, String title, String message, Icon icon, int iconType, int optionType, int messageType, boolean isAlwaysOnTop, String... choices) {
        JDialog dialog = new JDialog();
        dialog.setSize(380, 200);
        dialog.setLocationRelativeTo(owner);//设置对话框的位置相对于父组件
        dialog.setModal(true);
        dialog.setAlwaysOnTop(isAlwaysOnTop);
        dialog.setTitle(title);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout(10, 10));
        //dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // 创建图标标签
        {
            if (icon == null) {
                Icon tempIcon = null;
                switch (iconType) {

                    case ERROR_MESSAGE:
                        tempIcon = GetIcon.getIcon(Main.class.getResource("/image/optionDialogIcon/error.png"), 50, 50);
                        break;
                    case INFORMATION_MESSAGE:
                        tempIcon = GetIcon.getIcon(Main.class.getResource("/image/optionDialogIcon/information.png"), 50, 50);
                        break;
                    case WARNING_MESSAGE:
                        tempIcon = GetIcon.getIcon(Main.class.getResource("/image/optionDialogIcon/warn.png"), 50, 50);
                        break;
                }
                JLabel iconLabel = new JLabel(tempIcon);
                dialog.add(iconLabel, BorderLayout.WEST);//设置图标标签的位置 - 左
            } else {
                JLabel iconLabel = new JLabel(icon);
                dialog.add(iconLabel, BorderLayout.WEST);//设置图标标签的位置 - 左
            }
        }


        // 创建消息文本区域
        AtomicReference<String> inputStr = new AtomicReference<>("");

        JPanel toolsPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        CTTextField inputField = new CTTextField();
        CTComboBox choiceBox = new CTComboBox();
        if (choices != null && choices.length > 0 && !choices[0].isEmpty()) {
            choiceBox.addItems(choices);
        }
        {
            JPanel panel = new JPanel(new BorderLayout(10, 10));

            // 创建消息文本区域
            {
                JScrollPane messagePanel;

                //处理数据
                if (message == null) {
                    message = "";
                } else {
                    if (message.contains("\\n")) {
                        message = message.replace("\\n", "\n");
                    }

                }
                //创建一个文本区域
                JTextArea messageArea = new JTextArea();
                messageArea.setText(message);
                messageArea.setEditable(false);//设置文本区域不可编辑
                //messageArea.setFocusable(false);//设置文本区域可聚焦
                messageArea.setOpaque(false);//设置文本区域不透明
                messageArea.setLineWrap(true);//设置文本区域自动换行
                messageArea.setWrapStyleWord(true);//设置文本区域自动换行时单词不被分割
                messageArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
                messagePanel = new JScrollPane(messageArea);
                messagePanel.setBorder(null);

                messageArea.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        messageArea.selectAll();
                    }
                });

                messageArea.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        EasterEgg.errorAction();
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        EasterEgg.errorAction();
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        EasterEgg.errorAction();
                    }
                });


                panel.add(messagePanel, BorderLayout.CENTER);
            }
            //选择框
            if (choiceBox.getItemCount() > 0) {
                toolsPanel.add(choiceBox);
            }
            // 输入框
            if (messageType == MESSAGE_INPUT) {

                toolsPanel.add(inputField);
            }

            panel.add(toolsPanel, BorderLayout.SOUTH);
            dialog.add(panel, BorderLayout.CENTER);//设置消息文本区域的位置 - 中间

        }

        AtomicInteger choose = new AtomicInteger(2);

        // 创建按钮面板
        {
            if (optionType == YES_NO_BUTTONS) {

                ChooseButton yesButton = new ChooseButton("是") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        choose.set(YES_OPTION);
                        if (messageType == MESSAGE_INPUT) {
                            inputStr.set(inputField.getText());
                        }
                        dialog.dispose();
                    }
                };
                yesButton.requestFocus();
                ChooseButton noButton = new ChooseButton("否") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        choose.set(NO_OPTION);
                        dialog.dispose();
                    }
                };
                JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                buttonPanel.add(yesButton);
                buttonPanel.add(noButton);
                dialog.add(buttonPanel, BorderLayout.SOUTH);//设置按钮面板的位置 - 下

            } else if (optionType == YES_BUTTONS) {
                ChooseButton yesButton = new ChooseButton("确定") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        choose.set(NO_OPTION);
                        dialog.dispose();
                    }
                };
                yesButton.requestFocus();
                JPanel buttonPanel = new JPanel(new GridLayout(1, 1, 10, 10));
                buttonPanel.add(yesButton);
                dialog.add(buttonPanel, BorderLayout.SOUTH);//设置按钮面板的位置 - 下

            }
        }


        dialog.setVisible(true);


        if (optionType == YES_NO_BUTTONS) {
            if (choices != null && choices.length > 0 && !choices[0].isEmpty()) {//如果有特殊选项
                if (messageType == MESSAGE_TEXT) {// 没有输入框
                    if (choose.get() == YES_OPTION)
                        return new Object[]{choiceBox.getSelectedItem()};
                    else
                        return new Object[]{null};
                } else if (messageType == MESSAGE_INPUT) {// 有输入框
                    return new Object[]{choiceBox.getSelectedItem(), inputStr.get()};
                }
            } else {
                if (messageType == MESSAGE_TEXT) {// 没有输入框
                    return new Object[]{choose.get()};
                } else if (messageType == MESSAGE_INPUT) {// 有输入框
                    return new Object[]{inputStr.get()};
                }
            }


        }

        return null;
    }

}

abstract class ChooseButton extends CTTextButton implements ActionListener {
    public ChooseButton(String text) {
        super(text);
        this.addActionListener(this);
    }
}

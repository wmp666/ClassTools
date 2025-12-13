package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.appFileControl.IconControl;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton.CTRoundTextButton;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.Menu.CTPopupMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class CTOptionPane {
    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;

    public static final int ERROR_MESSAGE = 0;
    public static final int INFORMATION_MESSAGE = 1;
    public static final int WARNING_MESSAGE = 2;

    public static final int YEAR_MONTH_DAY = 3;
    public static final int MONTH_DAY = 2;
    public static final int HOURS_MINUTES = 1;
    public static final int HOURS_MINUTES_SECOND = 0;

    private static final int YES_NO_BUTTONS = 0;
    private static final int YES_BUTTONS = 1;

    private static final int MESSAGE_TEXT = 0;
    private static final int MESSAGE_INPUT = 1;

    private static final Object i = new Object();

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
    public static int showConfirmDialog(Container owner, String title, String message, Icon icon, int iconType, boolean isAlwaysOnTop) {
        Object o = showDefaultDialog(owner, title, message, icon, iconType, YES_NO_BUTTONS, MESSAGE_TEXT, isAlwaysOnTop);
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
    public static String showChoiceDialog(Container owner, String title, String message, Icon icon, int iconType, boolean isAlwaysOnTop, String... choices) {
        Object[] o = showDefaultDialog(owner, title, message, icon, iconType, YES_NO_BUTTONS, MESSAGE_TEXT, isAlwaysOnTop, choices);
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

        BasicDialog basicDialog =
                switch (CTInfo.isError ? 1 : 0) {
                    case 1 -> createDialog(owner, "骇客已入侵", title, icon, iconType, isAlwaysOnTop);
                    default -> createDialog(owner, title, "", icon, iconType, isAlwaysOnTop);
                };

        JDialog dialog = basicDialog.dialog;
        JPanel toolsPanel = basicDialog.toolsPanel;

        JTextArea messageArea = new JTextArea();
        CTTextField inputField = new CTTextField();
        CTComboBox choiceBox = new CTComboBox();
        // 创建消息文本区域
        AtomicReference<String> inputStr = new AtomicReference<>("");

        if (choices != null && choices.length > 0 && !choices[0].isEmpty()) {
            choiceBox.addItems(choices);
        }
        {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setOpaque(false);

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

                messageArea.setText(message);
                messageArea.setEditable(false);//设置文本区域不可编辑
                //messageArea.setFocusable(false);//设置文本区域可聚焦
                messageArea.setOpaque(false);//设置文本区域不透明
                messageArea.setLineWrap(true);//设置文本区域自动换行
                messageArea.setWrapStyleWord(true);//设置文本区域自动换行时单词不被分割
                messageArea.setForeground(CTColor.textColor);
                messageArea.setAutoscrolls(false);
                messageArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (15 * CTInfo.dpi)));


                messageArea.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        messageArea.selectAll();
                    }
                });

                messageArea.addMouseListener(new MouseAdapter() {
                    @Override//鼠标点击
                    public void mouseClicked(MouseEvent e) {
                        int button = e.getButton();
                        if (button == MouseEvent.BUTTON3) {
                            CTPopupMenu ETPopupMenu = new CTPopupMenu();

                            CTRoundTextButton edit = new CTRoundTextButton("编辑");
                            edit.setIcon(GetIcon.getIcon("编辑", 20, 20));
                            edit.addActionListener(event -> {
                                EasterEgg.errorAction();
                            });

                            ETPopupMenu.add(edit);

                            CTRoundTextButton copy = new CTRoundTextButton("复制");
                            copy.setIcon(GetIcon.getIcon("编辑", 20, 20));
                            copy.addActionListener(event -> {

                                //将字符串复制到剪切板。
                                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                                Transferable tText = new StringSelection(messageArea.getSelectedText());
                                clip.setContents(tText, null);
                            });
                            ETPopupMenu.add(copy);

                            ETPopupMenu.show(messageArea, e.getX(), e.getY());
                        }
                    }
                });

                messagePanel = new JScrollPane(messageArea);
                messagePanel.setOpaque(false);
                messagePanel.getViewport().setOpaque(false);
                messagePanel.setBorder(null);
                messageArea.setCaretPosition(0);

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
        createChoiceButton(dialog, optionType, new ChoiceButtonListener() {
            @Override
            public void yesButtonClick() {
                choose.set(YES_OPTION);
                if (messageType == MESSAGE_INPUT) {
                    inputStr.set(inputField.getText());
                }
                dialog.dispose();
            }

            @Override
            public void noButtonClick() {
                choose.set(NO_OPTION);
                dialog.dispose();
            }
        });

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (title.equals("doge")) return;

                CTOptionPane.showMessageDialog(dialog, "doge", "请认真看窗口内容!!!\n请不要尝试跳过选择过程(按下关闭键),\n请认真选择", GetIcon.getIcon("茜特菈莉", IconControl.COLOR_DEFAULT, 100, 100), CTOptionPane.INFORMATION_MESSAGE, true);
            }

            @Override
            public void windowOpened(WindowEvent e) {
                dialog.getWindowListeners()[0].windowOpened(e);
            }
        });

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

    /**
     * 时间选择器
     *
     * @param owner         对话框的父组件
     * @param icon          对话框图标
     * @param iconType      对话框图标类型
     * @param timeStyle         时间样式
     * @param isAlwaysOnTop 是否始终置顶
     * @return 时间选择结果
     */
    public static int[] showTimeChooseDialog(Component owner, String title, Icon icon, int iconType, int timeStyle, boolean isAlwaysOnTop) {
        int[] times = new int[]{0, 0, 0};
        if (timeStyle == YEAR_MONTH_DAY) {
            times[0] = Calendar.getInstance().get(Calendar.YEAR);
            times[1] = Calendar.getInstance().get(Calendar.MONTH) + 1;
            times[2] = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        } else if (timeStyle == MONTH_DAY) {

            times[0] = Calendar.getInstance().get(Calendar.MONTH) + 1;
            times[1] = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        } else if (timeStyle == HOURS_MINUTES) {
            times[0] = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            times[1] = Calendar.getInstance().get(Calendar.MINUTE);

        } else if (timeStyle == HOURS_MINUTES_SECOND) {
            times[0] = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            times[1] = Calendar.getInstance().get(Calendar.MINUTE);
            times[2] = Calendar.getInstance().get(Calendar.SECOND);
        }
        return showTimeChooseDialog(owner, title, icon, iconType, timeStyle, true, times);
    }

    /**
     * 时间选择器
     *
     * @param owner         对话框的父组件
     * @param icon          对话框图标
     * @param iconType      对话框图标类型
     * @param style         时间样式
     * @param isAlwaysOnTop 是否始终置顶
     * @param times         时间
     * @return 时间选择结果
     */
    public static int[] showTimeChooseDialog(Component owner, String title, Icon icon, int iconType, int style, boolean isAlwaysOnTop, int[] times) {

        BasicDialog basicDialog =
                switch (CTInfo.isError ? 1 : 0) {
                    case 1 -> createDialog(owner, "骇客已入侵", title, icon, iconType, isAlwaysOnTop);
                    default -> createDialog(owner, "时间选择器", title, icon, iconType, isAlwaysOnTop);
                };

        JDialog dialog = basicDialog.dialog;
        JPanel toolsPanel = basicDialog.toolsPanel;

        JPanel yearPanel = new JPanel(new BorderLayout());
        CTSpinner yearSpinner = new CTSpinner(new SpinnerNumberModel(2000, 2000, Calendar.getInstance().get(Calendar.YEAR) + 20, 1));
        {
            yearPanel.setOpaque(false);

            yearSpinner.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            JLabel yearLabel = new JLabel("年:");
            yearLabel.setForeground(CTColor.textColor);
            yearLabel.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            yearPanel.add(yearSpinner, BorderLayout.CENTER);
            yearPanel.add(yearLabel, BorderLayout.WEST);
        }

        JPanel monthPanel = new JPanel(new BorderLayout());
        CTSpinner monthSpinner = new CTSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        {
            monthPanel.setOpaque(false);
            monthSpinner.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            JLabel monthLabel = new JLabel("月:");
            monthLabel.setForeground(CTColor.textColor);
            monthLabel.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            monthPanel.add(monthSpinner, BorderLayout.CENTER);
            monthPanel.add(monthLabel, BorderLayout.WEST);
        }

        JPanel dayPanel = new JPanel(new BorderLayout());
        CTSpinner daySpinner = new CTSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        {
            dayPanel.setOpaque(false);
            daySpinner.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            JLabel dayLabel = new JLabel("日:");
            dayLabel.setForeground(CTColor.textColor);
            dayLabel.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            dayPanel.add(daySpinner, BorderLayout.CENTER);
            dayPanel.add(dayLabel, BorderLayout.WEST);
        }

        JPanel hourPanel = new JPanel(new BorderLayout());
        CTSpinner hourSpinner = new CTSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        {
            hourPanel.setOpaque(false);
            hourSpinner.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            JLabel hourLabel = new JLabel("时:");
            hourLabel.setForeground(CTColor.textColor);
            hourLabel.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            hourPanel.add(hourSpinner, BorderLayout.CENTER);
            hourPanel.add(hourLabel, BorderLayout.WEST);
        }

        JPanel minutePanel = new JPanel(new BorderLayout());
        CTSpinner minuteSpinner = new CTSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        {
            minutePanel.setOpaque(false);
            minuteSpinner.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            JLabel minuteLabel = new JLabel("分:");
            minuteLabel.setForeground(CTColor.textColor);
            minuteLabel.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            minutePanel.add(minuteSpinner, BorderLayout.CENTER);
            minutePanel.add(minuteLabel, BorderLayout.WEST);
        }

        JPanel secondPanel = new JPanel(new BorderLayout());
        CTSpinner secondSpinner = new CTSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        {
            secondPanel.setOpaque(false);
            secondSpinner.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            JLabel secondLabel = new JLabel("秒:");
            secondLabel.setForeground(CTColor.textColor);
            secondLabel.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            secondPanel.add(secondSpinner, BorderLayout.CENTER);
            secondPanel.add(secondLabel, BorderLayout.WEST);
        }
        if (style == YEAR_MONTH_DAY) {
            yearSpinner.setValue(times[0]);
            monthSpinner.setValue(times[1]);
            daySpinner.setValue(times[2]);

            toolsPanel.add(yearPanel);
            toolsPanel.add(monthPanel);
            toolsPanel.add(dayPanel);
        } else if (style == MONTH_DAY) {
            monthSpinner.setValue(times[0]);
            daySpinner.setValue(times[1]);

            toolsPanel.add(monthPanel);
            toolsPanel.add(dayPanel);
        } else if (style == HOURS_MINUTES) {
            hourSpinner.setValue(times[0]);
            minuteSpinner.setValue(times[1]);

            toolsPanel.add(hourPanel);
            toolsPanel.add(minutePanel);
        } else if (style == HOURS_MINUTES_SECOND) {
            hourSpinner.setValue(times[0]);
            minuteSpinner.setValue(times[1]);
            secondSpinner.setValue(times[2]);

            toolsPanel.add(hourPanel);
            toolsPanel.add(minutePanel);
            toolsPanel.add(secondPanel);
        }

        AtomicInteger choose = new AtomicInteger(2);
        ArrayList<Integer> result = new ArrayList<>();

        createChoiceButton(dialog, YES_NO_BUTTONS, new ChoiceButtonListener() {
            @Override
            public void yesButtonClick() {
                choose.set(YES_OPTION);

                if (style == YEAR_MONTH_DAY) {
                    result.add(Integer.valueOf(yearSpinner.getValue()));
                    result.add(Integer.valueOf(monthSpinner.getValue()));
                    result.add(Integer.valueOf(daySpinner.getValue()));
                } else if (style == MONTH_DAY) {
                    result.add(Calendar.getInstance().get(Calendar.YEAR));
                    result.add(Integer.valueOf(monthSpinner.getValue()));
                    result.add(Integer.valueOf(daySpinner.getValue()));
                } else if (style == HOURS_MINUTES) {
                    result.add(Integer.valueOf(hourSpinner.getValue()));
                    result.add(Integer.valueOf(minuteSpinner.getValue()));
                } else if (style == HOURS_MINUTES_SECOND) {
                    result.add(Integer.valueOf(hourSpinner.getValue()));
                    result.add(Integer.valueOf(minuteSpinner.getValue()));
                    result.add(Integer.valueOf(secondSpinner.getValue()));
                }
                dialog.dispose();
            }

            @Override
            public void noButtonClick() {
                choose.set(NO_OPTION);
                dialog.dispose();
            }
        });

        dialog.setVisible(true);


        if (choose.get() == YES_OPTION) {
            Integer[] array = result.toArray(new Integer[0]);
            int[] arrayInt = new int[result.size()];
            for (int i = 0; i < array.length; i++) {
                arrayInt[i] = array[i];
            }
            return arrayInt;
        }
        return new int[0];
    }

    public static String[] showChoicesDialog(Component owner, String title, String message, Icon icon, int iconType, boolean isAlwaysOnTop, String... choices) {
        BasicDialog basicDialog = createDialog(owner, title, message, icon, iconType, isAlwaysOnTop);
        JDialog dialog = basicDialog.dialog;
        JPanel toolsPanel = basicDialog.toolsPanel;

        JPanel choicesPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        choicesPanel.setOpaque(false);

        CTCheckBox[] checkBoxes = new CTCheckBox[choices.length];
        for (int i = 0; i < choices.length; i++) {
            CTCheckBox choice = new CTCheckBox(choices[i]);
            choice.setFont(CTFont.getDefaultFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
            checkBoxes[i] = choice;
            System.err.println(choices[i]);
        }

        for (int i = 0; i < choices.length; i++) {
            System.err.println(checkBoxes[i]);
            choicesPanel.add(checkBoxes[i]);
        }

        JScrollPane scrollPane = new JScrollPane(choicesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        toolsPanel.add(scrollPane);

        AtomicInteger choose = new AtomicInteger(2);
        createChoiceButton(dialog, YES_NO_BUTTONS, new ChoiceButtonListener() {
            @Override
            public void yesButtonClick() {
                choose.set(YES_OPTION);
                dialog.dispose();
            }

            @Override
            public void noButtonClick() {
                choose.set(NO_OPTION);
                dialog.dispose();
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if (choose.get() == YES_OPTION) {
            ArrayList<String> result = new ArrayList<>();
            for (int i = 0; i < choices.length; i++) {
                System.err.println(checkBoxes[i].getText() + " " + checkBoxes[i].isSelected());
                if (checkBoxes[i].isSelected()) {
                    result.add(choices[i]);
                }
            }
            return result.toArray(new String[0]);
        }
        return new String[0];
    }

    private static BasicDialog createDialog(Component owner, String title, String northTitle, Icon icon, int iconType, boolean isAlwaysOnTop) {
        JDialog dialog = new JDialog();
        dialog.setSize((int) (380 * CTInfo.dpi), (int) (200 * CTInfo.dpi));
        dialog.setLocationRelativeTo(owner);//设置对话框的位置相对于父组件
        dialog.setModal(true);
        dialog.setAlwaysOnTop(isAlwaysOnTop);
        dialog.setTitle(title);
        dialog.getContentPane().setBackground(CTColor.backColor);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JLabel iconLabel = new JLabel();

        // 创建图标标签
        {
            if (icon == null) {
                Icon tempIcon = null;
                switch (iconType) {

                    case ERROR_MESSAGE:
                        tempIcon = GetIcon.getIcon("错误", IconControl.COLOR_COLORFUL, 50, 50);
                        break;
                    case INFORMATION_MESSAGE:
                        tempIcon = GetIcon.getIcon("提示", IconControl.COLOR_COLORFUL, 50, 50);
                        break;
                    case WARNING_MESSAGE:
                        tempIcon = GetIcon.getIcon("警告", IconControl.COLOR_COLORFUL, 50, 50);
                        break;
                    default:
                        tempIcon = GetIcon.getIcon("提示", IconControl.COLOR_COLORFUL, 50, 50);
                        break;
                }
                iconLabel = new JLabel(tempIcon);
                dialog.setIconImage(((ImageIcon)tempIcon).getImage());
                dialog.add(iconLabel, BorderLayout.WEST);//设置图标标签的位置 - 左
            } else {
                iconLabel = new JLabel(icon);
                dialog.setIconImage(((ImageIcon)icon).getImage());
                dialog.add(iconLabel, BorderLayout.WEST);//设置图标标签的位置 - 左
            }
        }

        if (northTitle != null && !northTitle.isEmpty()) {
            JLabel messageLabel = new JLabel(northTitle);
            messageLabel.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            messageLabel.setForeground(CTColor.textColor);
            dialog.add(messageLabel, BorderLayout.NORTH);
        }

        JPanel toolsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        toolsPanel.setOpaque(false);

        dialog.add(toolsPanel, BorderLayout.CENTER);

        return new BasicDialog(dialog, toolsPanel);
    }

    private static void createChoiceButton(JDialog dialog, int optionType, ChoiceButtonListener  listener){
        // 创建按钮面板
        {
            if (optionType == YES_NO_BUTTONS) {

                ChooseButton yesButton = new ChooseButton("是") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listener.yesButtonClick();
                    }
                };

                dialog.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowOpened(WindowEvent e) {
                        yesButton.requestFocus();
                    }
                });

                ChooseButton noButton = new ChooseButton("否") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listener.noButtonClick();
                    }
                };
                JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                buttonPanel.setOpaque(false);
                buttonPanel.add(yesButton);
                buttonPanel.add(noButton);
                dialog.add(buttonPanel, BorderLayout.SOUTH);//设置按钮面板的位置 - 下

            } else if (optionType == YES_BUTTONS) {
                ChooseButton yesButton = new ChooseButton("确定") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listener.yesButtonClick();
                    }
                };

                dialog.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowOpened(WindowEvent e) {
                        yesButton.requestFocus();
                    }
                });

                JPanel buttonPanel = new JPanel(new GridLayout(1, 1, 10, 10));
                buttonPanel.setOpaque(false);
                buttonPanel.add(yesButton);
                dialog.add(buttonPanel, BorderLayout.SOUTH);//设置按钮面板的位置 - 下

            }
        }
    }
    public static void showFullScreenMessageDialog(String title, String message) {
        showFullScreenMessageDialog(title, message, 0, 10);
    }

    public static void showFullScreenMessageDialog(String title, String message, int maxShowTime) {
        showFullScreenMessageDialog(title, message, maxShowTime, 10);
    }

    /**
     * 全屏弹窗
     *
     * @param title       标题
     * @param message     内容
     * @param maxShowTime 最大显示时间
     * @param waitTime    等待时间
     */
    public static void showFullScreenMessageDialog(String title, String message, int maxShowTime, int waitTime) {

        JDialog messageDialog = new JDialog();
        messageDialog.setAlwaysOnTop(true);
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        messageDialog.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        messageDialog.setLocationRelativeTo(null);
        messageDialog.setUndecorated(true);
        messageDialog.getContentPane().setBackground(Color.BLACK);
        messageDialog.setLayout(new BorderLayout());
        messageDialog.setModal(true);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);

        titleLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.BIG_BIG));
        messageDialog.add(titleLabel, BorderLayout.NORTH);


        JTextArea textArea = new JTextArea(message);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);
        textArea.setLineWrap(true);// 激活自动换行功能
        textArea.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.MORE_BIG));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        messageDialog.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);

        CTProgressBar progressBar = new CTProgressBar(0, waitTime * 100);
        southPanel.add(progressBar, BorderLayout.NORTH);

        CTTextButton exitButton = new CTTextButton("关闭(" + waitTime + "s)", false);
        exitButton.setIcon("关闭", IconControl.COLOR_DEFAULT, 100, 100);
        exitButton.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.MORE_BIG));
        exitButton.setEnabled(false);
        southPanel.add(exitButton, BorderLayout.CENTER);

        messageDialog.add(southPanel, BorderLayout.SOUTH);

        messageDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {

                new Thread(() -> {
                    try {
                        for (int i = 0; i < waitTime * 100; i++) {
                            progressBar.setValue(waitTime * 100 - i);
                            exitButton.setText("关闭(" + (int) (waitTime - i * 0.01 + 1) + "s)");
                            Thread.sleep(10);
                        }
                    } catch (InterruptedException ex) {
                        Log.err.print(CTOptionPane.class, "发生异常", ex);
                    }

                    exitButton.setText("关闭");
                    exitButton.setEnabled(true);
                    exitButton.addActionListener(ev -> {
                        messageDialog.dispose();

                    });
                }).start();


            }
        });
        if (maxShowTime > 0) {
            Timer t = new Timer(maxShowTime * 1000, e -> {

                if (messageDialog.isVisible())
                    messageDialog.dispose();
            });
            t.setRepeats(false);
            t.start();
        }
        Log.info.print("showFullScreenMessageDialog", "显示全屏弹窗：" + title + ":" + message);

        messageDialog.setVisible(true);

    }

    private record BasicDialog(JDialog dialog, JPanel toolsPanel) {
    }

    private static final LinkedList<CTWindow> SSMDialogs = new LinkedList<>();
    public static void showSystemStyleMessageDialog(TrayIcon.MessageType iconType, String owner, String logInfo){

        new Thread(() ->{
        synchronized (SSMDialogs){
            while (!SSMDialogs.isEmpty()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            CTWindow dialog = new CTWindow();
            dialog.setTitle(owner);
            dialog.setAlwaysOnTop(true);
            dialog.setLayout(new BorderLayout());
            dialog.getContentPane().setBackground(CTColor.backColor);

            Timer t = new Timer(5000, ex -> {
                if (dialog.isVisible()) {
                    SSMDialogs.remove(dialog);
                    dialog.dispose();
                }
            });

            JLabel iconLabel = new JLabel();
            switch (iconType) {
                case ERROR -> iconLabel.setIcon(GetIcon.getIcon("错误", IconControl.COLOR_COLORFUL, 70, 70));
                case WARNING -> iconLabel.setIcon(GetIcon.getIcon("警告", IconControl.COLOR_COLORFUL, 70, 70));
                case INFO -> iconLabel.setIcon(GetIcon.getIcon("提示", IconControl.COLOR_COLORFUL, 70, 70));
            }
            iconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    t.stop();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    t.start();
                }
            });
            try {
                dialog.setIconImage(((ImageIcon)iconLabel.getIcon()).getImage());
            } catch (Exception ignored) {

            }
            dialog.add(iconLabel, BorderLayout.WEST);

            JTextArea title = new JTextArea(owner);
            title.setOpaque(false);
            title.setForeground(CTColor.textColor);
            title.setEditable(false);
            title.setLineWrap(true);
            title.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
            title.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    t.stop();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    t.start();
                }
            });
            dialog.add(title, BorderLayout.NORTH);

            JTextArea message = new JTextArea(logInfo);
            message.setOpaque(false);
            message.setEditable(false);
            message.setLineWrap(true);
            message.setForeground(CTColor.textColor);
            message.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.MORE_SMALL));
            message.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    t.stop();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    t.start();
                }
            });
            JScrollPane scrollPane = new JScrollPane(message);
            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            dialog.add(scrollPane, BorderLayout.CENTER);

            CTRoundTextButton closeButton = new CTRoundTextButton("关闭");
            closeButton.addActionListener(ev -> {
                SSMDialogs.remove(dialog);
                dialog.dispose();
            });
            closeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    t.stop();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    t.start();
                }
            });
            dialog.add(closeButton, BorderLayout.SOUTH);


            dialog.setSize((int) (380 * CTInfo.dpi), (int) (200 * CTInfo.dpi));
            dialog.setLocation(getBottomRightCornerLocation(dialog.getSize()));
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {

                    t.start();
                }

                @Override
                public void windowGainedFocus(WindowEvent e) {
                    t.stop();
                }

                @Override
                public void windowLostFocus(WindowEvent e) {
                    t.start();
                }
            });
            SSMDialogs.add(dialog);
            dialog.setVisible(true);
        }
        }).start();

    }
    private static Point getBottomRightCornerLocation(Dimension d) {
        return getBottomRightCornerLocation(d.width, d.height);
    }

    private static Point getBottomRightCornerLocation(int width, int height) {
        // 获取屏幕工作区
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle bounds = gc.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

        // 直接设置位置

        return new Point(bounds.x + bounds.width - insets.right - width - 5,
                bounds.y + bounds.height - insets.bottom - height - 5);

    }
}

abstract class ChooseButton extends CTRoundTextButton implements ActionListener {
    public ChooseButton(String text) {
        super(text);
        this.addActionListener(this);
    }
}

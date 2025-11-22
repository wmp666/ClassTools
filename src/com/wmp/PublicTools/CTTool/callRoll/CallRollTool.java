package com.wmp.PublicTools.CTTool.callRoll;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.CTTool.CTTool;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.infSet.InfSetDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class CallRollTool extends CTTool {

    JLabel nameLabel = new JLabel("名字");

    public CallRollTool() {
        super("点名器");

        this.setCtSetsPanelList(new CallRollSetsPanel(CTInfo.DATA_PATH));
    }

    @Override
    public JDialog getDialog() {
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("点名器");
        dialog.setSize((int) (300 * CTInfo.dpi), (int) (400 * CTInfo.dpi));
        dialog.getContentPane().setBackground(CTColor.backColor);

        JLabel label = new JLabel("点名器");
        label.setForeground(CTColor.textColor);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        dialog.add(label, BorderLayout.NORTH);

        nameLabel.setForeground(CTColor.mainColor);
        nameLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.MORE_BIG));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 0));
        CTTextButton setsButton = new CTTextButton("设置");
        setsButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        setsButton.addActionListener(e -> {
            try {
                new InfSetDialog("快捷工具设置");
            } catch (Exception ex) {
                Log.err.print(getClass(), "设置打开失败", ex);
                throw new RuntimeException(ex);
            }
        });
        buttonPanel.add(setsButton);
        CTTextButton dianMingButton = new CTTextButton("点名(" + CallRollInfoControl.getCount() + "次)");
        dianMingButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        dianMingButton.addActionListener(e -> {
            callRoll();
        });
        buttonPanel.add(dianMingButton);

        dialog.add(nameLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();

        return dialog;
    }

    private void callRoll() {
        String[] nameList;
        try {
            nameList = CallRollInfoControl.getDianMingInfo();
        } catch (IOException ex) {
            Log.err.print(getClass(), "获取点名信息出错", ex);
            return;
        }
        if (nameList == null) {
            Log.err.print(getClass(), "获取点名信息出错");
            return;
        }
        int nameCount = CallRollInfoControl.getCount();
        String[] resultName = new String[nameCount];
        for (int i = 0; i < nameCount; i++) {
            resultName[i] = nameList[new Random().nextInt(nameList.length)];
        }


        String[] finalNameList = nameList;
        new Thread(() -> {
            //匀速循环
            {
                int waitTime = 50;
                int count = 0;
                for (int i = 0; i < 50; i++) {
                    int finalCount = count;
                    SwingUtilities.invokeLater(() -> {
                        if (finalNameList != null) {
                            nameLabel.setText(finalNameList[finalCount]);
                        }
                        nameLabel.repaint();
                    });
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ex) {
                        Log.err.print(getClass(), "线程中断", ex);
                    }
                    if (count >= finalNameList.length - 1) count = 0;
                    else count++;
                }
            }

            //匀速循环-循环到目标的前七个
            {
                int countIndex = nameCount - 7;
                if (countIndex < 0) countIndex = finalNameList.length - Math.abs(countIndex);
                int waitTime = 50;
                int count = 0;
                for (int i = countIndex; i < 50; i++) {
                    int finalCount = count;
                    SwingUtilities.invokeLater(() -> {
                        nameLabel.setText(finalNameList[finalCount]);
                        nameLabel.repaint();
                    });
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ex) {
                        Log.err.print(getClass(), "线程中断", ex);
                    }
                    if (count >= finalNameList.length - 1) count = 0;
                    else count++;
                    if (count == countIndex) break;
                }
            }

            //减速循环
            {
                int step = 20;
                int waitTime = 50;
                int count = 0;
                int index = 0;
                while (true) {
                    int finalCount = index;
                    SwingUtilities.invokeLater(() -> {
                        nameLabel.setText(finalNameList[finalCount]);
                        nameLabel.repaint();
                    });

                    if (index >= finalNameList.length - 1) {
                        index = 0;
                    } else {
                        index++;
                    }
                    count++;

                    waitTime += step * count;
                    if (waitTime > 600) {
                        break;
                    }

                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ex) {
                        Log.err.print(getClass(), "线程中断", ex);
                    }
                }
                System.err.println(count);
            }

            SwingUtilities.invokeLater(() -> {
                nameLabel.setText(resultName[0]);
                nameLabel.repaint();
            });

            CTOptionPane.showFullScreenMessageDialog("点名结果", Arrays.toString(resultName), 0, 1);

        }).start();
    }
}

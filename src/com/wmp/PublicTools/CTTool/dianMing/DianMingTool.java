package com.wmp.PublicTools.CTTool.dianMing;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.CTTool.CTTool;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTOptionPane;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class DianMingTool extends CTTool {

    JLabel nameLabel = new JLabel("名字");

    public DianMingTool() {
        super("点名器");
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
            JDialog setsDialog = new JDialog();
            setsDialog.setModal(true);
            setsDialog.setAlwaysOnTop(true);
            setsDialog.setLayout(new BorderLayout());
            setsDialog.setTitle("设置");

            CTTextButton inputButton = new CTTextButton("导入");
            inputButton.addActionListener(e1 -> {
                String filePath = GetPath.getFilePath(setsDialog, "请选择文件", ".txt", "点名列表");
                if (filePath != null) {
                    try {
                        DianMingInfoControl.setDianMingInfo(filePath);
                    } catch (IOException ex) {
                        Log.err.print(getClass(), "导入文件出错", ex);
                    }
                }
            });
            setsDialog.add(inputButton, BorderLayout.CENTER);

            setsDialog.pack();
            setsDialog.setLocationRelativeTo(null);
            setsDialog.setVisible(true);
        });
        buttonPanel.add(setsButton);
        CTTextButton dianMingButton = new CTTextButton("点名");
        dianMingButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        dianMingButton.addActionListener(e -> {
            String[] dianMingInfo = null;
            try {
                dianMingInfo = DianMingInfoControl.getDianMingInfo();
            } catch (IOException ex) {
                Log.err.print(getClass(), "获取点名信息出错", ex);
            }
            if (dianMingInfo != null || !dianMingInfo[0].equals("err")) {
                String resultName = dianMingInfo[new Random().nextInt(dianMingInfo.length)];

                String[] finalDianMingInfo = dianMingInfo;
                new Thread(() -> {
                    //匀速循环
                    {
                        int waitTime = 50;
                        int count = 0;
                        for (int i = 0; i < 50; i++) {
                            int finalCount = count;
                            SwingUtilities.invokeLater(() -> {
                                nameLabel.setText(finalDianMingInfo[finalCount]);
                                nameLabel.repaint();
                            });
                            try {
                                Thread.sleep(waitTime);
                            } catch (InterruptedException ex) {
                                Log.err.print(getClass(), "线程中断", ex);
                            }
                            if (count >= finalDianMingInfo.length - 1) count = 0;
                            else count++;
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
                                nameLabel.setText(finalDianMingInfo[finalCount]);
                                nameLabel.repaint();
                            });

                            if (index >= finalDianMingInfo.length - 1) {
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
                    }

                    SwingUtilities.invokeLater(() -> {
                        nameLabel.setText(resultName);
                        nameLabel.repaint();
                    });

                    CTOptionPane.showFullScreenMessageDialog("点名结果", resultName, 0, 1);

                }).start();
            }
        });
        buttonPanel.add(dianMingButton);

        dialog.add(nameLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);


        return dialog;
    }
}

package com.wmp.classTools.frame.tools.cookie;

import com.wmp.Main;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.classTools.frame.ShowCookieDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class CookieSets {

    public static void CookieSetsDialog() {
        JDialog dialog = new JDialog();
        dialog.setSize(300, 175);
        dialog.setTitle("添加Cookie");
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        dialog.setLayout(null);
        dialog.setVisible(true);
    }
    public static void CookieSetsDialog(File file){
        if (file == null){
            JOptionPane.showMessageDialog(null, "文件为空", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog();
        SwingUtilities.invokeLater(() -> {

            dialog.setSize(300, 175);
            dialog.setTitle("修改Cookie" + file.getPath());
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);

            dialog.setLayout(null);
            dialog.setVisible(true);
        });

        //ZipPack.unzip(file.getRunPath(), Main.DATA_PATH + "\\Cookie\\");
    }
    public static void addCookie(File file){
        ZipPack.unzip(file.getPath(), Main.DATA_PATH + "\\Cookie\\", () -> {
            try {
                refreshParentWindow();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static void deleteCookie(File file) {
        final String DIALOG_TITLE = "世界拒绝了我";
        final int CONFIRM = JOptionPane.showConfirmDialog(null,
                "确认删除该 Cookie 配置？",
                DIALOG_TITLE,
                JOptionPane.YES_NO_OPTION);

        if (CONFIRM != JOptionPane.YES_OPTION) return;

        JDialog dialog = new JDialog();

        SwingUtilities.invokeLater(() -> {
            dialog.setSize(300, 80);
            dialog.setTitle("正在删除");
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);

            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            dialog.add(progressBar);

            dialog.setVisible(true);
        });

        new Thread(()->{
            new SwingWorker<Void, Void>() {
                //  执行耗时操作
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        if (file == null || !file.exists()) {
                            JOptionPane.showMessageDialog(null, "目标不存在", DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
                            return null;
                        }

                        if (file.isDirectory()) {
                            Files.walk(file.toPath())
                                    .sorted(Comparator.reverseOrder())
                                    .map(Path::toFile)
                                    .forEach(File::delete);
                        }

                        if (file.delete() || !file.exists()) {
                            JOptionPane.showMessageDialog(null, "删除成功", DIALOG_TITLE, JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            String errorType = file.canWrite() ? "文件被占用" : "权限不足";
                            JOptionPane.showMessageDialog(null, "删除失败：" + errorType, DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "文件遍历异常：" + e.getMessage(), DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
                    } catch (SecurityException e) {
                        JOptionPane.showMessageDialog(null, "安全限制：" + e.getMessage(), DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    // 执行完成，关闭进度条对话框
                    dialog.setVisible(false);
                    try {
                        refreshParentWindow();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.execute();
        }).start();



    }

    private static void refreshParentWindow() throws IOException {
        Window[] windows = Window.getWindows();
        for (Window w : windows) {
            if (w instanceof ShowCookieDialog) {
                ((ShowCookieDialog)w).refreshCookiePanel();
            }
        }
    }

}

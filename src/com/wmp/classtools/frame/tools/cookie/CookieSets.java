package com.wmp.classTools.frame.tools.cookie;

import com.wmp.Main;
import com.wmp.PublicTools.GetIcon;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.frame.ShowCookieDialog;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class CookieSets {

    public static void CookieSetsDialog() throws IOException, JSONException {
        CookieSetsDialog(null);
    }

    public static void CookieSetsDialog(File file) throws IOException, JSONException {
        JDialog dialog = new JDialog();
        Container container = dialog.getContentPane();
        String pin = "";
        Cookie cookie;

        if (file == null) {

            dialog.setTitle("添加Cookie");

            cookie = new Cookie();
        } else {
            File setsFile = new File(file, "setUp.json");
            if (!setsFile.exists()) {
                JOptionPane.showMessageDialog(dialog, "此插件无配置文件", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JSONObject jsonObject;
            {//读取文件
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(setsFile), StandardCharsets.UTF_8));
                StringBuilder s = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    s.append(line);
                }
                jsonObject = new JSONObject(s.toString());
            }
            System.out.println(jsonObject);
            pin = jsonObject.getString("pin");

            GetCookie getCookie = new GetCookie();
            cookie = getCookie.getCookieMap().get(pin);


            dialog.setTitle("修改Cookie");

        }


        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setResizable(false);
        container.setLayout(new BorderLayout());

        ArrayList<JPanel> setsPanelList = new ArrayList<>();
        final int[] index = {0};

        JTextField nameTextField = new JTextField();
        if (cookie.getName() != null){
            nameTextField.setText(cookie.getName());
        }

        JTextField pinTextField = new JTextField(pin);
        JComboBox<String> styleComboBox = new JComboBox<>();
        styleComboBox.addItem("exe");
        styleComboBox.addItem("video");
        styleComboBox.addItem("music");
        styleComboBox.addItem("image");
        styleComboBox.addItem("directory");
        styleComboBox.addItem("file");
        styleComboBox.addItem("other");
        styleComboBox.setSelectedItem(cookie.getStyle());// 设置默认选中
        JTextField iconTextField = new JTextField();
        if (cookie.getIcon() != null){
            iconTextField.setText(cookie.getIcon().toString());
        }
        JTextField runTextField = new JTextField();
        if (cookie.getRunPath() != null){
            runTextField.setText(cookie.getRunPath().toString());
        }
        StringBuilder parameters = new StringBuilder();
        cookie.getParameters().forEach(s -> {
            parameters.append(s).append(";");
        });
        JTextField parametersTextField = new JTextField(parameters.toString());

        //设置界面
        {
            JPanel helloPanel = new JPanel(new BorderLayout());

            JLabel helloLabel = new JLabel("请按下一步开始设置!");
            helloLabel.setFont(new Font("微软雅黑", -1, 25));
            helloPanel.add(helloLabel, BorderLayout.CENTER);

            JLabel iconLabel = new JLabel(GetIcon.getIcon(Main.class.getResource("/image/icon.png"), 100, 100));
            helloPanel.add(iconLabel, BorderLayout.WEST);

            setsPanelList.add(helloPanel);

            JPanel step1Panel = new JPanel(new GridLayout(7, 1, 7, 5));
            {
                step1Panel.setBorder(BorderFactory.createTitledBorder("设置插件配置文件"));

                JPanel namePanel = new JPanel();
                namePanel.setLayout(new GridLayout(1,2));
                namePanel.add(new JLabel("插件名称:"));
                namePanel.add(nameTextField);

                JPanel pinPanel = new JPanel();
                pinPanel.setLayout(new GridLayout(1,2));
                pinPanel.add(new JLabel("插件pin:"));
                pinPanel.add(pinTextField);

                JPanel stylePanel = new JPanel();
                stylePanel.setLayout(new GridLayout(1,2));
                stylePanel.add(new JLabel("插件样式:"));
                styleComboBox.setFont(new Font("微软雅黑", -1, 15));
                stylePanel.add(styleComboBox);

                JPanel iconPanel = new JPanel();
                iconPanel.setLayout(new GridLayout(1,2));
                iconPanel.add(new JLabel("插件图标路径:"));
                iconPanel.add(iconTextField);


                JPanel runPanel = new JPanel();
                runPanel.setLayout(new GridLayout(1,2));
                runPanel.add(new JLabel("运行指令:"));
                runPanel.add(runTextField);

                JPanel parametersPanel = new JPanel();
                parametersPanel.setLayout(new GridLayout(1,2));
                parametersPanel.add(new JLabel("运行参数:"));
                parametersPanel.add(parametersTextField);

                step1Panel.add(namePanel);
                step1Panel.add(pinPanel);
                step1Panel.add(stylePanel);
                step1Panel.add(iconPanel);
                step1Panel.add(runPanel);
                step1Panel.add(parametersPanel);
                if (!(styleComboBox.getSelectedItem() != null &&
                        styleComboBox.getSelectedItem().toString().equals("exe"))){
                    parametersPanel.setVisible(false);
                }
                styleComboBox.addItemListener(s -> {
                    if (s.getStateChange() == ItemEvent.SELECTED) {// 选中
                        if (Objects.requireNonNull(styleComboBox.getSelectedItem()).toString().equals("exe")) {
                            parametersPanel.setVisible(true);
                        } else {
                            parametersPanel.setVisible(false);
                        }
                        step1Panel.revalidate();
                        step1Panel.repaint();
                    }
                });

                setsPanelList.add(step1Panel);
            }

            JPanel step2Panel = new JPanel(new BorderLayout());
            {
                step2Panel.setBorder(BorderFactory.createTitledBorder("添加必要文件"));
                JLabel label = new JLabel("将点击打开插件目录,将必要文件添加至目录");
                label.setFont(new Font("微软雅黑", -1, 16));
                step2Panel.add(label, BorderLayout.NORTH);

                CTButton openDirButton = new CTButton(CTButton.ButtonText, "打开插件目录",
                        "/image/openExp.png", "/image/openExp.png", 30, 100, () -> {
                    File cookiePath = cookie.getCookiePath();
                    if (cookiePath == null || !cookiePath.exists()) {
                        try {
                            String finalPin = pinTextField.getText();
                            System.out.println(Main.DATA_PATH + "Cookie\\" + finalPin + "\\");
                            cookiePath = new File(Main.DATA_PATH + "Cookie\\" + finalPin + "\\");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        cookiePath.mkdirs();
                        System.out.println(cookiePath.getPath());
                    }
                    OpenInExp.open(cookiePath.getPath());
                });
                openDirButton.setBackground(Color.WHITE);
                openDirButton.setForeground(Color.BLACK);
                openDirButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
                step2Panel.add(openDirButton, BorderLayout.CENTER);

                setsPanelList.add(step2Panel);
            }

            JPanel step3Panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("设置完成");
            label.setFont(new Font("微软雅黑", -1, 25));
            label.setHorizontalAlignment(JLabel.CENTER);
            step3Panel.add(label, BorderLayout.CENTER);

            setsPanelList.add(step3Panel);

            container.add(setsPanelList.get(index[0]), BorderLayout.CENTER);
        }
        //设置按钮组
        {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            CTButton lastButton = new CTButton("上一步", 30, 100, () -> {
            });
            CTButton nextButton = new CTButton("下一步", 30, 100, () -> {
            });


            lastButton.setBackground(Color.WHITE);
            lastButton.setForeground(Color.BLACK);
            lastButton.setFont(new Font("微软雅黑", -1, 16));
            lastButton.setBorderPainted(true);
            lastButton.setEnabled(false);
            lastButton.setCallback(() -> {
                if (index[0] > 0) {
                    container.remove(setsPanelList.get(index[0]));
                    index[0]--;
                    container.add(setsPanelList.get(index[0]), BorderLayout.CENTER);

                }

                //当index已经改变后，修改按钮状态
                if (index[0] == 0) {
                    lastButton.setEnabled(false);
                }
                if (!(index[0] == setsPanelList.size() - 1)) {
                    nextButton.setText("下一步");
                }

                container.revalidate();
                container.repaint();
            });


            nextButton.setBackground(Color.WHITE);
            nextButton.setForeground(Color.BLACK);
            nextButton.setFont(new Font("微软雅黑", -1, 16));
            nextButton.setCallback(() -> {
                if (index[0] < setsPanelList.size() - 1) {
                    container.remove(setsPanelList.get(index[0]));
                    index[0]++;

                    container.add(setsPanelList.get(index[0]), BorderLayout.CENTER);

                } else {
                    //设置
                    JSONObject result = new JSONObject();
                    result.put("name", nameTextField.getText());
                    result.put("pin", pinTextField.getText());
                    result.put("style", Objects.requireNonNull(styleComboBox.getSelectedItem()).toString());
                    result.put("icon", iconTextField.getText());

                    result.put("run", runTextField.getText());
                    if (styleComboBox.getSelectedItem().toString().equals("exe")){
                        String[] split = parametersTextField.getText().split(";");
                        ArrayList<String> parametersList = new ArrayList<>(Arrays.asList(split));
                        result.put("parameters", parametersList);
                    }

                    String cookiePath;
                    if (cookie.getCookiePath() == null || !cookie.getCookiePath().exists()){
                        cookiePath = Main.DATA_PATH + "Cookie\\" + pinTextField.getText() + "\\";
                        new File(cookiePath).mkdirs();
                    }else{
                        cookiePath = cookie.getCookiePath().getPath();
                    }

                    try {
                        Files.write(Paths.get(cookiePath, "setUp.json"),
                                result.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        JOptionPane.showMessageDialog(dialog, "设置完成", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(dialog, "设置失败\n" + e.getMessage(), "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(e);
                    }


                    dialog.setVisible(false);
                    try {
                        refreshParentWindow();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                //当index已经改变后，修改按钮文字
                if (index[0] == setsPanelList.size() - 1) {
                    nextButton.setText("完成");
                }
                if (index[0] != 0) {
                    lastButton.setEnabled(true);
                }

                container.revalidate();
                container.repaint();
            });
            nextButton.setBorderPainted(true);

            buttonPanel.add(lastButton);
            buttonPanel.add(nextButton);

            container.add(buttonPanel, BorderLayout.SOUTH);
        }


        dialog.setVisible(true);
        //ZipPack.unzip(file.getRunPath(), Main.DATA_PATH + "\\Cookie\\");
    }

    public static void addCookie(File file) {
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

        new Thread(() -> {
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
                ((ShowCookieDialog) w).refreshCookiePanel();
            }
        }
    }

}

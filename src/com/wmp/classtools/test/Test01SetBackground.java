package com.wmp.classTools.test;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.IOForInfo;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test01SetBackground extends JFrame {
    public Test01SetBackground() throws HeadlessException, IOException {
        this.setTitle("设置背景");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);

        File BGPath = new File(CTInfo.DATA_PATH + "\\ScreenProduct\\background.json");
        if (!BGPath.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(BGPath);
                fileWriter.write("{}");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONObject jsonObject = new JSONObject(new IOForInfo(BGPath).GetInfos());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //背景
        {
            JLabel viewLabel = new JLabel();
            viewLabel.setBounds(0, 0, 400, 300);
            if (jsonObject.has("path")) {
                ImageIcon icon = new ImageIcon(jsonObject.getString("path"));

                icon.setImage(icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH));

                viewLabel.setIcon(icon);
            }

            //JPanel jPanel = new JPanel(null);
            //jPanel.add(viewLabel);

            this.getLayeredPane().add(viewLabel, Integer.valueOf(Integer.MIN_VALUE));
        }

        JPanel pane = (JPanel) this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.setOpaque(false);//透明

        //this.getContentPane().add(jPanel, BorderLayout.CENTER);
        //this.getContentPane().setLayout(new GridLayout(2, 1));

        JLabel name = new JLabel("请选择图片");
        pane.add(name, BorderLayout.NORTH);

       /* JButton button = new JButton("选择图片");
        this.getContentPane().add(button);*/


        this.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new Test01SetBackground();
    }
}

package com.wmp.classTools.test;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Test02SetBackground extends JFrame {
    public Test02SetBackground() throws HeadlessException, IOException {
        this.setTitle("设置背景");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        String[] exitStrList = {
                "愿此行，终抵群星",
                "我们终将重逢",
                "明天见",
                "聚散终有时，山水有相逢。",
                "樱花凋零时，方知一期一会。",
        };
        String exitStr = exitStrList[new Random().nextInt(exitStrList.length)];


        JLabel viewLabel = new JLabel();
        {


            //背景
            {

                viewLabel.setBounds(0, 0, screenSize.width, screenSize.height);

                String imageStr = switch (exitStr) {
                    case "我们终将重逢" -> "wmzjcf.png";
                    case "明天见" -> "mtj.png";
                    case "愿此行，终抵群星" -> "ycxzdqx.png";
                    default -> "";
                };

                if (imageStr.isEmpty()) {
                    ImageIcon icon = new ImageIcon(Main.class.getResource("/image/exitBG/wmzjcf.png"));// + imageStr

                    icon.setImage(icon.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH));

                    viewLabel.setIcon(icon);

                    viewLabel.revalidate();
                    viewLabel.repaint();
                } else {
                    viewLabel.setBackground(CTColor.backColor);
                }

            }
        }
        this.getLayeredPane().add(viewLabel, Integer.valueOf(Integer.MIN_VALUE));

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
        new Test02SetBackground();
    }
}

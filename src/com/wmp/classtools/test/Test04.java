package com.wmp.classTools.test;

import com.wmp.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Scanner;

public class Test04 {
    public static void main(String[] args) throws InterruptedException {

        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入等待时间(ms):");
            int num = scanner.nextInt();
            if (num == 0){
                System.exit(0);
            }

            JDialog window = new JDialog();
            ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/image/openEasterEgg.gif")));
            Image image = imageIcon.getImage();

            int iconHeight = imageIcon.getIconHeight();
            int iconWidth = imageIcon.getIconWidth();

            imageIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_DEFAULT);
            /*imageIcon.setImageObserver((img, infoflags, x, y, width, height) -> {
                if ((infoflags & ImageObserver.ALLBITS) != 0) {
                    // 当图片加载完成后启动关闭计时器
                    Timer timer = new Timer(100, e -> window.dispose());
                    timer.setRepeats(false);
                    timer.start();
                }
                return true;
            });*/
            JLabel label = new JLabel("", imageIcon, SwingConstants.CENTER);

            window.setSize(iconWidth, iconHeight);
            window.setLocationRelativeTo(null);
            window.setAlwaysOnTop(true);
            window.add(label);

            window.setVisible(true);
            Thread.sleep(num);
            window.dispose();
        }
    }
}

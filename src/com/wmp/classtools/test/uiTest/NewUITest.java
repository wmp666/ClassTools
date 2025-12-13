package com.wmp.classTools.test.uiTest;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.classTools.CTComponent.CTCheckBox;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.CTProgressBar;

import javax.imageio.spi.ImageInputStreamSpi;
import javax.swing.*;
import java.awt.*;

public class NewUITest {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        new CTInfo();

        CTOptionPane.showSystemStyleMessageDialog(TrayIcon.MessageType.ERROR, "New UI Test", "New UI Test");
        CTOptionPane.showSystemStyleMessageDialog(TrayIcon.MessageType.INFO, "信息", "New UI Test");
        CTOptionPane.showSystemStyleMessageDialog(TrayIcon.MessageType.WARNING, "New UI Test", "警告");


        JFrame frame = new JFrame("New UI Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(0, 1));
        frame.setAlwaysOnTop(true);

        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.BIG));
        JPanel preview = new JPanel();
        preview.setBorder(BorderFactory.createTitledBorder("预览"));
        colorChooser.setPreviewPanel(preview);

        System.out.println(JColorChooser.showDialog(frame, "颜色选择", Color.BLACK));

        frame.setVisible(true);
    }
}

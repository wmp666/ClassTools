package com.wmp.classTools.test;

import com.wmp.classTools.CTComponent.CTProgressBar;

import javax.swing.*;
import java.awt.*;

public class NewUITest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("New UI Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(0, 1));
        frame.setAlwaysOnTop(true);

        CTProgressBar progressBar = new CTProgressBar();
        progressBar.setValue(0);

        Timer timer = new Timer(200, e -> {
            int value = progressBar.getValue();
            if (value < 100) {
                progressBar.setValue(value + 1);
            } else {
                progressBar.setValue(0);
            }
            System.out.println(value);
        });
        timer.start();

        CTProgressBar progressBar2 = new CTProgressBar();
        progressBar2.setIndeterminate(true);

        frame.add(progressBar);
        frame.add(progressBar2);


        frame.setVisible(true);
    }
}

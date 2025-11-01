package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CTSpinner extends JPanel {

    CTTextField textField = new CTTextField();

    public CTSpinner() {
        this(new SpinnerNumberModel());
    }

    public CTSpinner(SpinnerModel model) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);


        CTTextButton last = new CTTextButton("<", false);
        CTTextButton next = new CTTextButton(">", false);

        textField.setText(model.getValue().toString());

        last.setFocusable(false);
        next.setFocusable(false);

        last.addActionListener(e -> {
            setPrevious(model);
        });
        next.addActionListener(e -> {
            setNext(model);
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT
                    || keyCode == KeyEvent.VK_KP_DOWN || keyCode == KeyEvent.VK_KP_RIGHT) {
                    //下移数字
                    setNext(model);
                } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_LEFT
                        || keyCode == KeyEvent.VK_KP_UP || keyCode == KeyEvent.VK_KP_LEFT) {
                    //上移数字

                    setPrevious(model);
                }
            }
        });
        this.add(last, BorderLayout.WEST);
        this.add(textField, BorderLayout.CENTER);
        this.add(next, BorderLayout.EAST);
    }

    private void setNext(SpinnerModel model) {
        Object nextValue = model.getNextValue();
        if (nextValue != null) {
            model.setValue(nextValue);
            textField.setText(nextValue.toString());
        }
    }

    private void setPrevious(SpinnerModel model) {
        Object previousValue = model.getPreviousValue();
        if (previousValue != null) {
            model.setValue(previousValue);
            textField.setText(previousValue.toString());
        }
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public String getValue() {
        return textField.getText();
    }
}

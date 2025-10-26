package com.wmp.classTools.frame;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.CTTool.CTTool;
import com.wmp.PublicTools.CTTool.dianMing.DianMingTool;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.classTools.CTComponent.CTButton.CTRoundTextButton;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class CTTools extends JDialog {
    public CTTools() {
        new CTTools(0);
        new CTTools(1);
    }

    private CTTools(int style){

        initFrame();

        Timer timer = new Timer(10000, e -> {
            this.setAlwaysOnTop(true);
        });
        timer.start();

        ArrayList<CTTool> tools = new ArrayList<>();

        tools.add(new DianMingTool());

        CTRoundTextButton openButton = new CTRoundTextButton(style == 0?"<":">");
        openButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        openButton.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("快捷工具");
            dialog.setModal(true);
            dialog.setLayout(new GridLayout(0,1));

            tools.forEach(tool -> {
                CTRoundTextButton button = new CTRoundTextButton(tool.getName());
                button.addActionListener(ex -> {
                    tool.showTool();
                });
                dialog.getContentPane().add(button);
            });

            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

        });
        this.add(openButton, BorderLayout.CENTER);


        this.pack();
        if (style == 0){
            this.setShape(new RoundRectangle2D.Double(0, 0, this.getWidth() + CTInfo.arcw, this.getHeight(), CTInfo.arcw, CTInfo.arch));
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(screenSize.width - this.getWidth(), screenSize.height * 3 / 5);
        }else{
            this.setShape(new RoundRectangle2D.Double(-CTInfo.arcw, 0, this.getWidth() + CTInfo.arcw, this.getHeight(), CTInfo.arcw, CTInfo.arch));
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(0, screenSize.height * 3 / 5);
        }
        this.setVisible(true);
    }

    private void initFrame() {
        this.setTitle("快捷工具");
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setUndecorated( true);
        this.setAlwaysOnTop(true);
        this.setSize(500, 500);
        this.setLayout(new BorderLayout());

        this.setBackground(new Color(172, 172, 172));
    }
}

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
    private static final ArrayList<CTTools> oldClass = new ArrayList<>();
    private static final ArrayList<CTTool> tools = new ArrayList<>();
    public CTTools() {
        oldClass.forEach(CTTools::dispose);
        oldClass.clear();
        new CTTools(0);
        new CTTools(1);
    }

    private CTTools(int style){

        oldClass.add(this);

        initFrame();


        tools.clear();
        tools.add(new DianMingTool());

        CTRoundTextButton openButton = new CTRoundTextButton(style == 0?"<":">");
        openButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        openButton.addActionListener(e -> {
            showDialog();

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

    public static void showDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("快捷工具");
        dialog.setModal(true);
        dialog.setLayout(new GridLayout(0,1, (int) (5 * CTInfo.dpi), (int) (5 * CTInfo.dpi)));

        tools.forEach(tool -> {
            CTRoundTextButton button = new CTRoundTextButton(tool.getName());
            button.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
            button.addActionListener(ex -> {
                tool.showTool();
            });
            dialog.getContentPane().add(button);
        });

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void initFrame() {
        this.setTitle("快捷工具");
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setUndecorated( true);
        this.setAlwaysOnTop(true);
        this.setSize(500, 500);
        this.setLayout(new BorderLayout());

        this.getContentPane().setBackground(CTColor.backColor);
    }
}

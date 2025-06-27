package com.wmp.classTools.extraPanel.duty.panel;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.PeoPanelProcess;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.io.InfProcess;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTIconButton;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.extraPanel.duty.settings.DutyListSetsPanel;
import com.wmp.classTools.extraPanel.duty.type.DutyDay;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DPanel extends CTPanel {


    private final ArrayList<DutyDay> DutyList = new ArrayList<>();
    private int index; //当前日期索引
    private final File DutyListPath;
    private final File indexPath;

    public DPanel(File DutyListPath, File indexPath) throws IOException {

        this.DutyListPath = DutyListPath;
        this.indexPath = indexPath;

        ArrayList<CTSetsPanel> setsPanelList = new ArrayList<>();
        setsPanelList.add(new DutyListSetsPanel(Main.DATA_PATH));
        this.setCtSetsPanelList(setsPanelList);
        this.setName("DPanel");
        //设置容器布局- 绝对布局
        this.setLayout(new BorderLayout());

        initDutyList(DutyListPath);

        initIndex(indexPath);

        initContainer();



    }

    private void initContainer() throws IOException {
        JPanel InfoPanel = new JPanel();
        InfoPanel.setLayout(new GridBagLayout());
        InfoPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        //gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;// 左对齐

        JLabel CLBBLabel = new JLabel();
        CLBBLabel.setText("擦黑板:");
        CLBBLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        CLBBLabel.setForeground(CTColor.textColor);

        InfoPanel.add(CLBBLabel, gbc);

        DutyDay now = new DutyDay(DutyDay.setDutyPersonList("数据异常"), DutyDay.setDutyPersonList("数据异常"));
        try {
            now = DutyList.get(index);
        } catch (Exception e) {
            new IOForInfo(indexPath).SetInfo("0");
            Log.err.print("CTPanel-DutyPanel", "数据异常,请检查数据文件\n问题:" + e.getMessage());
            throw new RuntimeException(e);
        }

        initPeople(now.getClBlackBroadList(), gbc, InfoPanel);


        JLabel CLFLabel = new JLabel();
        CLFLabel.setText("扫地:");
        CLFLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        CLFLabel.setForeground(CTColor.textColor);
        gbc.gridy++;
        InfoPanel.add(CLFLabel, gbc);


        initPeople(now.getClFloorList(), gbc, InfoPanel);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setOpaque(false);

        {

            CTIconButton last = new CTIconButton("上一天",
                    "/image/%s/last_0.png",
                    "/image/%s/last_1.png", 30, () -> {
                int i = Log.info.showChooseDialog(this, "CTPanel-DutyPanel-日期切换", "确认切换至上一天");
                if (i == 0) {
                    if (index > 0) index--;
                    else index = DutyList.size() - 1;
                }


                try {
                    new IOForInfo(indexPath).SetInfo(String.valueOf(index));
                    refresh();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            buttonPanel.add(last, BorderLayout.WEST);
        }

        {

            CTIconButton next = new CTIconButton("下一天",
                    "/image/%s/next_0.png",
                    "/image/%s/next_1.png", 30, () -> {

                int i = Log.info.showChooseDialog(this, "CTPanel-DutyPanel-日期切换", "确认切换至下一天");

                if (i == 0) {
                    if (index < DutyList.size() - 1) index++;

                    else index = 0;
                }


                try {
                    new IOForInfo(indexPath).SetInfo(String.valueOf(index));
                    refresh();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            buttonPanel.add(next, BorderLayout.EAST);
        }


        this.add(InfoPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.NORTH);
    }

    private void initPeople(ArrayList<String> array, GridBagConstraints gbc, Container c) {
        if (array == null || array.isEmpty()) {
            return;
        }

        JScrollPane showPeoPanel = PeoPanelProcess.getShowPeoPanel(array);

        gbc.gridy++;
        c.add(showPeoPanel, gbc);

    }


    //初始化索引
    private void initIndex(File indexPath) throws IOException {
        IOForInfo ioForInfo = new IOForInfo(indexPath);

        String[] inf = ioForInfo.GetInfo();

        //System.out.println(inf);
        if (inf[0].equals("err")) {
            //将数据改为默认-空,需要用户自行输入数据
            index = 0;
            ioForInfo.SetInfo("0");
        }else{
            index = Integer.parseInt(inf[0]);
        }
        Log.info.print("DPanel-initIndex", "值日索引:" + index);
    }

    //初始化数据
    private void initDutyList(File dutyPath) throws IOException {
        //获取inf
        IOForInfo ioForInfo = new IOForInfo(dutyPath);

        //System.out.println("DutyPath:" + dutyPath);

        String[] inf = ioForInfo.GetInfo();


        if (inf[0].equals("err")) {
            //将数据改为默认-空,需要用户自行输入数据

            ioForInfo.SetInfo("[尽快,设置] [请]",
                    "[尽快,设置,0] [请]");

            inf = new String[]{"[尽快,设置] [请]",
                    "[尽快,设置,0] [请]"};
        } else if (inf[0].equals("null")) {
            //总会有的
        }

        //处理inf
        DutyList.clear();
        String[] inftempList = inf;
        for (String s : inftempList) {
            ArrayList<String[]> strings = InfProcess.RDExtractNames(s);

            try {
                DutyList.add(new DutyDay(DutyDay.setDutyPersonList(strings.get(0)),
                        DutyDay.setDutyPersonList(strings.get(1))));
            } catch (Exception e) {
                if (strings.size() <= 2){
                    Log.err.print(this, "CTPanel-DutyPanel-初始化数据", "请检查数据格式是否正确");
                }
                throw new RuntimeException(e);
            }
        }

        Log.info.print("DPanel-initDutyList", "值日数据:" + DutyList);
    }


    // 刷新方法
    @Override
    public void refresh() throws IOException {

        this.removeAll();
        initDutyList(DutyListPath);
        initIndex(indexPath);
        initContainer();
        //repaint();
        //revalidate();


    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

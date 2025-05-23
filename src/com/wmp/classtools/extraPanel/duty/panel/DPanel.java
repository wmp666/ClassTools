package com.wmp.classTools.extraPanel.duty.panel;

import com.wmp.Main;
import com.wmp.PublicTools.InfProcess;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.PeoPanelProcess;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton;
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
    private int DPanelMaxWidth = 250;
    private int DPanelMixY; //面板的高度
    private int index; //当前日期索引
    private final File DutyListPath;
    private final File indexPath;

    public DPanel(int mixY,File DutyListPath, File indexPath) throws IOException {

        this.DutyListPath = DutyListPath;
        this.indexPath = indexPath;
        appendNextPanelY(mixY);

        ArrayList<CTSetsPanel> setsPanelList = new ArrayList<>();
        setsPanelList.add(new DutyListSetsPanel(Main.DATA_PATH));
        this.setCtSetsPanelList(setsPanelList);

        //设置容器布局- 绝对布局
        this.setLayout(null);

        initDutyList(DutyListPath);

        initIndex(indexPath);

        initContainer();

        this.setSize(DPanelMaxWidth, DPanelMixY + 5);
        appendNextPanelY(DPanelMixY);

    }

    private void initContainer() throws IOException {
        JLabel CLBBLabel = new JLabel();
        CLBBLabel.setText("擦黑板:");
        CLBBLabel.setFont(new Font("微软雅黑",Font.BOLD,20));
        CLBBLabel.setForeground(CTColor.textColor);
        CLBBLabel.setBounds(5,5,100,22);
        this.add(CLBBLabel);
        DPanelMixY = DPanelMixY + CLBBLabel.getHeight() + 2;

        DutyDay now = new DutyDay(DutyDay.setDutyPersonList("数据异常"), DutyDay.setDutyPersonList("数据异常"));
        try {
            now = DutyList.get(index);
        } catch (Exception e) {
            new IOForInfo(indexPath).SetInfo("0");
            Log.err.print("CTPanel-DutyPanel", "数据异常,请检查数据文件\n问题:" + e.getMessage());
            throw new RuntimeException(e);
        }

        int[] tempList01 = initPeople(now.getClBlackBroadList(), DPanelMixY);
        DPanelMixY = tempList01[0];

        JLabel CLFLabel = new JLabel();
        CLFLabel.setText("扫地:");
        CLFLabel.setFont(new Font("微软雅黑",Font.BOLD,20));
        CLFLabel.setForeground(CTColor.textColor);
        CLFLabel.setBounds(5,DPanelMixY + 2,100,22);
        this.add(CLFLabel);
        DPanelMixY = DPanelMixY + CLFLabel.getHeight() + 2;


        int[] tempList02 = initPeople(now.getClFloorList(), DPanelMixY);
        DPanelMixY = tempList02[0];


        {

            CTButton next = new CTButton("下一天",
                    "/image/%s/next_0.png",
                    "/image/%s/next_1.png", 30, () -> {

                int i = Log.info.inputInt(this, "CTPanel-DutyPanel-日期切换", "确认切换至下一天");

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
            //next.setFont(new Font("微软雅黑", -1, 10));
            next.setLocation(215, 0);
            this.add(next);
        }

        {

            CTButton last = new CTButton("上一天",
                    "/image/%s/last_0.png",
                    "/image/%s/last_1.png", 30, () -> {
                int i = Log.info.inputInt(this, "CTPanel-DutyPanel-日期切换", "确认切换至上一天");
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
            last.setLocation(175, 0);
            this.add(last);
        }

    }

    private int[] initPeople(ArrayList<String> array, int mixY) {
        if (array == null || array.isEmpty()) {
            return new int[]{mixY, 0}; // 空集合直接返回基准值（根据业务需求调整）
        }

        Object[] o = PeoPanelProcess.getPeopleName(array);

        JLabel personLabel = new JLabel(String.valueOf(o[0]));
        personLabel.setFont(new Font("微软雅黑", Font.BOLD, 23));
        personLabel.setBackground(CTColor.backColor);
        personLabel.setForeground(CTColor.mainColor);
        personLabel.setBounds(5, mixY + 3,
                Integer.parseInt(String.valueOf(o[2])) * 23 + 8,
                30 * Integer.parseInt(String.valueOf(o[1])));

        DPanelMaxWidth = Math.max(DPanelMaxWidth, personLabel.getWidth());
        this.add(personLabel);

        return new int[]{personLabel.getY() + personLabel.getHeight(), personLabel.getHeight()}; // 可替换为常量提升可读性
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

        DPanelMaxWidth = 250;
        DPanelMixY = 0;
        this.removeAll();
        initDutyList(DutyListPath);
        initIndex(indexPath);
        initContainer();
        //repaint();
        //revalidate();

        this.setSize(DPanelMaxWidth, DPanelMixY + 5);

        appendNextPanelY(DPanelMixY);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

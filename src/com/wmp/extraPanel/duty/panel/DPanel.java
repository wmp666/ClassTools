package com.wmp.extraPanel.duty.panel;

import com.wmp.CTColor;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.extraPanel.duty.type.DutyDay;
import com.wmp.PublicTools.InfProcess;
import com.wmp.PublicTools.PeoPanelProcess;
import com.wmp.PublicTools.io.IOStreamForInf;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DPanel extends CTPanel {


    private final ArrayList<DutyDay> DutyList = new ArrayList<>();
    private int DPanelMixY;
    private int index;
    private final File DutyListPath;
    private final File indexPath;

    public DPanel(int mixY,File DutyListPath, File indexPath) throws IOException {

        this.DutyListPath = DutyListPath;
        this.indexPath = indexPath;
        setNextPanelY(mixY);

        //设置容器布局- 绝对布局
        this.setLayout(null);

        initDutyList(DutyListPath);

        initIndex(indexPath);

        initContainer(DutyListPath);

        this.setSize(250,DPanelMixY + 5);
        setNextPanelY(DPanelMixY);

    }

    private void initContainer(File DutyListPath) throws IOException {
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
            new IOStreamForInf(indexPath).SetInf("0");
            JOptionPane.showMessageDialog(null, "数据异常,请检查数据文件\n问题:" + e.getMessage(), "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            //throw new RuntimeException(e);
        }

        DPanelMixY = initPeople(now.getClBlackBroadList(), DPanelMixY);

        JLabel CLFLabel = new JLabel();
        CLFLabel.setText("扫地:");
        CLFLabel.setFont(new Font("微软雅黑",Font.BOLD,20));
        CLFLabel.setForeground(CTColor.textColor);
        CLFLabel.setBounds(5,DPanelMixY + 2,100,22);
        this.add(CLFLabel);
        DPanelMixY = DPanelMixY + CLFLabel.getHeight() + 2;


        DPanelMixY = initPeople(now.getClFloorList(), DPanelMixY);

        {

            CTButton next = new CTButton("下一天",
                    "/image/%s/next_0.png",
                    "/image/%s/next_1.png", 30, () -> {

                int i = JOptionPane.showConfirmDialog(this, "确认切换至下一天", "询问", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    if (index < DutyList.size() - 1) index++;

                    else index = 0;
                }


                try {
                    new IOStreamForInf(indexPath).SetInf(String.valueOf(index));
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
                int i = JOptionPane.showConfirmDialog(this, "确认切换至上一天", "询问", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    if (index > 0) index--;
                    else index = DutyList.size() - 1;
                }


                try {
                    new IOStreamForInf(indexPath).SetInf(String.valueOf(index));
                    refresh();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            last.setLocation(175, 0);
            this.add(last);
        }

    }

    private int initPeople(ArrayList<String> array, int mixY) {
        if (array == null || array.isEmpty()) {
            return mixY; // 空集合直接返回基准值（根据业务需求调整）
        }

        Object[] o = PeoPanelProcess.getPeopleName(array);

        JLabel personLabel = new JLabel(String.valueOf(o[0]));
        personLabel.setFont(new Font("微软雅黑", Font.BOLD, 23));
        personLabel.setBackground(CTColor.backColor);
        personLabel.setForeground(CTColor.mainColor);
        personLabel.setBounds(5, mixY + 3, 250, 30 * Integer.parseInt(String.valueOf(o[1])));

        this.add(personLabel);

        return personLabel.getY() + personLabel.getHeight(); // 可替换为常量提升可读性
    }


    //初始化索引
    private void initIndex(File indexPath) throws IOException {
        IOStreamForInf ioStreamForInf = new IOStreamForInf(indexPath);

        String[] inf = ioStreamForInf.GetInf();

        //System.out.println(inf);
        if(inf[0].equals("error")){
            //将数据改为默认-空,需要用户自行输入数据
            index = 0;
            ioStreamForInf.SetInf("0");
        }else{
            index = Integer.parseInt(inf[0]);
        }
        System.out.println("index:" + index);
    }

    //初始化数据
    private void initDutyList(File dutyPath) throws IOException {
        //获取inf
        IOStreamForInf ioStreamForInf = new IOStreamForInf(dutyPath);

        //System.out.println("DutyPath:" + dutyPath);

        String[] inf = ioStreamForInf.GetInf();

        System.out.println(inf);
        if(inf[0].equals("error")){
            //将数据改为默认-空,需要用户自行输入数据

            ioStreamForInf.SetInf("[尽快,设置] [请]",
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
                    JOptionPane.showMessageDialog(this, "请检查数据格式是否正确", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                }
                throw new RuntimeException(e);
            }
        }

    }


    // 刷新方法
    @Override
    public void refresh() throws IOException {

        DPanelMixY = 0;
        this.removeAll();
        initDutyList(DutyListPath);
        initIndex(indexPath);
        initContainer(DutyListPath);
        //repaint();
        //revalidate();

        this.setSize(250,DPanelMixY + 5);

        setNextPanelY(DPanelMixY);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

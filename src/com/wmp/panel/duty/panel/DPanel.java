package com.wmp.panel.duty.panel;

import com.wmp.classtools.CTComponent.DuButton;
import com.wmp.classtools.CTComponent.CTPanel;
import com.wmp.io.IOStreamForInf;
import com.wmp.panel.duty.type.DutyDay;
import com.wmp.tools.InfProcess;

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
        setMixY(mixY);

        //设置容器布局- 绝对布局
        this.setLayout(null);

        initDutyList(DutyListPath);

        initIndex(indexPath);

        initContainer(DutyListPath);

        this.setSize(250,DPanelMixY + 5);
        setMixY(DPanelMixY);

    }

    private void initContainer(File DutyListPath) {
        JLabel CLBBLabel = new JLabel();
        CLBBLabel.setText("擦黑板:");
        CLBBLabel.setFont(new Font("微软雅黑",Font.BOLD,20));
        CLBBLabel.setBounds(5,5,100,22);
        this.add(CLBBLabel);
        DPanelMixY = DPanelMixY + CLBBLabel.getHeight() + 2;

        DutyDay now = DutyList.get(index);

        DPanelMixY = initPeople(now.getClBlackBroad(), DPanelMixY);

        JLabel CLFLabel = new JLabel();
        CLFLabel.setText("扫地:");
        CLFLabel.setFont(new Font("微软雅黑",Font.BOLD,20));
        CLFLabel.setBounds(5,DPanelMixY + 2,100,22);
        this.add(CLFLabel);
        DPanelMixY = DPanelMixY + CLFLabel.getHeight() + 2;


        DPanelMixY = initPeople(now.getClFloor(), DPanelMixY);


//        JButton settings = new JButton("设置");
//        settings.setBounds(5,DPanelMixY + 5,100,32);
//        // 在settings按钮事件处修改
//        settings.addActionListener(e -> {
//            DutyDay current = DutyList.get(index);
//            new InfSetDialog(null, DutyListPath, current,index, () -> {
//                try {
//                    refreshDisplay();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }).setVisible(true);
//        });
//        this.add(settings);

        {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/next_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/image/next_1.png"));
            imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            DuButton next = new DuButton(imageIcon,imageIcon2, 30, () -> {

                int i = JOptionPane.showConfirmDialog(this, "确认切换至下一天", "询问", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    if (index < DutyList.size() - 1) index++;

                    else index = 0;
                }


                try {
                    new IOStreamForInf(indexPath).SetInf(String.valueOf(index));
                    refreshDisplay();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            //next.setFont(new Font("微软雅黑", -1, 10));
            next.setLocation(215, 0);
            this.add(next);
        }

        {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/last_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/image/last_1.png"));
            imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            DuButton last = new DuButton(imageIcon, imageIcon2, 30, () -> {
                int i = JOptionPane.showConfirmDialog(this, "确认切换至上一天", "询问", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    if (index > 0) index--;
                    else index = DutyList.size() - 1;
                }


                try {
                    new IOStreamForInf(indexPath).SetInf(String.valueOf(index));
                    refreshDisplay();
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

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        int size = array.size();
        int index = (size + 2) / 3;  // 修正组数计算逻辑

        for (int i = 0; i < index; i++) {
            int base = i * 3;
            sb.append(array.get(base));

            if (base + 1 < size) {
                sb.append(",").append(array.get(base + 1));
            }
            if (base + 2 < size) {
                sb.append(",").append(array.get(base + 2));
            }

            if (i < index - 1) {  // 仅在非最后一组后添加换行
                sb.append("<br>");
            }
        }

        sb.append("</html>");


        JLabel clbbPersonLabel = new JLabel(sb.toString());
        clbbPersonLabel.setFont(new Font("微软雅黑", Font.BOLD, 23));
        clbbPersonLabel.setForeground(new Color(0x0090FF));
        clbbPersonLabel.setBounds(5, mixY + 3, 250, 30 * index);

        this.add(clbbPersonLabel);

        return clbbPersonLabel.getY() + clbbPersonLabel.getHeight(); // 可替换为常量提升可读性
    }


    //初始化索引
    private void initIndex(File indexPath) throws IOException {
        IOStreamForInf ioStreamForInf = new IOStreamForInf(indexPath);

        String inf = ioStreamForInf.GetInf();

        //System.out.println(inf);
        if(inf.isEmpty() || inf.equals("error")){
            //将数据改为默认-空,需要用户自行输入数据
            index = 0;
            ioStreamForInf.SetInf("0");
        }else{
            index = Integer.parseInt(inf);
        }
        System.out.println("index:" + index);
    }

    //初始化数据
    private void initDutyList(File dutyPath) throws IOException {
        //获取inf
        IOStreamForInf ioStreamForInf = new IOStreamForInf(dutyPath);

        //System.out.println("DutyPath:" + dutyPath);

        String inf = ioStreamForInf.GetInf();

        //System.out.println(inf);
        if(inf.equals("null")){
            //将数据改为默认-空,需要用户自行输入数据

            ioStreamForInf.SetInf("""
        [请] [尽快,设置]
        [请] [尽快,设置,0]
        """);
        } else if (inf.equals("error")) {
            //总会有的
        }

        //处理inf
        DutyList.clear();
        String[] inftempList = inf.split("\n");
        for (String s : inftempList) {
            ArrayList<String[]> strings = InfProcess.RDExtractNames(s);

            DutyList.add(new DutyDay(DutyDay.setDutyPersonList(strings.get(0)),
                    DutyDay.setDutyPersonList(strings.get(1))));
        }

        System.out.println(DutyList);
    }


    // 刷新方法
    public void refreshDisplay() throws IOException {

        DPanelMixY = 0;
        this.removeAll();
        initDutyList(DutyListPath);
        initIndex(indexPath);
        initContainer(DutyListPath);
        //repaint();
        //revalidate();

        this.setSize(250,DPanelMixY + 5);

        setMixY(DPanelMixY);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

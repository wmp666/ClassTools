package com.wmp.classTools.frame.tools.cookie;

import com.wmp.PublicTools.printLog.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileDragDropLabel extends JLabel {

    private final ArrayList<File> fileList = new ArrayList<>();
    //private final JLabel statusLabel;
	//private final JPanel this;

    public FileDragDropLabel() {

        //this.setLayout(new BorderLayout());
        this.setBackground(new Color(240, 240, 240));
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        
        this.setText("添加");
        this.setFont(new Font("微软雅黑", Font.BOLD, 16));

        JLabel temp = this;
        // 设置拖拽支持
        new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                temp.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                updateStatus("释放鼠标上传文件", Color.BLUE);
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                resetPanelAppearance();
            }

            @Override
            public void drop(DropTargetDropEvent event) {
                try {
                    // 接受拖拽操作
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    
                    // 获取拖拽文件列表
                    // 修改后代码片段（添加类型检查与安全标注）
                    @SuppressWarnings("unchecked")
                    List<File> transferredList = (List<File>)
                            event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    fileList.clear();
                    transferredList.forEach(file -> {
                        String fileName = file.getName();
                        if (fileName.endsWith(".zip")){
                            fileList.add(file);
                        }else{
                            Log.err.print("插件管理页-文件上传", "文件不是zip文件");
                        }
                    });

                    // 处理文件上传
                    handleFileUpload(fileList);
                    
                    event.dropComplete(true);
                } catch (Exception ex) {
                    event.dropComplete(false);
                    updateStatus("上传失败: " + ex.getMessage(), Color.RED);
                }
                resetPanelAppearance();
            }
        });

    }

    private void handleFileUpload(List<File> files) {
        // 使用SwingWorker防止阻塞UI线程
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (File file : files) {
                    // 模拟文件上传处理
                    Thread.sleep(1000); // 替换为实际上传逻辑
                    SwingUtilities.invokeLater(() -> {
                        //updateStatus("已上传", new Color(0, 150, 0)));

                        Log.info.print("插件管理页-添加文件", "文件位置:" + file.getPath());
                        CookieSets.addCookie(file);
                    });
                }
                updateStatus("添加", Color.BLACK);
                return null;
            }
        }.execute();
    }

    private void resetPanelAppearance() {
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        updateStatus("添加", Color.BLACK);
    }

    private void updateStatus(String text, Color color) {
        //this.setHorizontalAlignment(JLabel.CENTER);
        this.setText(text);
        this.setForeground(color);
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FileDragDropLabel().setVisible(true);
        });
    }*/
}
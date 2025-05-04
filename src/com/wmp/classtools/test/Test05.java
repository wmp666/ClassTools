package com.wmp.classTools.test;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

public class Test05 extends JFrame {

    //文件拖拽
    private final JLabel statusLabel;
	private final JPanel dropPanel;

    public Test05() {
        this.setTitle("文件拖拽上传示例");
        this.setSize(400, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        dropPanel = new JPanel(new BorderLayout());
        dropPanel.setBackground(new Color(240, 240, 240));
        dropPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        
        statusLabel = new JLabel("拖拽文件到此区域", SwingConstants.CENTER);
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        dropPanel.add(statusLabel, BorderLayout.CENTER);

        // 设置拖拽支持
        new DropTarget(dropPanel, new DropTargetAdapter() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                dropPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
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
                    List<File> files = (List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    
                    // 处理文件上传
                    handleFileUpload(files);
                    
                    event.dropComplete(true);
                } catch (Exception ex) {
                    event.dropComplete(false);
                    updateStatus("上传失败: " + ex.getMessage(), Color.RED);
                }
                resetPanelAppearance();
            }
        });

        add(dropPanel);
    }

    private void handleFileUpload(List<File> files) {
        // 使用SwingWorker防止阻塞UI线程
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
				String names = "";
                for (File file : files) {
                    // 模拟文件上传处理
                    Thread.sleep(1000); // 替换为实际上传逻辑
					if(file.isDirectory()){
						names = names + file.getName() + "(文件夹)";
					}else{
						names = names + file.getName() + "(文件)";
					}
					String temp = "已上传: " + names;
                    SwingUtilities.invokeLater(() -> 
                        updateStatus(temp, new Color(0, 150, 0)));
						System.out.println("文件位置:" + file.getPath());
                }
                return null;
            }
        }.execute();
    }

    private void resetPanelAppearance() {
        dropPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        updateStatus("拖拽文件到此区域", Color.BLACK);
    }

    private void updateStatus(String text, Color color) {
        statusLabel.setText(text);
        statusLabel.setForeground(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Test05().setVisible(true);
        });
    }
}
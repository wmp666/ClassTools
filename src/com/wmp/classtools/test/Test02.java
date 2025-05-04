package com.wmp.classTools.test;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test02 {
    //测试Markdown文件的显示
    public static void main(String[] args) throws URISyntaxException, IOException {

        try {
            //使用系统UI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        URL resource = Test02.class.getResource("/help/如何导入表格数据.md");
        String markdown = "";
        if (resource != null) {
            byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
            markdown = new String(bytes);
        }

        System.out.println("文件位置 :" + resource);
        System.out.println("文件原内容 :" + markdown);

        //替换图片路径
        //获取resource.toURI()的上级目录
        URI uri = resource.toURI();//.replace("help/", "")
        String s = uri.getPath();
        File file = new File(s);

        String parent = file.getParent().replace("help", "help\\");
        System.out.println("文件上级目录 :" + parent);
        markdown = markdown.replaceAll("!\\[.*]\\(images/", "![](file:" + parent.replace("\\", "/") + "images/");

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // 解析Markdown为节点
        var document = parser.parse(markdown);

        // 将节点渲染为HTML
        String html = renderer.render(document);

        //将images 路径改为绝对路径
        System.out.println("文件渲染内容(HTML) :" + html);

        // 创建JFrame显示HTML内容
        JFrame frame = new JFrame("Markdown Viewer");
        JEditorPane editorPane = new JEditorPane("text/html", html);
        editorPane.setFont(new Font("Arial", -1, 16));
        editorPane.setEditable(false);
        frame.getContentPane().add(new JScrollPane(editorPane), BorderLayout.CENTER);
        frame.setSize(800, 600);
        //frame.pack();// 调整窗口大小以适应内容
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

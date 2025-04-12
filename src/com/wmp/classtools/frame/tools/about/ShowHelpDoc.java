package com.wmp.classTools.frame.tools.about;

import com.wmp.PublicTools.GetIcon;
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

public class ShowHelpDoc extends JDialog {

    private Container c;
    private JScrollPane helpDocPane = new JScrollPane();


    public ShowHelpDoc() throws URISyntaxException, IOException {

        initDialog();
        c = this.getContentPane();



        c.add(getChooseHelpDoc(), BorderLayout.WEST);

        //URL resource = getClass().getResource("/help/如何导入表格数据.md");
        //String html = initHelpDoc(resource);
        //helpDocPane = getHelpDocPane(html);
        c.add(helpDocPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private JScrollPane getChooseHelpDoc() {
        JPanel chooseHelpDoc = new JPanel();
        chooseHelpDoc.setLayout(new FlowLayout());
        chooseHelpDoc.setBackground(Color.WHITE);

        JList<String> list = new JList<>();
        list.setListData(new String[]{"如何导入表格数据", "启动参数"});
        list.setFont(new Font("微软雅黑", Font.BOLD, 25));
        list.addListSelectionListener(e -> {
            if (!list.isSelectionEmpty()) {
                String s = list.getSelectedValue();
                try {
                    switch (s){
                        case "如何导入表格数据"->{
                            URL resource = getClass().getResource("/help/如何导入表格数据.md");
                            String html = initHelpDoc(resource);
                            this.helpDocPane.setViewportView(getHelpDocPane(html));
                            this.helpDocPane.repaint();
                            //c.remove(this.helpDocPane);
                            //this.helpDocPane = helpDocPane;
                            //c.add(this.helpDocPane, BorderLayout.CENTER);

                        }
                        case "启动参数"->{
                            URL resource = getClass().getResource("/help/启动参数.md");
                            String html = initHelpDoc(resource);
                            this.helpDocPane.setViewportView(getHelpDocPane(html));
                            this.helpDocPane.repaint();
                            //JScrollPane helpDocPane = getHelpDocPane(html);
                            //c.remove(this.helpDocPane);
                            //this.helpDocPane = helpDocPane;
                            //c.add(helpDocPane, BorderLayout.CENTER);
                        }
                    }
                    this.repaint();
                } catch (IOException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            }
        });

        chooseHelpDoc.add(list);

        return new JScrollPane(chooseHelpDoc);
    }

    private static String initHelpDoc(URL resource) throws IOException, URISyntaxException {
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
        return html;
    }

    private void initDialog() {

        this.setModal(true);
        this.setTitle("帮助");
        this.getContentPane().setLayout(new BorderLayout());// 设置布局为边界布局
        this.setSize(800, 600);
        this.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/icon.png"),
                        32, 32).getImage());
        //居中显示
        this.setLocationRelativeTo(null);
    }

    private static JEditorPane getHelpDocPane(String html) {
        JEditorPane editorPane = new JEditorPane("text/html", html);
        editorPane.setFont(new Font("Arial", -1, 16));
        editorPane.setEditable(false);

        return editorPane;
    }


}

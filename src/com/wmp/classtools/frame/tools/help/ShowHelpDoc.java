package com.wmp.classTools.frame.tools.help;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.ResourceLocalizer;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ShowHelpDoc extends JFrame {

    private Container c;
    private JScrollPane helpDocPane = new JScrollPane();

    public static final String INPUT_DOC = "如何导入表格数据";
    public static final String START_PARAMETER = "启动参数";
    public static final String CONFIG_PLUGIN = "如何配置插件";
    public static final String HELP_DOC = "帮助文档";

    public ShowHelpDoc() throws URISyntaxException, IOException {
        new ShowHelpDoc(null);
    }

    public ShowHelpDoc(String s) throws URISyntaxException, IOException {

        initDialog();

        c = this.getContentPane();

        c.add(getChooseHelpDoc(), BorderLayout.WEST);

        c.add(getButtonPanel(), BorderLayout.SOUTH);

        copyDocImage("InputExcel-0.png", "InputExcel-1.png", "InputExcel-2.png","SM-0.png", "SM-1.png");

        for (int i = 1; i <= 28; i++) {
            copyDocImage("image" + i + ".png");
        }


        if (s != null){
            showHelpDoc(s);
        }
        c.add(helpDocPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private static String initHelpDoc(String name) throws IOException, URISyntaxException {

        copyDoc(name);

        String markdown = "";
        String parent = CTInfo.TEMP_PATH + "help\\";

        markdown = Files.readString(Paths.get(parent + name));

        //String parent = file.getParent().replace("help", "help\\");
        //System.out.println("文件上级目录 :" + parent);
        markdown = markdown.replaceAll("!\\[.*]\\(images/", "![](file:" + parent.replace("\\", "/") + "images/")
                .replaceAll("<img src=\"images/", "<img src=\"file:" + parent.replace("\\", "/") + "images/");

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // 解析Markdown为节点
        var document = parser.parse(markdown);

        // 将节点渲染为HTML
        String html = renderer.render(document);

        //将images 路径改为绝对路径
        return html;
    }

    private static void copyDoc(String DocName){
        //将resource/help中的文件复制到dataPath中
        String dataPath = CTInfo.TEMP_PATH + "help\\";

        ResourceLocalizer.copyEmbeddedFile(dataPath, "/help/", DocName);

    }

    private static void copyDocImage(String... imageNameList){
        for (String s : imageNameList) {
            copyDocImage(s);
        }

    }
    private static void copyDocImage(String imageName){
        //将resource/help中的文件复制到dataPath中
        String dataPath = CTInfo.TEMP_PATH + "help\\images\\";

        ResourceLocalizer.copyEmbeddedFile(dataPath, "/help/images/", imageName);

    }
    private static JEditorPane getHelpDocPane(String html) {
        JEditorPane editorPane = new JEditorPane("text/html", html);
        editorPane.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
        editorPane.setAutoscrolls(true);// 设置是否允许自动滚动
        editorPane.setEditable(false);

        return editorPane;
    }

    private void showHelpDoc(String s) throws IOException, URISyntaxException {
        switch (s) {
            case INPUT_DOC -> {

                String html = initHelpDoc("如何导入表格数据.md");
                this.helpDocPane.setViewportView(getHelpDocPane(html));
                this.helpDocPane.repaint();
            }
            case START_PARAMETER -> {
                String html = initHelpDoc("启动参数.md");
                this.helpDocPane.setViewportView(getHelpDocPane(html));
                this.helpDocPane.repaint();
            }
            case CONFIG_PLUGIN -> {
                String html = initHelpDoc("如何配置插件.md");
                this.helpDocPane.setViewportView(getHelpDocPane(html));
                this.helpDocPane.repaint();
            }
            case HELP_DOC -> {
                String html = initHelpDoc("帮助文档.md");
                this.helpDocPane.setViewportView(getHelpDocPane(html));
                this.helpDocPane.repaint();
            }
        }
    }

    private JPanel getButtonPanel() {
        JButton exitButton = new JButton("退出");
        exitButton.addActionListener(e -> this.dispose());

        JButton openInExpButton = new JButton("打开所在位置");
        openInExpButton.addActionListener(e -> {
            OpenInExp.open(CTInfo.TEMP_PATH + "help\\");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(exitButton);
        buttonPanel.add(openInExpButton);

        return buttonPanel;
    }

    private void initDialog() {

        this.setTitle("帮助");
        this.getContentPane().setLayout(new BorderLayout());// 设置布局为边界布局
        this.setSize(800, 600);
        this.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/doc.png"),
                        32, 32).getImage());
        //居中显示
        this.setLocationRelativeTo(null);
    }

    private JScrollPane getChooseHelpDoc() {
        JPanel chooseHelpDoc = new JPanel();
        chooseHelpDoc.setLayout(new FlowLayout());
        chooseHelpDoc.setBackground(Color.WHITE);

        JList<String> list = new JList<>();
        list.setListData(new String[]{"如何导入表格数据", "启动参数", "如何配置插件", "帮助文档"});
        list.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        list.addListSelectionListener(e -> {
            if (!list.isSelectionEmpty()) {
                String s = list.getSelectedValue();
                try {
                    showHelpDoc(s);
                    this.repaint();
                } catch (IOException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            }
        });

        chooseHelpDoc.add(list);

        return new JScrollPane(chooseHelpDoc);
    }


}

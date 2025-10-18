package com.wmp.classTools.frame;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.DownloadURLFile;
import com.wmp.PublicTools.io.ResourceLocalizer;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.videoView.MediaPlayer;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.CTComponent.CTList;
import com.wmp.classTools.CTComponent.Menu.CTMenuItem;
import com.wmp.classTools.CTComponent.Menu.CTPopupMenu;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowHelpDoc extends JFrame {

    public static final ArrayList<String> helpDocs = new ArrayList<>();
    public static final int INPUT_DOC = 0;
    public static final int START_PARAMETER = 1;
    public static final int CONFIG_PLUGIN = 2;
    public static final int HELP_DOC = 3;

    static {
        helpDocs.add("如何导入表格数据");
        helpDocs.add("启动参数");
        helpDocs.add("如何配置插件");
        helpDocs.add("帮助文档");

    }

    private Container c;
    private JScrollPane helpDocPane = new JScrollPane();

    public ShowHelpDoc() throws URISyntaxException, IOException {
        this(null);
    }

    public ShowHelpDoc(int index) throws URISyntaxException, IOException {
        this(helpDocs.get(index));
    }

    public ShowHelpDoc(String s) throws URISyntaxException, IOException {

        initDialog();

        c = this.getContentPane();

        c.add(getChooseHelpDoc(), BorderLayout.WEST);

        c.add(getButtonPanel(), BorderLayout.SOUTH);

        copyDocImage("InputExcel-0.png", "InputExcel-1.png", "InputExcel-2.png", "SM-0.png", "SM-1.png");

        for (int i = 1; i <= 28; i++) {
            copyDocImage("image" + i + ".png");
        }


        if (s != null) {
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

    private static void copyDoc(String DocName) {
        //将resource/help中的文件复制到dataPath中
        String dataPath = CTInfo.TEMP_PATH + "help\\";

        ResourceLocalizer.copyEmbeddedFile(dataPath, "/help/", DocName);

    }

    private static void copyDocImage(String... imageNameList) {
        for (String s : imageNameList) {
            copyDocImage(s);
        }

    }

    private static void copyDocImage(String imageName) {
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

    public static String getHelpDocStr(int index) {
        return helpDocs.get(index);
    }

    private void showHelpDoc(String s) throws IOException, URISyntaxException {
        if (s == null) return;

        this.helpDocPane.setViewportView(getHelpDocPane(initHelpDoc(s + ".md")));
        this.helpDocPane.repaint();

    }

    private void showHelpDoc(int index) throws IOException, URISyntaxException {
        showHelpDoc(getHelpDocStr(index));
    }

    private JPanel getButtonPanel() {
        JButton exitButton = new JButton("退出");
        exitButton.addActionListener(e -> this.dispose());

        JButton downloadDocButton = new JButton("下载详细文档");
        downloadDocButton.addActionListener(e -> {
            try {
                JSONArray helpDocsJSArray = new JSONObject(
                        GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools/releases/tags/0.0.3"))
                        .getJSONArray("assets");
                HashMap<String, String> helpDocMap = new HashMap<>();
                helpDocsJSArray.forEach(object -> {
                    if (object instanceof JSONObject jsonObject) {
                        helpDocMap.put(jsonObject.getString("name"), jsonObject.getString("browser_download_url"));
                    }
                });

                CTPopupMenu popupMenu = new CTPopupMenu();

                CTMenuItem helpDocMenuItem = new CTMenuItem("帮助文档(helpdoc.docx)");
                helpDocMenuItem.addActionListener(e1 -> {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            DownloadURLFile.downloadWebFile(ShowHelpDoc.this, null, helpDocMap.get("helpdoc.docx"), CTInfo.TEMP_PATH + "help\\WebFile");
                            return null;
                        }

                        @Override
                        protected void done() {
                            try {
                                MediaPlayer.playOther(CTInfo.TEMP_PATH + "help\\WebFile\\helpdoc.docx");
                            } catch (IOException ex) {
                                Log.err.print(getClass(), "", ex);
                                throw new RuntimeException(ex);
                            }
                        }

                    }.execute();

                });
                popupMenu.add(helpDocMenuItem);

                popupMenu.show(downloadDocButton, 0, downloadDocButton.getHeight());
            } catch (Exception ex) {
                Log.err.print(getClass(), "", ex);
                throw new RuntimeException(ex);
            }

        });
        JButton openInExpButton = new JButton("打开所在位置");
        openInExpButton.addActionListener(e -> {
            OpenInExp.open(CTInfo.TEMP_PATH + "help\\");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(exitButton);
        buttonPanel.add(openInExpButton);
        buttonPanel.add(downloadDocButton);

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


        String[] list = new String[ShowHelpDoc.helpDocs.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = ShowHelpDoc.helpDocs.get(i);
        }

        CTList switchPanel = new CTList(list,
                0, (e, choice) -> {
            try {
                showHelpDoc(choice);
                ShowHelpDoc.this.repaint();
            } catch (Exception ex) {
                Log.err.print(getClass(), "文档打开失败", ex);
                throw new RuntimeException(ex);
            }
        }
        );


        JScrollPane mainPanelScroll = new JScrollPane(switchPanel);
        mainPanelScroll.setBorder(BorderFactory.createEmptyBorder());
        mainPanelScroll.getViewport().setBackground(Color.WHITE);
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);

        return mainPanelScroll;
    }


}

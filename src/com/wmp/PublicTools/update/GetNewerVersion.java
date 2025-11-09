package com.wmp.PublicTools.update;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.DownloadURLFile;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.PublicTools.web.SslUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class GetNewerVersion {

    private static final int NEW_VERSION = 1;
    private static final int TEST_VERSION = 2;
    private static final String NewVerFileUrl = "https://api.github.com/repos/wmp666/ClassTools/releases/latest";
    public static int newerVersion = 1;
    public static int importUpdate = 2;
    private static JPanel view;
    //private static JDialog dialog;
    private static String versionContent = "";//更新说明
    private static String downloadUrl = "null";
    private static String sha256 = null;
    //https://github.com/wmp666/ClassTools/releases/download/1.6.4/ClassTools.jar

    private static String getLatestVersion(int updateMode) {
        try {
            String version = null;
            if (updateMode == NEW_VERSION) {
                // 获取原始JSON响应
                String json = GetWebInf.getWebInf(NewVerFileUrl);

                // 使用JSONObject解析
                JSONObject release = new JSONObject(json);
                version = release.getString("tag_name");
                versionContent = release.getString("body").replace("\\r\\n", "\n");

                // 获取准确下载地址
                JSONArray assets = release.getJSONArray("assets");
                for (int i = 0; i < assets.length(); i++) {
                    JSONObject asset = assets.getJSONObject(i);
                    if (asset.getString("name").endsWith(".jar")) {
                        downloadUrl = asset.getString("browser_download_url");
                        sha256 = asset.getString("digest").split(":")[1];
                        break;
                    }
                }
            } else {
                // 获取原始JSON响应
                String json = GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools/releases");

                AtomicReference<String> newBate = new AtomicReference<>("0.0.0");
                AtomicReference<JSONObject> newRelease = new AtomicReference<>();
                // 使用JSONObject解析
                JSONArray releases = new JSONArray(json);
                //获取最新测试版
                releases.forEach(release -> {
                    JSONObject JSONRelease = (JSONObject) release;
                    if (JSONRelease.getBoolean("prerelease")) {
                        if (isNewerVersion(JSONRelease.getString("tag_name"), newBate.get()) != 0) {
                            newBate.set(JSONRelease.getString("tag_name"));
                            newRelease.set(JSONRelease);
                        }
                    }
                });

                version = newBate.get();
                versionContent = newRelease.get().getString("body").replace("\\r\\n", "\n");

                // 获取准确下载地址
                JSONArray assets = newRelease.get().getJSONArray("assets");
                for (int i = 0; i < assets.length(); i++) {
                    JSONObject asset = assets.getJSONObject(i);
                    if (asset.getString("name").endsWith(".jar")) {
                        sha256 = asset.getString("digest").split(":")[1];
                        downloadUrl = asset.getString("browser_download_url");
                        break;
                    }
                }
                if (downloadUrl.equals("null")) {
                    Log.err.print(null, GetNewerVersion.class, "无法获取下载地址");
                } else {
                    Log.info.print(null, "获取新版本", "获取下载地址成功");
                }
            }

            return version;
        } catch (Exception e) {
            Log.err.print(GetNewerVersion.class, "版本获取失败", e);
        }
        return null;
    }

    public static void checkForUpdate(Window dialog, JPanel panel, boolean showMessage) {
        checkForUpdate(dialog, panel, showMessage, true);
    }

    public static void checkForUpdate(Window dialog, JPanel panel, boolean showMessage, boolean inquireUpdateWay) {

        if (Main.allArgs.get("screenProduct:show").contains(Main.argsList)) {
            Log.err.print(null, GetNewerVersion.class, "屏保状态,无法更新");
            return;
        }

        int updateMode;

        if (inquireUpdateWay) {
            String s = Log.info.showChooseDialog(dialog, "检查更新", "选择更新版本", "最新版", "测试版");
            //updateMode = s.equals() ? NEW_VERSION : TEST_VERSION;
            if (s.equals("最新版")) updateMode = NEW_VERSION;
            else if (s.equals("测试版")) updateMode = TEST_VERSION;
            else {
                Log.err.print(null, GetNewerVersion.class, "未设置更新方式");
                return;
            }
        } else {
            updateMode = NEW_VERSION;
        }


        if (panel != null) {
            /*view = panel;
            view.removeAll();*/
            panel.removeAll();

        }

        new SwingWorker<Void, Void>() {
            String latestVersion;

            protected Void doInBackground() throws Exception {
                latestVersion = getLatestVersion(updateMode == NEW_VERSION ? NEW_VERSION : TEST_VERSION);
                return null;
            }

            protected void done() {
                if (latestVersion == null) {

                    Log.err.print(dialog, GetNewerVersion.class, "无法获取版本信息");

                    return;
                }
                int i = isNewerVersion(latestVersion, CTInfo.version);

                Thread updateThread = new Thread(() -> {
                    Log.info.message(dialog, "更新至 " + latestVersion, "即将开始更新, 无论更新是否完成都将关闭程序, 没有提醒");

                    IOForInfo.deleteDirectoryRecursively(Path.of(CTInfo.TEMP_PATH + "UpdateFile\\"));

                    DownloadURLFile.downloadWebFile(dialog, panel, downloadUrl, CTInfo.TEMP_PATH + "UpdateFile\\", sha256);

                    try {
                        IOForInfo.copyFile(Path.of(CTInfo.TEMP_PATH, "UpdateFile", "ClassTools.jar"), Path.of(GetPath.getAppPath(GetPath.SOURCE_FILE_PATH), "ClassTools.jar"));
                    } catch (IOException e) {
                        Log.err.print(dialog, GetNewerVersion.class, "更新文件失败", e);
                    }

                    System.exit(0);
                });

                if (updateMode == NEW_VERSION) {
                    if (i == 1) {
                        Log.info.print("发现新版本", "发现新版本 " + latestVersion);
                        int result = Log.info.showChooseDialog(dialog, "发现新版本",
                                "发现新版本 " + latestVersion + "，是否下载？\n" + versionContent);


                        if (result == JOptionPane.YES_OPTION) {
                            updateThread.start();

                        }
                    } else if (i == 2) {
                        Log.info.message(dialog, "发现新版本",
                                "发现新版本 " + latestVersion + "，是否下载？\n" + versionContent);

                        updateThread.start();
                    } else {
                        if (showMessage) {
                            Log.info.message(dialog, "获取新版本", "当前已是最新版本");

                        } else {
                            Log.info.print("获取新版本", "当前已是最新版本");
                        }

                    }
                } else {
                    if (i != 0) {
                        int result = Log.info.showChooseDialog(dialog, "发现测试版本",
                                "发现测试版本 " + latestVersion + "，是否下载？\n" + versionContent);


                        if (result == JOptionPane.YES_OPTION) {
                            updateThread.start();

                        }
                    } else {
                        if (showMessage) {
                            Log.info.message(dialog, "获取测试版本", "无最新测试版本");

                        } else {
                            Log.info.print("获取测试版本", "无最新测试版本");
                        }

                    }
                }


            }
        }.execute();// 开始执行异步任务
    }

    /**
     * 比较版本号
     *
     * @param remote 新版本号
     * @param local  旧版本号
     * @return 0:已是最新，1:新版本，2:重要更新
     */
    public static int isNewerVersion(String remote, String local) {
        // 实现版本号比较逻辑（需根据你的版本号格式调整）
        String[] remoteParts = remote.split("\\.");
        String[] localParts = local.split("\\.");
        for (int i = 0; i < Math.min(remoteParts.length, localParts.length); i++) {
            int remotePart = Integer.parseInt(remoteParts[i]);
            int localPart = Integer.parseInt(localParts[i]);
            if (remotePart > localPart) {//有最新版
                if (i == 0 || i == 1) {
                    return importUpdate;
                } else {
                    if (local.length() >= 5 && remote.length() < 5) return importUpdate;
                    else return newerVersion;
                }

            } else if (remotePart < localPart) {
                return 0;
            }
        }
        // 如果版本号相同，则比较长度
        if (remoteParts.length > localParts.length) {
            return newerVersion;
        } else {
            return 0;
        }
    }


    public static void getSource(Window dialog, JPanel panel) {
        if (panel != null) {
            view = panel;
            view.removeAll();
        }


        new SwingWorker<Void, Void>() {
            String sourceURL;

            protected Void doInBackground() throws Exception {
                sourceURL = getSourceURL();
                return null;
            }

            protected void done() {
                if (sourceURL == null) {
                    Log.err.print(dialog, GetNewerVersion.class, "无法获取下载地址");
                    return;
                }

                int result = Log.info.showChooseDialog(dialog, "发现新版本", "检测到源代码文件，是否下载？");

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        downloadSource(sourceURL);
                    } catch (Exception e) {
                        Log.err.print(dialog, GetNewerVersion.class, "下载失败", e);
                    }
                }
            }
        }.execute();
    }

    private static String getSourceURL() throws Exception {
        SslUtils.ignoreSsl();
        try {
            // 获取原始JSON响应
            String json = Jsoup.connect(NewVerFileUrl)
                    .header("Accept", "application/vnd.github.v3+json")
                    .timeout(10000)
                    .ignoreContentType(true)
                    .execute()
                    .body();

            // 使用JSONObject解析
            JSONObject release = new JSONObject(json);

            // 获取准确下载地址
            String sourceURL = release.getString("zipball_url");


            return sourceURL;
        } catch (Exception e) {
            Log.err.print(GetNewerVersion.class, "信息解析失败", e);
        }
        return null;
    }

    private static void downloadSource(String downloadUrl) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI(downloadUrl));
    }


}

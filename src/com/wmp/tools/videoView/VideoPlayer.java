package com.wmp.tools.videoView;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class VideoPlayer {

    /*static {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }*/

    public static void playVideo(String filePath) throws IOException {
        File videoFile = new File(filePath);
        /*if (!videoFile.exists()) {
            showAlert("错误", "文件不存在: " + filePath);
            return;
        }*/

        //newStyleToShowVideo(videoFile);
        Desktop.getDesktop().open(videoFile);
    }

    /*private static void newStyleToShowVideo(File videoFile) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            Media media = new Media(videoFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            // 错误处理
            mediaPlayer.setOnError(() -> handleError(stage, mediaPlayer, "播放错误", mediaPlayer.getError()));
            media.setOnError(() -> handleError(stage, mediaPlayer, "加载错误", media.getError()));

            // 窗口尺寸监听器
            ChangeListener<Number> widthListener = (obs, oldVal, newVal) ->
                    mediaView.setFitWidth(newVal.doubleValue() - 20);
            ChangeListener<Number> heightListener = (obs, oldVal, newVal) ->
                    mediaView.setFitHeight(newVal.doubleValue() - 20);

            mediaPlayer.setOnReady(() -> {
                //MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setPreserveRatio(true);

                StackPane root = new StackPane(mediaView);
                root.setStyle("-fx-background-color: black;");

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double maxWidth = screenSize.width * 0.8;
                double maxHeight = screenSize.height * 0.8;

                double[] dimensions = calculateInitialDimensions(media.getWidth(), media.getHeight(), maxWidth, maxHeight);
                Scene scene = new Scene(root, dimensions[0], dimensions[1]);

                stage.setScene(scene);
                stage.setTitle(videoFile.getName());
                stage.centerOnScreen();

                // 绑定尺寸监听器
                stage.widthProperty().addListener(widthListener);
                stage.heightProperty().addListener(heightListener);

                stage.setOnCloseRequest(e -> {
                    cleanupResources(stage, mediaPlayer, widthListener, heightListener);
                });

                stage.show();
                mediaPlayer.play();
            });

            mediaPlayer.setOnEndOfMedia(() -> cleanupResources(stage, mediaPlayer, widthListener, heightListener));
        });
    }

    private static void cleanupResources(Stage stage, MediaPlayer mediaPlayer,
                                         ChangeListener<Number> widthListener,
                                         ChangeListener<Number> heightListener) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        if (stage != null) {
            stage.widthProperty().removeListener(widthListener);
            stage.heightProperty().removeListener(heightListener);
            stage.close();
        }
        System.out.println("资源清理完成");
    }

    // 保留其他辅助方法（calculateInitialDimensions, handleError, showAlert）

    private static double[] calculateInitialDimensions(double videoWidth, double videoHeight, double maxWidth, double maxHeight) {
        double initWidth = Math.min(videoWidth, maxWidth);
        double initHeight = Math.min(videoHeight, maxHeight);

        if (videoWidth > 0 && videoHeight > 0) {
            double aspectRatio = videoWidth / videoHeight;
            double calculatedHeight = initWidth / aspectRatio;
            if (calculatedHeight > maxHeight) {
                initWidth = maxHeight * aspectRatio;
                initHeight = maxHeight;
            } else {
                initHeight = calculatedHeight;
            }
        }
        return new double[]{initWidth, initHeight};
    }

    private static void cleanupResources(Stage stage, MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        if (stage != null) {
            stage.close();
        }
        System.out.println("资源已清理");
    }

    private static void handleError(Stage stage, MediaPlayer mediaPlayer, String title, MediaException error) {
        System.err.println(title + ": " + error.getMessage());
        cleanupResources(stage, mediaPlayer);
        showAlert(title, error.getMessage());
    }

    private static void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }*/
}
package Simulation.Model.Aerodynamic;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class test extends Application {

    @Override
    public void start(Stage primaryStage) {
        int width = 600 ;
        int height = 600 ;
        WritableImage img = new WritableImage(width, height);
        byte[] pixels = createPixels(width, height);

        DoubleProperty hue = new SimpleDoubleProperty();
        hue.addListener((obs, oldValue, newValue) -> {
            updateImage(img, pixels, newValue.doubleValue());
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), new KeyValue(hue, 360)));
        timeline.setCycleCount(Animation.INDEFINITE);


        Scene scene = new Scene(new StackPane(new ImageView(img)));
        primaryStage.setScene(scene);
        primaryStage.show();

        timeline.play();
    }

    private void updateImage(WritableImage img, byte[] pixels, double hue) {
        int[] colorIndex = new int[256];
        for (int i = 0 ; i < colorIndex.length; i++) {
            Color c = Color.hsb(hue, 1.0*i/colorIndex.length, 1.0);
            colorIndex[i] = getArgb(c);
        }
        int w = (int) img.getWidth();
        int h = (int) img.getHeight();
        img.getPixelWriter().setPixels(0, 0, w, h, PixelFormat.createByteIndexedInstance(colorIndex), pixels, 0, w);
    }

    private int getArgb(Color c) {
        int a = (int) (255*c.getOpacity());
        int r = (int) (255*c.getRed());
        int g = (int) (255*c.getGreen());
        int b = (int) (255*c.getBlue());
        return (a << 24) | (r << 16) | (g << 8) | b ;
    }

    private byte[] createPixels(int width, int height) {
        byte[] pixels = new byte[width * height];
        int d = width * width + height * height;
        for (int i = 0 ; i < pixels.length; i++) {
            int x = i % width ;
            int y = i / width ;
            pixels[i] = (byte) (256 * (height * y + width * x) / d);
        }
        return pixels ;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

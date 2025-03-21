package fr.skillup.core;

import fr.skillup.controllers.HomeController;
import fr.skillup.core.config.Config;
import fr.skillup.core.window.Window;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.logging.Logger;

public class SkillUP extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.getMemory();
        String os = System.getProperty("os.name");

        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        System.setProperty("prism.targetvram", "2");
        System.setProperty("prism.forceGPU", "true");
        System.setProperty("prism.vsync", "true");
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("javafx.webContext.allowLocalStorage", "true");
        System.setProperty("javafx.webContext.useHardwareAcceleration", "true");
        System.setProperty("javafx.web.enableMultiThread", "true");

        if (os.contains("win")) {
            System.setProperty("prism.order", "d3d");
        } else if (os.contains("mac")) {
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.allowhidpi", "true");
        } else if (os.contains("nix") || os.contains("nux")) {
            System.setProperty("prism.order", "es2");
        }

        Window window = new Window(Config.get("app.title"));
        window.show(HomeController.class);
        window.setMinWidth(Double.parseDouble(Config.get("app.min.width")));
        window.setMinHeight(Double.parseDouble(Config.get("app.min.height")));
        Window.getInstance().getIcons().add((new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/fr/skillup/assets/images/favicon.png")))));
    }

    private void getMemory() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        String msgMaxMemory = "Max Memory: " + maxMemory / (1024 * 1024) + " MB";
        String msgTotalMemory = "Total Memory: " + totalMemory / (1024 * 1024) + " MB";
        String msgFreeMemory = "Free Memory: " + freeMemory / (1024 * 1024) + " MB";

        Logger.getGlobal().info(msgMaxMemory);
        Logger.getGlobal().info(msgTotalMemory);
        Logger.getGlobal().info(msgFreeMemory);
    }
}

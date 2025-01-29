package fr.skillup.core;

import fr.skillup.controllers.HomeController;
import fr.skillup.core.config.Config;
import fr.skillup.core.window.Window;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class SkillUP extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        System.setProperty("prism.order", "es2");
        System.setProperty("prism.forceGPU", "true");
        System.setProperty("prism.vsync", "true");
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("javafx.webContext.allowLocalStorage", "true");
        System.setProperty("javafx.webContext.useHardwareAcceleration", "true");
        System.setProperty("javafx.webContext.useMultiThread", "true");
        System.setProperty("javafx.webContext.useGL", "true");
        System.setProperty("javafx.webContext.useGPU", "true");
        System.setProperty("javafx.webContext.useD3D", "true");
        System.setProperty("javafx.webContext.useSW", "false");
        System.setProperty("javafx.webContext.useMultiTouch", "true");

        Config.load();

        Window window = new Window(Config.get("app.title"));
        window.show(HomeController.class);
        window.setMinWidth(Double.parseDouble(Config.get("app.min.width")));
        window.setMinHeight(Double.parseDouble(Config.get("app.min.height")));
        Window.getInstance().getIcons().add((new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/fr/skillup/assets/images/favicon.png")))));
    }
}

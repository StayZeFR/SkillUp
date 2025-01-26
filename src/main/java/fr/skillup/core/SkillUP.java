package fr.skillup.core;

import fr.skillup.controllers.BootstrapController;
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
        Config.load();

        Window window = new Window(Config.get("app.title"));
        window.show(HomeController.class);
        window.setMinWidth(Double.parseDouble(Config.get("app.min.width")));
        window.setMinHeight(Double.parseDouble(Config.get("app.min.height")));
        Window.getInstance().getIcons().add((new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/fr/skillup/assets/images/favicon.png")))));
    }
}

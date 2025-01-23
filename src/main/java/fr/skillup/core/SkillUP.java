package fr.skillup.core;

import fr.skillup.controllers.HomeController;
import fr.skillup.core.config.Config;
import fr.skillup.core.window.Window;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class SkillUP extends Application {

    private static Window window;

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Config.load();

        SkillUP.window = new Window(Config.get("app.title"));
        SkillUP.window.show(HomeController.class);
        Window.getInstance().getIcons().add((new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/fr/skillup/assets/images/favicon.png")))));

        SkillUP.window.show();
    }
}

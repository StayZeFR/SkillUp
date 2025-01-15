package fr.skillup.core;

import fr.skillup.controllers.HomeController;
import fr.skillup.core.config.Config;
import fr.skillup.core.database.Database;
import fr.skillup.core.window.Window;
import javafx.application.Application;
import javafx.stage.Stage;

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

        SkillUP.window.show();
    }
}

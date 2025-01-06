package fr.skillup.core.window;

import fr.skillup.core.controller.Controller;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;

public class Window extends Stage {

    public Window() {
        this.setTitle("SkillUP");
    }

    public Window(String title) {
        super.setTitle(title);
    }

    public void show(Class<? extends Controller> clazz) {
        try {
            Controller controller = clazz.getDeclaredConstructor().newInstance();
            controller.setWindow(this);
            controller.init();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}

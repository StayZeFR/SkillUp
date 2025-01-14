package fr.skillup.core.bridge;

import fr.skillup.core.controller.Controller;
import javafx.scene.web.WebView;

import java.lang.reflect.InvocationTargetException;

public class Bridge {
    private final WebView webView;

    public Bridge(WebView webView) {
        this.webView = webView;
    }

    public Controller get(String controller) {
        Class<? extends Controller> clazz = null;
        try {
            clazz = Class.forName("fr.skillup.controllers." + controller).asSubclass(Controller.class);
            return clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void log(String message) {
        System.out.println(message);
    }
}

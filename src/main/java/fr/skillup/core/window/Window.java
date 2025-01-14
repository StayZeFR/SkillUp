package fr.skillup.core.window;

import fr.skillup.core.controller.Controller;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Window extends Stage {

    private static Window instance;

    private WebView webView;

    public Window() {
        this.setTitle("?");
    }

    public Window(String title) {
        Window.instance = this;
        super.setTitle(title);
        this.webView = new WebView();
        this.setScene(new Scene(this.webView));
    }

    public void show(Class<? extends Controller> clazz, Map<String, Object> params) {
        try {
            Controller controller = clazz.getDeclaredConstructor().newInstance();
            controller.setParams(params);
            controller.init();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void show(Class<? extends Controller> clazz) {
        this.show(clazz, new HashMap<>());
    }

    public WebView getWebView() {
        return this.webView;
    }

    public static Window getInstance() {
        return Window.instance;
    }

}

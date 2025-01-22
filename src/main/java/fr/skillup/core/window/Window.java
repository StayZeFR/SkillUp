package fr.skillup.core.window;

import fr.skillup.core.controller.Controller;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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
        this.webView.getEngine().setJavaScriptEnabled(true);
        this.webView.getEngine().setOnError(event -> Logger.getLogger(Window.class.getName()).severe("JS : " + event.getMessage()));
        this.webView.getEngine().setOnAlert(event -> Logger.getLogger(Window.class.getName()).info("JS Alert : " + event.getData()));
        this.webView.setOnContextMenuRequested(Event::consume);
        this.setScene(new Scene(this.webView));
    }

    /**
     * Afficher une vue dans la fenêtre avec des paramètres
     * @param clazz : Classe du controller
     * @param params : Paramètres à passer au controller
     */
    public void show(Class<? extends Controller> clazz, Map<String, Object> params) {
        try {
            Controller controller = clazz.getDeclaredConstructor().newInstance();
            controller.setParams(params);
            controller.init();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.getLogger(Window.class.getName()).severe(e.getMessage());
        }
    }

    /**
     * Afficher une vue dans la fenêtre sans paramètres
     * @param clazz : Classe du controller
     */
    public void show(Class<? extends Controller> clazz) {
        this.show(clazz, new HashMap<>());
    }

    /**
     * Récupérer le WebView de la fenêtre
     * @return WebView : WebView de la fenêtre
     */
    public WebView getWebView() {
        return this.webView;
    }

    /**
     * Récupérer l'instance de la fenêtre principale
     * @return Window : Instance de la fenêtre principale
     */
    public static Window getInstance() {
        return Window.instance;
    }

}

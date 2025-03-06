package fr.skillup.core.window;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.utils.Extractor;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.OS;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Window extends Stage {

    private static Window instance;

    private JFXPanel panel;

    public Window() {
        this.setTitle("?");
    }

    public Window(String title) {
        Window.instance = this;
        super.setTitle(title);

        //String path = Extractor.extractLibrary("jcef/", "chrome_elf.dll");
        //assert path != null;
        this.panel = new JFXPanel();

        SwingNode node = new SwingNode();
        System.setProperty("java.library.path", "C:\\Users\\bland\\Documents\\win64\\bin\\lib\\win64\\");

        SwingUtilities.invokeLater(() -> {
            System.out.println(System.getProperty("java.library.path"));
            CefApp app = CefApp.getInstance();
            CefClient client = app.createClient();
            CefBrowser browser = client.createBrowser("https://www.google.com", false, false);
            this.panel.add(browser.getUIComponent());
        });

        node.setContent(this.panel);
        StackPane root = new StackPane(node);
        Scene scene = new Scene(root);
        super.setScene(scene);
    }

    /**
     * Afficher une vue dans la fenêtre avec des paramètres
     * @param clazz : Classe du controller
     * @param params : Paramètres à passer au controller
     */
    public void show(Class<? extends Controller> clazz, Map<String, Object> params) {
        //this.show(clazz, params, this.webView);
    }

    /**
     * Afficher une vue dans la fenêtre sans paramètres
     * @param clazz : Classe du controller
     */
    public void show(Class<? extends Controller> clazz) {
        this.show(clazz, new HashMap<>());
    }

    private void show(Class<? extends Controller> clazz, Map<String, Object> params, WebView webView) {
        try {
            Controller controller = clazz.getDeclaredConstructor().newInstance();
            controller.setWebView(webView);
            controller.setParams(params);
            controller.init();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.getLogger(Window.class.getName()).severe(e.getMessage());
        }
    }

    /**
     * Récupérer le WebView de la fenêtre
     * @return WebView : WebView de la fenêtre
     */
    public WebView getWebView() {
        // return this.webView;
        return new WebView();
    }

    /**
     * Récupérer l'instance de la fenêtre principale
     * @return Window : Instance de la fenêtre principale
     */
    public static Window getInstance() {
        return Window.instance;
    }

}

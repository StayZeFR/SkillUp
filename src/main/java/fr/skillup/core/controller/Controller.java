package fr.skillup.core.controller;

import fr.skillup.core.utils.HTMLBuilder;
import fr.skillup.core.window.Window;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    private Window window;

    public void setWindow(Window window) {
        this.window = window;
    }

    public Window getWindow() {
        return this.window;
    }

    public abstract void init();

    protected void render(String view, Map<String, String> params) {
        WebView webView = new WebView();
        webView.getEngine().loadContent(HTMLBuilder.buildView(view + ".html"), "text/html");

        this.window.setScene(new Scene(webView));
        this.window.show();
    }

    protected void render(String view) {
        this.render(view, new HashMap<>());
    }
}

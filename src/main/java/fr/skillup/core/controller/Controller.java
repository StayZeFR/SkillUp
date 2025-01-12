package fr.skillup.core.controller;

import fr.skillup.core.bridge.Bridge;
import fr.skillup.core.tools.HTMLBuilder;
import fr.skillup.core.window.Window;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

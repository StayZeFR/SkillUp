package fr.skillup.core.controller;

import fr.skillup.core.bridge.Bridge;
import fr.skillup.core.utils.HTMLBuilder;
import fr.skillup.core.window.Window;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    protected Window window = Window.getInstance();

    public abstract void init();

    protected void render(String view, Map<String, String> params) {
        WebView webView = this.window.getWebView();
        webView.getEngine().loadContent(HTMLBuilder.buildView(view + ".html"), "text/html");

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("bridge", new Bridge(webView));
                webView.getEngine().executeScript("Bridge.init()");
            }
        });
    }

    protected void render(String view) {
        this.render(view, new HashMap<>());
    }
}

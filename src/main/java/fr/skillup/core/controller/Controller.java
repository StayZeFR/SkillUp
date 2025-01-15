package fr.skillup.core.controller;

import fr.skillup.core.bridge.Bridge;
import fr.skillup.core.utils.HTMLBuilder;
import fr.skillup.core.window.Window;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    protected Window window = Window.getInstance();
    protected Map<String, Object> params;

    public abstract void init();

    protected void render(String view, Map<String, Object> params) {
        WebView webView = this.window.getWebView();
        webView.getEngine().setJavaScriptEnabled(true);
        webView.getEngine().setOnError(event -> System.out.println("Error : " + event.getMessage()));
        webView.getEngine().setOnAlert(event -> System.out.println("Alert : " + event.getData()));
        webView.getEngine().loadContent(HTMLBuilder.buildView(view + ".html", params), "text/html");

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("params", params);
                window.setMember("bridge", new Bridge(webView));
                webView.getEngine().executeScript("Bridge.init()");
                webView.getEngine().executeScript("App.start()");
            }
        });
    }

    protected void render(String view) {
        this.render(view, new HashMap<>());
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}

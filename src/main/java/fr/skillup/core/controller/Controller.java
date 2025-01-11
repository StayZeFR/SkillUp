package fr.skillup.core.controller;

import fr.skillup.core.bridge.Bridge;
import fr.skillup.core.tools.HTMLBuilder;
import fr.skillup.core.window.Window;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

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
        /*WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        String path = "/fr/skillup/views/" + view + ".html";
        if (this.getClass().getResource(path) == null) {
            throw new RuntimeException("View not found: " + view);
        }



        webView.getEngine().load(Objects.requireNonNull(this.getClass().getResource("/fr/skillup/views/" + view + ".html")).toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("bridge", new Bridge(webView));
                webEngine.executeScript("""
                    var script = document.createElement('script');
                    script.src = '../assets/js/loader.js';
                    script.onload = function() {
                        Loader.init();
                    };
                    document.body.appendChild(script);
                """);
                if (!params.isEmpty()) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        String json = mapper.writeValueAsString(params);
                        webEngine.executeScript("App.setParams(" + json + ");");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                webEngine.executeScript("Bridge.init();");
            }
        });*/
        WebView webView = new WebView();
        webView.getEngine().loadContent(HTMLBuilder.buildView(view + ".html"));

        this.window.setScene(new Scene(webView));
        this.window.show();
    }

    protected void render(String view) {
        this.render(view, new HashMap<>());
    }
}

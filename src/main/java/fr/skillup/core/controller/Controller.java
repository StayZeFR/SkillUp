package fr.skillup.core.controller;

import fr.skillup.core.bridge.Bridge;
import fr.skillup.core.utils.HTMLBuilder;
import fr.skillup.core.window.Window;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    private static ChangeListener<Worker.State> currentListener = null;
    protected Window window = Window.getInstance();
    protected Map<String, Object> params;
    private WebView webView;

    /**
     * Initialisation du contrôleur
     */
    public abstract void init();

    /**
     * Permet de charger une vue dans la fenêtre
     *
     * @param view   : Nom de la vue
     * @param params : Paramètres à passer à la vue
     */
    protected void render(String view, Map<String, Object> params) {
        this.webView.getEngine().getHistory().go(this.webView.getEngine().getHistory().getEntries().size());
        this.webView.getEngine().loadContent(HTMLBuilder.buildView(view + ".html", params), "text/html");

        if (Controller.currentListener != null) {
            this.webView.getEngine().getLoadWorker().stateProperty().removeListener(currentListener);
        }

        Controller.currentListener = (obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject jsObj = (JSObject) this.webView.getEngine().executeScript("window");
                jsObj.setMember("params", params);
                jsObj.setMember("bridge", Bridge.getInstance());
                this.webView.getEngine().executeScript("Bridge.init()");
                this.webView.getEngine().executeScript("App.start()");
                if (!Window.getInstance().isShowing()) {
                    Window.getInstance().show();
                }
            }
        };

        this.webView.getEngine().getLoadWorker().stateProperty().addListener(currentListener);
    }

    /**
     * Permet de charger une vue dans la fenêtre
     *
     * @param view : Nom de la vue
     */
    protected void render(String view) {
        this.render(view, new HashMap<>());
    }

    /**
     * Permet de récupérer les paramètres passés au contrôleur
     *
     * @param params : Paramètres passés au contrôleur
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }
}

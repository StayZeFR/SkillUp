package fr.skillup.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.skillup.core.window.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.*;
import java.net.URL;
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
        WebEngine webEngine = webView.getEngine();
        System.out.println(this.getClass().getResource("/fr/skillup/views/" + view + ".html").toExternalForm());

        webView.getEngine().load(Objects.requireNonNull(this.getClass().getResource("/fr/skillup/views/" + view + ".html")).toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                if (!params.isEmpty()) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        String json = mapper.writeValueAsString(params);
                        webEngine.executeScript("setParams(" + json + ");");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        this.window.setScene(new Scene(webView));
        this.window.show();
    }

    protected void render(String view) {
        this.render(view, new HashMap<>());
    }

}

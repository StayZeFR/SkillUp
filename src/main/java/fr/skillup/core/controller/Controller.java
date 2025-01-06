package fr.skillup.core.controller;

import fr.skillup.core.window.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    private Window window;

    public void setWindow(Window window) {
        this.window = window;
    }

    public abstract void init();

    protected void render(String view, Map<String, String> params) {
        String path = "/fr/skillup/views/" + view + ".fxml";
        InputStream stream = this.getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("FXML file not found: " + view);
        }

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String content = builder.toString();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String tag = "{{" + entry.getKey() + "}}";
            content = content.replace(tag, entry.getValue());
        }

        FXMLLoader loader = new FXMLLoader();
        try {
            Parent root = loader.load(new ByteArrayInputStream(content.getBytes()));
            this.window.setScene(new Scene(root));
            this.window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void render(String view) {
        this.render(view, new HashMap<>());
    }

}

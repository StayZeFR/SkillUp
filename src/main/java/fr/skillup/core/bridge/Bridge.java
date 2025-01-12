package fr.skillup.core.bridge;

import fr.skillup.core.controller.Controller;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Bridge {
    private WebView webView;

    public Bridge(WebView webView) {
        this.webView = webView;
    }

    public Controller getController(String controller) {
        /*try {
            String path = "/fr/skillup/controllers/" + controller + ".class";
            return (Controller) Class.forName(path).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/
        return null;
    }

    public String readView(String view) {
        System.out.println("Reading view: " + view);
        try {
            byte[] bytes = Files.readAllBytes(Path.of(Objects.requireNonNull(this.getClass().getResource("/fr/skillup/views/" + view + ".html")).toURI().getPath()));
            System.out.println(new String(bytes));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return "Hello world!";
    }
}

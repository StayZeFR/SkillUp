package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends Controller {

    @FXML
    private ImageView icon;

    @Override
    public void init() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "Ilann BLANDIN");
        this.render("home_view", params);
    }
}

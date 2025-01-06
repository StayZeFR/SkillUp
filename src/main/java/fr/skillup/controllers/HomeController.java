package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends Controller {

    @FXML
    public Button btn;
    @FXML
    public Text txt;

    @Override
    public void init() {
        Map<String, String> params = new HashMap<>();
        params.put("click", "Peux tu cliquer ?");
        this.render("home_view", params);
    }

    public void click() {
        this.txt.setText("Merci !");
    }
}

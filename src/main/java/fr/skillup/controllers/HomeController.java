package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends Controller {
    @Override
    public void init() {
        Map<String, String> params = new HashMap<>();
        params.put("click", "Peux tu cliquer ?");
        this.render("home_view", params);
    }
}

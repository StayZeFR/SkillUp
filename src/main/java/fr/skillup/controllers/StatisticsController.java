package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import java.util.HashMap;
import java.util.Map;

public class StatisticsController extends Controller {

    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "statistics");
        this.render("statistics_view", params);
    }
}

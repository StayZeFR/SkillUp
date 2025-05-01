package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import java.util.HashMap;
import java.util.Map;

public class StatisticsController extends Controller {
    private static final String VIEW = "statistics";

    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "statistics");
        this.render("statistics_view", params);
    }

    public void viewStatisticsSkills() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", StatisticsController.VIEW);
        super.window.show(StatisticsSkillsController.class, params);
    }


}

package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

import java.util.HashMap;
import java.util.Map;

public class MissionsController extends Controller {
    private static final String VIEW = "missions";

    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        super.render("missions_view", params);
    }

    public void addMission() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        params.put("action", "add");
        super.window.show(ActionMissionController.class, params);
    }

    public void editMission(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        params.put("action", "edit");
        params.put("id", id);
        super.window.show(ActionMissionController.class, params);
    }
}

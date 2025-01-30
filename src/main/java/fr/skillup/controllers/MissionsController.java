package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

import java.util.HashMap;
import java.util.Map;

public class MissionsController extends Controller {
    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "missions");
        super.render("missions_view", params);
    }

    public void addMission() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "missions");
        params.put("action", "add");
        super.window.show(ActionMissionController.class, params);
    }

    public void editMission(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "missions");
        params.put("action", "edit");
        params.put("id", id);
        super.window.show(ActionMissionController.class, params);
    }
}

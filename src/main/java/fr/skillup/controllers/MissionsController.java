package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.MissionModel;

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

    public void viewMissions() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        super.window.show(MissionsController.class, params);
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

    public String getMissions() {
        MissionModel model = Model.get(MissionModel.class);
        return model.getMissions().toJson();
    }
}

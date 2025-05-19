package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.MissionModel;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends Controller {

    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "home");
        this.render("home_view", params);
    }

    public String getWarningMissions() {
        MissionModel model = Model.get(MissionModel.class);
        return model.getWarningMissions().toJson();
    }
}

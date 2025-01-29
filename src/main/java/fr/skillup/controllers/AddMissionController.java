package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

import java.util.HashMap;
import java.util.Map;

public class AddMissionController extends Controller {

    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "missions");
        this.render("addMission");
    }
}

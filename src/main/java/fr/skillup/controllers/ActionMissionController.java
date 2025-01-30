package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

public class ActionMissionController extends Controller {

    @Override
    public void init() {
        this.render("action-mission_view", super.params);
    }
}

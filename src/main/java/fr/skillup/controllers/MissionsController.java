package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

public class MissionsController extends Controller {
    @Override
    public void init() {

        this.render("missions_view", this.params);
    }
}

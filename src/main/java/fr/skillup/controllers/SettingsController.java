package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

public class SettingsController extends Controller {
    @Override
    public void init() {
        this.render("settings_view", this.params);
    }
}

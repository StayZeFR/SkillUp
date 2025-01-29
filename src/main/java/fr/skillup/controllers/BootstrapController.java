package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

public class BootstrapController extends Controller {
    @Override
    public void init() {
        this.render("bootstrap_view");
    }

    public void showHome() {
        super.window.show(HomeController.class);
    }
}

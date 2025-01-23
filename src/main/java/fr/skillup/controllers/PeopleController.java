package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

public class PeopleController extends Controller {
    @Override
    public void init() {
        this.render("people_view", this.params);

    }
}

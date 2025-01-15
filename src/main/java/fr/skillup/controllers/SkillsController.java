package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

public class SkillsController extends Controller {
    @Override
    public void init() {
        this.render("skills_view", this.params);
    }
}

package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

public class StatisticsSkillsController extends Controller {

    public static final String VIEW = "statistics-skills";

    @Override
    public void init() {
        this.render("statistics-skills_view", super.params);
    }
}

package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;

import java.util.HashMap;
import java.util.Map;

public class StatisticsTimeController extends Controller {
    public static final String VIEW = "statistics-time";

    @Override
    public void init() {
        this.render("statistics-time_view", super.params);
    }
}

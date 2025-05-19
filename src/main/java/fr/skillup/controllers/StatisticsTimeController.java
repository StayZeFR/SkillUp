package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.StatisticModel;

public class StatisticsTimeController extends Controller {
    public static final String VIEW = "statistics-time";

    @Override
    public void init() {
        this.render("statistics-time_view", super.params);
    }

    public String getStatYearBySkill(int skillId) {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatYearBySkill(skillId).toJson();
    }

    public String getStatPersonMonthSkills(int skillId) {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatPersonMonthSkills(skillId).toJson();
    }
}

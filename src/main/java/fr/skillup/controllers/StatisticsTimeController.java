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

    /**
     * Récupère les statistiques sur les personnes ayant une compétence
     *
     * @param skillId : id de la compétence
     * @return les statistiques sur les personnes ayant une compétence
     */
    public String getStatYearBySkill(int skillId) {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatYearBySkill(skillId).toJson();
    }

    /**
     * Récupère les statistiques sur les personnes ayant une compétence
     *
     * @param skillId : id de la compétence
     * @return les statistiques sur les personnes ayant une compétence
     */
    public String getStatPersonMonthSkills(int skillId) {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatPersonMonthSkills(skillId).toJson();
    }
}

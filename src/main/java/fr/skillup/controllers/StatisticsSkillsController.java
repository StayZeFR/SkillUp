package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.StatisticModel;

public class StatisticsSkillsController extends Controller {

    public static final String VIEW = "statistics-skills";

    @Override
    public void init() {
        this.render("statistics-skills_view", super.params);
    }

    /**
     * Récupère les statistiques sur les personnes ayant une compétence
     *
     * @param skillId : id de la compétence
     * @return les statistiques sur les personnes ayant une compétence
     */
    public String getStatPersonsHaveSkills(int skillId) {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatPersonsHaveSkills(skillId).toJson();
    }

    /**
     * Récupère les statistiques sur les personnes ayant une compétence
     *
     * @return les statistiques sur les personnes ayant une compétence
     */
    public String getStatTop5Skills() {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatTop5Skills().toJson();
    }
}

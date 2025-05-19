package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.StatisticModel;

import java.util.HashMap;
import java.util.Map;

public class StatisticsController extends Controller {
    private static final String VIEW = "statistics";

    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "statistics");
        this.render("statistics_view", params);
    }

    /**
     * Affiche l'écran de gestion des statistiques
     */
    public void viewStatisticsOverall() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", StatisticsController.VIEW);
        super.window.show(StatisticsController.class, params);
    }

    /**
     * Affiche l'écran de gestion des statistiques par compétences
     */
    public void viewStatisticsSkills() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", StatisticsController.VIEW);
        super.window.show(StatisticsSkillsController.class, params);
    }

    /**
     * Affiche l'écran de gestion des statistiques par temps
     */
    public void viewStatisticsTime() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", StatisticsController.VIEW);
        super.window.show(StatisticsTimeController.class, params);
    }

    /**
     * Récupère les statistiques sur les missions
     *
     * @return les statistiques sur les missions
     */
    public String getStatMissionsCompleted() {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatMissionsCompleted().toJson();
    }

    /**
     * Récupère les statistiques sur les missions par statut
     *
     * @return les statistiques sur les missions par statut
     */
    public String getStatMissionsByStatus() {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatMissionsByStatus().toJson();
    }

    /**
     * Récupère les statistiques sur les missions par type
     *
     * @return les statistiques sur les missions par type
     */
    public String getStatAssignment() {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatAssignment().toJson();
    }

}

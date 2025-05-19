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

    public void viewStatisticsOverall() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", StatisticsController.VIEW);
        super.window.show(StatisticsController.class, params);
    }

    public void viewStatisticsSkills() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", StatisticsController.VIEW);
        super.window.show(StatisticsSkillsController.class, params);
    }

    public void viewStatisticsTime() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", StatisticsController.VIEW);
        super.window.show(StatisticsTimeController.class, params);
    }

    public String getStatMissionsCompleted() {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatMissionsCompleted().toJson();
    }

    public String getStatMissionsByStatus() {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatMissionsByStatus().toJson();
    }

    public String getStatAssignment() {
        StatisticModel model = Model.get(StatisticModel.class);
        return model.getStatAssignment().toJson();
    }

}

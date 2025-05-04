package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;
import fr.skillup.core.utils.DateUtils;
import fr.skillup.models.MissionModel;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MissionsController extends Controller {
    private static final String VIEW = "missions";

    @Override
    public void init() {
        updateMissions("2025-12-10");
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        super.render("missions_view", params);
    }

    public void viewMissions() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        super.window.show(MissionsController.class, params);
    }

    public void addMission() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        params.put("action", "add");
        super.window.show(ActionMissionController.class, params);
    }

    public void editMission(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        params.put("action", "edit");
        params.put("id", id);
        super.window.show(ActionMissionController.class, params);
    }

    public String getMissions() {
        MissionModel model = Model.get(MissionModel.class);
        return model.getMissions().toJson();
    }

    public void updateMissions(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        CompletableFuture.runAsync(() -> {
            MissionModel model = Model.get(MissionModel.class);
            Result missions = model.getMissions();
            missions.forEach(mission -> {
                Date start = DateUtils.toDate(mission.get("mission_start_date"));
                switch (mission.get("life_cycle_label").toString()) {
                    case "In preparation" -> {
                        // TODO
                    }
                    case "Planned" -> {
                        if (date.isEqual(start.toLocalDate()) || date.isAfter(start.toLocalDate())) {
                            model.updateMissionStatus(mission.get("mission_id"), "In progress");
                        }
                    }
                    case "In progress" -> {
                        if (date.isAfter(start.toLocalDate().plusDays(mission.get("mission_duration")))) {
                            model.updateMissionStatus(mission.get("mission_id"), "Done");
                        }
                    }
                    case "Done" -> {
                        // TODO
                    }
                }
            });
        });
    }
}

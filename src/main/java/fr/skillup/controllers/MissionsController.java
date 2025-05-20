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
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class MissionsController extends Controller {
    private static final String VIEW = "missions";

    @Override
    public void init() {
        LocalDate today = LocalDate.now();
        updateMissions(today.toString());
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        super.render("missions_view", params);
    }

    /**
     * Affiche l'écran de gestion des missions
     */
    public void viewMissions() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        super.window.show(MissionsController.class, params);
    }

    /**
     * Afficher l'écran de l'ajout d'une mission
     */
    public void addMission() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        params.put("action", "add");
        super.window.show(ActionMissionController.class, params);
    }

    /**
     * Afficher l'écran de l'édition d'une mission
     *
     * @param id : id de la mission
     */
    public void editMission(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("view", MissionsController.VIEW);
        params.put("action", "edit");
        params.put("id", id);
        super.window.show(ActionMissionController.class, params);
    }

    /**
     * Récupère la liste des missions
     *
     * @return liste des missions
     */
    public String getMissions() {
        MissionModel model = Model.get(MissionModel.class);
        return model.getMissions().toJson();
    }

    /**
     * Mets à jour le statut des missions
     *
     * @param dateStr : date de mise à jour
     */
    public void updateMissions(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        CompletableFuture.runAsync(() -> {
            MissionModel model = Model.get(MissionModel.class);
            Result missions = model.getMissions();
            missions.forEach(mission -> {
                Date start = DateUtils.toDate(mission.get("mission_start_date"));
                if (Objects.equals(mission.get("life_cycle_label").toString(), "Planned")) {
                    if (date.isEqual(start.toLocalDate()) || date.isAfter(start.toLocalDate())) {
                        model.updateMissionStatus(mission.get("mission_id"), "In progress");
                    }
                } else if (Objects.equals(mission.get("life_cycle_label").toString(), "In progress") && date.isAfter(start.toLocalDate().plusDays(Integer.parseInt(mission.get("mission_duration").toString())))) {
                    model.updateMissionStatus(mission.get("mission_id"), "Done");
                }
            });
        });
    }
}

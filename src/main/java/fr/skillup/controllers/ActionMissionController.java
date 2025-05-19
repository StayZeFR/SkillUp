package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.MissionModel;
import fr.skillup.models.SkillModel;

import java.util.List;
import java.util.Map;

public class ActionMissionController extends Controller {

    @Override
    public void init() {
        this.render("action-mission_view", super.params);
    }

    /**
     * Récupère la liste des compétences
     *
     * @return liste des compétences
     */
    public String getSkills() {
        SkillModel model = Model.get(SkillModel.class);
        return model.getSkills().toJson();
    }

    /**
     * Récupère la liste des compétences d'une mission
     *
     * @param skills : liste des compétences
     * @param notIn  : liste des compétences à ne pas inclure
     * @param id     : id de la mission
     * @return liste des compétences
     */
    public String getPeopleSkillsMatch(List<String> skills, List<String> notIn, int id) {
        MissionModel model = Model.get(MissionModel.class);
        return model.getPeopleSkillsMatch(skills, notIn, id).toJson();
    }

    /**
     * Ajoute une mission
     *
     * @param title     : titre de la mission
     * @param date      : date de la mission
     * @param duration  : durée de la mission
     * @param nbPeople  : nombre de personnes
     * @param skills    : compétences de la mission
     * @param people    : personnes de la mission
     * @param lifeCycle : cycle de vie de la mission
     */
    public void addMission(String title, String date, int duration, int nbPeople, Map<String, String> skills, List<String> people, int lifeCycle) {
        MissionModel model = Model.get(MissionModel.class);
        model.addMission(title, date, duration, nbPeople, skills, people, lifeCycle);
    }

    /**
     * Modifie une mission
     *
     * @param id        : id de la mission
     * @param title     : titre de la mission
     * @param date      : date de la mission
     * @param duration  : durée de la mission
     * @param nbPeople  : nombre de personnes
     * @param skills    : compétences de la mission
     * @param people    : personnes de la mission
     * @param lifeCycle : cycle de vie de la mission
     */
    public void editMission(int id, String title, String date, int duration, int nbPeople, Map<String, String> skills, List<String> people, int lifeCycle) {
        MissionModel model = Model.get(MissionModel.class);
        model.editMission(id, title, date, duration, nbPeople, skills, people, lifeCycle);
    }

    /**
     * Récupère une mission
     *
     * @param id : id de la mission
     * @return mission
     */
    public String getMission(int id) {
        MissionModel model = Model.get(MissionModel.class);
        return model.getMission(id).toJson();
    }

    /**
     * Récupère les compétences d'une mission
     *
     * @param id : id de la mission
     * @return compétences
     */
    public String getMissionSkills(int id) {
        MissionModel model = Model.get(MissionModel.class);
        return model.getMissionSkills(id).toJson();
    }

    /**
     * Récupère les personnes d'une mission
     *
     * @param id : id de la mission
     * @return personnes
     */
    public String getMissionPeople(int id) {
        MissionModel model = Model.get(MissionModel.class);
        return model.getMissionPeople(id).toJson();
    }
}

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

    public String getSkills() {
        SkillModel model = Model.get(SkillModel.class);
        return model.getSkills().toJson();
    }

    public String getPeopleSkillsMatch(List<String> skills, List<String> notIn, int id) {
        MissionModel model = Model.get(MissionModel.class);
        return model.getPeopleSkillsMatch(skills, notIn, id).toJson();
    }

    public void addMission(String title, String date, int duration, int nbPeople, Map<String, String> skills, List<String> people, int lifeCycle) {
        MissionModel model = Model.get(MissionModel.class);
        model.addMission(title, date, duration, nbPeople, skills, people, lifeCycle);
    }

    public void editMission(int id, String title, String date, int duration, int nbPeople, Map<String, String> skills, List<String> people, int lifeCycle) {
        MissionModel model = Model.get(MissionModel.class);
        model.editMission(id, title, date, duration, nbPeople, skills, people, lifeCycle);
    }

    public String getMission(int id) {
        MissionModel model = Model.get(MissionModel.class);
        return model.getMission(id).toJson();
    }

    public String getMissionSkills(int id) {
        MissionModel model = Model.get(MissionModel.class);
        return model.getMissionSkills(id).toJson();
    }

    public String getMissionPeople(int id) {
        MissionModel model = Model.get(MissionModel.class);
        return model.getMissionPeople(id).toJson();
    }
}

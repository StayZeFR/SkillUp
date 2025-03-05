package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.SkillModel;

public class ActionMissionController extends Controller {

    @Override
    public void init() {
        this.render("action-mission_view", super.params);
    }

    public String getSkills() {
        SkillModel model = Model.get(SkillModel.class);
        return model.getSkills().toJson();
    }
}

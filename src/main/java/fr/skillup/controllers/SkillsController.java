package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.SkillModel;

import java.util.HashMap;
import java.util.Map;

public class SkillsController extends Controller {
    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "skills");
        this.render("skills_view", params);
    }

    public String getCategories() {
        SkillModel model = Model.get(SkillModel.class);
        return model.getCategories().toJson();
    }

    public String getSkills() {
        SkillModel model = Model.get(SkillModel.class);
        return model.getSkills().toJson();
    }
}

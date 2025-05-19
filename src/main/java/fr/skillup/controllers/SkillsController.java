package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.SkillModel;

import java.util.HashMap;
import java.util.Map;

public class SkillsController extends Controller {
    private static final String VIEW = "skills";

    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", "skills");
        this.render("skills_view", params);
    }

    /**
     * Affiche l'écran de gestion des compétences
     */
    public void viewSkills() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", SkillsController.VIEW);
        super.window.show(SkillsController.class, params);
    }

    /**
     * Récupère la liste des catégories de compétences
     *
     * @return liste des catégories de compétences
     */
    public String getCategories() {
        SkillModel model = Model.get(SkillModel.class);
        return model.getCategories().toJson();
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
}

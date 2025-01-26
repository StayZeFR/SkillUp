package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.PersonModel;
import fr.skillup.models.SkillModel;

public class PeopleController extends Controller {
    @Override
    public void init() {
        this.render("people_view", this.params);

    }

    public String getPeople() {
        PersonModel model = Model.get(PersonModel.class);
        return model.getPersons().toJson();
    }
}

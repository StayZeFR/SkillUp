package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.PersonModel;

public class PeopleController extends Controller {
    @Override
    public void init() {
        this.render("people_view", this.params);
    }

    public String getPeople() {
        PersonModel model = Model.get(PersonModel.class);
        return model.getPersons().toJson();
    }

    public String getPersonSkills(int id) {
        PersonModel model = Model.get(PersonModel.class);
        return model.getPersonSkills(id).toJson();
    }
}

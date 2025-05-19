package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.PersonModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleController extends Controller {
    private static final String VIEW = "people";

    @Override
    public void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", PeopleController.VIEW);
        super.render("people_view", params);
    }

    public void viewPeople() {
        Map<String, Object> params = new HashMap<>();
        params.put("view", PeopleController.VIEW);
        super.window.show(PeopleController.class, params);
    }

    public String getPeople() {
        PersonModel model = Model.get(PersonModel.class);
        return model.getPersons().toJson();
    }

    public String getPersonSkills(int id) {
        PersonModel model = Model.get(PersonModel.class);
        return model.getPersonSkills(id).toJson();
    }

    public void savePerson(int id, String firstName, String lastName, String job, List<String> skills) {
        PersonModel model = Model.get(PersonModel.class);
        model.savePerson(id, firstName, lastName, job, skills);
    }

    public void addPerson(String firstName, String lastName, String job, List<String> skills) {
        PersonModel model = Model.get(PersonModel.class);
        model.addPerson(firstName, lastName, job, skills);
    }
}

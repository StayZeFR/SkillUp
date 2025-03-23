package fr.skillup.controllers;

import fr.skillup.core.controller.Controller;
import fr.skillup.core.model.Model;
import fr.skillup.models.PersonModel;
import java.util.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PeopleController extends Controller {
    private static final String VIEW = "people";
    @Override
    public void init() {
        System.out.println("PeopleController.init() called from Bridge");
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

    public void updatePerson(String firstname, String lastname, String job) {
        Logger logger = Logger.getLogger(PeopleController.class.getName());
        logger.info("Méthode updatePerson appelée.");

        logger.info("Prénom : " + firstname);
        logger.info("Nom : " + lastname);
        logger.info("Emploi : " + job);
    }
}

package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

public class PersonModel extends Model {

    public Result getPersons() {
        return this.select("SELECT id, firstname, lastname FROM person", Integer.class, String.class, String.class);
    }

}

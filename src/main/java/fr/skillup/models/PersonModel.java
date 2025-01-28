package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

import java.sql.Date;

public class PersonModel extends Model {

    public Result getPersons() {
        return this.select("SELECT id, firstname, lastname, entry_date, job, picture FROM person ORDER BY id", Integer.class, String.class, String.class, Date.class, String.class, String.class);
    }

}

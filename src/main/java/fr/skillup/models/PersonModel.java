package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PersonModel extends Model {

    public Result getPersons() {
        return this.select("SELECT id, firstname, lastname, DATE_FORMAT(entry_date, '%d/%m/%Y') AS entry_date, job, picture FROM person ORDER BY id", Integer.class, String.class, String.class, Date.class, String.class, String.class);
    }

    public Result getPerson(int id) {
        List<Object> params = new ArrayList<>();
        params.add(id);
        return this.select("SELECT id, firstname, lastname, DATE_FORMAT(entry_date, '%d/%m/%Y') AS entry_date, job, picture FROM person WHERE id = ?", params, Integer.class, String.class, String.class, Date.class, String.class, String.class);
    }

    public Result getPersonSkills(int id) {
        List<Object> params = List.of(id);
        String query = "SELECT c.id AS category_id, c.label AS category_label, c.color AS category_color, c.icon AS category_icon, s.id AS skill_id, s.label AS skill_label FROM category c, skill s, person_skill ps WHERE c.id = s.category_id AND s.id = ps.skill_id AND ps.person_id = ?;";
        return this.select(query, params, Integer.class, String.class, String.class, String.class, Integer.class, String.class);
    }

}

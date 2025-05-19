package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PersonModel extends Model {

    /**
     * Récupération de la liste des personnes
     *
     * @return les personnes
     */
    public Result getPersons() {
        return this.select("SELECT id, firstname, lastname, DATE_FORMAT(entry_date, '%d/%m/%Y') AS entry_date, job, picture FROM person ORDER BY id", Integer.class, String.class, String.class, Date.class, String.class, String.class);
    }

    /**
     * Récupération d'une personne
     *
     * @param id l'id de la personne
     * @return la personne
     */
    public Result getPerson(int id) {
        List<Object> params = new ArrayList<>();
        params.add(id);
        return this.select("SELECT id, firstname, lastname, DATE_FORMAT(entry_date, '%d/%m/%Y') AS entry_date, job, picture FROM person WHERE id = ?", params, Integer.class, String.class, String.class, Date.class, String.class, String.class);
    }

    /**
     * Récupération de la liste des compétences d'une personne
     *
     * @param id : l'id de la personne
     * @return la liste des compétences
     */
    public Result getPersonSkills(int id) {
        List<Object> params = List.of(id);
        String query = "SELECT c.id AS category_id, c.label AS category_label, c.color AS category_color, c.icon AS category_icon, s.id AS skill_id, s.label AS skill_label FROM category c, skill s, person_skill ps WHERE c.id = s.category_id AND s.id = ps.skill_id AND ps.person_id = ?;";
        return this.select(query, params, Integer.class, String.class, String.class, String.class, Integer.class, String.class);
    }

    /**
     * Modifie une personne
     *
     * @param id        : l'id de la personne
     * @param firstName : le prénom
     * @param lastName  : le nom
     * @param job       : le métier
     */
    public void savePerson(int id, String firstName, String lastName, String job) {
        List<Object> params = List.of(firstName, lastName, job, id);
        this.execute("UPDATE person SET firstname = ?, lastname = ?, job = ? WHERE id = ?", params);
    }

    /**
     * Ajoute une nouvelle personne dans la base
     *
     * @param firstName : le prénom
     * @param lastName  : le nom
     * @param job       : le métier
     */
    public void insertPerson(String firstName, String lastName, String job, String picture) {
        List<Object> params = List.of(firstName, lastName, job, picture.isEmpty() ? null : picture);
        this.execute(
                "INSERT INTO person (firstname, lastname, job, picture, entry_date) VALUES (?, ?, ?, ?, NOW())",
                params
        );
    }



}

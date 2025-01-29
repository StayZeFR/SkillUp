package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

public class SkillModel extends Model {

    public Result getCategories() {
        String query = "SELECT id, label, color, icon FROM category ORDER BY id;";
        return super.select(query, Integer.class, String.class, String.class, String.class);
    }

    public Result getSkills() {
        String query = "SELECT s.id AS 'skill_id', s.label AS 'skill_label', s.category_id, c.label AS 'category_label', c.color AS 'category_color', c.icon AS 'category_icon' FROM skill s, category c WHERE s.category_id = c.id;";
        return super.select(query, Integer.class, String.class, Integer.class, String.class, String.class, String.class);
    }

}

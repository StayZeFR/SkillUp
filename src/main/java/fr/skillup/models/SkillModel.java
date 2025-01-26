package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

public class SkillModel extends Model {

    public Result getCategories() {
        String query = "SELECT id, label, color, icon FROM category ORDER BY id;";
        return super.select(query, Integer.class, String.class, String.class, String.class);
    }

    public Result getSkills() {
        String query = "SELECT s.id as 'skill_id', s.label as 'skill_label', s.category_id, c.label as 'category_label', c.color as 'category_color', c.icon as 'category_icon' FROM skill s, category c WHERE s.category_id = c.id;";
        return super.select(query, Integer.class, String.class, Integer.class, String.class, String.class, String.class);
    }

}

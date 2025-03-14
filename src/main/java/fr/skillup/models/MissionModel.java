package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

import java.util.List;

public class MissionModel extends Model {

    public Result getPeopleSkillsMatch(List<String> skills) {
        String query = "select\n" +
                "    p.id as person_id,\n" +
                "    p.firstname as person_firstname,\n" +
                "    p.lastname as person_lastname,\n" +
                "    COALESCE((\n" +
                "    select GROUP_CONCAT(ps.skill_id SEPARATOR ',')\n" +
                "        from person_skill ps\n" +
                "        where ps.person_id = p.id\n" +
                "        and ps.skill_id in (" + String.join(",", skills) + ")\n" +
                "    ), '') as matchs,\n" +
                "    (select count(*)\n" +
                "     from person_skill ps\n" +
                "     where ps.person_id = p.id\n" +
                "     and ps.skill_id in (" + String.join(",", skills) + "))\n" +
                "     as nb_matchs\n" +
                "from person p\n" +
                "where p.id not in (select pm.person_id from person_mission pm, mission m, life_cycle lc where m.life_cycle_id = lc.id and lc.label = 'DONE')" +
                "and exists (select * from person_skill ps where ps.person_id = p.id and ps.skill_id in (" + String.join(",", skills) + "))" +
                "order by nb_matchs desc;";

        return super.select(query, Integer.class, String.class, String.class, String.class, Integer.class);
    }

}

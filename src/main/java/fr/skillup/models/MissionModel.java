package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

import java.util.List;
import java.util.Map;

public class MissionModel extends Model {
    public Result getPeopleSkillsMatch(List<String> skills, List<String> notIn, int id) {
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
                "where p.id not in (select distinct pm.person_id from person_mission pm, mission m, life_cycle lc where pm.mission_id = m.id and m.life_cycle_id = lc.id and lc.label <> 'DONE' " + (id == 0 ? "" : "and m.id <> ?") + ")\n" +
                "and exists (select * from person_skill ps where ps.person_id = p.id and ps.skill_id in (" + String.join(",", skills) + "))" +
                (!notIn.isEmpty() ? "and p.id not in (" + String.join(",", notIn) + ")\n" : "") +
                "order by nb_matchs desc;";
        if (id == 0) {
            return super.select(query, Integer.class, String.class, String.class, String.class, Integer.class);
        }
        List<Object> params = List.of(id);
        return super.select(query, params, Integer.class, String.class, String.class, String.class, Integer.class);
    }

    public void addMission(String title, String date, int duration, int nbPeople, Map<String, String> skills, List<String> people, int lifeCycle) {
        String query = "insert into mission (title, start_date, duration, nb_people, life_cycle_id)\n" +
                "values (?, ?, ?, ?, ?);";
        List<Object> params = List.of(title, date, duration, nbPeople, lifeCycle);
        int id = super.insert(query, params);
        for (Map.Entry<String, String> entry : skills.entrySet()) {
            query = "insert into mission_skill (mission_id, skill_id, nb_people)\n" +
                    "values (?, ?, ?);";
            params = List.of(id, entry.getKey(), entry.getValue());
            super.insert(query, params);
        }
        for (String person : people) {
            query = "insert into person_mission (person_id, mission_id)\n" +
                    "values (?, ?);";
            params = List.of(person, id);
            super.insert(query, params);
        }
    }

    public void editMission(int id, String title, String date, int duration, int nbPeople, Map<String, String> skills, List<String> people, int lifeCycle) {
        String query = "update mission\n" +
                "set title = ?, start_date = ?, duration = ?, nb_people = ?, life_cycle_id = ?\n" +
                "where id = ?;";
        List<Object> params = List.of(title, date, duration, nbPeople, lifeCycle, id);
        super.execute(query, params);
        query = "delete from mission_skill\n" +
                "where mission_id = ?;";
        params = List.of(id);
        super.execute(query, params);
        for (Map.Entry<String, String> entry : skills.entrySet()) {
            query = "insert into mission_skill (mission_id, skill_id, nb_people)\n" +
                    "values (?, ?, ?);";
            params = List.of(id, entry.getKey(), entry.getValue());
            super.insert(query, params);
        }
        query = "delete from person_mission\n" +
                "where mission_id = ?;";
        params = List.of(id);
        super.execute(query, params);
        for (String person : people) {
            query = "insert into person_mission (person_id, mission_id)\n" +
                    "values (?, ?);";
            params = List.of(person, id);
            super.insert(query, params);
        }
    }

    public Result getMissions() {
        String query = "select\n" +
                "    m.id as mission_id,\n" +
                "    m.title as mission_title,\n" +
                "    DATE_FORMAT(m.start_date, '%d/%m/%Y') as mission_start_date,\n" +
                "    m.duration as mission_duration,\n" +
                "    m.nb_people as mission_nb_people,\n" +
                "    lc.id as life_cycle_id,\n" +
                "    lc.label as life_cycle_label\n" +
                "from mission m, life_cycle lc\n" +
                "where m.life_cycle_id = lc.id;";
        return super.select(query, Integer.class, String.class, String.class, Integer.class, Integer.class, String.class);
    }

    public Result getMission(int id) {
        String query = "select\n" +
                "    id,\n" +
                "    title,\n" +
                "    DATE_FORMAT(start_date, '%d/%m/%Y') as start_date,\n" +
                "    duration,\n" +
                "    nb_people,\n" +
                "    life_cycle_id\n" +
                "from\n" +
                "    mission\n" +
                "where id = ?;";
        List<Object> params = List.of(id);
        return super.select(query, params, Integer.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
    }

    public Result getMissionSkills(int id) {
        String query = "select\n" +
                "    mission_id,\n" +
                "    skill_id,\n" +
                "    nb_people,\n" +
                "    (nb_people - (select count(*) from person_mission pm, person_skill ps where pm.mission_id = ms.mission_id and pm.person_id = ps.person_id and ps.skill_id = ms.skill_id)) as missing\n" +
                "from\n" +
                "    mission_skill ms\n" +
                "where mission_id = ?;";
        List<Object> params = List.of(id);
        return super.select(query, params, Integer.class, Integer.class, Integer.class, Integer.class);
    }

    public Result getMissionPeople(int id) {
        String query = "select\n" +
                "    p.id as person_id,\n" +
                "    p.firstname as person_firstname,\n" +
                "    p.lastname as person_lastname,\n" +
                "    GROUP_CONCAT(ps.skill_id SEPARATOR ',') as matchs,\n" +
                "    count(ps.skill_id) as nb_matchs\n" +
                "from\n" +
                "   person_mission pm,\n" +
                "   mission_skill ms,\n" +
                "   person_skill ps,\n" +
                "   person p\n" +
                "where\n" +
                "   pm.mission_id = ms.mission_id\n" +
                "   and pm.person_id = ps.person_id\n" +
                "   and ms.skill_id = ps.skill_id\n" +
                "   and pm.person_id = p.id\n" +
                "   and pm.mission_id = ?\n" +
                "group by\n" +
                "   pm.mission_id,\n" +
                "   pm.person_id;";
        List<Object> params = List.of(id);
        return super.select(query, params, Integer.class, String.class, String.class, String.class, Integer.class);
    }
}

package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

import java.util.List;
import java.util.Map;

public class MissionModel extends Model {
    public Result getPeopleSkillsMatch(List<String> skills, List<String> notIn, int id) {
        String query = """
                        select
                            p.id as person_id,
                            p.firstname as person_firstname,
                            p.lastname as person_lastname,
                            COALESCE((
                                select GROUP_CONCAT(ps.skill_id SEPARATOR ',')
                                from person_skill ps
                                where ps.person_id = p.id
                                and ps.skill_id in (%s)
                            ), '') as matchs,
                            (
                                select count(*)
                                from person_skill ps
                                where ps.person_id = p.id
                                and ps.skill_id in (%s)
                            ) as nb_matchs
                        from person p
                        where p.id not in (
                            select distinct pm.person_id
                            from person_mission pm, mission m, life_cycle lc
                            where pm.mission_id = m.id
                            and m.life_cycle_id = lc.id
                            and lc.label <> 'DONE' %s
                        )
                        and exists (
                            select *
                            from person_skill ps
                            where ps.person_id = p.id
                            and ps.skill_id in (%s)
                        )
                        %s
                        order by nb_matchs desc;
                """.formatted(
                String.join(",", skills),
                String.join(",", skills),
                (id == 0 ? "" : "and m.id <> ?"),
                String.join(",", skills),
                (!notIn.isEmpty() ? "and p.id not in (%s)".formatted(String.join(",", notIn)) : "")
        );

        if (id == 0) {
            return super.select(query, Integer.class, String.class, String.class, String.class, Integer.class);
        }
        List<Object> params = List.of(id);
        return super.select(query, params, Integer.class, String.class, String.class, String.class, Integer.class);
    }

    public void addMission(String title, String date, int duration, int nbPeople, Map<String, String> skills, List<String> people, int lifeCycle) {
        String query = "insert into mission (title, start_date, duration, nb_people, life_cycle_id) values (?, ?, ?, ?, ?);";
        List<Object> params = List.of(title, date, duration, nbPeople, lifeCycle);
        int id = super.insert(query, params);
        for (Map.Entry<String, String> entry : skills.entrySet()) {
            query = "insert into mission_skill (mission_id, skill_id, nb_people) values (?, ?, ?);";
            params = List.of(id, entry.getKey(), entry.getValue());
            super.insert(query, params);
        }
        for (String person : people) {
            query = "insert into person_mission (person_id, mission_id) values (?, ?);";
            params = List.of(person, id);
            super.insert(query, params);
        }
    }

    public void editMission(int id, String title, String date, int duration, int nbPeople, Map<String, String> skills, List<String> people, int lifeCycle) {
        String query = """
                update mission
                set title = ?, start_date = ?, duration = ?, nb_people = ?, life_cycle_id = ?
                where id = ?;
                """;
        List<Object> params = List.of(title, date, duration, nbPeople, lifeCycle, id);
        super.execute(query, params);
        query = "delete from mission_skill where mission_id = ?;";
        params = List.of(id);
        super.execute(query, params);
        for (Map.Entry<String, String> entry : skills.entrySet()) {
            query = "insert into mission_skill (mission_id, skill_id, nb_people) values (?, ?, ?);";
            params = List.of(id, entry.getKey(), entry.getValue());
            super.insert(query, params);
        }
        query = "delete from person_mission where mission_id = ?;";
        params = List.of(id);
        super.execute(query, params);
        for (String person : people) {
            query = "insert into person_mission (person_id, mission_id) values (?, ?);";
            params = List.of(person, id);
            super.insert(query, params);
        }
    }

    public Result getMissions() {
        String query = """
                select
                    m.id as mission_id,
                    m.title as mission_title,
                    DATE_FORMAT(m.start_date, '%d/%m/%Y') as mission_start_date,
                    m.duration as mission_duration,
                    m.nb_people as mission_nb_people,
                    lc.id as life_cycle_id,
                    lc.label as life_cycle_label
                from
                    mission m,
                    life_cycle lc
                where
                    m.life_cycle_id = lc.id;
                """;
        return super.select(query, Integer.class, String.class, String.class, Integer.class, Integer.class, Integer.class, String.class);
    }

    public Result getMission(int id) {
        String query = """
                select
                    id,
                    title,
                    DATE_FORMAT(start_date, '%d/%m/%Y') as start_date,
                    duration,
                    nb_people,
                    life_cycle_id
                from
                    mission
                where id = ?;
                """;
        List<Object> params = List.of(id);
        return super.select(query, params, Integer.class, String.class, String.class, Integer.class, Integer.class, Integer.class);
    }

    public Result getMissionSkills(int id) {
        String query = """
                select
                    mission_id,
                    skill_id,
                    nb_people,
                    (
                        nb_people - (
                            select count(*)
                            from person_mission pm, person_skill ps
                            where pm.mission_id = ms.mission_id
                            and pm.person_id = ps.person_id
                            and ps.skill_id = ms.skill_id
                        )
                    ) as missing
                from
                    mission_skill ms
                where mission_id = ?;
                """;
        List<Object> params = List.of(id);
        return super.select(query, params, Integer.class, Integer.class, Integer.class, Integer.class);
    }

    public Result getMissionPeople(int id) {
        String query = """
                select
                    p.id as person_id,
                    p.firstname as person_firstname,
                    p.lastname as person_lastname,
                    GROUP_CONCAT(ps.skill_id SEPARATOR ',') as matchs,
                    count(ps.skill_id) as nb_matchs
                from
                    person_mission pm,
                    mission_skill ms,
                    person_skill ps,
                    person p
                where
                    pm.mission_id = ms.mission_id
                    and pm.person_id = ps.person_id
                    and ms.skill_id = ps.skill_id
                    and pm.person_id = p.id
                    and pm.mission_id = ?
                group by
                    pm.mission_id,
                    pm.person_id;
                """;
        List<Object> params = List.of(id);
        return super.select(query, params, Integer.class, String.class, String.class, String.class, Integer.class);
    }

    public void updateMissionStatus(int missionId, String status) {
        String query = """
                update mission
                set life_cycle_id = (select id from life_cycle where label = ?)
                where id = ?;
                """;
        List<Object> params = List.of(status, missionId);
        super.execute(query, params);
    }
}

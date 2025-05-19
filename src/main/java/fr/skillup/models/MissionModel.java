package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

import java.util.List;
import java.util.Map;

public class MissionModel extends Model {

    /**
     * Récupère les personnes qui ont les compétences demandées
     *
     * @param skills : les compétences demandées
     * @param notIn  : les personnes à exclure
     * @param id     : l'id de la mission
     * @return : la liste des personnes qui ont les compétences demandées
     */
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

    /**
     * Ajoute une mission
     *
     * @param title     : le titre de la mission
     * @param date      : la date de début de la mission
     * @param duration  : la durée de la mission (en jours)
     * @param nbPeople  : le nombre de personnes nécessaires pour la mission
     * @param skills    : les compétences nécessaires pour la mission
     * @param people    : les personnes affectées à la mission
     * @param lifeCycle : le cycle de vie de la mission (voir la table life_cycle)
     */
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

    /**
     * Modifie une mission
     *
     * @param id        : l'id de la mission
     * @param title     : le titre de la mission
     * @param date      : la date de début de la mission
     * @param duration  : la durée de la mission (en jours)
     * @param nbPeople  : le nombre de personnes nécessaires pour la mission
     * @param skills    : les compétences nécessaires pour la mission
     * @param people    : les personnes affectées à la mission
     * @param lifeCycle : le cycle de vie de la mission (voir la table life_cycle)
     */
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

    /**
     * Récupère toutes les missions
     *
     * @return : la liste des missions
     */
    public Result getMissions() {
        String query = """
                select
                    m.id as mission_id,
                    m.title as mission_title,
                    DATE_FORMAT(m.start_date, '%d/%m/%Y') as mission_start_date,
                    m.duration as mission_duration,
                    m.nb_people as mission_nb_people,
                    lc.id as life_cycle_id,
                    lc.label as life_cycle_label,
                    case when DATE(DATE_ADD(NOW(), INTERVAL 1 DAY)) = start_date and lc.label = 'In preparation' then 1 else 0 end as warning
                from
                    mission m,
                    life_cycle lc
                where
                    m.life_cycle_id = lc.id;
                """;
        return super.select(query, Integer.class, String.class, String.class, Integer.class, Integer.class, Integer.class, String.class, Integer.class);
    }

    /**
     * Récupère une mission
     *
     * @param id : l'id de la mission
     * @return : la mission
     */
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

    /**
     * Récupère les compétences d'une mission
     *
     * @param id : l'id de la mission
     * @return : la liste des compétences
     */
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

    /**
     * Récupère les personnes affectées à une mission
     *
     * @param id : l'id de la mission
     * @return : la liste des personnes
     */
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

    /**
     * Modifie le statut d'une mission
     *
     * @param missionId : l'id de la mission
     * @param status    : le statut de la mission
     */
    public void updateMissionStatus(int missionId, String status) {
        String query = """
                update mission
                set life_cycle_id = (select id from life_cycle where label = ?)
                where id = ?;
                """;
        List<Object> params = List.of(status, missionId);
        super.execute(query, params);
    }

    /**
     * Récupère les missions en préparation qui commencent demain
     *
     * @return : la liste des missions
     */
    public Result getWarningMissions() {
        String query = """
                select
                	m.id as mission_id,
                    m.title as mission_title,
                    DATE_FORMAT(m.start_date, '%d/%m/%Y') as mission_start_date,
                    m.duration as mission_duration
                from mission m, life_cycle lc
                where m.life_cycle_id = lc.id
                and lc.label = 'In preparation'
                and DATE(DATE_ADD(NOW(), INTERVAL 1 DAY)) = m.start_date;
                """;
        return super.select(query, Integer.class, String.class, String.class, Integer.class);
    }
}

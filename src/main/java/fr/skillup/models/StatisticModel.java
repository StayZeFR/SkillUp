package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

import java.util.List;

public class StatisticModel extends Model {

    /**
     * Récupère le nombre de missions en cours et terminées
     *
     * @return le nombre de missions en cours et terminées
     */
    public Result getStatMissionsCompleted() {
        String request = """
                select
                    sum( case when lc.label <> 'Done' then 1 else 0 end ) as progress,
                    sum( case when lc.label = 'Done' then 1 else 0 end ) as done
                from mission m, life_cycle lc
                where m.life_cycle_id = lc.id;
                """;
        return super.select(request, Integer.class, Integer.class);
    }

    /**
     * Récupère le nombre de missions par statut
     *
     * @return le nombre de missions par statut
     */
    public Result getStatMissionsByStatus() {
        String request = """
                select
                    sum( case when lc.label = 'In preparation' then 1 else 0 end ) as in_prep,
                    sum( case when lc.label = 'Planned' then 1 else 0 end ) as planned,
                    sum( case when lc.label = 'In progress' then 1 else 0 end ) as in_progress,
                    sum( case when lc.label = 'Done' then 1 else 0 end ) as done
                from mission m, life_cycle lc
                where m.life_cycle_id = lc.id
                """;
        return super.select(request, Integer.class, Integer.class, Integer.class, Integer.class);
    }

    /**
     * Récupère le nombre de personnes assignées et non assignées à une mission
     *
     * @return le nombre de personnes assignées et non assignées à une mission
     */
    public Result getStatAssignment() {
        String request = """
                select
                    sum( case when pm.mission_id is not null then 1 else 0 end) as assigned,
                    sum( case when pm.mission_id is null then 1 else 0 end) as unassigned
                from person p
                left join person_mission pm on p.id = pm.person_id
                left join mission m on pm.mission_id = m.id
                left join life_cycle lc on m.life_cycle_id = lc.id
                where lc.label <> 'Done' or lc.label is null;
                """;
        return super.select(request, Integer.class, Integer.class);
    }

    /**
     * Récupère le nombre de personnes ayant une compétence
     *
     * @param skillId : l'id de la compétence
     * @return le nombre de personnes ayant la compétence
     */
    public Result getStatPersonsHaveSkills(int skillId) {
        String request = """
                select
                	count(distinct person_id) as nb_have_skill,
                    (select count(*) from person) as nb_total
                from person_skill
                where skill_id = ?;
                """;
        List<Object> params = List.of(skillId);
        return super.select(request, params, Integer.class, Integer.class);
    }

    /**
     * Récupère le top 5 des compétences les plus demandées
     *
     * @return le top 5 des compétences les plus demandées
     */
    public Result getStatTop5Skills() {
        String request = """
                select
                	s.label as skill_label,
                	count(ms.skill_id) as nb
                from mission_skill ms, skill s
                where ms.skill_id = s.id
                group by skill_id
                order by nb desc
                limit 5;
                """;
        return super.select(request, String.class, Integer.class);
    }

    /**
     * Récupère le nombre de missions par mois pour une compétence donnée
     *
     * @param skillId : l'id de la compétence
     * @return le nombre de missions par mois pour la compétence
     */
    public Result getStatYearBySkill(int skillId) {
        String request = """
                with months as
                	(select 1 as m
                     union all
                     select 2
                     union all
                     select 3
                     union all
                     select 4
                     union all
                     select 5
                     union all
                     select 6
                     union all
                     select 7
                     union all
                     select 8
                     union all
                     select 9
                     union all
                     select 10
                     union all
                     select 11
                     union all
                     select 12)
                select
                	months.m,
                	count(m.id) as total
                from months
                left join mission m on year(m.start_date) = year(now()) and month(m.start_date) = months.m and\s
                	(select 1
                     from mission_skill ms
                     where ms.mission_id = m.id
                     and ms.skill_id = ?)
                group by months.m
                order by months.m;
                """;
        List<Object> params = List.of(skillId);
        return super.select(request, params, Integer.class, Integer.class);
    }

}

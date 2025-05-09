package fr.skillup.models;

import fr.skillup.core.database.Result;
import fr.skillup.core.model.Model;

public class StatisticModel extends Model {

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

}

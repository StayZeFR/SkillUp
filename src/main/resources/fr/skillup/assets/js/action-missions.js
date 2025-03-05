App.onLoad(async () => {
    Bridge.getAsync("ActionMissionController", "getSkills", []).then((result) => {
        const skills = result.reduce((group, element) => {
            const {category_label} = element;
            group[category_label] = group[category_label] ?? [];
            group[category_label].push(element);
            return group;
        }, {});

        const select = document.getElementById("select-skills");
        select.setEnableSearch(true);
        select.setTitle("Select Skills");
        select.setMultiple(true);
        for (const category_label in skills) {
            for (const skill of skills[category_label]) {
                select.addOption(skill.skill_id, skill.skill_label, category_label);
            }
        }

        select.addEventListener("change", (event) => {
            const { value, label, checked } = event.detail;
        });

        /*skills.forEach((skill) => {
            App.log(JSON.stringify(skill));
            const { category_label, skills } = skill;
            App.log(category_label);
            document.getElementById("select-people").innerHTML = "<span>" + category_label + "</span>";
            skills.forEach((skill) => {
                document.getElementById("select-people").innerHTML += "<option value=" + skill.skill_id + ">" + skill.skill_label + "</option>";
            });
        })*/
    });
});
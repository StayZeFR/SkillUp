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

        const skillElement = (id, title) => {
            const skill = result.find(item => item.skill_id === parseInt(id));
            const icon = skill.category_icon;
            const color = skill.category_color;

            return `
                        <div class="skill" data-id="${id}">
                            <div style="background-color: ${color}">
                                ${icon}
                                <span>${title}</span>
                            </div>
                            <div class="action">
                                <div class="people">
                                    <button>-</button>
                                    <div>
                                        <span class="nb-people">0</span>
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
                                        </svg>
                                    </div>
                                    <button>+</button>
                                </div>
                                <button>
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
                                    </svg>
                                </button>
                                <div class="alert">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126ZM12 15.75h.007v.008H12v-.008Z" />
                                    </svg>
                                    <span><span>3</span> people missing</span>
                                </div>
                            </div>
                        </div>
        `};

        select.addEventListener("change", (event) => {
            const { value, label, checked } = event.detail;
            if (checked) {
                document.getElementById("list-skills").innerHTML += skillElement(value, label);
            } else {
                document.querySelector(`.skill[data-id="${value}"]`).remove();
            }
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
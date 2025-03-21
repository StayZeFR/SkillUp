let skillsSelected = {};
let peopleSelected = {};
let peopleFind = [];

App.onLoad(async () => {
    const select = document.getElementById("select-skills");
    const people = document.getElementById("select-people");

    select.setEnableSearch(true);
    select.setTitle("Select Skills");
    select.setMultiple(true);

    people.setDisabled(true);
    people.setTitle("Select person");

    document.getElementById("valid").innerText = window.params.get("action") === "add" ? "Add mission" : "Save";

    document.getElementById("date-mission").addEventListener("input", (event) => {
        let value = event.target.value.replace(/\D/g, "");
        let formattedValue = '';

        if (value.length > 2) {
            formattedValue = value.substring(0, 2) + "/";
        } else {
            formattedValue = value;
        }

        if (value.length > 4) {
            formattedValue += value.substring(2, 4) + "/";
            formattedValue += value.substring(4, 8);
        } else if (value.length > 2) {
            formattedValue += value.substring(2, 4);
        }

        event.target.value = formattedValue;
    });

    document.getElementById("nb_people-mission").addEventListener("input", (event) => {
        const value = parseInt(event.target.value);
        if (value > 0) {
            people.setDisabled(false);
            resetPeopleSelected();
        } else {
            people.setDisabled(true);
            people.clear();
        }
    });

    Bridge.getAsync("ActionMissionController", "getSkills", []).then((result) => {
        const skills = result.reduce((group, element) => {
            const {category_label} = element;
            group[category_label] = group[category_label] ?? [];
            group[category_label].push(element);
            return group;
        }, {});

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
                                    <button onclick="managePeople('-', '${id}')">-</button>
                                    <div>
                                        <span class="nb-people">1</span>
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
                                        </svg>
                                    </div>
                                    <button onclick="managePeople('+', '${id}')">+</button>
                                </div>
                                <button onclick="deleteSkill('${id}')">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
                                    </svg>
                                </button>
                                <div class="alert">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126ZM12 15.75h.007v.008H12v-.008Z" />
                                    </svg>
                                    <span><span>1</span> people missing</span>
                                </div>
                            </div>
                        </div>
        `
        };

        select.addEventListener("change", (event) => {
            const {value, label, checked} = event.detail;

            if (checked) {
                skillsSelected[value] = {nbPeople: 1, missing: 1};
                document.getElementById("list-skills").innerHTML += skillElement(value, label);
            } else {
                delete skillsSelected[value];
            }

            if (Object.entries(skillsSelected).length <= 0) {
                people.setDisabled(true);
            } else {
                people.setDisabled(false);
                resetPeopleSelected();
            }
        });

    });
});

function cancel() {
    Bridge.call("layouts.DefaultLayoutController", "moveTo", ["missions"]);
}

function valid() {
    const title = document.getElementById("title-mission").value;
    const date = document.getElementById("date-mission").value;
    const duration = document.getElementById("duration-mission").value;
    const nbPeople = document.getElementById("nb_people-mission").value;

    /*if (title === "" || date === "" || duration === "" || nbPeople === "") {
        return;
    }*/
    

    App.log(JSON.stringify(skillsSelected));
    App.log(JSON.stringify(peopleSelected));
}

function managePeople(operator, id) {
    if (operator === "+") {
        skillsSelected[id].nbPeople++;
        skillsSelected[id].missing++;
    } else if (operator === "-") {
        if (skillsSelected[id].nbPeople > 1) {
            skillsSelected[id].nbPeople--;
            skillsSelected[id].missing--;
        }
    }
    document.querySelector(`.skill[data-id="${id}"] .nb-people`).innerText = skillsSelected[id].nbPeople;
    document.querySelector(`.skill[data-id="${id}"] .alert span span`).innerText = skillsSelected[id].missing;
    document.getElementById("select-people").setDisabled(false);
}

function deleteSkill(id) {
    delete skillsSelected[id];
    document.querySelector(`.skill[data-id="${id}"]`).remove();
    document.getElementById("select-skills").unselect(id);
}

function findMatchingPeople() {
    const select = document.getElementById("select-skills");
    const people = document.getElementById("select-people");

    let selected = select.getSelected();
    if (!(selected instanceof Array)) {
        selected = [selected];
    }

    let skillsMatching = [];
    for (const skill of selected) {
        if (skillsSelected[skill].missing === 0) {
            skillsMatching.push(skill);
        }
    }
    const skills = selected.filter(item => !skillsMatching.includes(item));

    if (skills.length > 0) {
        people.setDisabled(false);
        Bridge.getAsync("ActionMissionController", "getPeopleSkillsMatch", [skills, Object.keys(peopleSelected)]).then((result) => {
            people.clear();
            peopleFind = result;
            for (const person of result) {
                const html = `
                        <div style="display: flex; flex-direction: column; gap: 5px;">
                            <span>${person.person_firstname} ${person.person_lastname}</span>
                            <div style="background-color: #F2F0FD; border-radius: 5px; padding: 5px;">
                                ${person.nb_matchs} matching skill${person.nb_matchs > 1 ? "s" : ""}
                            </div>
                        </div>
                    `;
                people.addOption(person.person_id, html);
            }
        });
    } else {
        people.clear();
        people.setDisabled(true);
    }
}

function resetPeopleSelected() {
    for (const skill in skillsSelected) {
        skillsSelected[skill].missing = skillsSelected[skill].nbPeople;
        document.querySelector(`.skill[data-id="${skill}"] .alert span span`).innerText = skillsSelected[skill].missing;
    }
    peopleSelected = {};
    peopleFind = [];
    document.getElementById("list-people").innerHTML = "";
    findMatchingPeople();
}

function addPerson() {
    const selected = document.getElementById("select-people").getSelected();
    if (selected) {
        const person = peopleFind.find(item => item.person_id === parseInt(selected));
        peopleSelected[selected] = person; 
        const html = `
                        <div class="person">
                            <div>
                                <span>${person.person_firstname} ${person.person_lastname}</span>
                                <button>
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
                                    </svg>
                                </button>
                            </div>
                            <div class="matching">
                                <span><span>${person.nb_matchs}</span> matching skills</span>
                            </div>
                        </div>
        `;
        document.getElementById("list-people").innerHTML += html;
        const skills = person.matchs.split(",");
        for (const skill of skills) {
            if (skillsSelected[skill].missing > 0) {
                skillsSelected[skill].missing--;
                document.querySelector(`.skill[data-id="${skill}"] .alert span span`).innerText = skillsSelected[skill].missing;
            }
        }
        const max = parseInt(document.getElementById("nb_people-mission").value);
        if (Object.entries(peopleSelected).length >= max) {
            document.getElementById("select-people").setDisabled(true);
        }
        findMatchingPeople();
    }
}
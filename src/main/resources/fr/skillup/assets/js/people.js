let peopleFiltered;
let allSkills;

App.onLoad(() => {
    const people = JSON.parse(Bridge.get("PeopleController", "getPeople"));
    const selectAddSkills = document.getElementById("add-skills");
    selectAddSkills.setMultiple(true);
    selectAddSkills.setEnableSearch(true);
    selectAddSkills.setTitle("Select skills");
    Bridge.getAsync("SkillsController", "getSkills").then((skills) => {
        allSkills = skills;
        skills.forEach(skill => {
            selectAddSkills.addOption(skill["id"], skill["skill_label"]);
        });
    });

    peopleFiltered = people;
    let max = 6;

    window.addEventListener("resize", () => {
        max = 6;
        initTable(filter(peopleFiltered), 0, max);
    });

    document.getElementById("filter-label").addEventListener("input", () => {
        peopleFiltered = filter(people);
        initTable(peopleFiltered, 0, max);
    });

    document.getElementById("first-page-button").addEventListener("click", () => {
        initTable(peopleFiltered, 0, max);
    });
    document.getElementById("previous-page-button").addEventListener("click", () => {
        let start = parseInt(document.getElementById("current-page").innerText) - 2;
        if (start < 0) {
            start = 0;
        }
        initTable(peopleFiltered, start * max, max);
    });
    document.getElementById("next-page-button").addEventListener("click", () => {
        let start = parseInt(document.getElementById("current-page").innerText);
        if (start < Math.ceil(peopleFiltered.length / max)) {
            initTable(peopleFiltered, start * max, max);
        }
    });
    document.getElementById("last-page-button").addEventListener("click", () => {
        let start = Math.ceil(peopleFiltered.length / max) - 1;
        initTable(peopleFiltered, start * max, max);
    });
    initTable(peopleFiltered, 0, max);

    selectAddSkills.addEventListener("change", () => {
        App.log(JSON.stringify(document.getElementById("add-skills").getSelected()));
    });
});

function reset() {
    peopleFiltered = JSON.parse(Bridge.get("PeopleController", "getPeople"));
    document.getElementById("filter-label").value = "";
}

function filter(people) {
    const label = document.getElementById("filter-label").value;
    let peopleFiltered = people;
    if (label !== "") {
        peopleFiltered = peopleFiltered.filter(person =>
            person["firstname"].toLowerCase().includes(label.toLowerCase()) ||
            person["lastname"].toLowerCase().includes(label.toLowerCase())
        );
    }
    return peopleFiltered;
}

function initTable(people, start, max) {
    const container = document.getElementById("container-personnel");
    container.innerHTML = "";

    for (let i = start; i < start + max && i < people.length; i++) {
        const person = people[i];
        person["skills"] = JSON.parse(Bridge.get("PeopleController", "getPersonSkills", [parseInt(person["id"])]));

        const peopleCard = document.createElement("div");
        peopleCard.classList.add("people-card");

        peopleCard.innerHTML = `
            <div class='info-card'>
                <img src='data:image/png;base64,${person["picture"]}' alt='People Picture' class='profile-picture'/>
                <div class='info'>
                    <p class='name'>${person["firstname"]} ${person["lastname"]}</p>
                    <p class='job'>${person["job"]}</p>
                </div>
                <button class='pen'>
                    <img src='assets/images/pen.svg' alt='Modify'/>
                 </button>
            </div>
            <div class='skill-people'>
                ${displaySkills(person["skills"])}
            </div>
        `;

        peopleCard.querySelector(".pen").addEventListener("click", () => showModal(person));
        container.appendChild(peopleCard);
    }

    function displaySkills(skills) {
        let html = skills.slice(0, 2).map(skill => `
        <div class='skill-text' style="background-color: #${skill["category_color"]}">
            ${skill["category_icon"]}<p>${skill["skill_label"]}</p>
        </div>
    `).join("");

        if (skills.length > 2) {
            html += `
            <div class='skill-text other'>
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" class="size-5">
                    <path d="M5.127 3.502 5.25 3.5h9.5c.041 0 .082 0 .123.002A2.251 2.251 0 0 0 12.75 2h-5.5a2.25 2.25 0 0 0-2.123 1.502ZM1 10.25A2.25 2.25 0 0 1 3.25 8h13.5A2.25 2.25 0 0 1 19 10.25v5.5A2.25 2.25 0 0 1 16.75 18H3.25A2.25 2.25 0 0 1 1 15.75v-5.5ZM3.25 6.5c-.04 0-.082 0-.123.002A2.25 2.25 0 0 1 5.25 5h9.5c.98 0 1.814.627 2.123 1.502a3.819 3.819 0 0 0-.123-.002H3.25Z"/>
                </svg>
                <p>+ ${skills.length - 2} others</p>
            </div>`;
        }
        return html;
    }

    function displaySkillsModal(skills) {
        let html = skills.slice(0, 10).map(skill => `
        <div class='skill-text' style="background-color: #${skill["category_color"]}">
            ${skill["category_icon"]}<p>${skill["skill_label"]}</p>
        </div>
    `).join("");
        return html;
    }

    function showModal(person) {
        document.getElementById("modal-container").classList.add("show");
        document.getElementById("modal-id").value = person["id"];
        document.getElementById("modal-firstname").value = person["firstname"];
        document.getElementById("modal-lastname").value = person["lastname"];
        document.getElementById("modal-job").value = person["job"];
        document.getElementById("modal-picture").src = `data:image/png;base64,${person["picture"]}`;
        document.getElementById("modal-general-name").innerText = `${person["firstname"]} ${person["lastname"]}`;
        document.getElementById("modal-general-job").innerText = person["job"];
        document.getElementById("modal-entry-date").value = person["entry_date"];
        document.getElementById("skills-modal").innerHTML = displaySkillsModal(person["skills"]);
        person["skills"].forEach(skill => {
            App.log(skill["skill_id"])
            document.getElementById("add-skills").select(skill["skill_id"]);
        });
        App.log(JSON.stringify(document.getElementById("add-skills").getSelectedLabels()));
    }


    document.getElementById("current-page").innerText = Math.ceil(start / max + 1).toString();
    document.getElementById("max-page").innerText = Math.ceil(people.length / max).toString();
    if (start === 0) {
        document.getElementById("first-page-button").style.visibility = "hidden";
        document.getElementById("previous-page-button").style.visibility = "hidden";
    } else {
        document.getElementById("first-page-button").style.visibility = "visible";
        document.getElementById("previous-page-button").style.visibility = "visible";
    }
    if (start + max >= people.length) {
        document.getElementById("next-page-button").style.visibility = "hidden";
        document.getElementById("last-page-button").style.visibility = "hidden";
    } else {
        document.getElementById("next-page-button").style.visibility = "visible";
        document.getElementById("last-page-button").style.visibility = "visible";
    }
}

function save() {
    const id = parseInt(document.getElementById("modal-id").value);
    const firstname = document.getElementById("modal-firstname").value.trim();
    const lastname = document.getElementById("modal-lastname").value.trim();
    const job = document.getElementById("modal-job").value.trim();

    if (firstname === "" || lastname === "" || job === "") {

    }

    const params = [id, firstname, lastname, job];
    Bridge.call("PeopleController", "savePerson", params);
    closeModal();
    reset();
    initTable(peopleFiltered, 0, 6);
}

function closeModal() {
    document.getElementById("modal-container").classList.remove("show");
}

function showModalAdd() {
    document.getElementById("modal-container-add").classList.add("show");
}

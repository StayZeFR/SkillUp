let peopleFiltered;

App.onLoad(() => {
    const people = JSON.parse(Bridge.get("PeopleController", "getPeople"));
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
        if (start < 0) start = 0;
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
});

function reset() {
    peopleFiltered = JSON.parse(Bridge.get("PeopleController", "getPeople"));
    document.getElementById("filter-label").value = "";
}

function filter(people) {
    const label = document.getElementById("filter-label").value;
    return label !== ""
        ? people.filter(person =>
            person["firstname"].toLowerCase().includes(label.toLowerCase()) ||
            person["lastname"].toLowerCase().includes(label.toLowerCase())
        )
        : people;
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
                <img src='data:image/png;base64,${person["picture"] || ""}' alt='People Picture' class='profile-picture'/>
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

    document.getElementById("current-page").innerText = Math.ceil(start / max + 1).toString();
    document.getElementById("max-page").innerText = Math.ceil(people.length / max).toString();

    document.getElementById("first-page-button").style.visibility = start === 0 ? "hidden" : "visible";
    document.getElementById("previous-page-button").style.visibility = start === 0 ? "hidden" : "visible";
    document.getElementById("next-page-button").style.visibility = start + max >= people.length ? "hidden" : "visible";
    document.getElementById("last-page-button").style.visibility = start + max >= people.length ? "hidden" : "visible";
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
                    <path d="M6 10a4 4 0 1 1 8 0 4 4 0 0 1-8 0Z"/>
                </svg>
                <p>+ ${skills.length - 2} others</p>
            </div>`;
    }

    return html;
}

function displaySkillsModal(skills) {
    return skills.slice(0, 10).map(skill => `
        <div class='skill-text' style="background-color: #${skill["category_color"]}">
            ${skill["category_icon"]}<p>${skill["skill_label"]}</p>
        </div>
    `).join("");
}

function showModal(person) {
    document.getElementById("modal-mode").value = "edit";
    document.getElementById("modal-container").classList.add("show");

    document.getElementById("modal-id").value = person["id"];
    document.getElementById("modal-firstname").value = person["firstname"];
    document.getElementById("modal-lastname").value = person["lastname"];
    document.getElementById("modal-job").value = person["job"];
    document.getElementById("modal-picture").src = `data:image/png;base64,${person["picture"] || ""}`;
    document.getElementById("modal-general-name").innerText = `${person["firstname"]} ${person["lastname"]}`;
    document.getElementById("modal-general-job").innerText = person["job"];
    document.getElementById("modal-entry-date").value = person["entry_date"];
    document.getElementById("skills-modal").innerHTML = displaySkillsModal(person["skills"]);
    skillsContainer.innerHTML = "";
    person["skills"].forEach(skill => {
        skillsContainer.innerHTML += `
            <div class='skill-text-modal' style="background-color: #${skill["category_color"]}">
                ${skill["category_icon"]}
                <p>${skill["skill_label"]}</p>
            </div>`;
    });

    document.getElementById("save-button").style.display = "inline-block";
    document.getElementById("create-button").style.display = "none";
}

function showModalCreate() {
    document.getElementById("modal-mode").value = "create";
    document.getElementById("modal-container").classList.add("show");

    document.getElementById("modal-id").value = "Auto";
    document.getElementById("modal-firstname").value = "";
    document.getElementById("modal-lastname").value = "";
    document.getElementById("modal-job").value = "";
    document.getElementById("modal-entry-date").value = new Date().toISOString().slice(0, 10);

    document.getElementById("modal-picture").src = "";
    document.getElementById("modal-general-name").innerText = "New Person";
    document.getElementById("modal-general-job").innerText = "";
    document.getElementById("skills-modal").innerHTML = "";
    skillsContainer.innerHTML = "";

    document.getElementById("save-button").style.display = "none";
    document.getElementById("create-button").style.display = "inline-block";
}

function showModalAdd() {
    showModalCreate();
}

function closeModal() {
    document.getElementById("modal-container").classList.remove("show");
}

function save() {
    const id = parseInt(document.getElementById("modal-id").value);
    const firstname = document.getElementById("modal-firstname").value.trim();
    const lastname = document.getElementById("modal-lastname").value.trim();
    const job = document.getElementById("modal-job").value.trim();

    if (firstname === "" || lastname === "" || job === "") return;

    const params = [id, firstname, lastname, job];
    Bridge.call("PeopleController", "savePerson", params);
    closeModal();
    reset();
    initTable(peopleFiltered, 0, 6);
}

function create() {
    const firstname = document.getElementById("modal-firstname").value.trim();
    const lastname = document.getElementById("modal-lastname").value.trim();
    const job = document.getElementById("modal-job").value.trim();
    const picture = ""; // à compléter si vous ajoutez un champ

    if (!firstname || !lastname || !job) {
        alert("Tous les champs sont obligatoires !");
        return;
    }

    const params = [firstname, lastname, job, picture];
    Bridge.call("PeopleController", "insertPerson", params);

    alert("Personne ajoutée !");
    closeModal();
    reset();
    initTable(peopleFiltered, 0, 6);
}

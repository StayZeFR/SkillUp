// people.js

console.log("✅ people.js chargé");

let peopleFiltered;

App.onLoad(() => {
    console.log("🔁 App.onLoad exécuté");

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
    console.log("🔄 reset() appelé");
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
                        <path d="..."/>
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
        skillsContainer.innerHTML = "";
        person["skills"].forEach(skill => {
            skillsContainer.innerHTML += `
                <div class='skill-text-modal' style="background-color: #${skill["category_color"]}">
                    ${skill["category_icon"]}
                    <p>${skill["skill_label"]}</p>
                </div>`;
        });
    }
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

function closeModal() {
    document.getElementById("modal-container").classList.remove("show");
}

function showModalAdd() {
    console.log("showModalAdd called");
    const modal = document.getElementById("modal-container-add");
    if (modal) {
        modal.classList.add("show");
        console.log("Modal displayed");
    } else {
        console.warn("Modal container not found");
    }
}

function closeModalAdd() {
    const modal = document.getElementById("modal-container-add");
    if (modal) modal.classList.remove("show");
}

function addPerson() {
    const firstname = document.getElementById("add-firstname").value.trim();
    const lastname = document.getElementById("add-lastname").value.trim();
    const job = document.getElementById("add-job").value.trim();
    const picture = document.getElementById("add-picture").value.trim(); // <- nouveau champ

    if (!firstname || !lastname || !job) {
        alert("❌ Tous les champs sont obligatoires !");
        return;
    }

    console.log("✅ addPerson() appelée avec :", firstname, lastname, job, picture || "(aucune image)");

    const params = [firstname, lastname, job, picture]; // <- inclu picture même s'il est vide
    Bridge.call("PeopleController", "addPerson", params);

    alert("✅ Personne ajoutée !");
    closeModalAdd();
    reset();
    initTable(peopleFiltered, 0, 6);

    // Nettoyage des champs
    document.getElementById("add-firstname").value = "";
    document.getElementById("add-lastname").value = "";
    document.getElementById("add-job").value = "";
    document.getElementById("add-picture").value = ""; // <- reset image aussi
}


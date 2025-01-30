App.onLoad(() => {
    const people = JSON.parse(Bridge.get("PeopleController", "getPeople"));

    document.getElementById("load-more").addEventListener("click", () => {
        showPeople(people);
    });

    showPeople(people);
});

function showPeople(people) {
    const max = 6;
    let start = 0;
    const container = document.getElementById("container-personnel");
    if (container.innerHTML !== "") {
        start = container.children.length;
    }
    if (start + max >= people.length) {
        document.getElementById("load-more").style.display = "none";
    }
    for (let i = start; i < start + max; i++) {
        const person = people[i];
        person["skills"] = JSON.parse(Bridge.get("PeopleController", "getPersonSkills", [parseInt(person["id"])]));
        let html = `
                  <div class='people-card'>
                    <div class='info-card'>
                      <img src='data:image/png;base64,${person["picture"]}' alt='People Picture' class='profile-picture'/>
                      <div class='info'>
                        <p class='name'>${person["firstname"]} ${person["lastname"]}</p>
                        <p class='job'>${person["job"]}</p>
                      </div>
                      <div>
                        <button class='pen'>
                          <img src='assets/images/pen.svg' alt='Modify' onclick='showModal(${JSON.stringify(person)})'/>
                        </button>
                      </div>
                    </div>
                    <div class='skill-people'>`;
        for (let i = 0; i < (person["skills"].length > 2 ? 2 : person["skills"].length); i++) {
            const skill = person["skills"][i];
            html += `
                      <div class='skill-text' style="background-color: #${skill["category_color"]}">
                        ${skill["category_icon"]}
                        <p>${skill["skill_label"]}</p>
                      </div>`;
        }
        if (person["skills"].length > 2) {
            html += `
                      <div class='skill-text other'>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" class="size-5">
                          <path d="M5.127 3.502 5.25 3.5h9.5c.041 0 .082 0 .123.002A2.251 2.251 0 0 0 12.75 2h-5.5a2.25 2.25 0 0 0-2.123 1.502ZM1 10.25A2.25 2.25 0 0 1 3.25 8h13.5A2.25 2.25 0 0 1 19 10.25v5.5A2.25 2.25 0 0 1 16.75 18H3.25A2.25 2.25 0 0 1 1 15.75v-5.5ZM3.25 6.5c-.04 0-.082 0-.123.002A2.25 2.25 0 0 1 5.25 5h9.5c.98 0 1.814.627 2.123 1.502a3.819 3.819 0 0 0-.123-.002H3.25Z" />
                        </svg>
                        <p>+ ${person["skills"].length - 2} other${(person["skills"].length - 2) === 1 ? "" : "s"}</p>
                      </div>`;
        }
        html += `
                    </div>
                  </div>`;
        document.getElementById("container-personnel").innerHTML += html;
    }
}

function showModal(person) {
    document.getElementById("modal-id").value = person["id"];
    document.getElementById("modal-firstname").value = person["firstname"];
    document.getElementById("modal-lastname").value = person["lastname"];
    document.getElementById("modal-job").value = person["job"];
    document.getElementById("modal-picture").src = `data:image/png;base64,${person["picture"]}`;
    document.getElementById("modal-general-name").innerText = `${person["firstname"]} ${person["lastname"]}`;
    document.getElementById("modal-general-job").innerText = person["job"];

    document.getElementById("modal-container").classList.add("show");

}

function closeModal() {
    document.getElementById("modal-container").classList.remove("show");
}
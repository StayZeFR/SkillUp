App.onLoad(() => {
    const people = JSON.parse(Bridge.get("PeopleController", "getPeople"));

    showPeople(people);
});

function showPeople(people) {
    const max = 6;
    let start = 0;
    const container = document.getElementById("container-personnel");
    if (container.innerHTML !== "") {
        start = container.children.length;
    }
    for (let i = start; i < start + max; i++) {
        const person = people[i];
        const html = `
                    <div class='info-card'>
                      <img src='data:image/png;base64,${person["picture"]}' alt='People Picture' class='profile-picture'/>
                      <div class='info'>
                        <p class='name'>${person["firstname"]} ${person["lastname"]}</p>
                        <p class='job'>${person["job"]}</p>
                      </div>
                      <div>
                        <button class='pen'>
                          <img src='assets/images/pen.svg' alt='Modify' onclick='showModal(${person["id"]})'/>
                        </button>
                      </div>
                    </div>
                    <div class='skill-people'>
                      <div class='skill-text-dev'>
                        <img src='assets/images/dev.svg' alt='dev-skill' class='skill-icon'/>
                        <p>Testing</p>
                      </div>
                      <div class='skill-text-support'>
                        <img src='assets/images/support.svg' alt='support-skill' class='skill-icon'/>
                        <p>Service Delivery</p>
                      </div>
                    </div>`;
        const element = document.createElement("div");
        element.classList.add("people-card");
        element.innerHTML = html;
        document.getElementById("container-personnel").insertBefore(element, document.getElementById("load-more"));
    }

    if (start + max >= people.length) {
        document.getElementById("load-more").style.display = "none";
    }
}

function showModal(id) {
    document.getElementById("modal-container").classList.add("show");
}

function closeModal() {
    document.getElementById("modal-container").classList.remove("show");
}
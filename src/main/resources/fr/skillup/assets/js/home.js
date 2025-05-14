let selectedDay = null;

App.onLoad(async () => {
    /*const skills = JSON.parse(Bridge.get("SkillsController", "getSkills"));
    if (skills !== null) {
        initTable(skills);
    }*/

    const people = JSON.parse(Bridge.get("PeopleController", "getPeople"));
    if (people !== null) {
        showPeople(people);
    }

    const header = document.querySelector(".calendar h3");
    const dates = document.querySelector(".dates");
    const navs = document.querySelectorAll("#prev, #next");

    const months = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    let date = new Date();
    let month = date.getMonth();
    let year = date.getFullYear();

    const today = new Date();

    function renderCalendar() {
        const start = new Date(year, month, 1).getDay();
        const endDate = new Date(year, month + 1, 0).getDate();
        const end = new Date(year, month, endDate).getDay();
        const endDatePrev = new Date(year, month, 0).getDate();

        let datesHtml = "";

        for (let i = start; i > 0; i--) {
            datesHtml += `<li class="inactive">${endDatePrev - i + 1}</li>`;
        }

        for (let i = 1; i <= endDate; i++) {
            const currentDate = new Date(year, month, i);
            let classList = [];

            if (
                i === today.getDate() &&
                month === today.getMonth() &&
                year === today.getFullYear()
            ) {
                classList.push("today");
            }

            if (
                selectedDay &&
                selectedDay.day === i &&
                selectedDay.month === month &&
                selectedDay.year === year
            ) {
                classList.push("selected");
            }

            if (currentDate < today.setHours(0, 0, 0, 0)) {
                classList.push("inactive"); // Marque comme non sélectionnable
            }

            datesHtml += `<li class="${classList.join(" ")}" data-day="${i}">${i}</li>`;
        }

        for (let i = end; i < 6; i++) {
            datesHtml += `<li class="inactive">${i - end + 1}</li>`;
        }

        dates.innerHTML = datesHtml;
        header.textContent = `${months[month]} ${year}`;

        // Activer le clic seulement sur les dates valides
        document.querySelectorAll(".dates li:not(.inactive)").forEach(li => {
            li.addEventListener("click", () => {
                selectedDay = {
                    day: parseInt(li.dataset.day),
                    month,
                    year
                };
                renderCalendar();
                console.log(`Date sélectionnée : ${selectedDay.day}/${selectedDay.month + 1}/${selectedDay.year}`);
            });
        });
    }

    navs.forEach((nav) => {
        nav.addEventListener("click", (e) => {
            const btnId = e.target.id;

            if (btnId === "prev" && month === 0) {
                year--;
                month = 11;
            } else if (btnId === "next" && month === 11) {
                year++;
                month = 0;
            } else {
                month = btnId === "next" ? month + 1 : month - 1;
            }

            renderCalendar();
        });
    });

    renderCalendar();
});

function initTable(skills) {
    document.getElementById("table-skills").innerHTML = "";
    skills.slice(0, 6).forEach(skill => {
        document.getElementById("table-skills").innerHTML += `
            <tr>
                <td>
                    <div style='background-color: #${skill["category_color"]};'>
                        ${skill["category_icon"]} ${skill["skill_label"]}
                    </div>
                </td>
            </tr>`;
    });
}

function showPeople(people) {
    document.getElementById("table-people").innerHTML = "";
    people.slice(0, 8).forEach(person => {
        let html = `
            <tr>
                <td>
                    <div class='people-card'>
                        <div class='info-card'>
                            <img src='data:image/png;base64,${person["picture"]}' alt='People Picture' class='profile-picture'/>
                            <div class='info'>
                                <p class='name'>${person["firstname"]} ${person["lastname"]}</p>
                                <p class='job'>${person["job"]}</p>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>`;
        document.getElementById("table-people").innerHTML += html;
    });

}

function modifyDate() {
    const date = selectedDay.year + "-" + (selectedDay.month + 1).toString().padStart(2, "0") + "-" + selectedDay.day.toString().padStart(2, "0");
    Bridge.call("MissionsController", "updateMissions", [date]);
}

function moveToAddMission() {
    Bridge.call("MissionsController", "addMission");
}

function moveToListOfSkills() {
    Bridge.call("SkillsController", "viewSkills");
}

function moveToListOfPeople() {
    Bridge.call("PeopleController", "viewPeople");
}

function moveToMissionsManagement() {
    Bridge.call("MissionsController", "viewMissions");
}



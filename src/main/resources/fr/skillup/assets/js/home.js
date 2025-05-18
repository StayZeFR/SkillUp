let selectedDay = null;

App.onLoad(async () => {
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

        document.querySelectorAll(".dates li:not(.inactive)").forEach(li => {
            li.addEventListener("click", () => {
                selectedDay = {
                    day: parseInt(li.dataset.day),
                    month,
                    year
                };
                renderCalendar();
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

    Bridge.getAsync("HomeController", "getWarningMissions").then(result => {
        document.getElementById("nb-warning").innerText = result.length;

        for (i = 0; i < 2; i++) {
            const mission = result[i];
            const html = `
                            <div class="mission">
                                <header>
                                    <div class="date">
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
                                            <path fill-rule="evenodd" d="M6.75 2.25A.75.75 0 0 1 7.5 3v1.5h9V3A.75.75 0 0 1 18 3v1.5h.75a3 3 0 0 1 3 3v11.25a3 3 0 0 1-3 3H5.25a3 3 0 0 1-3-3V7.5a3 3 0 0 1 3-3H6V3a.75.75 0 0 1 .75-.75Zm13.5 9a1.5 1.5 0 0 0-1.5-1.5H5.25a1.5 1.5 0 0 0-1.5 1.5v7.5a1.5 1.5 0 0 0 1.5 1.5h13.5a1.5 1.5 0 0 0 1.5-1.5v-7.5Z" clip-rule="evenodd" />
                                        </svg>
                                        <span>${mission.mission_start_date}</span>
                                    </div>
                                    <div class="action">
                                        <button style="display: none;">
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
                                                <path d="M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z" />
                                                <path fill-rule="evenodd" d="M1.323 11.447C2.811 6.976 7.028 3.75 12.001 3.75c4.97 0 9.185 3.223 10.675 7.69.12.362.12.752 0 1.113-1.487 4.471-5.705 7.697-10.677 7.697-4.97 0-9.186-3.223-10.675-7.69a1.762 1.762 0 0 1 0-1.113ZM17.25 12a5.25 5.25 0 1 1-10.5 0 5.25 5.25 0 0 1 10.5 0Z" clip-rule="evenodd" />
                                            </svg>
                                        </button>
                                        <button onclick="moveToEditMission(${mission.mission_id})">
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
                                                <path d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-8.4 8.4a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32l8.4-8.4Z" />
                                                <path d="M5.25 5.25a3 3 0 0 0-3 3v10.5a3 3 0 0 0 3 3h10.5a3 3 0 0 0 3-3V13.5a.75.75 0 0 0-1.5 0v5.25a1.5 1.5 0 0 1-1.5 1.5H5.25a1.5 1.5 0 0 1-1.5-1.5V8.25a1.5 1.5 0 0 1 1.5-1.5h5.25a.75.75 0 0 0 0-1.5H5.25Z" />
                                            </svg>
                                        </button>
                                    </div>
                                </header>
                                <div class="description">
                                    ${mission.mission_title}
                                </div>
                            </div>
            `;
            document.getElementById("list-missions-container").innerHTML += html;
        }
    });
}

function modifyDate() {
    const date = selectedDay.year + "-" + (selectedDay.month + 1).toString().padStart(2, "0") + "-" + selectedDay.day.toString().padStart(2, "0");
    Bridge.call("MissionsController", "updateMissions", [date]);
    Toast.fire({
        icon: "success",
        title: "The date is correctly modified"
    });
}

function moveToAddMission() {
    Bridge.call("MissionsController", "addMission");
}

function moveToListOfWarnings() {
    Bridge.call("MissionsController", "viewMissions");
}

function moveToListOfPeople() {
    Bridge.call("PeopleController", "viewPeople");
}

function moveToMissionsManagement() {
    Bridge.call("MissionsController", "viewMissions");
}

function moveToEditMission(id) {
    Bridge.call("MissionsController", "editMission", [id]);
}

const Toast = Swal.mixin({
    toast: true,
    position: "top-end",
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.onmouseenter = Swal.stopTimer;
        toast.onmouseleave = Swal.resumeTimer;
    }
});


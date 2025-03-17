App.onLoad(async () => {
    const skills = JSON.parse(Bridge.get("SkillsController", "getSkills"));
    if (skills !== null) {
        initTable(skills);
    }

    const people = JSON.parse(Bridge.get("PeopleController", "getPeople"));
    if (people !== null) {
        showPeople(people);
    }
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
    people.slice(0, 9).forEach(person => {
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
    let className =
      i === date.getDate() &&
      month === new Date().getMonth() &&
      year === new Date().getFullYear()
        ? ' class="today"'
        : "";
    datesHtml += `<li${className}>${i}</li>`;
  }

  for (let i = end; i < 6; i++) {
    datesHtml += `<li class="inactive">${i - end + 1}</li>`;
  }

  dates.innerHTML = datesHtml;
  header.textContent = `${months[month]} ${year}`;
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

}

function moveToAddMission() {
    Bridge.call("MissionsController", "addMission");
}

function moveToListOfSkills() {
    Bridge.call("SkillsController", "init");
}
function moveToListOfPeople() {
    Bridge.call("PeopleController", "init");
}
function moveToMissionsManagement() {
    Bridge.call("MissionsController", "init");
}



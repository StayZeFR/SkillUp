App.onLoad(() => {
    const categories = JSON.parse(Bridge.get("SkillsController", "getCategories"));
    const skills = JSON.parse(Bridge.get("SkillsController", "getSkills"));
    if (categories !== null) {
        categories.forEach(category => {
            document.getElementById("filter-category-list").innerHTML += "<div class='select-item' data-value='" + category["id"] + "' onclick='select(this)'>" + category["label"] + "</div>";
        });
    }

    let skillsFiltered = skills;
    let max = getMax();
    window.addEventListener("resize", () => {
        max = getMax();
        initTable(filter(skills), 0, max);
    });

    Array.from(document.querySelectorAll("#filter-category-list > .select-item")).forEach(select => {
        select.addEventListener("click", (event) => {
            skillsFiltered = filter(skills);
            initTable(skillsFiltered, 0, max);
        });
    });
    document.getElementById("filter-label").addEventListener("input", () => {
        skillsFiltered = filter(skills);
        initTable(skillsFiltered, 0, max);
    });

    document.getElementById("first-page-button").addEventListener("click", () => {
        initTable(skillsFiltered, 0, max);
    });
    document.getElementById("previous-page-button").addEventListener("click", () => {
        let start = parseInt(document.getElementById("current-page").innerText) - 2;
        if (start < 0) {
            start = 0;
        }
        initTable(skillsFiltered, start * max, max);
    });
    document.getElementById("next-page-button").addEventListener("click", () => {
        let start = parseInt(document.getElementById("current-page").innerText);
        if (start < Math.ceil(skills.length / max)) {
            initTable(skillsFiltered, start * max, max);
        }
    });
    document.getElementById("last-page-button").addEventListener("click", () => {
        let start = Math.ceil(skills.length / max) - 1;
        initTable(skillsFiltered, start * max, max);
    });

    initTable(skillsFiltered, 0, max);
});

function getMax() {
    let max = 5;
    if (window.innerHeight < 800) {
        max = 7;
    } else if (window.innerHeight < 950) {
        max = 10;
    } else if (window.innerHeight < 1100) {
        max = 12;
    } else if (window.innerHeight < 1250) {
        max = 15;
    } else if (window.innerHeight < 1400) {
        max = 18;
    }
    return max;
}

function filter(skills) {
    const category = document.getElementById("filter-category").getAttribute("data-value");
    const label = document.getElementById("filter-label").value;
    let skillsFiltered = skills;
    if (category && category !== "all") {
        skillsFiltered = skills.filter(skill => skill["category_id"] === parseInt(category));
    }
    if (label !== "") {
        skillsFiltered = skillsFiltered.filter(skill => skill["skill_label"].toLowerCase().includes(label.toLowerCase()));
    }
    return skillsFiltered;
}

function initTable(skills, start, max) {
    if (skills !== null) {
        document.getElementById("table-skills").innerHTML = "";
        for (let i = start; i < (start + max); i++) {
            const skill = skills[i];
            if (skill !== undefined) {
                document.getElementById("table-skills").innerHTML += "<tr>" +
                    "<td>" + skill["skill_id"] + "</td>" +
                    "<td>" + skill["category_label"]+ "</td>" +
                    "<td><div style='background-color: #" + skill["category_color"] + ";'>" + skill["category_icon"] + skill["skill_label"] + "</div></td>" +
                    "</tr>";
            }
        }
        document.getElementById("current-page").innerText = Math.ceil(start / max + 1).toString();
        document.getElementById("max-page").innerText = Math.ceil(skills.length / max).toString();
        if (start === 0) {
            document.getElementById("first-page-button").style.visibility = "hidden";
            document.getElementById("previous-page-button").style.visibility = "hidden";
        } else {
            document.getElementById("first-page-button").style.visibility = "visible";
            document.getElementById("previous-page-button").style.visibility = "visible";
        }
        if (start + max >= skills.length) {
            document.getElementById("next-page-button").style.visibility = "hidden";
            document.getElementById("last-page-button").style.visibility = "hidden";
        } else {
            document.getElementById("next-page-button").style.visibility = "visible";
            document.getElementById("last-page-button").style.visibility = "visible";
        }
    }
}
App.onLoad(() => {
    const categories = JSON.parse(Bridge.get("SkillsController", "getCategories"));
    if (categories !== null) {
        categories.forEach(category => {
            document.getElementById("filter-category").innerHTML += "<option value='" + category["id"] + "'>" + category["label"] + "</option>";
        });
    }

    let skills = JSON.parse(Bridge.get("SkillsController", "getSkills"));
    let max = getMax();
    window.addEventListener("resize", () => {
        max = getMax();
        initTable(skills, 0, max);
    });

    document.getElementById("first-page-button").addEventListener("click", () => {
        initTable(skills, 0, max);
    });
    document.getElementById("previous-page-button").addEventListener("click", () => {
        let start = parseInt(document.getElementById("current-page").innerText) - 2;
        if (start < 0) {
            start = 0;
        }
        initTable(skills, start * max, max);
    });
    document.getElementById("next-page-button").addEventListener("click", () => {
        let start = parseInt(document.getElementById("current-page").innerText);
        if (start < Math.ceil(skills.length / max)) {
            initTable(skills, start * max, max);
        }
    });
    document.getElementById("last-page-button").addEventListener("click", () => {
        let start = Math.ceil(skills.length / max) - 1;
        initTable(skills, start * max, max);
    });

    initTable(skills, 0, max);
});

function getMax() {
    let max = 10;
    if (window.innerHeight > 800) {
        max = 15;
    } else if (window.innerHeight > 950) {
        max = 20;
    }
    return max;
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
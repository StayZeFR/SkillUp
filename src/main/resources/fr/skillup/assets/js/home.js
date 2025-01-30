App.onLoad(() => {
    const skills = JSON.parse(Bridge.get("SkillsController", "getSkills"));

    if (skills !== null) {
        initTable(skills);
    }
});

function initTable(skills) {
    skills.slice(0, 9).forEach(skill => {
        document.getElementById("table-skills").innerHTML += `
            <tr>
                <td><div style='background-color: #${skill["category_color"]};'>
                        ${skill["category_icon"]} ${skill["skill_label"]}
                    </div></td>
            </tr>`;
    });






}

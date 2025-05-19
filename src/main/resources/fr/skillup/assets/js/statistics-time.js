let lineChartSkills;

App.onLoad(async () => {
    const select = document.getElementById("select-skills");
    select.setTitle("Select a skill");
    select.setEnableSearch(true);

    Bridge.getAsync("SkillsController", "getSkills").then(skills => {
        skills.forEach(skill => {
            select.addOption(skill["skill_id"], skill["skill_label"]);
        });

        lineChartSkills = new Chart(document.getElementById("lineChartSkills"), {
            type: "line",
            data: {
                labels: ["J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"],
                datasets: [{
                    borderColor: "rgba(123, 105, 237, 1)",
                    backgroundColor: "rgba(123, 105, 237, 0.2)",
                    pointBackgroundColor: "rgba(123, 105, 237, 0.2)",
                    pointBorderColor: "#7B69ED",
                    pointRadius: 6,
                    pointHoverRadius: 8,
                    fill: false,
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }

                }
            }

        });

        select.addEventListener("change", (event) => {
            const skillId = parseInt(select.getSelected());

            Bridge.getAsync("StatisticsTimeController", "getStatYearBySkill", [skillId]).then((result) => {
                lineChartSkills.data.datasets[0].data = [
                    result[0]["total"],
                    result[1]["total"],
                    result[2]["total"],
                    result[3]["total"],
                    result[4]["total"],
                    result[5]["total"],
                    result[6]["total"],
                    result[7]["total"],
                    result[8]["total"],
                    result[9]["total"],
                    result[10]["total"],
                    result[11]["total"]
                ];
                lineChartSkills.update();
            });
        });
    });
});
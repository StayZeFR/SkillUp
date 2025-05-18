let chartSkills;

App.onLoad(async () => {
    const select = document.getElementById("filter-skills");
    select.setTitle("Select a skill");
    select.setEnableSearch(true);

    Bridge.getAsync("SkillsController", "getSkills").then(skills => {
        skills.forEach(skill => {
            select.addOption(skill["skill_id"], skill["skill_label"]);
        });

        chartSkills = new Chart(document.getElementById("chartSkills"), {
            type: "doughnut",
            data: {
                labels: ["People with the skill", "People without the skill"],
                datasets: [{
                    backgroundColor: ["#FFEFE2", "#C79CFF"],
                    borderColor: ["#EF825E", "#9747FF"],
                    borderWidth: 2,
                    hoverOffset: 10
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false,
                        position: "left",
                        align: "start",
                        labels: {
                            padding: 10,
                            font: {
                                family: "Arial"
                            },
                            color: "#959191"
                        }
                    }
                }
            }

        });

        select.addEventListener("change", (event) => {
            const skillId = parseInt(select.getSelected());

            Bridge.getAsync("StatisticsSkillsController", "getStatPersonsHaveSkills", [skillId]).then((result) => {
                chartSkills.data.datasets[0].data = [result[0]["nb_have_skill"], result[0]["nb_total"]];
                chartSkills.update();
            });
        });
    });

    Bridge.getAsync("StatisticsSkillsController", "getStatTop5Skills").then((result) => {
        new Chart(document.getElementById("chartSkillsTop5"), {
            type: "bar",
            data: {
                labels: [result[0]["skill_label"], result[1]["skill_label"], result[2]["skill_label"], result[3]["skill_label"], result[4]["skill_label"]],
                datasets: [{
                    data: [result[0]["nb"], result[1]["nb"], result[2]["nb"], result[3]["nb"], result[4]["nb"]],
                    borderWidth: 2,
                    backgroundColor: ["#F3E8FD", "#E7FDF6","#E0F8FB","#FAF3DE","#FFEFE2"],
                    borderColor: ["#7B69ED", "#58A68A","#6EDBE8","#FFD24C","#EF825E"],
                    borderRadius: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            font: {
                                family: "Arial"
                            },
                            color: "#959191"
                        }
                    },
                    x: {
                        ticks: {
                            display: false,
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
    });
});
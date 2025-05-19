let lineChartSkills;
let chartTime;

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
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                }
            }
        });

        chartTime = new Chart(document.getElementById("chartTime"), {
            type: "bar",
            data: {
                labels: ["J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"],
                datasets: [
                    {
                        label: "Busy",
                        borderWidth: 1,
                        backgroundColor: "#C79CFF",
                        borderColor: "#9747FF",
                        borderRadius: 4,
                        order: 1,
                        grouped: false,
                    },
                    {
                        label: "Available",
                        borderWidth: 1,
                        backgroundColor: "#E0F8FB",
                        borderColor: "#6EDBE8",
                        borderRadius: 4,
                        order: 2,

                    }

                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        stacked: false,
                        ticks: {
                            font: {
                                family: "Arial"
                            }
                        },
                        grid: {
                            drawOnChartArea: false
                        }
                    },
                    y: {
                        stacked: false,
                        beginAtZero: true,
                        max: 10,
                        ticks: {
                            stepSize: 1,
                            font: {
                                family: "Arial"
                            }
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: "bottom",
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

            Bridge.getAsync("StatisticsTimeController", "getStatPersonMonthSkills", [skillId]).then((result) => {
                App.log(JSON.stringify(result));
                chartTime.data.datasets[0].data = [
                    result[0]["mission_total"],
                    result[1]["mission_total"],
                    result[2]["mission_total"],
                    result[3]["mission_total"],
                    result[4]["mission_total"],
                    result[5]["mission_total"],
                    result[6]["mission_total"],
                    result[7]["mission_total"],
                    result[8]["mission_total"],
                    result[9]["mission_total"],
                    result[10]["mission_total"],
                    result[11]["mission_total"]
                ];
                chartTime.data.datasets[1].data = [
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
                chartTime.update();
            });
        });
    });
});
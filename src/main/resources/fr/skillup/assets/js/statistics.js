App.onLoad(async () => {
    const statMissionsCompleted = document.getElementById("stat_missions-completed");
    const statMissionsByStatus = document.getElementById("stat_missions-by-status");
    const statPersonAssignment = document.getElementById("stat_person-assignment");

    Bridge.getAsync("StatisticsController", "getStatMissionsCompleted").then((data) => {
        new Chart(statMissionsCompleted, {
            type: "pie",
            data: {
                labels: ["Completed missions", "Missions in progress"],
                datasets: [{
                    data: [parseInt(data[0]["done"]), parseInt(data[0]["progress"])],
                    backgroundColor: ["#C79CFF", "#FAF3DE"],
                    borderColor: ["#9747FF", "#FFD24C"],
                    borderWidth: 2,
                    hoverOffset: 10
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: "bottom",
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
    });

    Bridge.getAsync("StatisticsController", "getStatMissionsByStatus").then((data) => {
        new Chart(statMissionsByStatus, {
            type: "bar",
            data: {
                labels: ["In prep", "Planned", "In progress", "Done"],
                datasets: [{
                    data: [parseInt(data[0]["in_prep"]), parseInt(data[0]["planned"]), parseInt(data[0]["in_progress"]), parseInt(data[0]["done"])],
                    borderWidth: 2,
                    backgroundColor: "#C79CFF",
                    borderColor: "#9747FF",
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
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
                            font: {
                                family: "Arial"
                            },
                            color: "#959191"
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

    Bridge.getAsync("StatisticsController", "getStatAssignment").then((data) => {
        new Chart(statPersonAssignment, {
            type: "doughnut",
            data: {
                labels: ["Assigned collaborators", "Unassigned Collaborators"],
                datasets: [{
                    data: [data[0]["assigned"], data[0]["unassigned"]],
                    backgroundColor: ['#C79CFF', '#E7FDF6'],
                    borderColor: ['#9747FF', '#58A68A'],
                    borderWidth: 2,
                    hoverOffset: 10
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: "bottom",
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
    });

});

function viewStatisticsOverall() {
    Bridge.call("StatisticsController", "viewStatisticsOverall");
}

function viewStatisticsSkills() {
    Bridge.call("StatisticsController", "viewStatisticsSkills");
}

function viewStatisticsTime() {
    Bridge.call("StatisticsController", "viewStatisticsTime");
}

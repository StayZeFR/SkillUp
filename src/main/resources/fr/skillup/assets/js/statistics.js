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

App.onLoad(() => {
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



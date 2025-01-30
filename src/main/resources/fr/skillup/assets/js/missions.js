App.onLoad(() => {
});

function moveToAddMission() {
    Bridge.call("MissionsController", "addMission");
}


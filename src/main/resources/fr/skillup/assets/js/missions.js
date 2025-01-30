App.onLoad(() => {
});

function moveToAddMission() {
    Bridge.call("layouts.DefaultLayoutController", "moveTo", ["add-mission"]);

}


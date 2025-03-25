App.onLoad(async () => {
    Bridge.getAsync("MissionsController", "getMissions").then((result) => {
        for (mission of result) {
            const html = `
                            <div class="mission">
                                <header>
                                    <div class="date">
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
                                            <path fill-rule="evenodd" d="M6.75 2.25A.75.75 0 0 1 7.5 3v1.5h9V3A.75.75 0 0 1 18 3v1.5h.75a3 3 0 0 1 3 3v11.25a3 3 0 0 1-3 3H5.25a3 3 0 0 1-3-3V7.5a3 3 0 0 1 3-3H6V3a.75.75 0 0 1 .75-.75Zm13.5 9a1.5 1.5 0 0 0-1.5-1.5H5.25a1.5 1.5 0 0 0-1.5 1.5v7.5a1.5 1.5 0 0 0 1.5 1.5h13.5a1.5 1.5 0 0 0 1.5-1.5v-7.5Z" clip-rule="evenodd" />
                                        </svg>
                                        <span>${mission.mission_start_date}</span>
                                    </div>
                                    <div class="action">
                                        <button style="display: none;">
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
                                                <path d="M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z" />
                                                <path fill-rule="evenodd" d="M1.323 11.447C2.811 6.976 7.028 3.75 12.001 3.75c4.97 0 9.185 3.223 10.675 7.69.12.362.12.752 0 1.113-1.487 4.471-5.705 7.697-10.677 7.697-4.97 0-9.186-3.223-10.675-7.69a1.762 1.762 0 0 1 0-1.113ZM17.25 12a5.25 5.25 0 1 1-10.5 0 5.25 5.25 0 0 1 10.5 0Z" clip-rule="evenodd" />
                                            </svg>
                                        </button>
                                        <button onclick="moveToEditMission(${mission.mission_id})">
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
                                                <path d="M21.731 2.269a2.625 2.625 0 0 0-3.712 0l-1.157 1.157 3.712 3.712 1.157-1.157a2.625 2.625 0 0 0 0-3.712ZM19.513 8.199l-3.712-3.712-8.4 8.4a5.25 5.25 0 0 0-1.32 2.214l-.8 2.685a.75.75 0 0 0 .933.933l2.685-.8a5.25 5.25 0 0 0 2.214-1.32l8.4-8.4Z" />
                                                <path d="M5.25 5.25a3 3 0 0 0-3 3v10.5a3 3 0 0 0 3 3h10.5a3 3 0 0 0 3-3V13.5a.75.75 0 0 0-1.5 0v5.25a1.5 1.5 0 0 1-1.5 1.5H5.25a1.5 1.5 0 0 1-1.5-1.5V8.25a1.5 1.5 0 0 1 1.5-1.5h5.25a.75.75 0 0 0 0-1.5H5.25Z" />
                                            </svg>
                                        </button>
                                    </div>
                                </header>
                                <div class="description">
                                    ${mission.mission_title}
                                </div>
                                <!--<div class="people">
                                    <div class="rounded">
                                        <img src="assets/images/alvaro.png" alt="Avatar">
                                    </div>
                                    <div class="rounded">
                                        <img src="assets/images/alvaro.png" alt="Avatar">
                                    </div>
                                    <div class="rounded">
                                        <span>+2</span>
                                    </div>
                                </div>-->
                            </div>
            `;
            switch (mission.life_cycle_id) {
                case 1:
                    document.getElementById("missions-list_in-preperation").innerHTML += html;
                    break;
                case 2:
                    document.getElementById("missions-list_planned").innerHTML += html;
                    break;
                case 3:
                    document.getElementById("missions-list_in-progress").innerHTML += html;
                    break;
                case 4:
                    document.getElementById("missions-list_done").innerHTML += html;
                    break;
            }
        }
    });
});

function moveToAddMission() {
    Bridge.call("MissionsController", "addMission");
}

function moveToEditMission(id) {
    Bridge.call("MissionsController", "editMission", [id]);
}


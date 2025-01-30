App.onLoad(() => {
    if (window.params.get("view")) {
        document.querySelector(".nav[data-target='" + window.params.get("view") + "']").classList.add("active");
    }

    Array.from(document.getElementsByClassName("nav")).forEach(nav => {
        nav.addEventListener("click", () => {
            if (!nav.classList.contains("active")) {
                let target = nav.getAttribute("data-target");
                Bridge.call("layouts.DefaultLayoutController", "moveTo", [
                    target
                ]);
            }
        });
    });
});

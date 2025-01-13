document.addEventListener("DOMContentLoaded", function () {
    let navButtons = document.querySelectorAll(".nav");
    navButtons.forEach(function (btn) {
        btn.addEventListener("click", function (e) {
            let target = e.target.getAttribute("target");
            App.log("Navigating to " + target);
        });
    });
});
App.onLoad(() => {
    if (window.params.get("view")) {
        $(".nav").removeClass("active");
        $(".nav[data-target='" + window.params.get("view") + "']").addClass("active");
    }

    $(".nav").click(function () {
        let target = $(this).attr("data-target");
        Bridge.get("layouts.DefaultLayoutController").moveTo(target);
    });
});

document.addEventListener("DOMContentLoaded", (event) => {
    console.log('hello world');
});
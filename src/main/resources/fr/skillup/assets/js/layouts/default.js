$(document).ready(function () {
    if (window.params.get("view")) {
        $(".nav[data-target='" + window.params.get("view") + "']").addClass("active");
    }

    $(".nav").click(function () {
        let target = $(this).attr("data-target");
        Bridge.get("layouts.DefaultLayoutController").moveTo(target);
    });
});
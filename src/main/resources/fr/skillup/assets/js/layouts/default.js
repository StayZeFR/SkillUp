$(document).ready(function () {
    $(".nav").click(function () {
        let target = $(this).attr("data-target");
        Bridge.get("layouts.DefaultLayoutController").moveTo(target);
    });
});
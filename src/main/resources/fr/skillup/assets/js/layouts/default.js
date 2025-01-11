App.onLoad(() => {
    const controller = document.querySelectorAll("controller");
    controller.forEach(element => {
        const clazz = element.getAttribute("class");
        document.body.innerHTML += clazz + "<br>";
    });
});
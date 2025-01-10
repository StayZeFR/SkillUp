function setParams(params = {}) {
    let html = document.body.innerHTML;
    for (const key in params) {
        html = html.replaceAll("{{" + key + "}}", params[key]);
    }
    document.body.innerHTML = html;
}
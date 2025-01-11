class App {
    static #loaders = [];
    static #controllers = {};

    static #load() {
        this.#loaders.forEach(loader => {
            loader();
        });
    }

    static onLoad(callback) {
        this.#loaders.push(callback);
    }

    static setParams(params = {}) {
        let html = document.body.innerHTML;
        for (const key in params) {
            html = html.replaceAll("{{" + key + "}}", params[key]);
        }
        document.body.innerHTML = html;
        this.#load();
    }
}
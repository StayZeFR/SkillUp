class App {
    static #params = [];
    static #loaders = [];

    static onLoad(callback) {
        App.#loaders.push(callback);
    }

    static start() {
        App.#loaders.forEach(loader => loader());
    }

    static log(msg) {
        Bridge.getBridge().log(msg);
    }
}
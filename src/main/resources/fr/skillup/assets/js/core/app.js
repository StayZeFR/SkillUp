class App {
    static #loaders = [];

    static onLoad(callback) {
        App.#loaders.push(callback);
    }

    /*static async start() {
        await Promise.all(App.#loaders.map(loader => loader()));
    }*/

    static start() {
        App.#loaders.forEach(loader => loader());
    }

    static log(msg) {
        Bridge.getBridge().log(msg);
    }
}
class Bridge {
    static #bridge = null;
    static #callbacks = {};

    static init() {
        Bridge.#bridge = window.bridge;
    }

    static getBridge() {
        return Bridge.#bridge;
    }

    static get(controller, method, params = []) {
        return Bridge.#bridge.get(controller, method, JSON.stringify(params));
    }

    static async getAsync(controller, method, params = []) {
        return new Promise((resolve, reject) => {
            const id = Bridge.#generateId();
            Bridge.#callbacks[id] = {};
            Bridge.#callbacks[id]["resolve"] = resolve;
            Bridge.#callbacks[id]["reject"] = reject;
            Bridge.#bridge.getAsync(controller, method, JSON.stringify(params), id);
        });
    }

    static call(controller, method, params = []) {
        Bridge.#bridge.call(controller, method, JSON.stringify(params));
    }

    static async callAsync(controller, method, params = []) {
        await Bridge.#bridge.callAsync(controller, method, JSON.stringify(params));
    }

    static callback(id, type, response) {
        Bridge.#callbacks[id][type](response);
        delete Bridge.#callbacks[id];
    }

    static #generateId() {
        return Math.random().toString(36).substring(2, 9);
    }

}
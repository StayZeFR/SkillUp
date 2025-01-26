class Bridge {
    static #bridge = null;

    static init() {
        Bridge.#bridge = window.bridge;
    }

    static getBridge() {
        return Bridge.#bridge;
    }

    static get(controller, method, params = []) {
        return Bridge.#bridge.get(controller, method, JSON.stringify(params));
    }

    static call(controller, method, params = []) {
        Bridge.#bridge.call(controller, method, JSON.stringify(params));
    }

}
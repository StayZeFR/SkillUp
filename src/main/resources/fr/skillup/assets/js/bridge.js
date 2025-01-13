class Bridge {
    static #bridge = null;

    static init() {
        Bridge.#bridge = window.bridge;
    }

    static getBridge() {
        return Bridge.#bridge;
    }

    static get(controller) {
        return Bridge.#bridge.get(controller);
    }

}
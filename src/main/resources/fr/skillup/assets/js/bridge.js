class Bridge {
    static #bridge = null;

    static init() {
        this.#bridge = window.bridge;
    }

    static get(controller) {
        return this.#bridge.getController(controller);
    }

}
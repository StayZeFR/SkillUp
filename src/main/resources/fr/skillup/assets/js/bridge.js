class Bridge {
    static #bridge = null;
    static #data = {};

    static init() {
        Bridge.#bridge = window.bridge;
    }

    static getBridge() {
        return Bridge.#bridge;
    }

    static addData(key, value) {
        Bridge.#data[key] = value;
    }

    static get(controller, method, params = []) {
        return Bridge.#bridge.get(controller, method, JSON.stringify(params));
    }

    static call(controller, method, params = []) {
        Bridge.#bridge.call(controller, method, JSON.stringify(params));
    }

    /*
    static get(controller, call, params = []) {
        return new Promise((resolve, reject) => {
            Bridge.#bridge.get(controller, call, params, resolve, reject);
        }
    }

    static call(controller, call, params = []) {
        Bridge.#bridge.call(controller, call, params);
    }*/

}
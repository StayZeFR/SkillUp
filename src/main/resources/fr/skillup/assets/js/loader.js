class Loader {
    static init() {
        const extend = document.getElementsByTagName("extend")[0];
        const parent = extend.getAttribute("parent");
        const html = window.bridge.readView(parent);
    }
}

class Extend extends HTMLElement {
    constructor() {
        super();
    }
}

customElements.define("extend", Extend);
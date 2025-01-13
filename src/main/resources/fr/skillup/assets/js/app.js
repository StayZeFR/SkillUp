class App {
    static #params = [];

    static log(msg) {
        Bridge.getBridge().log(msg);
    }
}
App.onLoad(async () => {
    await new Promise(r => setTimeout(() => {
        Bridge.call("BootstrapController", "showHome");
    }, 1000));
});
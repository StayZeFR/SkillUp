App.onLoad(() => {
    const selects = document.querySelectorAll(".select > .select-selected");

    if (selects) {
        selects.forEach(select => {
            select.addEventListener("click", (event) => {
                const selectItems = event.target.parentElement.querySelector(".select-items");
                if (selectItems.classList.contains("select-hide")) {
                    selectItems.classList.remove("select-hide");
                } else {
                    selectItems.classList.add("select-hide");
                }
            });
        });
    }

    document.addEventListener("click", (event) => {
        if (!event.target.classList.contains("select-selected")) {
            const selectItems = document.querySelector(".select-items");
            if (selectItems) {
                selectItems.classList.add("select-hide");
            }
        }
    });
});

function select(target) {
    const value = target.getAttribute("data-value");
    const text = target.innerText;
    const select = target.parentElement.parentElement;
    const selected = select.querySelector(".select-selected");
    selected.value = text;
    selected.setAttribute("data-value", value);
}

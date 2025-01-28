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

    /*const selectItems = document.querySelectorAll(".select > .select-items > .select-item");
    if (selectItems) {
        selectItems.forEach(selectItem => {
            selectItem.addEventListener("click", (event) => {
                const select = event.target.parentElement.parentElement;
                const selected = select.querySelector(".select-selected");
                selected.innerText = event.target.innerText;
            });
        });
    }*/

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
    selected.innerText = text;
    selected.setAttribute("data-value", value);
}

function blobToImage(blob) {
    return new Promise(resolve => {
        const url = URL.createObjectURL(blob)
        let img = new Image()
        img.onload = () => {
        URL.revokeObjectURL(url)
        resolve(img)
        }
        img.src = url
    });
}

function base64ToBlob(base64, mimeType) {
    if (base64.startsWith("data:image/png;base64,")) {
        base64 = base64.split(",")[1];
    }

    const byteString = atob(base64);
    const arrayBuffer = new Uint8Array(byteString.length);

    for (let i = 0; i < byteString.length; i++) {
        arrayBuffer[i] = byteString.charCodeAt(i);
    }

    return new Blob([arrayBuffer], { type: mimeType });
}

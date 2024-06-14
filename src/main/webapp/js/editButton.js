document.getElementById("edit").addEventListener("click", function () {
    document.getElementById("amount").hidden = false;
    document.getElementById("categoryId").hidden = false;
    document.getElementById("remarks").hidden = false;
    document.getElementById("update").hidden = false;
    document.getElementById("edit").hidden = true;
    document.getElementById("delete").hidden = true;
    document.getElementById("id").hidden = true;
    document.getElementById("defaultAmount").hidden = true;
    document.getElementById("defaultRemarks").hidden = true;
    document.getElementById("back").hidden = false;
});

document.getElementById("back").addEventListener("click", function () {
    document.getElementById("amount").hidden = true;
    document.getElementById("categoryId").hidden = true;
    document.getElementById("remarks").hidden = true;
    document.getElementById("update").hidden = true;
    document.getElementById("back").hidden = true;
    document.getElementById("defaultAmount").hidden = false;
    document.getElementById("defaultRemarks").hidden = false;
    document.getElementById("delete").hidden = false;
    document.getElementById("edit").hidden = false;
});


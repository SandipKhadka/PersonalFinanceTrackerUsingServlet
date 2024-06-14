function edit(id) {
    document.getElementById("amount-" + id).hidden = false;
    document.getElementById("categoryId-" + id).hidden = false;
    document.getElementById("remarks-" + id).hidden = false;
    document.getElementById("update-" + id).hidden = false;
    document.getElementById("edit-" + id).hidden = true;
    document.getElementById("delete-" + id).hidden = true;
    document.getElementById("id-" + id).hidden = true;
    document.getElementById("defaultAmount-" + id).hidden = true;
    document.getElementById("defaultRemarks-" + id).hidden = true;
    document.getElementById("defaultCategory-" + id).hidden = true;
    document.getElementById("back-" + id).hidden = false;
}

function back(id) {
    document.getElementById("amount-" + id).hidden = true;
    document.getElementById("categoryId-" + id).hidden = true;
    document.getElementById("remarks-" + id).hidden = true;
    document.getElementById("update-" + id).hidden = true;  // Fix typo (update -> update)
    document.getElementById("back-" + id).hidden = true;
    document.getElementById("defaultAmount-" + id).hidden = false;
    document.getElementById("defaultRemarks-" + id).hidden = false;
    document.getElementById("defaultCategory-" + id).hidden = false;
    document.getElementById("delete-" + id).hidden = false;
    document.getElementById("edit-" + id).hidden = false;
}

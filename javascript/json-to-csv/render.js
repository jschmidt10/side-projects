"use strict";

const { ipcRenderer } = require("electron");
const { dialog } = require('electron').remote;

/*
 * Selects a file off the local filesystem and populates the given element with the selected filename.
 */
function pickFile(event, elementId) {
    event.preventDefault();
    let selected = dialog.showOpenDialog({ properties: [ 'openFile' ] });
    if (selected) {
        document.getElementById(elementId).value = selected;
    }
}

/*
 * Selects a directory off the local filesystem and populates the given element with the selected directory.
 */
function pickDirectory(event, elementId) {
    event.preventDefault();
    let selected = dialog.showOpenDialog({ properties: [ 'openDirectory' ] });
    if (selected) {
        document.getElementById(elementId).value = selected;
    }
}

/*
 * Adds a new message to a message list.
 */
function addMessage(listId, message) {
    let list = document.getElementById(listId);
    let item = document.createElement("li");
    item.innerHTML = message;
    list.appendChild(item);
}

/*
 * Checks if the error list contains any elements.
 */
function hasErrors() {
    let errorList = document.getElementById("errorList");
    return errorList.innerHTML.length !== 0;
}

/*
 * Validates the user input fields.
 */
function validateForm() {   
    validateField(this.inputDir, "You must select an input directory!");
    validateField(this.outputDir, "You must select an output directory!");
    return !hasErrors();
}

/*
 * Validates that a single field is filled in. If it is not, then a new error item is added to the error list.
 */
function validateField(field, errorMsg) {
    if (field === undefined || field.length === 0) {
        addMessage("errorList", errorMsg);
    }
}

/*
 * Clears out the converter form
 */
function clearForm(event) {
    event.preventDefault();

    document.getElementById("inputDir").value = "";
    document.getElementById("outputDir").value = "";
    document.getElementById("errorList").innerHTML = "";
    document.getElementById("successList").innerHTML = "";
}

/*
 * Validates the user input and submits data to the backend.
 */
function submitForm(event) {
    event.preventDefault();

    let form = {};

    form.inputDir = document.getElementById("inputDir").value;
    form.outputDir = document.getElementById("outputDir").value;
    form.errorList = document.getElementById("errorList");
    form.successList = document.getElementById("successList");

    // clear out previous error/success messages
    form.errorList.innerHTML = "";
    form.successList.innerHTML = "";

    if (validateForm.call(form)) {
        console.log(`Converting ${form.inputDir} into ${form.outputDir}`);
        ipcRenderer.send("submit", form.inputDir, form.outputDir);
    }
}

// Event handlers for results from the backend
ipcRenderer.on("failure", (event, err) => addMessage("errorList", JSON.stringify(err)));
ipcRenderer.on("success", (event, msgs) => msgs.forEach(m => addMessage("successList", m)));
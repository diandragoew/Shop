function getUrl(url, callback) {
    var req = new XMLHttpRequest();
    req.open("GET", url, true);
    req.addEventListener("load", function () {
        if (req.status < 400)
            callback(req.responseText);
        else
            callback(null, new Error("Request failed: " + req.statusText));
    });
    req.addEventListener("error", function () {
        callback(null, new Error("Network error"));
    });
    req.send(null);
}

function loadHeader(headerPath, targetElementId) {
    var req = new XMLHttpRequest();
    req.open("GET", headerPath, true);
    req.addEventListener("load", function () {
        if (req.status >= 200 && req.status < 400) {
            document.getElementById(targetElementId).innerHTML = req.responseText;
        } else {
            console.error("Failed to load header: " + req.statusText);
        }
    });
    req.addEventListener("error", function () {
        console.error("Network error while loading header.");
    });
    req.send(null);
}
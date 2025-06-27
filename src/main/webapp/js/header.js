function loadHeader(headerPath, targetElementId, callback) {
    var req = new XMLHttpRequest();
    req.open("GET", headerPath, true);
    req.addEventListener("load", function () {
        if (req.status >= 200 && req.status < 400) {
            document.getElementById(targetElementId).innerHTML = req.responseText;

            updateHeaderTextBasedOnLogin();

            if (typeof callback === 'function') {
                callback();
            }

        } else {
            console.error("Failed to load header: " + req.statusText);
        }
    });
    req.addEventListener("error", function () {
        console.error("Network error while loading header.");
    });
    req.send(null);
}

// 🔁 This logic runs after header is loaded
function updateHeaderTextBasedOnLogin() {
    getUrl("/isLoggedIn", function (data, error) {
        if (error) {
            console.error("Error checking login status:", error);
            return;
        }

        const isLoggedIn = data.trim() === "true";

        const logDiv = document.getElementById("headerSection__about__log");
        const loginText = logDiv?.querySelector(":scope > p");

        const profileDiv = document.getElementById("headerSection__about__profile");
        const profileText = profileDiv?.querySelector(":scope > p");

        if (isLoggedIn) {
            addCreateAdInHeader();

            logDiv.className = 'headerSection__log__out';
            loginText.textContent = 'изход';

            profileText.remove();
            addMyProfileInHeader();

            // ✅ Only attach click listener if class is headerSection__log__out
            logDiv.addEventListener("click", function () {
                if (logDiv.classList.contains("headerSection__log__out")) {
                    displayLogoutForm();
                }
            });

        } else {
            logDiv.className = 'headerSection__log__in';
            loginText.textContent = 'вход';

            profileDiv.className = 'headerSection__createProfile';
            profileText.textContent = 'create profile';
            // ✅ Only attach click listener if class is headerSection__createProfile
            profileDiv.addEventListener("click", function () {
                if (profileDiv.classList.contains("headerSection__createProfile")) {
                    displayCreateProfile();
                }
            });
            // ✅ Only attach click listener if class is headerSection__log__in
            logDiv.addEventListener("click", function () {
                if (logDiv.classList.contains("headerSection__log__in")) {
                    displayLoginForm();
                }
            });
        }

    });
}


function replaceProfileCellWithEditProfile() {
    const headerMainRow = document.getElementById("headerMainRow");

    const oldProfileCell = document.getElementById("cell__about__headerSection__about__profile");

    const editProfileTemplate = document.getElementById("headerSection__editProfile__template");

    const clone = editProfileTemplate.content.cloneNode(true);

    const newEditProfileCell = clone.querySelector("#cell__about__headerSection__editProfile");

    headerMainRow.replaceChild(newEditProfileCell, oldProfileCell);

}
function addCreateAdInHeader() {
    const headerTable = document.getElementById("headerSection__table");

    const template = document.getElementById("headerSection__createAd__template");
    const clone = template.content.cloneNode(true);
    const createAdCell = clone.querySelector("#cell__about__headerSection__createAd");
    const targetRow = headerTable.querySelector("#headerMainRow");
    targetRow.insertBefore(createAdCell, targetRow.firstChild);

}

function displayLoginForm() {
    // Avoid duplicates by checking if form is already in the DOM
    if (document.querySelector("#headerSection__about__log .loginForm")) return;

    const template = document.querySelector("#headerSection__about__log #loginFormTemplate");
    if (!template) {
        console.error("Login form template not found");
        return;
    }

    const clone = template.content.cloneNode(true);
    const loginForm = clone.querySelector(".loginForm");
    document.getElementById("headerSection__about__log").appendChild(loginForm);
    removeElement(loginForm);
    fillLoginForm(); // Call after appending to DOM
}


function fillLoginForm() {
    const form = document.querySelector("#headerSection__about__log .loginForm");
    if (!form) {
        console.error("Login form not found in DOM");
        return;
    }

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const loginData = {
            userName: form.querySelector("#name").value,
            password: form.querySelector("#pass").value
        };

        console.log("Sending login request:", loginData);

        fetch("http://localhost:8080/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(loginData)
        })
            .then(response => {
                // Check the response status FIRST
                if (response.status === 200) {
                    // If 200 OK, proceed to parse JSON
                    console.log("Login successful!");
                    alert("Successful login!"); // Display success message
                    window.location.reload();

                    return response.json(); // Continue parsing the JSON response
                } else if (response.status === 401) {
                    // If 401 Unauthorized
                    console.error("Login failed: Invalid credentials.");
                    alert("Invalid login!"); // Display invalid message
                    // Don't try to parse JSON if it's an error, or handle the error body specifically
                    throw new Error("Invalid login credentials (401)"); // Throw to catch block
                } else if (response.status >= 400) {
                    // Handle other client-side or server-side errors (4xx or 5xx)
                    console.error(`Login failed with status: ${response.status}`);
                    alert("Invalid login!"); // Display invalid message for other errors
                    // Attempt to parse JSON error message if provided by backend
                    return response.json().then(errorData => {
                        console.error("Backend error details:", errorData);
                        throw new Error(`Server error: ${response.status} - ${JSON.stringify(errorData)}`);
                    }).catch(() => {
                        // Catch if parsing errorData fails (e.g., non-JSON error response)
                        throw new Error(`Server error: ${response.status} - Could not parse error details.`);
                    });
                }
                // Fallback for unexpected status codes (should be handled by above)
                return response.json();
            })
            .then(data => {
                // This .then() block will only execute if the first .then() successfully returned response.json()
                console.log("Login response data:", data);
                // Any further processing of the 200 OK data
            })
            .catch(error => {
                // This .catch() block will handle any errors thrown in the .then() blocks
                // or network errors from the fetch itself.
                console.error("Error during login request:", error);
                // The alert is already handled in the .then() block for 401/4xx/5xx,
                // but this will catch pure network errors or uncaught issues.
                if (error.message.includes("Failed to fetch")) {
                    alert("Network error or server unreachable. Please try again.");
                }
                // For other specific errors, the alert is already given above
            });
    });
}

function fillCreateForm() {
    const form = document.querySelector("#headerSection__about__profile .createForm");
    if (!form) {
        console.error("Create form not found in DOM"); // Corrected console error message
        return;
    }

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const createData = {
            userName: form.querySelector("#createName").value,
            password: form.querySelector("#createPass").value,
            email: form.querySelector("#createEmail").value,
            phone: form.querySelector("#createPhone").value
        };

        console.log("Sending create user request:", createData); // Corrected console message

        fetch("http://localhost:8080/create-user", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(createData)
        })
            .then(response => {
                // Check the response status FIRST
                if (response.status === 201) { // Changed from 200 to 201 for successful creation
                    console.log("User created successfully!");
                    alert("User created successfully!"); // Display success message
                    window.location.reload(); // Reload page to reflect login status

                    // If your backend for 201 returns any JSON, you can parse it here
                    // If it returns nothing or just a plain text success, you can just return
                    return response.text().then(text => text ? JSON.parse(text) : {}); // Handle potential empty/text response for 201
                } else if (response.status === 409) { // Handle 409 Conflict specifically
                    return response.text().then(errorMessage => {
                        console.error("User creation failed: " + errorMessage);
                        alert(errorMessage); // Display the specific message from the backend
                        throw new Error(errorMessage); // Throw to stop further .then() execution
                    });
                } else if (response.status >= 400) { // Handle other client-side errors (4xx)
                    // Attempt to parse JSON error message if provided by backend, otherwise, read as text
                    return response.text().then(text => { // Always try to read as text first for generic errors
                        let errorMessage = "An unexpected error occurred.";
                        try {
                            const errorData = JSON.parse(text); // Try parsing as JSON
                            errorMessage = errorData.message || JSON.stringify(errorData);
                        } catch (e) {
                            errorMessage = text; // If not JSON, use the raw text
                        }
                        console.error(`User creation failed with status ${response.status}: ${errorMessage}`);
                        alert(errorMessage);
                        throw new Error(`Server error: ${response.status} - ${errorMessage}`);
                    });
                }
                // Fallback for unexpected status codes (e.g., 5xx server errors)
                // Always try to read as text for a fallback to avoid parsing errors
                return response.text().then(text => {
                    let errorMessage = "An unexpected server error occurred.";
                    try {
                        const errorData = JSON.parse(text);
                        errorMessage = errorData.message || JSON.stringify(errorData);
                    } catch (e) {
                        errorMessage = text;
                    }
                    console.error(`Unexpected response status ${response.status}: ${errorMessage}`);
                    alert(errorMessage);
                    throw new Error(`Unexpected server response: ${response.status} - ${errorMessage}`);
                });
            })
            .then(data => {
                // This .then() block will only execute if the initial response was 201
                // and you decided to parse its body.
                console.log("Create user response data (for 201 status):", data);
            })
            .catch(error => {
                // This .catch() block will handle any errors thrown in the .then() blocks
                // or network errors from the fetch itself.
                console.error("Error during user creation request:", error);
                if (error.message.includes("Failed to fetch")) {
                    alert("Network error or server unreachable. Please try again.");
                }
                // For other specific errors, the alert is already given in the .then() chain
            });
    });
}

function removeElement(element) {

    // Detect outside click
    setTimeout(() => {  // timeout ensures it doesn't immediately trigger on same click
        function outsideClickHandler(event) {
            // Check if the clicked target is NOT the element itself,
            // AND NOT a descendant of the element.
            if (!element.contains(event.target)) {
                element.remove();
                document.removeEventListener("click", outsideClickHandler);
            }
        }
        document.addEventListener("click", outsideClickHandler);
    }, 0);
}

function addMyProfileInHeader() {

    // Avoid duplicates by checking if form is already in the DOM
    if (document.querySelector("#headerSection__about__profile #myProfile")) return;

    const template = document.querySelector("#headerSection__about__profile #myProfileTemplate");
    if (!template) {
        console.error("myProfile template not found");
        return;
    }
    const clone = template.content.cloneNode(true);
    const myProfile = clone.querySelector("#myProfile");
    document.getElementById("headerSection__about__profile").appendChild(myProfile);
}

function displayCreateProfile() {

    // Avoid duplicates by checking if form is already in the DOM
    if (document.querySelector("#headerSection__about__profile #createForm")) return;

    const template = document.querySelector("#headerSection__about__profile #createProfileTemplate");
    if (!template) {
        console.error("createProfile template not found");
        return;
    }
    const clone = template.content.cloneNode(true);
    const createForm = clone.querySelector("#createForm");

    document.getElementById("headerSection__about__profile").appendChild(createForm);
    removeElement(createForm);
    fillCreateForm(); // Call after appending to DOM

}
function displayLogoutForm() {
    // Avoid duplicates by checking if form is already in the DOM
    if (document.querySelector("#headerSection__about__log .logoutForm")) {
        return;
    }
    const template = document.querySelector("#headerSection__about__log #logoutFormTemplate");
    if (!template) {
        console.error("Logout form template not found");
        return;
    }

    const clone = template.content.cloneNode(true);
    const logoutForm = clone.querySelector(".logoutForm");
    document.getElementById("headerSection__about__log").appendChild(logoutForm);
    removeElement(logoutForm);
    chooseFromLogoutForm(); // Call after appending to DOM
}

function chooseFromLogoutForm() {
    const form = document.querySelector("#headerSection__about__log .logoutForm");
    if (!form) {
        console.error("Logout form not found in DOM.");
        return;
    }

    // Get references to the specific "Yes" and "No" buttons
    const yesButton = form.querySelector('.logoutForm__button__yes');
    const noButton = form.querySelector('.logoutForm__button__no');

    // Attach click listener to the "Yes" button
    if (yesButton) {
        yesButton.addEventListener('click', function () {
            logOut(); // Call the logout API (presumably handles actual logout and page reload)
            form.remove(); // Close the form after initiating logout
            event.stopPropagation(); // Prevent click from bubbling up to logDiv
        });
    }
    // Attach click listener to the "No" button
    if (noButton) {
        noButton.addEventListener('click', function () {
            console.log("Logout 'No' button clicked. Closing form."); // For debugging
            form.remove(); // Close the form without logging out
            event.stopPropagation(); // Prevent click from bubbling up to logDiv
        });
    }


}

function logOut() {

    fetch('/logout', { method: 'POST' })
        .then(response => {
            // Check the response status FIRST
            if (response.status === 200) {
                // If 200 OK, proceed to parse JSON
                console.log("Login successful!");
                alert("Successful logout!"); // Display success message
                window.location.reload();

            } return response.json();
        });
}
function loadHeader(headerPath, targetElementId) {
    var req = new XMLHttpRequest();
    req.open("GET", headerPath, true);
    req.addEventListener("load", function () {
        if (req.status >= 200 && req.status < 400) {
            document.getElementById(targetElementId).innerHTML = req.responseText;

            // Wait for DOM update, then run login/profile logic
            updateHeaderTextBasedOnLogin();
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
        const loginText = logDiv?.querySelector("p");

        const profileDiv = document.getElementById("headerSection__about__myProfile");
        const profileText = profileDiv?.querySelector("p");

        if (loginText && profileText) {
            if (isLoggedIn) {
                logDiv.className = 'headerSection__log__out';
                loginText.textContent = 'изход';

                profileDiv.className = 'headerSection__myProfile';
                profileText.textContent = 'my profile';

                // ✅ Only attach click listener if class is headerSection__log__out
                logDiv.addEventListener("click", function () {
                    if (logDiv.classList.contains("headerSection__log__out") ) {
                        displayLogoutForm();
                    }
                });
            } else {
                logDiv.className = 'headerSection__log__in';
                loginText.textContent = 'вход';

                profileDiv.className = 'headerSection__createProfile';
                profileText.textContent = 'create profile';


                // ✅ Only attach click listener if class is headerSection__log__in
                logDiv.addEventListener("click", function () {
                    if (logDiv.classList.contains("headerSection__log__in")) {
                        displayLoginForm();
                    }
                });
            }
        }
    });
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
function removeElement(element) {

    // Detect outside click
    setTimeout(() => {  // timeout ensures it doesn't immediately trigger on same click
        function outsideClickHandler(event) {
            // Check if the clicked target is NOT the element itself,
            // AND NOT a descendant of the element.
            // .contains() checks if an element is a descendant of another.
            if (!element.contains(event.target) && event.target !== element) {
                element.remove();
                document.removeEventListener("click", outsideClickHandler);
            }
        }
        document.addEventListener("click", outsideClickHandler);
    }, 0);
}

function displayLogoutForm() {
    // Avoid duplicates by checking if form is already in the DOM
    if (document.querySelector("#headerSection__about__log .logoutForm")){
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
        yesButton.addEventListener('click', function() {
            logOut(); // Call the logout API (presumably handles actual logout and page reload)
            form.remove(); // Close the form after initiating logout
            event.stopPropagation(); // Prevent click from bubbling up to logDiv
        });
    }
    // Attach click listener to the "No" button
    if (noButton) {
        noButton.addEventListener('click', function() {
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
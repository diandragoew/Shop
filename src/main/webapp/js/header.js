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

        // Convert string response ("true" or "false") to boolean
        const isLoggedIn = data.trim() === "true";

        const headerSectionLogin = document.querySelector('#headerSection__about__login');
        const loginLink = document.querySelector('#headerSection__about__login p');

        const headerSectionProfile = document.querySelector('#headerSection__about__myProfile');
        const profileLink = document.querySelector('#headerSection__about__myProfile p');

        if (loginLink && profileLink) {
            if (isLoggedIn) {
                headerSectionLogin.className = 'headerSection__log__out'; // replaces all existing classes on the element
                loginLink.textContent = 'изход';

                headerSectionProfile.className = 'headerSection__myProfile'; // replaces all existing classes on the element
                profileLink.textContent = 'my profile';
            } else {
                headerSectionLogin.className = 'headerSection__log__in'; // replaces all existing classes on the element
                loginLink.textContent = 'вход';

                headerSectionProfile.className = 'headerSection__createProfile'; // replaces all existing classes on the element
                profileLink.textContent = 'create profile';
            }
        }
    });
}

// script.js — basic functionality for dismissing flash messages

document.addEventListener("DOMContentLoaded", function () {
    const alerts = document.querySelectorAll(".alert");

    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.display = "none";
        }, 3000); // Hide after 3 seconds
    });
});

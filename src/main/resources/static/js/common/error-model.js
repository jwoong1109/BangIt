function showErrorModal(message) {
    var modal = document.getElementById("errorModal");
    var span = modal.getElementsByClassName("close")[0];
    var modalBody = document.getElementById("errorModalBody");

    modalBody.textContent = message;
    modal.style.display = "block";

    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    var errorMessage = document.getElementById('errorModal').getAttribute('data-error-message');
    if (errorMessage) {
        showErrorModal(errorMessage);
    }
});
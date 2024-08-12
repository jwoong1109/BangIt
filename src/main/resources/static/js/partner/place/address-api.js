let uploadedImages = [];

function handleImageUpload(event) {
    const files = event.target.files;
    const previewContainer = document.getElementById('imagePreviewContainer');
    previewContainer.innerHTML = ''; // Clear existing previews

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = function(e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.style.width = '100px';
            img.style.height = '100px';
            img.style.objectFit = 'cover';
            img.style.margin = '5px';
            previewContainer.appendChild(img);

            uploadedImages.push(file);
        }

        reader.readAsDataURL(file);
    }
}

document.getElementById('placeSaveForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(this);

    // Remove existing image files from the form
    formData.delete('placeImages');

    // Add uploaded images to the form data
    uploadedImages.forEach((file, index) => {
        formData.append(`placeImage${index}`, file);
    });

    fetch('/partner/placeSave', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('숙소가 성공적으로 등록되었습니다.');
            window.location.href = '/partner';
        } else {
            alert('숙소 등록에 실패했습니다: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('숙소 등록 중 오류가 발생했습니다.');
    });
});
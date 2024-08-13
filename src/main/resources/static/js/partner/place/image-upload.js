function handleImageUpload(event, previewContainerId) {
    const file = event.target.files[0];
    const previewContainer = document.getElementById(previewContainerId);
    previewContainer.innerHTML = ''; // Clear existing preview

    if (file) {
        const reader = new FileReader();

        reader.onload = function(e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.style.width = '100px';
            img.style.height = '100px';
            img.style.objectFit = 'cover';
            img.style.margin = '5px';
            previewContainer.appendChild(img);
        }

        reader.readAsDataURL(file);
    }
}

document.getElementById('placeSaveForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(this);

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
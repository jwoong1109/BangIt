function uploadImage(url, formData) {
    const csrfToken = document.querySelector('input[name="_csrf"]');
    
    let headers = {};
    if (csrfToken) {
        headers['X-CSRF-TOKEN'] = csrfToken.value;
    }
    
    return fetch(url, {
        method: "POST",
        body: formData,
        headers: headers
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Server responded with status: ' + response.status);
        }
        return response.json();
    })
    .catch(error => {
        console.error('Error:', error);
        throw error;
    });
}

function previewImage(input, previewDiv) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            previewDiv.innerHTML = `<img src="${e.target.result}" alt="Image preview" style="max-width: 100%; max-height: 200px;">`;
            previewDiv.style.display = 'block';
            const plusIcon = input.closest('.btn-file')?.querySelector('.plus-icon');
            if (plusIcon) plusIcon.style.display = 'none';
        }
        reader.readAsDataURL(input.files[0]);
    }
}

function fileupload(input) {
    const files = input.files;
    if (files.length < 1) {
        console.log("파일이 선택되지 않았어요");
        return;
    }

    const fileType = files[0].type;
    if (!fileType.startsWith('image/')) {
        alert("이미지 파일이 아닙니다.");
        input.value = '';
        return;
    }

    const fileSize = files[0].size;
    if (fileSize > 2 * 1024 * 1024) {
        alert("파일용량제한: 2MB이내의 파일을 사용하세요:" + fileSize);
        input.value = '';
        return;
    }

    // 파일 미리보기
    let previewDiv;
    if (input.id === 'mainImage') {
        previewDiv = document.getElementById('mainImagePreview');
    } else {
        previewDiv = input.closest('.btn-file').querySelector('.image-preview-container');
    }
    previewImage(input, previewDiv);

    // 서버로 파일 업로드
    var formData = new FormData();
    formData.append("file", files[0]);

    uploadImage("/partner/uploadImage", formData)
        .then(result => {
            console.log("Server response:", result);
            const {url, bucketKey, orgName} = result;

            if (!bucketKey || !orgName) {
                console.error("Missing data in server response");
                return;
            }

            // hidden 필드 업데이트
            let bucketKeyInput, orgNameInput;
            if (input.id === 'mainImage') {
                bucketKeyInput = document.querySelector('input[name="mainImageBucketKey"]');
                orgNameInput = document.querySelector('input[name="mainImageOrgName"]');
            } else {
                bucketKeyInput = input.closest('.btn-file').querySelector('input[name="additionalImageBucketKeys"]');
                orgNameInput = input.closest('.btn-file').querySelector('input[name="additionalImageOrgNames"]');
            }

            if (bucketKeyInput) bucketKeyInput.value = bucketKey;
            if (orgNameInput) orgNameInput.value = orgName;

            console.log("Updated bucketKey:", bucketKeyInput?.value);
            console.log("Updated orgName:", orgNameInput?.value);
        })
        .catch(error => {
            console.error("파일업로드 실패:", error);
            alert("파일 업로드에 실패했습니다. 자세한 내용은 콘솔을 확인하세요.");
        });
}

function submitForm() {
    const form = document.getElementById('roomForm');
    form.submit();
}
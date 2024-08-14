function uploadImage(url, formData) {
    // CSRF 토큰을 hidden 필드에서 가져옵니다.
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
    const reader = new FileReader();
    reader.onload = function(e) {
        let previewDiv, plusIcon;
        if (input.id === 'mainImage') {
            previewDiv = document.getElementById('mainImagePreview');
        } else {
            const label = input.closest('.btn-file');
            previewDiv = label.querySelector('.image-preview-container');
            plusIcon = label.querySelector('.plus-icon');
        }

        previewDiv.innerHTML = `<img src="${e.target.result}" alt="Image preview" style="max-width: 100%; max-height: 200px;">`;
        previewDiv.style.display = 'block';
        if (plusIcon) plusIcon.style.display = 'none';
    }
    reader.readAsDataURL(files[0]);

    var formData = new FormData();
    formData.append("file", files[0]);

    uploadImage("/partner/uploadImage", formData)
        .then(result => {
            console.log("Server response:", result);
            const url = result.url;
            const bucketKey = result.bucketKey;
            const orgName = result.orgName;

            if (!url || !bucketKey || !orgName) {
                console.error("Missing data in server response");
                return;
            }

            // hidden 필드 업데이트
            let bucketKeyInput, orgNameInput;
            if (input.id === 'mainImage') {
                bucketKeyInput = document.querySelector('input[name="mainImageBucketKey"]');
                orgNameInput = document.querySelector('input[name="mainImageOrgName"]');
                if (bucketKeyInput) bucketKeyInput.value = bucketKey;
                if (orgNameInput) orgNameInput.value = orgName;
            } else {
                bucketKeyInput = input.closest('.btn-file').querySelector('input[name="additionalImageBucketKeys"]');
                orgNameInput = input.closest('.btn-file').querySelector('input[name="additionalImageOrgNames"]');

                // 기존 값을 배열로 변환
                let keys = bucketKeyInput.value ? bucketKeyInput.value.split(',') : [];
                let names = orgNameInput.value ? orgNameInput.value.split(',') : [];

                // 새 값 추가 (중복 방지)
                if (!keys.includes(bucketKey)) {
                    keys.push(bucketKey);
                    names.push(orgName);
                }

                // 최대 2개로 제한
                keys = keys.slice(-2);
                names = names.slice(-2);

                // 다시 문자열로 변환하여 저장
                bucketKeyInput.value = keys.join(',');
                orgNameInput.value = names.join(',');
            }

            console.log("Updated bucketKeys:", bucketKeyInput.value);
            console.log("Updated orgNames:", orgNameInput.value);
        })
        .catch(error => {
            console.error("파일업로드 실패:", error);
            alert("파일 업로드에 실패했습니다. 자세한 내용은 콘솔을 확인하세요.");
        });
}

function submitForm() {
    const form = document.getElementById('placeForm');
    form.submit();
}
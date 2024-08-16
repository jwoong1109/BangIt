function validateForm() {
    let errors = [];

    // 숙소 이름 검사
    const name = document.getElementById('place-name');
    if (name && name.value.trim() === '') {
        errors.push('숙소 이름을 입력해주세요.');
    }

    // 숙소 설명 검사
    const description = document.getElementById('place-description');
    if (description && description.value.trim() === '') {
        errors.push('숙소 설명을 입력해주세요.');
    }

    // 지역 선택 검사
    const regionInputs = document.querySelectorAll('input[name="region"]');
    let regionSelected = false;
    regionInputs.forEach(input => {
        if (input.checked) {
            regionSelected = true;
        }
    });
    if (!regionSelected) {
        errors.push('지역을 선택해주세요.');
    }

    // 상세 주소 검사
    const detailedAddress = document.getElementById('detailed-address');
    if (detailedAddress && detailedAddress.value.trim() === '') {
        errors.push('상세 주소를 입력해주세요.');
    }

    // 숙소 유형 검사
    const typeInputs = document.querySelectorAll('input[name="type"]');
    let typeSelected = false;
    typeInputs.forEach(input => {
        if (input.checked) {
            typeSelected = true;
        }
    });
    if (!typeSelected) {
        errors.push('숙소 유형을 선택해주세요.');
    }

    // 메인 이미지 검사
    const mainImageInput = document.querySelector('input[name="mainImageBucketKey"]');
    if (mainImageInput && mainImageInput.value === '') {
        errors.push('메인 이미지를 업로드해주세요.');
    }

    // 오류가 있다면 첫 번째 오류 메시지만 표시
    if (errors.length > 0) {
        alert(errors[0]);
        return false;
    }

    return true;
}

function submitForm() {
    if (validateForm()) {
        const form = document.getElementById('placeForm');
        if (form) {
            form.submit();
        } else {
            console.error('Form not found');
        }
    }
}

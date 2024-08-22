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

function openModal(roomId) {
    fetch(`/api/rooms/${roomId}`)
        .then(response => response.json())
        .then(room => {
            document.getElementById('modalRoomName').textContent = room.roomName;
            document.getElementById('modalRoomInfo').textContent = room.roomInformation;
            document.getElementById('modalCheckIn').textContent = room.checkInTime;
            document.getElementById('modalCheckOut').textContent = room.checkOutTime;
            document.getElementById('modalGuests').textContent = room.guests + '명';
            document.getElementById('modalPrice').textContent = '₩' + room.roomPrice.toLocaleString() + ' / 박';
            document.getElementById('modalRefundPolicy').textContent = room.refundPolicy;

            document.getElementById('roomModal').style.display = 'block';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('방 정보를 불러오는 데 실패했습니다.');
        });
}

function closeModal() {
    document.getElementById('roomModal').style.display = 'none';
}

// 창 바깥을 클릭하면 모달이 닫히도록 설정
window.onclick = function(event) {
    const modal = document.getElementById('roomModal');
    if (event.target == modal) {
        closeModal();
    }
}

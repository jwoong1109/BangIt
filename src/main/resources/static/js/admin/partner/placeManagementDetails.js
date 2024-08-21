// 닫기 버튼 이벤트
// 닫기 버튼 요소 선택
const closePopupButton = document.getElementById('closePopup');

// 팝업 요소 선택
const popupElement = document.querySelector('.place-details-popup');

// 닫기 버튼에 클릭 이벤트 리스너 추가
closePopupButton.addEventListener('click', () => {
    // 팝업 요소가 존재하는지 확인
    if (popupElement) {
        // 팝업의 display 스타일을 'none'으로 설정
        popupElement.style.display = 'none';
    } else {
        console.error('팝업 요소를 찾을 수 없습니다.');
    }
});

// 승인 버튼에 클릭 이벤트 리스너 추가
document.getElementById('approveButton').addEventListener('click', function() {
    // data-place-id 속성에서 숙소 ID를 가져옴
    const placeId = this.getAttribute('data-place-id');
    // 승인 처리 함수 호출
    approvePlace(placeId);
});

// 거절 버튼에 클릭 이벤트 리스너 추가
document.getElementById('rejectButton').addEventListener('click', function() {
    // data-place-id 속성에서 숙소 ID를 가져옴
    const placeId = this.getAttribute('data-place-id');
    // 거절 처리 함수 호출
    rejectPlace(placeId);
});

/**
 * 숙소 승인 처리 함수
 * @param {string} placeId - 승인할 숙소의 ID
 */
function approvePlace(placeId) {
    // AJAX 요청을 통해 서버에 승인 요청을 보냄
    fetch(`/admin/partner/placeManagement/approve/${placeId}`, { method: 'POST' })
        .then(response => {
            if (response.ok) {
                // 요청이 성공적으로 처리된 경우
                alert('숙소가 승인되었습니다.');
                // 부모 창 새로고침 (목록 업데이트를 위해)
                window.parent.location.reload();
            } else {
                // 요청 처리 중 오류가 발생한 경우
                alert('승인 처리 중 오류가 발생했습니다.');
            }
        });
}

/**
 * 숙소 거절 처리 함수
 * @param {string} placeId - 거절할 숙소의 ID
 */
function rejectPlace(placeId) {
    // AJAX 요청을 통해 서버에 거절 요청을 보냄
    fetch(`/admin/partner/placeManagement/reject/${placeId}`, { method: 'POST' })
        .then(response => {
            if (response.ok) {
                // 요청이 성공적으로 처리된 경우
                alert('숙소가 거절되었습니다.');
                // 부모 창 새로고침 (목록 업데이트를 위해)
                window.parent.location.reload();
            } else {
                // 요청 처리 중 오류가 발생한 경우
                alert('거절 처리 중 오류가 발생했습니다.');
            }
        });
}
document.addEventListener('DOMContentLoaded', function() {
    const searchResults = document.querySelector('.search-results ul');
    const reservationInfo = document.querySelector('.reservation-info');
    const noUserSelectedReservation = reservationInfo.querySelector('.no-user-selected');
    const reservationDetails = reservationInfo.querySelector('.reservation-details');
    
    const placeItems = document.querySelectorAll('.user-item');
    const roomSaveBtn = document.getElementById('roomSave'); // ID 수정
    const placeDetails = document.querySelector('.place-details');
    const roomDetails = document.querySelector('.room-details');
    let selectedPlaceId = null;

    placeItems.forEach(item => {
        item.addEventListener('click', function() {
            selectedPlaceId = this.getAttribute('data-place-id');
            // 숙소 상세 정보 로드
            loadPlaceDetails(selectedPlaceId);
        });
    });

    if (roomSaveBtn) {
        roomSaveBtn.addEventListener('click', function() {
            if (selectedPlaceId) {
                loadRoomRegisterForm(selectedPlaceId);
            } else {
                alert('숙소를 먼저 선택해주세요.');
            }
        });
    } else {
        console.error('방 저장 버튼을 찾을 수 없습니다');
    }

    function loadPlaceDetails(placeId) {
        fetch(`/partner/placeDetails/${placeId}`)
            .then(response => response.text())
            .then(html => {
                placeDetails.innerHTML = html;
            })
            .catch(error => {
                console.error('숙소 상세 정보 로드 오류:', error);
                placeDetails.innerHTML = '<p>숙소 상세 정보를 로드하는 중 오류가 발생했습니다. 다시 시도해주세요.</p>';
            });
    }

    function loadRoomRegisterForm(placeId) {
        fetch(`/partner/roomRegisterForm?placeId=${placeId}`)
            .then(response => response.text())
            .then(html => {
                roomDetails.innerHTML = html;
                setupRoomRegisterForm();
            })
            .catch(error => {
                console.error('방 등록 폼 로드 오류:', error);
                roomDetails.innerHTML = '<p>방 등록 폼을 로드하는 중 오류가 발생했습니다. 다시 시도해주세요.</p>';
            });
    }

    function setupRoomRegisterForm() {
        const form = document.querySelector('form[action="/partner/roomSave"]');
        if (form) {
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                const formData = new FormData(this);
                fetch('/partner/roomSave', {
                    method: 'POST',
                    body: formData,
                })
                .then(response => {
                    if (response.ok) {
                        alert('방이 성공적으로 등록되었습니다.');
                        window.location.reload();  // 페이지 새로고침
                    } else {
                        alert('방 저장에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('방 저장 오류:', error);
                    alert('방 저장 중 오류가 발생했습니다.');
                });
            });
        }
    }

    searchResults.addEventListener('click', function(e) {
        const userItem = e.target.closest('.user-item');
        if (userItem) {
            // 선택된 사용자 강조
            searchResults.querySelectorAll('.user-item').forEach(item => item.classList.remove('selected'));
            userItem.classList.add('selected');

            // 예약 정보 섹션 업데이트
            noUserSelectedReservation.style.display = 'none';
            reservationDetails.style.display = 'block';

            // 콘솔에 로그 추가
            console.log('사용자가 클릭한 항목:', userItem.textContent);
            console.log('예약 정보 및 활동 정보 표시됨');
        }
    });
});
    
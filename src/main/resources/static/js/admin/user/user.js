document.addEventListener('DOMContentLoaded', function() {
    const searchResults = document.querySelector('.search-results ul');
    const reservationInfo = document.querySelector('.reservation-info');
    const userActivity = document.querySelector('.user-activity');
    const noUserSelectedReservation = reservationInfo.querySelector('.no-user-selected');
    const noUserSelectedActivity = userActivity.querySelector('.no-user-selected');
    const reservationDetails = reservationInfo.querySelector('.reservation-details');
    const activityDetails = userActivity.querySelector('.activity-details');
    const reservationPopup = document.querySelector('.reservation-popup');
    const closePopupButton = reservationPopup.querySelector('.close-popup');

    searchResults.addEventListener('click', function(e) {
        const userItem = e.target.closest('.user-item');
        if (userItem) {
            // 선택된 사용자 강조
            searchResults.querySelectorAll('.user-item').forEach(item => item.classList.remove('selected'));
            userItem.classList.add('selected');

            // 예약 정보 섹션 업데이트
            noUserSelectedReservation.style.display = 'none';
            reservationDetails.style.display = 'block';

            // 활동 내역 섹션 업데이트
            noUserSelectedActivity.style.display = 'none';
            activityDetails.style.display = 'block';

            // 콘솔에 로그 추가
            console.log('User clicked:', userItem.textContent);
            console.log('Reservation details and activity details displayed');
        }
    });
    
    
    
    reservationDetails.addEventListener('click', function(e) {
        const reservationItem = e.target.closest('.user-item');
        if (reservationItem) {
            // 여기서 실제 데이터를 가져와 팝업을 채웁니다.
            // 이 예제에서는 더미 데이터를 사용합니다.
			document.getElementById('popup-accommodation').textContent = reservationItem.children[3].textContent;
			document.getElementById('popup-room').textContent = reservationItem.children[4].textContent;
			document.getElementById('popup-checkin-date').textContent = reservationItem.children[1].textContent;
			document.getElementById('popup-checkout-date').textContent = reservationItem.children[2].textContent;
			document.getElementById('popup-checkin-time').textContent = '15:00';
			document.getElementById('popup-checkout-time').textContent = '11:00';
			document.getElementById('popup-payment-date').textContent = reservationItem.children[0].textContent;
			document.getElementById('popup-payment-info').textContent = '신용카드';
			document.getElementById('popup-status').textContent = '예약확정';
            reservationPopup.style.display = 'block';
        }
    });

    closePopupButton.addEventListener('click', function() {
        reservationPopup.style.display = 'none';
    });

    // 팝업 외부 클릭 시 닫기
    reservationPopup.addEventListener('click', function(e) {
        if (e.target === reservationPopup) {
            reservationPopup.style.display = 'none';
        }
    });
    
    
    
    
    
    
    
    
    
});
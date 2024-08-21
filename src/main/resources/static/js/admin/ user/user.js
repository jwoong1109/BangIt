document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM fully loaded');

    const searchResults = document.querySelector('.search-results ul');
    const reservationInfo = document.querySelector('.reservation-info');
    const userActivity = document.querySelector('.user-activity');
    const noUserSelectedReservation = reservationInfo.querySelector('.no-user-selected');
    const noUserSelectedActivity = userActivity.querySelector('.no-user-selected');
    const reservationDetails = reservationInfo.querySelector('.reservation-details');
    const activityDetails = userActivity.querySelector('.activity-details');
    const reservationPopup = document.querySelector('.reservation-popup');
    const closePopupButton = reservationPopup.querySelector('.close-popup');
    
    let currentUserData = null;

    console.log('Search results element:', searchResults);

    searchResults.addEventListener('click', function(e) {
        const userItem = e.target.closest('.user-item');
        if (!userItem) return;

        const userId = userItem.getAttribute('data-user-id');
        console.log('Clicked user ID:', userId);

        // 선택된 사용자 강조
        searchResults.querySelectorAll('.user-item').forEach(item => item.classList.remove('selected'));
        userItem.classList.add('selected');

        // 선택된 사용자의 정보 비동기 요청
        fetch(`/admin/user/detail/${userId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Received data:', data);
                currentUserData = data;
                // 예약 정보 섹션 업데이트
                updateReservationInfo(data.reservations);
                // 활동 내역 섹션 업데이트
                updateActivityInfo(data.activities);
            })
            .catch(error => console.error('Error:', error));
    });

    function updateReservationInfo(reservations) {
        console.log('Updating reservation info:', reservations);
        noUserSelectedReservation.style.display = 'none';
        reservationDetails.style.display = 'block';
        
        const reservationList = reservationDetails.querySelector('ul');
        // 헤더를 제외한 기존 항목들 제거
        while (reservationList.children.length > 1) {
            reservationList.removeChild(reservationList.lastChild);
        }
        
        if (!reservations || reservations.length === 0) {
            const li = document.createElement('li');
            li.textContent = '예약 기록이 없습니다.';
            li.style.textAlign = 'center';
            li.style.gridColumn = '1 / -1'; // 모든 열을 차지하도록 설정
            reservationList.appendChild(li);
        } else {
            reservations.forEach(reservation => {
                const li = document.createElement('li');
                li.className = 'user-item';
                li.innerHTML = `
                    <span>${formatDate(reservation.reservationDate)}</span>
                    <span>${formatDate(reservation.checkInDate)}</span>
                    <span>${formatDate(reservation.checkOutDate)}</span>
                    <span>${reservation.placeName}</span>
                    <span>${reservation.roomName}</span>
                `;
                li.dataset.reservationId = reservation.id;
                reservationList.appendChild(li);
            });
        }
    }

    function updateActivityInfo(activities) {
        console.log('Updating activity info:', activities);
        noUserSelectedActivity.style.display = 'none';
        activityDetails.style.display = 'block';
        
        const activityList = activityDetails.querySelector('ul');
        // 헤더를 제외한 기존 항목들 제거
        while (activityList.children.length > 1) {
            activityList.removeChild(activityList.lastChild);
        }
        
        if (!activities || activities.length === 0) {
            const li = document.createElement('li');
            li.textContent = '활동 기록이 없습니다.';
            li.style.textAlign = 'center';
            li.style.gridColumn = '1 / -1'; // 모든 열을 차지하도록 설정
            activityList.appendChild(li);
        } else {
            activities.forEach(activity => {
                const li = document.createElement('li');
                li.className = 'user-item';
            
                li.innerHTML = `
                    <span>${activity.timestamp}</span>
                    <span>${activity.description}</span>
                    <span>${activity.detailRecord}</span>
                `;
                activityList.appendChild(li);
            });
        }
    }
    
    function formatDate(dateString) {
        const date = new Date(dateString);
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    }

    function formatTime(date) {
        return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
    }
    
    function getReservationStatusKoName(status) {
    const statusMap = {
        'PENDING': '대기중',
        'CONFIRMED': '확정',
        'CANCELLED': '취소됨'
    };
    return statusMap[status] || status; // 매핑되지 않은 상태는 원래 값 반환
}

	reservationDetails.addEventListener('click', function(e) {
		const reservationItem = e.target.closest('.user-item');
		if (reservationItem && currentUserData) {
			const reservationId = reservationItem.dataset.reservationId;
			const reservation = currentUserData.reservations.find(r => r.id == reservationId);
			if (reservation) {
				document.getElementById('popup-accommodation').textContent = reservation.placeName;
				document.getElementById('popup-room').textContent = reservation.roomName;
				document.getElementById('popup-checkin-date').textContent = formatDate(reservation.checkInDate);
				document.getElementById('popup-checkout-date').textContent = formatDate(reservation.checkOutDate);
				document.getElementById('popup-checkin-time').textContent = formatTime(reservation.checkInTime);
				document.getElementById('popup-checkout-time').textContent = formatTime(reservation.checkOutTime);
				document.getElementById('popup-payment-date').textContent = reservation.paymentDate ? formatDate(reservation.paymentDate) : '결제 정보 없음';
				document.getElementById('popup-payment-info').textContent = reservation.paymentMethod || '결제 정보 없음';
				document.getElementById('popup-status').textContent = getReservationStatusKoName(reservation.reservationStatus);
				reservationPopup.style.display = 'block';
			}
		}
		
		
	});
	
	function formatTime(timeString) {
    if (!timeString) return '정보 없음';
    const time = new Date(`1970-01-01T${timeString}`);
    return time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

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
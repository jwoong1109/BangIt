document.addEventListener('DOMContentLoaded', function() {
    const searchResults = document.querySelector('.search-results ul');
    const reservationInfo = document.querySelector('.reservation-info');
    const userActivity = document.querySelector('.user-activity');
    const noUserSelectedReservation = reservationInfo.querySelector('.no-user-selected');
    const reservationDetails = reservationInfo.querySelector('.reservation-details');

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
            console.log('User clicked:', userItem.textContent);
            console.log('Reservation details and activity details displayed');
        }
    });
    
    
});

document.addEventListener("DOMContentLoaded", function() {
	        const placeItems = document.querySelectorAll('.user-item');
	        const placeDetails = document.querySelector('.place-details');
	
	        placeItems.forEach(item => {
	            item.addEventListener('click', function() {
	                const placeId = this.getAttribute('data-place-id');
	                if (placeId) {
	                    fetch(`/partner/placeDetails/${placeId}`)
	                        .then(response => {
	                            if (!response.ok) {
	                                throw new Error('Server responded with status: ' + response.status);
	                            }
	                            return response.text();
	                        })
	                        .then(html => {
	                            placeDetails.innerHTML = html;
	                        })
	                        .catch(error => {
	                            console.error('Error loading place details:', error);
	                            placeDetails.innerHTML = '<p>Error loading place details. Please try again.</p>';
	                        });
	                } else {
	                    console.error('Place ID is missing');
	                    placeDetails.innerHTML = '<p>Error: Place ID is missing.</p>';
	                }
	            });
	        });
	    });
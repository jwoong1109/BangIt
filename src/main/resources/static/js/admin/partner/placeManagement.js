document.addEventListener('DOMContentLoaded', function() {
	console.log('DOM fully loaded');
	const popupOverlay = document.getElementById('popup-overlay');
	const popupContent = document.getElementById('popup-content');
	console.log('Popup elements:', { popupOverlay, popupContent });

	document.querySelectorAll('.show-popup').forEach(button => {
		button.addEventListener('click', function() {
			const placeId = this.getAttribute('data-place-id');
			console.log('Show popup clicked for place ID:', placeId);
			fetch(`/admin/partner/placeManagementDetails/${placeId}`)
				.then(response => {
					console.log('Fetch response status:', response.status);
					return response.text();
				})
				.then(html => {
					//상세페이지 html이생성
					console.log('Received HTML content');
					popupContent.innerHTML = html;
					popupOverlay.style.display = 'flex';
					console.log('Popup displayed');
				})
				.catch(error => {
					console.error('Error loading place details:', error);
					alert('숙소 상세 정보를 불러오는 데 실패했습니다.');
				});
		});
	});

	popupOverlay.addEventListener('click', function(event) {
		if (event.target === popupOverlay) {
			console.log('Closing popup');
			popupOverlay.style.display = 'none';
		}
	});
});

function approveOrRejectButtonClicked(placeId, placeStatus) {
	console.log('approveOrRejectButtonClicked called:', { placeId, placeStatus });
	// CSRF 토큰을 meta 태그에서 가져옴
	const csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');
	console.log('CSRF Token:', csrfToken);

	const data = { placeStatus: `${placeStatus}` };
	console.log('Request data:', data);

	// 컨트롤러에서 @RequestParam으로 enum 객체로 받았을 때 사용 방식
	// fetch(`/admin/partner/placeManagementDetails/${placeId}?placeStatus=${placeStatus}`
	fetch(`/admin/partner/placeManagementDetails/${placeId}`, {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json', // 요청 본문이 JSON인 경우
			'X-CSRF-TOKEN': csrfToken
		},
		body: JSON.stringify(data) // 본문 데이터를 JSON으로 변환
	})
		.then(response => {
			console.log('Fetch response status:', response.status);
			if (response.ok) {
				console.log('Request successful');
				alert("처리완료!");
				location.href = "/admin/partner/placeManagement" // 처리 후 페이지 새로고침
			} else {
				throw new Error('Network response was not ok');
			}
		})
		.catch(error => {
			console.error('Error updating place details:', error);
			alert('숙소 정보를 업데이트하는 데 실패했습니다.');
		});
}
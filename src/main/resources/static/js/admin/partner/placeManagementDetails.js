document.getElementById('closePopup').addEventListener('click', function() {
	window.parent.document.getElementById('popup-overlay').style.display = 'none';
});

document.getElementById('approveButton').addEventListener('click', function() {
	const placeId = this.getAttribute('data-place-id');
	approvePlace(placeId);
});

document.getElementById('rejectButton').addEventListener('click', function() {
	const placeId = this.getAttribute('data-place-id');
	rejectPlace(placeId);
});

function approvePlace(placeId) {
	// AJAX 요청을 통해 서버에 승인 요청을 보냅니다.
	fetch(`/admin/partner/placeManagement/approve/${placeId}`, { method: 'POST' })
		.then(response => {
			if (response.ok) {
				alert('숙소가 승인되었습니다.');
				window.parent.location.reload();
			} else {
				alert('승인 처리 중 오류가 발생했습니다.');
			}
		});
}

function rejectPlace(placeId) {
	// AJAX 요청을 통해 서버에 거절 요청을 보냅니다.
	fetch(`/admin/partner/placeManagement/reject/${placeId}`, { method: 'POST' })
		.then(response => {
			if (response.ok) {
				alert('숙소가 거절되었습니다.');
				window.parent.location.reload();
			} else {
				alert('거절 처리 중 오류가 발생했습니다.');
			}
		});
}
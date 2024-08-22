document.addEventListener('DOMContentLoaded', function() {
    const popupOverlay = document.getElementById('popup-overlay');
    const popupContent = document.getElementById('popup-content');

    document.querySelectorAll('.show-popup').forEach(button => {
        button.addEventListener('click', function() {
            const placeId = this.getAttribute('data-place-id');
            fetch(`/admin/partner/placeManagementDetails/${placeId}`)
                .then(response => response.text())
                .then(html => {
                    popupContent.innerHTML = html;
                    popupOverlay.style.display = 'flex';
                    
                    // 동적으로 추가된 버튼에 이벤트 리스너 추가
                    addButtonListeners();
                })
                .catch(error => {
                    console.error('Error loading place details:', error);
                    alert('숙소 상세 정보를 불러오는 데 실패했습니다.');
                });
        });
    });

    popupOverlay.addEventListener('click', function(event) {
        if (event.target === popupOverlay) {
            popupOverlay.style.display = 'none';
        }
    });

    // 동적으로 추가된 버튼에 이벤트 리스너를 추가하는 함수
    function addButtonListeners() {
        const approveButton = document.getElementById('approveButton');
        const rejectButton = document.getElementById('rejectButton');

        if (approveButton) {
            approveButton.addEventListener('click', function() {
                const placeId = this.getAttribute('data-place-id');
                approveOrRejectButtonClicked(placeId, 'APPROVED');
            });
        }

        if (rejectButton) {
            rejectButton.addEventListener('click', function() {
                const placeId = this.getAttribute('data-place-id');
                approveOrRejectButtonClicked(placeId, 'REJECTED');
            });
        }
    }
});

// 전역 스코프에 함수 정의
window.approveOrRejectButtonClicked = function(placeId, placeStatus) {
    const csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');
    const data = { placeStatus: `${placeStatus}` };

    fetch(`/admin/partner/placeManagementDetails/${placeId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok) {
            alert("처리완료!");
            location.href = "/admin/partner/placeManagement"
        } else {
            throw new Error('Network response was not ok');
        }
    })
    .catch(error => {
        console.error('Error updating place details:', error);
        alert('숙소 정보를 업데이트하는 데 실패했습니다.');
    });
}
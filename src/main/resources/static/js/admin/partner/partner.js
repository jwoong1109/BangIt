// 판매자 검색 결과 핸들러 설정 함수
function setupSellerSearch() {
    const searchResults = document.querySelector('.seller-search-results ul');

    if (searchResults) {
        searchResults.addEventListener('click', function(e) {
            const sellerItem = e.target.closest('li:not(.header)');
            if (sellerItem) {
                // 선택된 판매자 강조
                searchResults.querySelectorAll('li').forEach(item => item.classList.remove('selected'));
                sellerItem.classList.add('selected');

                // 숙소 목록 섹션 업데이트
                const accommodationListing = document.querySelector('.accommodation-listing');
                const noSellerSelected = accommodationListing?.querySelector('.no-seller-selected');
                const listingDetails = accommodationListing?.querySelector('.listing-details');

                if (noSellerSelected) noSellerSelected.style.display = 'none';
                if (listingDetails) listingDetails.style.display = 'block';

                console.log('판매자 클릭:', sellerItem.querySelector('span:first-child').textContent);
                
                // 숙소 목록 클릭 이벤트 설정
                setupAccommodationListingClick();
            }
        });
    } else {
        console.warn('판매자 검색 결과 요소를 찾을 수 없습니다.');
    }
}

// 숙소 목록 클릭 이벤트 설정 함수
function setupAccommodationListingClick() {
    const listingDetails = document.querySelector('.accommodation-listing .listing-details ul');
    const accommodationInfo = document.querySelector('.accommodation-info');
    const noSellerSelected = accommodationInfo.querySelector('.no-seller-selected');
    const accommodationInfoDetails = accommodationInfo.querySelector('.accommodation-info-details');

    if (listingDetails) {
        listingDetails.addEventListener('click', function(e) {
            const accommodationItem = e.target.closest('li:not(.header)');
            if (accommodationItem) {
                // 숙소 정보 추출
                const name = accommodationItem.querySelector('span:nth-child(1)').textContent;
                const location = accommodationItem.querySelector('span:nth-child(2)').textContent;
                const type = accommodationItem.querySelector('span:nth-child(3)').textContent;
                const price = accommodationItem.querySelector('span:nth-child(4)').textContent;

                // 숙소 정보 영역 업데이트
                document.getElementById('detail-location').textContent = location;
                document.getElementById('detail-roomNo').textContent = name;
                /*document.getElementById('detail-price').textContent = price;*/

                // 숙소 정보 영역 표시
                if (noSellerSelected) noSellerSelected.style.display = 'none';
                if (accommodationInfoDetails) accommodationInfoDetails.style.display = 'block';

                console.log('숙소 클릭:', name);
            }
        });
    } else {
        console.warn('숙소 목록 요소를 찾을 수 없습니다.');
    }
}

// 팝업 핸들러 설정 함수
function setupPopup() {
    const popup = document.querySelector('.accommodation-popup');
    const closePopupButton = popup?.querySelector('.close-popup');

    // 팝업 닫기 버튼 클릭 시 팝업을 닫습니다.
    if (closePopupButton) {
        closePopupButton.addEventListener('click', function() {
            if (popup) popup.style.display = 'none';
        });
    }

    // 팝업 외부 클릭 시 팝업을 닫습니다.
    if (popup) {
        popup.addEventListener('click', function(e) {
            if (e.target === popup) popup.style.display = 'none';
        });
    }

    // Esc 키를 눌렀을 때 팝업을 닫습니다.
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape' && popup) popup.style.display = 'none';
    });
}

// 페이지 로드 시 이벤트 핸들러 설정
document.addEventListener('DOMContentLoaded', function() {
    setupSellerSearch();
    setupPopup();
});
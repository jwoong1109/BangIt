// 페이지 로드 시 실행될 메인 함수
document.addEventListener('DOMContentLoaded', function() {
	setupMapHandlers();
});

// 지도 관련 핸들러 설정 함수
function setupMapHandlers() {
	const mapButton = document.querySelector('.map-icon');
	const mapPopup = document.querySelector('.map-popup');
	const closeMapPopupButton = mapPopup?.querySelector('.close-map-popup');

	// Kakao Maps API 스크립트를 비동기적으로 로드
	function loadKakaoMapsApi(callback) {
		const existingScript = document.querySelector('script[src="//dapi.kakao.com/v2/maps/sdk.js?appkey=7e77f388ffda8e38b7c06418520328a9&autoload=false"]');
		if (!existingScript) {
			const script = document.createElement('script');
			script.src = '//dapi.kakao.com/v2/maps/sdk.js?appkey=7e77f388ffda8e38b7c06418520328a9&autoload=false';
			script.onload = callback;
			document.head.appendChild(script);
		} else {
			callback();
		}
	}

	// 지도 생성 함수
	function initializeMap(containerId) {
		const mapContainer = document.getElementById(containerId);
		if (mapContainer && typeof kakao !== 'undefined' && kakao.maps) {
			const options = {
				center: new kakao.maps.LatLng(37.656125, 127.061896),
				level: 3
			};
			new kakao.maps.Map(mapContainer, options);
		}
	}

	// Kakao Maps API 로드 후 지도 초기화
	loadKakaoMapsApi(function() {
		if (typeof kakao !== 'undefined' && kakao.maps) {
			kakao.maps.load(function() {
				// 지도 팝업 열기 버튼 클릭 시
				if (mapButton) {
					mapButton.addEventListener('click', function() {
						if (mapPopup) {
							mapPopup.style.display = 'flex';
							initializeMap('map-popup');
						}
					});
				}

				// 지도 팝업 닫기 버튼 클릭 시
				if (closeMapPopupButton) {
					closeMapPopupButton.addEventListener('click', function() {
						if (mapPopup) {
							mapPopup.style.display = 'none';
						}
					});
				}

				// 팝업 외부 클릭 시 닫기
				if (mapPopup) {
					mapPopup.addEventListener('click', function(e) {
						if (e.target === mapPopup) {
							mapPopup.style.display = 'none';
						}
					});
				}

				// 기본 화면 지도 초기화 (필요한 경우)
				initializeMap('map');
			});
		}
	});
}

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
				console.log('숙소 상세 정보 표시됨');
			}
		});
	}
}

// 숙소 목록 및 팝업 핸들러 설정 함수
function setupAccommodationListing() {
	const listingDetails = document.querySelector('.accommodation-listing .listing-details');
	const accommodationPopup = document.querySelector('.accommodation-popup');
	const closePopupButton = accommodationPopup?.querySelector('.close-popup');

	if (listingDetails) {
		listingDetails.addEventListener('click', function(e) {
			const accommodationItem = e.target.closest('li:not(.header)');
			if (accommodationItem && accommodationPopup) {
				// 팝업에 숙소 데이터 채우기
				document.getElementById('popup-accommodation-name').textContent = accommodationItem.querySelector('span:nth-child(1)').textContent;
				document.getElementById('popup-accommodation-location').textContent = accommodationItem.querySelector('span:nth-child(2)').textContent;
				document.getElementById('popup-accommodation-category').textContent = accommodationItem.querySelector('span:nth-child(3)').textContent;
				document.getElementById('popup-accommodation-price').textContent = accommodationItem.querySelector('span:nth-child(4)').textContent;

				// 팝업 표시
				accommodationPopup.style.display = 'block';
			}
		});
	}

	// 팝업 닫기 버튼 클릭 시
	if (closePopupButton) {
		closePopupButton.addEventListener('click', function() {
			if (accommodationPopup) {
				accommodationPopup.style.display = 'none';
			}
		});
	}

	// 팝업 외부 클릭 시 닫기
	if (accommodationPopup) {
		accommodationPopup.addEventListener('click', function(e) {
			if (e.target === accommodationPopup) {
				accommodationPopup.style.display = 'none';
			}
		});
	}
}

/* 상세페이지 팝업 애니메이션 */
document.addEventListener('DOMContentLoaded', function() {
    const showPopupButtons = document.querySelectorAll('.show-popup');
    const popup = document.getElementById('details-popup');
    const closePopupButton = document.getElementById('close-popup');
    const popupContent = document.getElementById('popup-content');

    showPopupButtons.forEach(button => {
        button.addEventListener('click', function() {
            // 여기에 팝업 내용을 동적으로 설정하는 로직을 추가할 수 있습니다.
            // 예: API 호출을 통해 상세 정보를 가져오거나, 데이터 속성을 사용하여 정보를 전달할 수 있습니다.

            // 팝업 표시
            popup.classList.add('active');
        });
    });

    closePopupButton.addEventListener('click', function() {
        // 팝업 닫기
        popup.classList.remove('active');
    });

    // 팝업 외부 클릭 시 닫기
    popup.addEventListener('click', function(event) {
        if (event.target === popup) {
            popup.classList.remove('active');
        }
    });

    // Esc 키를 눌렀을 때 팝업 닫기
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            popup.classList.remove('active');
        }
    });
});







/*
// 모든 이벤트 핸들러를 설정하는 함수
function setupEventHandlers() {
	setupAsyncLinks();

	setupSellerSearch();
	setupAccommodationListing();
}

// 비동기 링크 설정 함수
function setupAsyncLinks() {
	// 이벤트 위임을 사용하여 문서 전체에 이벤트 리스너 추가
	document.addEventListener('click', function(event) {
		const link = event.target.closest('a[data-url]');
		if (!link) return; // 클릭된 요소가 data-url 속성을 가진 a 태그가 아니면 무시

		event.preventDefault(); // 기본 링크 동작 방지

		const url = link.getAttribute('data-url');
		if (!url) {
			console.error('No URL specified');
			return;
		}

		const sectionElement = document.querySelector('#adminlayoutWrap > main > section');
		if (!sectionElement) {
			console.error('Section element not found');
			return;
		}

		// AJAX 요청을 보냅니다
		fetch(url)
			.then(response => {
				if (response.ok) return response.text();
				throw new Error('Network response was not ok.');
			})
			.then(html => {
				sectionElement.innerHTML = html; // 응답 HTML로 영역 업데이트
				setupEventHandlers(); // 새로운 컨텐츠에 대해 이벤트 핸들러 재설정
			})
			.catch(error => {
				console.error('Fetch operation problem:', error);
			});
	});
}*/
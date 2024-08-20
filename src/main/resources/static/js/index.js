document.addEventListener("DOMContentLoaded", function() {
	// CSRF 토큰 및 헤더 이름 가져오기
	const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
	const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
	
	// Geolocation API를 사용하여 사용자 위치 가져오기
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			let latitude = position.coords.latitude;
			let longitude = position.coords.longitude;

			// 서버로 위치 정보 전송
			fetch('/get-nearby-hotels', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					[csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
				},
				body: JSON.stringify({
					latitude: latitude,
					longitude: longitude
				})
			})
			.then(response => response.json())
			.then(data => {
				// 데이터를 받아와서 HTML에 동적으로 뿌려줌
				displayNearbyHotels(data);
			})
			.catch(error => console.error('Error:', error));
		}, function(error) {
			console.error('Error getting geolocation:', error);
		});
	} else {
		console.error('Geolocation is not supported by this browser.');
	}
	
	// 호텔 데이터를 HTML에 동적으로 추가하는 함수
    function displayNearbyHotels(hotels) {
        var container = document.querySelector('.hotel-cards');
        container.innerHTML = '';  // 기존 내용을 비웁니다.

        hotels.forEach(hotel => {
            var hotelCard = `
                <div class="hotel-card">
                    <a href="/place/detail/${hotel.id}">
                        <img src="${hotel.imageUrl}" alt="Hotel">
                    </a>
                    <div class="hotel-name">
                        <h3>${hotel.name}</h3>
                        <p>거리: ${hotel.distance} km</p>
                    </div>
                </div>
            `;
            container.innerHTML += hotelCard;
        });
    }
	
	
	// jQuery UI Datepicker 한국어 설정 및 초기화
	/*	$.datepicker.setDefaults($.datepicker.regional['ko']);
	
		$("#checkin-date").datepicker({
			dateFormat: "yy년 MM dd일",
			minDate: 0, // 오늘 날짜 이후만 선택 가능
			onClose: function(selectedDate) {
				// 체크아웃 날짜의 최소 선택 가능 날짜를 체크인 날짜 이후로 설정
				$("#checkout-date").datepicker("option", "minDate", selectedDate);
			}
		});
	
		$("#checkout-date").datepicker({
			dateFormat: "yy년 MM dd일",
			minDate: 1 // 오늘 날짜 이후만 선택 가능
		});*/

	// 인원 수 조정
	$("#guest-minus").click(function() {
		let currentCount = parseInt($("#guest-count").val());
		if (currentCount > 1) {
			$("#guest-count").val(currentCount - 1);
		}
	});

	$("#guest-plus").click(function() {
		let currentCount = parseInt($("#guest-count").val());
		$("#guest-count").val(currentCount + 1);
	});

	// GSAP과 ScrollTrigger 플러그인 로드
	gsap.registerPlugin(ScrollTrigger);

	// 호텔 카드 애니메이션 설정
/*	gsap.from(".hotel-card", {
		scrollTrigger: {
			trigger: ".hotels",  // 애니메이션이 시작될 섹션
			start: "top 80%",    // 섹션이 화면에 80% 들어올 때 시작
			toggleActions: "play none none none"
		},
		opacity: 0,  // 처음에는 투명
		x: -50,      // 왼쪽에서 슬라이드
		duration: 1, // 애니메이션 지속 시간
		stagger: 0.3 // 각 카드마다 순차적으로 애니메이션 실행
	});*/
	
	gsap.utils.toArray('.hotel-card').forEach((card) => {
		gsap.from(card, {
			scrollTrigger: {
				trigger: card,
				start: 'top 75%',
				end: 'bottom 25%',
				toggleActions: 'play none none reverse'
			},
			opacity: 0,
			y: 100,
			duration: 1,
			ease: 'power1.out'
		});
	});
	
		gsap.utils.toArray('.profile-cards').forEach((card) => {
		gsap.from(card, {
			scrollTrigger: {
				trigger: card,
				start: 'top 75%',
				end: 'bottom 25%',
				toggleActions: 'play none none reverse'
			},
			opacity: 0,
			y: 100,
			duration: 1,
			ease: 'power1.out'
		});
	});

	gsap.utils.toArray('.pick-card').forEach((card) => {
		gsap.from(card, {
			scrollTrigger: {
				trigger: card,
				start: 'top 75%',
				end: 'bottom 25%',
				toggleActions: 'play none none reverse'
			},
			opacity: 0,
			y: 100,
			duration: 1,
			ease: 'power1.out'
		});
	});
	document.getElementById('loginLink').addEventListener('click', function(event) {
		event.preventDefault(); // 기본 링크 동작을 막습니다.

		// 페이지 전환 전 애니메이션
		gsap.to('main', {
			opacity: 0,
			duration: 1,
			ease: 'power1.inOut',
			onComplete: function() {
				// 페이지 이동
				window.location.href = '/login';
			}
		});
	});
});

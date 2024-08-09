/*************************웹소켓*************************
// SockJS를 사용하여 WebSocket 연결을 설정합니다.
// '/bangItBot'은 서버에서 설정한 WebSocket 엔드포인트입니다.
const socket = new SockJS('/bangItBot');
const stompClient = Stomp.over(socket);

// STOMP 클라이언트를 서버에 연결합니다.
stompClient.connect({}, function(frame) {
	console.log('Connected: ' + frame);

	// 메시지를 받았을 때 처리할 콜백 함수를 설정합니다.
	// 서버가 '/topic/bot' 경로로 메시지를 전송하면 이 함수가 호출됩니다.
	stompClient.subscribe('/topic/bot', function(message) {
		// 수신된 메시지를 alert 창으로 표시합니다.
		alert('Received: ' + message.body);
	});

	// ***** 사용자 정보를 서버에서 가져오는 함수 *****
	// 이 함수는 서버에서 현재 로그인한 사용자의 정보를 가져옵니다.
	function getUserName() {
		return fetch('/api/user/info')  // 사용자 정보를 반환하는 API 엔드포인트
			.then(response => response.json())
			.then(data => data.name);    // 사용자 이름을 반환
	}

	// 메시지를 서버로 전송하는 함수
	// content: 메시지의 내용
	// name: 메시지를 보낸 사람의 이름
	// key: 메시지의 고유 식별자 (예: 현재 시간)
	function sendMessage(content, key) {
		// 사용자 정보를 가져온 후 메시지를 전송합니다.
		getUserName().then(userName => {
			// 사용자 이름을 포함한 메시지 내용으로 수정합니다.
			const messageContent = `안녕하세요, ${userName}!`;

			// '/app/bot' 경로로 메시지를 전송합니다.
			// 빈 객체는 추가적인 헤더를 설정할 때 사용될 수 있습니다.
			// JSON.stringify를 사용하여 JavaScript 객체를 JSON 문자열로 변환하여 서버에 전송합니다.
			stompClient.send('/app/bot', {}, JSON.stringify({ content: messageContent, name: userName, key: key }));
		});
	}

	// 예시로 '안녕하세요, 로그인한 사용자의 이름!'라는 내용의 메시지를 전송합니다.
	// 현재 시간을 key로 설정합니다.
	sendMessage('', Date.now());
});
*/


$(document).ready(function() {
	// 챗봇 토글 버튼 클릭 시 챗봇 컨테이너의 표시 상태를 토글합니다
	$('#chatbot-toggle').click(function() {
		const $chatbotContainer = $('#chatbot-container');
		const $chatbotIcon = $('#chatbot-toggle i');

		if ($chatbotContainer.hasClass('visible')) {
			// 챗봇이 이미 보이고 있는 경우, 닫기 애니메이션 적용
			$chatbotContainer.removeClass('bubble-in').addClass('bubble-out');
			setTimeout(() => {
				$chatbotContainer.removeClass('visible bubble-out'); // 애니메이션 완료 후 숨기기
				$('#chatbot-toggle').removeClass('open'); // 버튼 애니메이션 원래 상태로 복원
				$chatbotIcon.attr('class', 'fas fa-comment-dots fa-flip-horizontal'); // 버튼 아이콘 원래 상태로 복원

				// 챗봇을 닫을 때 대화 내용과 하위 카테고리 숨기기
				$('#chatbot-messages').empty(); // 대화 내용 제거
				$('#chatbot-subcategories').hide(); // 하위 카테고리 숨기기
				$('#chatbot-categories').show(); // 카테고리 버튼 다시 보이기
			}, 300); // 애니메이션 시간과 일치시킵니다
		} else {
			// 챗봇이 보이지 않는 경우, 열기 애니메이션 적용
			$chatbotContainer.addClass('visible bubble-in');
			setTimeout(() => {
				$chatbotContainer.removeClass('bubble-in'); // 애니메이션 완료 후 클래스 제거
			}, 300); // 애니메이션 시간과 일치시킵니다
			$('#chatbot-toggle').addClass('open'); // 버튼 애니메이션 추가
			$chatbotIcon.attr('class', 'fas fa-times fa-flip-horizontal'); // 버튼 아이콘 변경

			// 챗봇을 열 때 초기 상태로 설정
			$('#chatbot-messages').html(`
                <div class="message bot">
                    <div class="profile">
                        <img src="/images/bangitbot.png" alt="Bot Profile">
                    </div>
                    <div class="bubble">
                        원하시는 질문의 카테고리를 선택해주세요
                    </div>
                </div>
            `); // 초기 메시지 표시
			$('#chatbot-categories').show(); // 카테고리 버튼 표시
			$('#chatbot-subcategories').hide(); // 하위 카테고리 숨기기
		}
	});

	// 페이지 로드 후 카테고리 버튼 즉시 표시
	createCategoryButtons();
});


// 예제 JSON 데이터 (서버에서 로드될 데이터)
const data = {
	"categories": [
		{
			"id": 1,
			"name": "예약",
			"subcategories": [
				{ "id": 11, "name": "일반" },
				{ "id": 12, "name": "반려동물 동반" }
			]
		},
		{
			"id": 2,
			"name": "예약 취소",
			"subcategories": [
				{ "id": 21, "name": "예약 취소" },
				{ "id": 22, "name": "환불 문의" }
			]
		},
		{
			"id": 3,
			"name": "인기 숙소",
			"subcategories": [
				{ "id": 31, "name": "top1" },
				{ "id": 32, "name": "top2" },
				{ "id": 33, "name": "top3" }
			]
		},
		{
			"id": 4,
			"name": "기타",
			"subcategories": [
				{ "id": 41, "name": "상담원 연결" }
			]
		}
	]
};

// 카테고리 버튼 생성 함수
function createCategoryButtons() {
	const $categoriesContainer = $('#chatbot-categories');
	$categoriesContainer.empty(); // 기존 카테고리 버튼 제거
	data.categories.forEach(category => {
		// 각 카테고리 버튼을 생성하고 컨테이너에 추가
		$categoriesContainer.append(`<button class="category-button" data-category-id="${category.id}">${category.name}</button>`);
	});
	$categoriesContainer.show(); // 카테고리 버튼 표시
}

// 카테고리 버튼 클릭 이벤트 처리
$(document).on('click', '.category-button', function() {
	const categoryId = $(this).data('category-id'); // 클릭한 버튼의 카테고리 ID 가져오기
	const selectedCategory = data.categories.find(cat => cat.id === categoryId); // 선택한 카테고리 찾기

	// 초기 메시지 제거
	$('#chatbot-messages').empty();

	// 기존 카테고리 버튼 숨기기
	$('#chatbot-categories').hide();

	// 하위 카테고리 버튼 업데이트
	const $subcategoriesContainer = $('#chatbot-subcategories');
	$subcategoriesContainer.empty(); // 기존 하위 카테고리 제거
	selectedCategory.subcategories.forEach(subcategory => {
		// 각 하위 카테고리 버튼을 생성하고 컨테이너에 추가
		$subcategoriesContainer.append(`<button class="subcategory-button" data-subcategory-id="${subcategory.id}">${subcategory.name}</button>`);
	});

	// 하위 카테고리 버튼 표시
	$subcategoriesContainer.show();
});

// 하위 카테고리 버튼 클릭 이벤트 처리
$(document).on('click', '.subcategory-button', function() {
	// 서브카테고리 버튼 숨기기
	$('#chatbot-subcategories').hide();

	// 추가 동작 (예: 다음 단계 진행 등)
	$('#chatbot-input').focus(); // 입력 상자 포커스
});

// 전송 버튼 클릭 이벤트 처리
$('#chatbot-send').click(function() {
    const userInput = $('#chatbot-input').val(); // 입력 상자의 값 가져오기
    console.log('User Input:', userInput); // 콘솔에 입력 값 출력
    if (userInput.trim() === "") {
        return; // 입력 값이 비어있을 경우 처리하지 않음
    }
    
    // 사용자 메시지 추가
    $('#chatbot-messages').append(`
        <div class="message user">
            <div class="bubble">
                ${userInput}
            </div>
        </div>
    `);

    // 입력 상자 비우기
    $('#chatbot-input').val('');
});


// Enter 키를 눌렀을 때 메시지 전송
$('#chatbot-input').keyup(function(event) {
	if (event.key === "Enter") {
		$('#chatbot-send').click();
	}
});


// 버튼 애니메이션 추가 (버튼의 배경 스타일을 토글)
const chatbotToggle = document.querySelector("#chatbot-toggle");
chatbotToggle.addEventListener("click", () => {
	chatbotToggle.classList.toggle("chatbot-btn-background"); // 챗봇 버튼 배경 스타일 토글
});

/*웹소켓 서버 코드*/

// 전역 변수로 STOMP 클라이언트 선언
var stompClient = null;
// 연결 상태를 표시할 요소의 ID
var connectionStatusElementId = 'connection-status';

/**
 * WebSocket 연결 함수
 * SockJS를 사용하여 웹소켓 연결을 생성하고 STOMP 클라이언트를 초기화합니다.
 */
function connect() {
	// SockJS 객체 생성 (서버의 엔드포인트 URL 지정)
	var socket = new SockJS('/bangItBot');
	// STOMP 클라이언트 생성
	stompClient = Stomp.over(socket);
	// 연결 시도, 성공 시 onConnected 호출, 실패 시 onError 호출
	stompClient.connect({}, onConnected, onError);

	// 연결 시도 중임을 UI에 표시
	updateConnectionStatus('연결 중...');
}

/**
 * 연결 성공 시 호출되는 콜백 함수
 * 토픽 구독 및 사용자 참가 메시지 전송
 */
function onConnected() {
	// 공개 토픽 구독
	stompClient.subscribe('/topic/public', onMessageReceived);

	// 사용자 참가 알림 전송
	stompClient.send("/app/chat.addUser",
		{},
		JSON.stringify({ sender: username, type: 'JOIN' })
	);

	// 연결 성공을 UI에 표시
	updateConnectionStatus('연결됨');
	console.log('WebSocket 연결 성공');
}

/**
 * 연결 실패 시 호출되는 콜백 함수
 * @param {Object} error - 에러 객체
 */
function onError(error) {
	console.error('WebSocket 연결 실패:', error);
	// 연결 실패를 UI에 표시
	updateConnectionStatus('연결 실패');
}

/**
 * 메시지 전송 함수
 * @param {Event} event - 이벤트 객체
 */
function sendMessage(event) {
	var messageInput = document.getElementById('messageInput'); // 메시지 입력 필드 요소
	var messageContent = messageInput.value.trim();
	if (messageContent && stompClient) {
		var chatMessage = {
			sender: username,
			content: messageContent,
			type: 'CHAT'
		};
		// 메시지 전송
		stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
		messageInput.value = ''; // 입력 필드 초기화
	}
	event.preventDefault();
}

/**
 * 메시지 수신 시 호출되는 콜백 함수
 * @param {Object} payload - 수신된 메시지 페이로드
 */
function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
	// 여기에 메시지 표시 로직 구현
	console.log('메시지 수신:', message);
}

/**
 * 연결 상태 업데이트 함수
 * @param {string} status - 현재 연결 상태
 */
function updateConnectionStatus(status) {
	var statusElement = document.getElementById(connectionStatusElementId);
	if (statusElement) {
		statusElement.textContent = '연결 상태: ' + status;
	}
}

/**
 * 연결 상태 확인 함수
 * 현재 STOMP 클라이언트의 연결 상태를 확인하고 콘솔에 로그를 출력합니다.
 */
function checkConnectionStatus() {
	if (stompClient && stompClient.connected) {
		console.log('WebSocket 연결 상태: 연결됨');
		updateConnectionStatus('연결됨');
	} else {
		console.log('WebSocket 연결 상태: 연결되지 않음');
		updateConnectionStatus('연결되지 않음');
	}
}

// 페이지 로드 시 연결 및 주기적 연결 상태 확인
document.addEventListener('DOMContentLoaded', function() {
	connect();
	// 5초마다 연결 상태 확인
	setInterval(checkConnectionStatus, 5000);
});

/********/

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

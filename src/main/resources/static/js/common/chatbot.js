$(document).ready(function() {
	// WebSocket 클라이언트 초기화 변수
	let stompClient = null;
	// 사용자 ID (실제 구현에서는 로그인한 사용자의 ID를 사용해야 함)
	const userId = 'User' + Math.floor(Math.random() * 1000);

	// WebSocket 연결을 설정하고 STOMP 클라이언트를 초기화하는 함수
	function connectWebSocket() {
		const socket = new SockJS('/bangItBot'); // WebSocket 엔드포인트
		stompClient = Stomp.over(socket);

		const headers = {};
		const token = $("meta[name='_csrf']").attr("content");
		const header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;

		stompClient.connect(headers, onConnected, onError);
	}

	// WebSocket 연결 성공 시 호출되는 함수
	function onConnected() {
		console.log('WebSocket 연결 성공');
		// 봇 응답을 수신하기 위한 구독 설정
		stompClient.subscribe('/topic/bot/' + userId, onMessageReceived);
	}

	// WebSocket 연결 오류 시 호출되는 함수
	function onError(error) {
		console.error('WebSocket 연결 오류:', error);
	}

	// 메시지를 서버로 전송하는 함수
	function sendMessage(message) {
		if (stompClient) {
			const chatMessage = {
				content: message,
				key: userId,
				userId: userId
			};
			// 서버로 메시지 전송
			stompClient.send("/app/query", {}, JSON.stringify(chatMessage));

			// 사용자 메시지를 화면에 표시
			displayMessage({ sender: 'user', content: message });
		}
	}

	// 서버로부터 메시지를 수신할 때 호출되는 함수
	function onMessageReceived(payload) {
		try {
			const message = JSON.parse(payload.body);
			displayMessage({ sender: 'bot', content: message.content });
		} catch (error) {
			console.error('메시지 파싱 오류:', error);
			displayMessage({ sender: 'bot', content: '오류가 발생했습니다. 다시 시도해 주세요.' });
		}
	}

	// 화면에 메시지를 표시하는 함수
	function displayMessage(message) {
		const $messagesContainer = $('#chatbot-messages');
		const messageClass = message.sender === 'bot' ? 'bot' : 'user';

		// 줄바꿈을 유지하면서 HTML 이스케이프 처리
		let content = message.content.replace(/\n/g, '<br>').replace(/</g, '&lt;').replace(/>/g, '&gt;');

		// 링크를 클릭 가능한 HTML로 변환
		content = content.replace(/(https?:\/\/[^\s]+|\/\w+\/[\w-]+)/g, '<a href="$1" target="_blank">$1</a>');

		// 메시지 HTML 구성
		let messageHTML = '';
		if (messageClass === 'bot') {
			messageHTML = `
        <div class="message ${messageClass}">
            <div class="profile">
                <img src="/images/bangitbot.png" alt="Bot Profile">
            </div>
            <div class="bubble">
                ${content}
            </div>
        </div>
        `;
		} else {
			messageHTML = `
        <div class="message ${messageClass}">
            <div class="bubble">
                ${content}
            </div>
        </div>
        `;
		}

		// 메시지를 채팅창에 추가하고 스크롤
		$messagesContainer.append(messageHTML);
		$messagesContainer.scrollTop($messagesContainer[0].scrollHeight);
	}

	// 채팅봇 토글 버튼 이벤트 핸들러
	$(document).off('click', '#chatbot-toggle').on('click', '#chatbot-toggle', function(e) {
		e.preventDefault();
		const $chatbotContainer = $('#chatbot-container');
		const $chatbotIcon = $('#chatbot-toggle i');
		const $messagesContainer = $('#chatbot-messages');

		if ($chatbotContainer.is(':visible')) {
			// 채팅창 숨기기
			$chatbotContainer.stop().fadeOut(300, function() {
				$messagesContainer.empty();
				// 기본 메시지 추가
				$messagesContainer.append(`
                <div class="message bot">
                    <div class="profile">
                        <img src="/images/bangitbot.png" alt="Bot Profile">
                    </div>
                    <div class="bubble">
                        무엇을 도와드릴까요?
                    </div>
                </div>
                `);
			});
			$(this).removeClass('open');
			$chatbotIcon.attr('class', 'fas fa-comment-dots fa-flip-horizontal');
		} else {
			// 채팅창 보이기
			$chatbotContainer.stop().fadeIn(300);
			$(this).addClass('open');
			$chatbotIcon.attr('class', 'fas fa-times fa-flip-horizontal');
		}
	});

	// 채팅봇 전송 버튼 클릭 이벤트 핸들러
	$('#chatbot-send').click(function() {
		const message = $('#chatbot-input').val().trim();
		if (message) {
			sendMessage(message);
			$('#chatbot-input').val('');
		}
	});

	// 채팅봇 입력 필드 Enter 키 이벤트 핸들러
	$('#chatbot-input').keypress(function(e) {
		if (e.which == 13) { // Enter 키 코드
			$('#chatbot-send').click(); // 전송 버튼 클릭 이벤트 트리거
		}
	});

	// 페이지 로드 시 채팅창 초기 상태 설정
	$('#chatbot-container').hide();

	// WebSocket 연결 시작
	connectWebSocket();
});
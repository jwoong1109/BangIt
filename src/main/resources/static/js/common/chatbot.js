// chatbot.js
document.addEventListener('DOMContentLoaded', function() {
	let stompClient = null;
	const userId = 'User' + Math.floor(Math.random() * 1000);
	let isChatbotVisible = false; // 챗봇 가시성 상태를 추적하는 변수

	// WebSocket 연결 함수
	function connectWebSocket() {
		const socket = new SockJS('/bangItBot');
		stompClient = Stomp.over(socket);

		const headers = {};
		const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
		const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
		headers[header] = token;

		stompClient.connect(headers, onConnected, onError);
	}

	function onConnected() {
		console.log('WebSocket 연결 성공');
		stompClient.subscribe('/topic/responses', onMessageReceived);
	}

	function onError(error) {
		console.error('WebSocket 연결 오류:', error);
		displayMessage({ sender: 'bot', content: '죄송합니다. 서버와의 연결에 문제가 있습니다. 잠시 후 다시 시도해 주세요.' });
	}

	function sendMessage(message) {
		if (stompClient && stompClient.connected) {
			const chatMessage = {
				content: message,
				key: userId,
				userId: userId
			};
			console.log('Sending message:', chatMessage); // 보내는 메시지를 로깅
			stompClient.send("/app/query", {}, JSON.stringify(chatMessage));
			displayMessage({ sender: 'user', content: message });
		} else {
			console.error('WebSocket is not connected');
			displayMessage({ sender: 'bot', content: '서버와의 연결이 끊어졌습니다. 페이지를 새로고침 해주세요.' });
		}
	}

	function onMessageReceived(payload) {
		console.log('Raw message received:', payload);
		try {
			const message = JSON.parse(payload.body);
			console.log('Parsed message:', message);
			if (message.content) {
				console.log('Displaying message:', message.content);
				displayMessage({ sender: 'bot', content: message.content });
			} else {
				console.error('Invalid message format:', message);
			}
		} catch (error) {
			console.error('Error parsing message:', error);
		}
	}

	function displayMessage(message) {
		const messagesContainer = document.getElementById('chatbot-messages');
		const messageClass = message.sender === 'bot' ? 'bot' : 'user';

		console.log('Displaying message:', message); // 표시할 메시지를 로깅
		let content = message.content.replace(/\n/g, '<br>').replace(/</g, '&lt;').replace(/>/g, '&gt;');
		content = content.replace(/(https?:\/\/[^\s]+|\/\w+\/[\w-]+)/g, '<a href="$1" target="_blank">$1</a>');

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

		messagesContainer.insertAdjacentHTML('beforeend', messageHTML);
		messagesContainer.scrollTop = messagesContainer.scrollHeight;
	}

	// 챗봇 토글 함수
	function toggleChatbot() {
		const chatbotContainer = document.getElementById('chatbot-container');
		const chatbotIcon = document.querySelector('#chatbot-toggle i');
		const chatbotToggle = document.getElementById('chatbot-toggle');

		if (isChatbotVisible) {
			chatbotContainer.classList.remove('visible');
			setTimeout(() => {
				chatbotContainer.style.display = 'none';
			}, 300);
			chatbotToggle.classList.remove('open');
			chatbotIcon.className = 'fas fa-comment-dots fa-flip-horizontal';
		} else {
			chatbotContainer.style.display = 'block';
			setTimeout(() => {
				chatbotContainer.classList.add('visible');
			}, 10);
			chatbotToggle.classList.add('open');
			chatbotIcon.className = 'fas fa-times fa-flip-horizontal';
		}

		isChatbotVisible = !isChatbotVisible; // 상태 토글
	}

	// 이벤트 리스너 설정
	document.getElementById('chatbot-toggle').addEventListener('click', function(e) {
		e.preventDefault();
		toggleChatbot();
	});

	document.getElementById('chatbot-send').addEventListener('click', function() {
		const input = document.getElementById('chatbot-input');
		const message = input.value.trim();
		if (message) {
			sendMessage(message);
			input.value = '';
		}
	});

	document.getElementById('chatbot-input').addEventListener('keypress', function(e) {
		if (e.key === 'Enter') {
			document.getElementById('chatbot-send').click();
		}
	});

	// 초기 상태 설정
	document.getElementById('chatbot-container').style.display = 'none';

	// WebSocket 연결 시작
	connectWebSocket();
});
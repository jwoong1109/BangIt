$(document).ready(function() {
	let stompClient = null;
	const username = 'User' + Math.floor(Math.random() * 1000);

	function connectWebSocket() {
		const socket = new SockJS('/bangItBot');
		stompClient = Stomp.over(socket);

		const headers = {};
		const token = $("meta[name='_csrf']").attr("content");
		const header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;

		stompClient.connect({}, onConnected, onError);
	}

	function onConnected() {
		console.log('WebSocket 연결 성공');
		stompClient.subscribe('/topic/public', onMessageReceived);
		stompClient.subscribe('/user/' + username + '/private', onPrivateMessage);
		stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
		updateConnectionStatus(true);
	}

	function onError(error) {
		console.error('WebSocket 연결 오류:', error);
		updateConnectionStatus(false);
	}

	function updateConnectionStatus(isConnected) {
		$('#connection-status').text(isConnected ? '연결됨' : '연결 끊김')
			.css('color', isConnected ? 'green' : 'red');
	}

	function sendMessage(message) {
		if (stompClient) {
			const chatMessage = {
				sender: username,
				content: message,
				type: 'CHAT'
			};
			stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
		}
	}

	function onMessageReceived(payload) {
		const message = JSON.parse(payload.body);
		displayMessage(message);
	}

	function onPrivateMessage(payload) {
		const message = JSON.parse(payload.body);
		displayMessage(message, true);
	}

	function displayMessage(message, isPrivate = false) {
		const $messagesContainer = $('#chatbot-messages');
		const messageClass = message.sender === 'bot' ? 'bot' : 'user';
		const privateClass = isPrivate ? 'private' : '';

		const messageHTML = `
            <div class="message ${messageClass} ${privateClass}">
                <div class="bubble">${message.content}</div>
            </div>
        `;

		$messagesContainer.append(messageHTML);
		$messagesContainer.scrollTop($messagesContainer[0].scrollHeight);
	}

	$('#chatbot-toggle').click(function() {
		const $chatbotContainer = $('#chatbot-container');
		const $chatbotIcon = $('#chatbot-toggle i');

		if ($chatbotContainer.hasClass('visible')) {
			$chatbotContainer.removeClass('bubble-in').addClass('bubble-out');
			setTimeout(() => {
				$chatbotContainer.removeClass('visible bubble-out');
				$(this).removeClass('open');
				$chatbotIcon.attr('class', 'fas fa-comment-dots fa-flip-horizontal');
			}, 300);
		} else {
			$chatbotContainer.addClass('visible bubble-in').removeClass('bubble-out');
			$(this).addClass('open');
			$chatbotIcon.attr('class', 'fas fa-times fa-flip-horizontal');
		}
	});

	$('#chatbot-send').click(function() {
		const message = $('#chatbot-input').val().trim();
		if (message) {
			sendMessage(message);
			$('#chatbot-input').val('');
		}
	});

	$('#chatbot-input').keypress(function(e) {
		if (e.which == 13) {
			$('#chatbot-send').click();
		}
	});

	// 웹소켓 연결 시작
	connectWebSocket();
});
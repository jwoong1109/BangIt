document.addEventListener('DOMContentLoaded', () => {
	// .nav-menu-area 클래스가 있는 <a> 태그들을 모두 선택합니다.
	const menuLinks = document.querySelectorAll('.nav-menu-area a');
	// .dash 클래스를 가진 요소를 선택합니다.
	const dash = document.querySelector('.dash');

	// 모든 메뉴 링크에 클릭 이벤트 리스너를 추가합니다.
	menuLinks.forEach(link => {
		link.addEventListener('click', (event) => {
			// 기본 링크 클릭 동작(페이지 이동 등)을 방지합니다.
			event.preventDefault();
			// 클릭한 링크의 data-content 속성 값을 가져옵니다.
			const content = link.getAttribute('data-content');
			// 콘텐츠를 슬라이드 아웃 애니메이션과 함께 숨기고, 애니메이션이 완료된 후 콘텐츠를 로드합니다.
			changeContent(content);
		});
	});

	// 페이지가 로드될 때 기본 콘텐츠를 'list'로 설정하여 로드합니다.
	loadContent('list');

	// 콘텐츠를 변경하는 함수입니다.
	function changeContent(content) {
		// 슬라이드 아웃 애니메이션 적용
		dash.classList.add('slide-out');

		// 애니메이션 완료 후 콘텐츠 변경
		dash.addEventListener('animationend', function onAnimationEnd() {
			// 콘텐츠 로드
			loadContent(content);
			// 슬라이드 아웃 클래스 제거
			dash.classList.remove('slide-out');
			// 슬라이드 인 애니메이션 적용
			dash.classList.add('slide-in');

			// 애니메이션이 끝난 후, slide-in 클래스 제거
			dash.addEventListener('animationend', () => {
				dash.classList.remove('slide-in');
			}, { once: true });

			// 기존의 animationend 이벤트 리스너 제거
			dash.removeEventListener('animationend', onAnimationEnd);
		}, { once: true });
	}

	// 콘텐츠를 로드하는 함수입니다.
	function loadContent(content) {
		switch (content) {
			// 'list' 콘텐츠를 로드합니다.
			case 'list':
				dash.innerHTML = '<h2>판매자 목록</h2><p>판매자 목록에 대한 내용...</p>';
				break;
			// 'approve' 콘텐츠를 로드합니다.
			case 'approve':
				dash.innerHTML = '<h2>판매자 승인</h2><p>판매자 승인에 대한 내용...</p>';
				break;
			// 'edit' 콘텐츠를 로드합니다.
			case 'edit':
				dash.innerHTML = '<h2>판매자 수정</h2><p>판매자 수정에 대한 내용...</p>';
				break;
			// 'delete' 콘텐츠를 로드합니다.
			case 'delete':
				dash.innerHTML = '<h2>판매자 삭제</h2><p>판매자 삭제에 대한 내용...</p>';
				break;
			// 'activity' 콘텐츠를 로드합니다.
			case 'activity':
				dash.innerHTML = '<h2>활동 내역 확인</h2><p>활동 내역 확인에 대한 내용...</p>';
				break;
			// 'permission' 콘텐츠를 로드합니다.
			case 'permission':
				dash.innerHTML = '<h2>권한 관리&계정 수정</h2><p>권한 관리 및 계정 수정에 대한 내용...</p>';
				break;
			// 위의 경우에 해당하지 않는 경우 기본 메시지를 표시합니다.
			default:
				dash.innerHTML = '<p>내용이 없습니다.</p>';
		}
	}
});

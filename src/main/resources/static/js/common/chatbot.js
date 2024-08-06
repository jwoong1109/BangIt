$(document).ready(function() {
    // 챗봇 토글 버튼 클릭 시 챗봇 컨테이너의 표시 상태를 토글합니다
    $('#chatbot-toggle').click(function() {
        const $chatbotContainer = $('#chatbot-container');
        const $chatbotIcon = $('#chatbot-icon');

        if ($chatbotContainer.hasClass('visible')) {
            // 챗봇이 이미 보이고 있는 경우, 닫기 애니메이션 적용
            $chatbotContainer.removeClass('bubble-in').addClass('bubble-out');
            setTimeout(() => {
                $chatbotContainer.removeClass('visible bubble-out'); // 애니메이션 완료 후 숨기기
                $('#chatbot-toggle').removeClass('open'); // 버튼 애니메이션 원래 상태로 복원
                $chatbotIcon.attr('src', 'button.png'); // 버튼 이미지 원래 상태로 복원
            }, 300); // 애니메이션 시간과 일치시킵니다
        } else {
            // 챗봇이 보이지 않는 경우, 열기 애니메이션 적용
            $chatbotContainer.addClass('visible bubble-in');
            setTimeout(() => {
                $chatbotContainer.removeClass('bubble-in'); // 애니메이션 완료 후 클래스 제거
            }, 300); // 애니메이션 시간과 일치시킵니다
            $('#chatbot-toggle').addClass('open'); // 버튼 애니메이션 추가
            $chatbotIcon.attr('src', 'close.png'); // 버튼 이미지 변경
        }
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
        $('#chatbot-messages').append(`<div><strong>사용자:</strong> ${userInput}</div>`); // 사용자 메시지 추가
        $('#chatbot-input').val(''); // 입력 상자 비우기
    });

    // 초기 메시지 표시
    $('#chatbot-messages').html('<p>원하시는 질문의 카테고리를 선택해주세요</p>');

    // 페이지 로드 후 카테고리 버튼 즉시 표시
    createCategoryButtons();
});

// 버튼 애니메이션 추가 (버튼의 배경 스타일을 토글)
const chatbotToggle = document.querySelector("#chatbot-toggle");
chatbotToggle.addEventListener("click", () => {
    document.body.classList.toggle("chatbot-btn-background"); // 챗봇 버튼 배경 스타일 토글
});

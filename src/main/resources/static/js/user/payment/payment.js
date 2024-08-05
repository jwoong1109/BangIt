$(document).ready(function() {
    // 모달 관련 요소 선택
    const $modal = $("#reservation-modal");
    const $openModalBtn = $(".pay-btn"); // 결제하기 버튼
    const $closeModalBtn = $(".close-btn");
    const $cancelBtn = $("#cancel-btn");

    // 모달 열기
    $openModalBtn.on("click", function() {
        $modal.show();
    });

    // 모달 닫기
    $closeModalBtn.on("click", function() {
        $modal.hide();
    });

    $cancelBtn.on("click", function() {
        $modal.hide();
    });

    // 모달 외부 클릭 시 닫기
    $(window).on("click", function(event) {
        if ($(event.target).is($modal)) {
            $modal.hide();
        }
    });
});
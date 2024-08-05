document.addEventListener("DOMContentLoaded", function() {
    // jQuery UI Datepicker 초기화
    $("#checkin-date").datepicker({
        dateFormat: "dd MM yy",
        minDate: 0 // 오늘 날짜 이후만 선택 가능
    });

    $("#checkout-date").datepicker({
        dateFormat: "dd MM yy",
        minDate: 1 // 오늘 날짜 이후만 선택 가능
    });

    // GSAP과 ScrollTrigger 플러그인 로드
    gsap.registerPlugin(ScrollTrigger);

    // 호텔 카드 애니메이션 설정
    gsap.from(".hotel-card", {
        scrollTrigger: {
            trigger: ".hotels",  // 애니메이션이 시작될 섹션
            start: "top 80%",    // 섹션이 화면에 80% 들어올 때 시작
            toggleActions: "play none none none"
        },
        opacity: 0,  // 처음에는 투명
        x: -50,      // 왼쪽에서 슬라이드
        duration: 1, // 애니메이션 지속 시간
        stagger: 0.3 // 각 카드마다 순차적으로 애니메이션 실행
    });

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

});

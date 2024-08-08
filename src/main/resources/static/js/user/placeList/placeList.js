document.addEventListener("DOMContentLoaded", function() {


	$.datepicker.setDefaults($.datepicker.regional['ko']);

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
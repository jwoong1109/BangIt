$(document).ready(function() {
    $('.reservation-item').click(function() {
        var reservationId = $(this).data('reservation-id');
        if (reservationId === undefined) {
            console.error("Reservation ID is undefined");
            return;
        }

        $.ajax({
            url: '/reservationDetails/' + reservationId,
            type: 'GET',
            success: function(response) {
                $('#reservation-details').html(response);
            },
            error: function(xhr, status, error) {
                console.error("An error occurred: " + error);
                console.error("Status: " + status);
                console.error("Response: " + xhr.responseText);
                alert("상세 정보를 불러오는 데 실패했습니다.");
            }
        });
    });

	function formatDate(dateString) {
		var date = new Date(dateString);
		return date.getFullYear() + '-' +
			('0' + (date.getMonth() + 1)).slice(-2) + '-' +
			('0' + date.getDate()).slice(-2);
	}

	function calculateNights(checkIn, checkOut) {
		var checkInDate = new Date(checkIn);
		var checkOutDate = new Date(checkOut);
		return Math.round((checkOutDate - checkInDate) / (1000 * 60 * 60 * 24));
	}

	function formatPrice(price) {
		return new Intl.NumberFormat('ko-KR').format(price);
	}
});

$(document).ready(function() {
	
	

	// 메일쓰기 버튼 클릭 이벤트
	const writeMailBtn = document.querySelector('.write-mail-btn');
	const mailWritModal = document.getElementById('mailWritModal');
	const writModalClose = mailWritModal.querySelector('.WritModalclose');

	writeMailBtn.addEventListener('click', function() {
		mailWritModal.style.display = 'block';
	});

	writModalClose.addEventListener('click', function() {
		mailWritModal.style.display = 'none';
	});

	// 모달 외부 클릭 시 닫기
	window.addEventListener('click', function(event) {
		if (event.target === mailWritModal) {
			mailWritModal.style.display = 'none';
		}
	});



	
	//상세 메일 비동기 요청
    $('.mail-detail-link').click(function(e) {
        e.preventDefault();
        var mailId = $(this).data('mail-id');
        
        $.ajax({
            url: '/admin/mail/detail/' + mailId,
            type: 'GET',
            success: function(response) {
                $('#mailDetailContent').html(response);
                $('#mailDetailModal').show();
            },
            error: function(xhr, status, error) {
                alert('메일 상세 정보를 불러오는데 실패했습니다.');
            }
        });
    });
    $('.close').click(function() {
        $('#mailDetailModal').hide();
    });

    $(window).click(function(event) {
        if (event.target == $('#mailDetailModal')[0]) {
            $('#mailDetailModal').hide();
        }
    });
});
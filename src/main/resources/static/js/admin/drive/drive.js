document.addEventListener('DOMContentLoaded', function() {
    
    
    //csrf 토큰 
    const token = $("meta[name='_csrf']").attr("content")
	const header = $("meta[name='_csrf_header']").attr("content");
    
    
    //비동기 요청을 위한 변수 
	const downloadButtons = document.querySelectorAll('.downloadBtn');
	const deleteButtons = document.querySelectorAll('.deleteBtn');
	
	
	//파일 업로드 팝업을 위한 변수
	const uploadBtn = document.querySelector('.uploadBtn');
  	const fileUploadWrap = document.querySelector('.file-uplod-Wrap');
  	const uploadCancelBtn = document.querySelector('.upload-cancle-btn');
	const fileInput = document.querySelector('input[type="file"]');
	     


	downloadButtons.forEach(button => {
		button.addEventListener('click', function() {
	
      		let fileName = this.getAttribute('data-file-name');
			const fileId = this.getAttribute('data-file-id');

			downloadFile(fileId, fileName);
		});
	});

	deleteButtons.forEach(button => {
		button.addEventListener('click', function() {
			const fileId = this.getAttribute('data-file-id');
			deleteFile(fileId);
		});
	});

	function downloadFile(fileId, fileName) {
		console.log('Initiating download for:', fileName);
		fetch(`/admin/drive/download/${fileId}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
			},
		})
			.then(response => {
				if (!response.ok) throw new Error('Network response was not ok.');
				return response.blob();
			})
			.then(blob => {
				const url = window.URL.createObjectURL(blob);
				const a = document.createElement('a');
				a.style.display = 'none';
				a.href = url;
				a.download = fileName;
				document.body.appendChild(a);
				a.click();
				window.URL.revokeObjectURL(url);
				console.log('Download initiated for:', fileName);
			})
			.catch(error => {
				console.error('Download error:', error);
				alert('파일 다운로드 중 오류가 발생했습니다.');
			});
	}


	function deleteFile(fileId) {
		if (confirm('정말로 이 파일을 삭제하시겠습니까?')) {
			fetch(`/admin/drive/delete/${fileId}`, {
				method: 'DELETE',
				headers: {
					'Content-Type': 'application/json',
					[header]: token

				},
			})
				.then(response => {
					alert('파일이 성공적으로 삭제되었습니다.');
					location.reload(); // 페이지 새로고침

				})

				.catch(error => console.error('Error:', error));
		}
	}
	
	
		  // 파일 업로드 버튼 클릭 이벤트
	  uploadBtn.addEventListener('click', function() {
	    fileUploadWrap.style.display = 'block';
	    fileInput.value = '';
	  });
	
	  // 업로드 취소 버튼 클릭 이벤트
	  uploadCancelBtn.addEventListener('click', function() {
	    fileUploadWrap.style.display = 'none';
	    fileInput.value = ''; // 파일 입력 필드 리셋
	  });
	









});
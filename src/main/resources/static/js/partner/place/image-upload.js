function submitForm() {
	const form = document.getElementById('placeForm');
	form.submit();
}

function fileupload(input) {
	const files = input.files;
	if (files.length < 1) {
		console.log("파일이 선택되지 않았어요");
		return;
	}

	const fileType = files[0].type;
	if (!fileType.startsWith('image/')) {
		alert("이미지 파일이 아닙니다.");
		input.value = '';
		return;
	}

	const fileSize = files[0].size;
	if (fileSize > 2 * 1024 * 1024) {
		alert("파일용량제한: 2MB이내의 파일을 사용하세요:" + fileSize);
		input.value = '';
		return;
	}

	// 파일 미리보기
	const reader = new FileReader();
	reader.onload = function(e) {
		let previewDiv, plusIcon;
		if (input.id === 'mainImage') {
			previewDiv = document.getElementById('mainImagePreview');
		} else {
			const label = input.closest('.btn-file');
			previewDiv = label.querySelector('.image-preview-container');
			plusIcon = label.querySelector('.plus-icon');
		}

		previewDiv.innerHTML = `<img src="${e.target.result}" alt="Image preview" style="max-width: 100%; max-height: 200px;">`;
		previewDiv.style.display = 'block';
		if (plusIcon) plusIcon.style.display = 'none';
	}
	reader.readAsDataURL(files[0]);

	var formData = new FormData();
	formData.append("file", files[0]);

	function fileupload(input) {
		// ... 기존 코드 ...

		uploadImage("/partner/uploadImage", formData)
			.then(result => {
				console.log(result);
				const url = result.url;
				const bucketKey = result.bucketKey;
				const orgName = result.orgName;

				// hidden 필드 업데이트
				let bucketKeyInput, orgNameInput;
				if (input.id === 'mainImage') {
					bucketKeyInput = document.querySelector('input[name="mainImageBucketKey"]');
					orgNameInput = document.querySelector('input[name="mainImageOrgName"]');
				} else {
					bucketKeyInput = input.closest('.btn-file').querySelector('input[name="additionalImageBucketKeys"]');
					orgNameInput = input.closest('.btn-file').querySelector('input[name="additionalImageOrgNames"]');

					// 기존 값에 새 값 추가 (쉼표로 구분)
					bucketKeyInput.value = bucketKeyInput.value ? bucketKeyInput.value + ',' + bucketKey : bucketKey;
					orgNameInput.value = orgNameInput.value ? orgNameInput.value + ',' + orgName : orgName;
				}

				if (bucketKeyInput) bucketKeyInput.value = bucketKey;
				if (orgNameInput) orgNameInput.value = orgName;
			})
			.catch(error => {
				alert("파일업로드 실패! : " + error);
			});
	}

}
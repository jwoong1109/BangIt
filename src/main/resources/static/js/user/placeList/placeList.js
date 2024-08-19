document.addEventListener("DOMContentLoaded", function() {
	//웹 크롤링을 위한 변수
	let places = document.querySelectorAll('.places');
	let yeogiEottae = document.querySelector('#yeogi-eottae');
	let priceComparison = document.querySelector('.price-comparison');


	//각 숙소를 배열로 받아 숙소를 호버 했을때 크롤링을 위한 비동기 요청 보내기
	places.forEach(function(place) {

		place.addEventListener('mouseenter', function() {

			//해당 배열 안에 있는 숙소 이름 가져오기
			let placeName = place.querySelector('.place-name').textContent.trim(); arguments

		     fetch(`/search/places/scrape/${encodeURIComponent(placeName)}`, {
                method: 'GET'
            })
       .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text().then(text => {
                    console.log('Raw server response:', text);  // 디버깅용
                    return JSON.parse(text);
                });
            })
            .then(data => {
                if (data && Object.keys(data).length > 0) {
                    updateYeogiEottaeInfo(data);
                    priceComparison.style.display = 'block';  // 정보가 있으면 표시
                } else {
                    priceComparison.style.display = 'none';  // 정보가 없으면 숨김
                }
            })
            .catch(error => {
               
                priceComparison.style.display = 'none';  // 에러 발생 시 숨김
            });
        });

        
    });
    function updateYeogiEottaeInfo(data) {
        // 이미지 업데이트
        yeogiEottae.querySelector('img').src = data.imageUrl;
        yeogiEottae.querySelector('img').alt = data.name;

        // 정보 업데이트
        let infoList = yeogiEottae.querySelectorAll('ul li');
        infoList[0].textContent = data.grade;
        infoList[1].textContent = data.name;
        infoList[2].textContent = data.location;
        infoList[3].textContent = data.price + '원';

        // 링크 업데이트 (옵션)
        yeogiEottae.querySelector('a').href = data.url;
    }
});
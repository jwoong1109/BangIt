document.addEventListener("DOMContentLoaded", function() {
	//웹 크롤링을 위한 변수
	let places = document.querySelectorAll('.places');
	let yanolja = document.querySelector('#yanolja');
	let yeogiEottae = document.querySelector('#yeogi-eottae');


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
                return response.text();
            })
            .then(data => console.log(data))
            .catch(error => console.error('Error:', error));


		});


	}); //끝


});
var mapContainer = document.getElementById('map'), // 지도를 표시할 div
	mapOption = {
		center: new daum.maps.LatLng(37.537187, 127.005476), // 지도의 중심좌표
		level: 5 // 지도의 확대 레벨
	};

//지도를 미리 생성
var map = new daum.maps.Map(mapContainer, mapOption);
//주소-좌표 변환 객체를 생성
var geocoder = new daum.maps.services.Geocoder();
//마커를 미리 생성
var marker = new daum.maps.Marker({
	position: new daum.maps.LatLng(37.537187, 127.005476),
	map: map
});


function address_execDaumPostcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			var addr = data.address; // 최종 주소 변수

			// 주소 정보를 해당 필드에 넣는다.
			document.getElementById("detailed-address").value = addr;
			// 주소로 상세 정보를 검색
			geocoder.addressSearch(data.address, function(results, status) {
				// 정상적으로 검색이 완료됐으면
				if (status === daum.maps.services.Status.OK) {

					var result = results[0]; //첫번째 결과의 값을 활용

					// 해당 주소에 대한 좌표를 받아서
					var coords = new daum.maps.LatLng(result.y, result.x);
					// 지도를 보여준다.

					// 위도와 경도 값을 hidden 필드에 설정
					document.getElementById("latitude").value = coords.getLat();
					document.getElementById("longitude").value = coords.getLng();

					mapContainer.style.display = "block";
					map.relayout();
					// 지도 중심을 변경한다.
					map.setCenter(coords);
					// 마커를 결과값으로 받은 위치로 옮긴다.
					marker.setPosition(coords)
				}
			});
		}
	}).open();
}

            /*<![CDATA[*/
            var mapContainer = document.getElementById('map');
            var mapOption = {
                center: new kakao.maps.LatLng(37.537187, 127.005476),
                level: 5
            };

            var map = new kakao.maps.Map(mapContainer, mapOption);
            var geocoder = new kakao.maps.services.Geocoder();
            var marker = new kakao.maps.Marker({
                map: map
            });

            var address = /*[[${place.detailedAddress}]]*/ '기본 주소';
            var lat = /*[[${place.latitude}]]*/ 37.537187;
            var lng = /*[[${place.longitude}]]*/ 127.005476;

            if (lat && lng) {
                var coords = new kakao.maps.LatLng(lat, lng);
                map.setCenter(coords);
                marker.setPosition(coords);
            } else {
                geocoder.addressSearch(address, function(results, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        var result = results[0];
                        var coords = new kakao.maps.LatLng(result.y, result.x);
                        map.setCenter(coords);
                        marker.setPosition(coords);
                    }
                });
            }
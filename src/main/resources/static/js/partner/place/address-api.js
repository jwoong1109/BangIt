var mapContainer, map, geocoder, marker;

function initializeMap() {
    mapContainer = document.getElementById('map');
    var mapOption = {
        center: new kakao.maps.LatLng(37.537187, 127.005476),
        level: 5
    };

    map = new kakao.maps.Map(mapContainer, mapOption);
    geocoder = new kakao.maps.services.Geocoder();
    marker = new kakao.maps.Marker({
        map: map
    });

    // 기존 장소 정보가 있다면 표시
    var address = document.getElementById('detailed-address').value;
    var lat = document.getElementById('latitude').value;
    var lng = document.getElementById('longitude').value;

    if (lat && lng) {
        var coords = new kakao.maps.LatLng(lat, lng);
        displayMap(coords);
    } else if (address) {
        geocoder.addressSearch(address, function(results, status) {
            if (status === kakao.maps.services.Status.OK) {
                var coords = new kakao.maps.LatLng(results[0].y, results[0].x);
                displayMap(coords);
            }
        });
    }
}

function displayMap(coords) {
    map.setCenter(coords);
    marker.setPosition(coords);
    mapContainer.style.display = "block";
    map.relayout();
}

function address_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = data.address;
            document.getElementById("detailed-address").value = addr;
            
            geocoder.addressSearch(addr, function(results, status) {
                if (status === kakao.maps.services.Status.OK) {
                    var coords = new kakao.maps.LatLng(results[0].y, results[0].x);
                    
                    document.getElementById("latitude").value = coords.getLat();
                    document.getElementById("longitude").value = coords.getLng();
                    
                    displayMap(coords);
                }
            });
        }
    }).open();
}

// 페이지 로드 시 지도 초기화
document.addEventListener('DOMContentLoaded', initializeMap);
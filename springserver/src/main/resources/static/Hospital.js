// 현재 위치 얻기 (HTML5 Geolocation API)
function getUserLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var latitude = position.coords.latitude;  // 위도
            var longitude = position.coords.longitude; // 경도

            // 위치를 서버로 전달 (예: POST 요청)
            fetch('/map/getHospital', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    latitude: latitude,
                    longitude: longitude
                })
            })
                .then(response => response.json()) // 서버에서 JSON 응답 받기
                .then(data => {
                    console.log('병원 목록:', data);  // 서버에서 받은 병원 목록 출력
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }, function(error) {
            console.error('위치 정보를 얻을 수 없습니다.', error);  // 위치 정보 오류 처리
        });
    } else {
        alert("이 브라우저는 위치 정보를 지원하지 않습니다.");  // Geolocation 지원하지 않는 경우
    }
}

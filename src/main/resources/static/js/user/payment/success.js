/**
 * 결제 완료 후 서버에 결제 정보 확인 및 저장 요청을 보내는 스크립트
 */

// URL 쿼리 파라미터에서 결제 관련 정보를 추출합니다.
// 결제 요청 시 보낸 데이터와 동일한지 확인하여 클라이언트에서 결제 금액을 조작하는 행위를 방지합니다.
const urlParams = new URLSearchParams(window.location.search);
const paymentKey = urlParams.get("paymentKey"); // 결제 키 (토스에서 제공)
const orderId = urlParams.get("orderId");       // 주문 ID
const amount = urlParams.get("amount");         // 결제 금액

// 결제 확인을 위한 함수
async function confirm() {
    // 서버에 보낼 결제 정보 데이터를 객체로 준비합니다.
    const requestData = {
        paymentKey: paymentKey,
        orderId: orderId,
        amount: amount,
    };

    // HTML 메타 태그에서 CSRF 토큰과 헤더 이름을 가져옵니다.
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // 결제 확인을 위해 서버에 POST 요청을 보냅니다.
    const response = await fetch("/confirm", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",  // 요청 본문이 JSON 형식임을 나타냅니다.
            [csrfHeader]: csrfToken // CSRF 토큰을 요청 헤더에 추가하여 보안을 강화합니다.
        },
        body: JSON.stringify(requestData), // JSON 형식으로 데이터를 직렬화하여 전송합니다.
    });

    // 서버로부터의 응답을 JSON으로 파싱합니다.
    const json = await response.json();

    // 응답이 실패인 경우 (HTTP 상태 코드가 200번대가 아닌 경우)
    if (!response.ok) {
        // 결제 실패 시 처리할 비즈니스 로직을 구현합니다.
        console.log(json);
        // 실패 페이지로 리다이렉션합니다.
        window.location.href = `/fail?message=${json.message}&code=${json.code}`;
    }

    // 결제가 성공한 경우
    // 결제 성공 시 처리할 비즈니스 로직을 구현합니다.
    console.log(json);
}

// DOMContentLoaded 이벤트가 발생하면 confirm 함수가 실행됩니다.
// 페이지 로드가 완료된 후에 결제 확인을 처리하기 위해 사용합니다.
document.addEventListener('DOMContentLoaded', function() {
    confirm(); // confirm 함수 실행

    // HTML 요소에서 결제 정보를 표시할 위치를 찾습니다.
    const paymentKeyElement = document.getElementById("paymentKey");
    const orderIdElement = document.getElementById("orderId");
    const amountElement = document.getElementById("amount");

    // null 체크: 요소가 존재하는지 확인합니다.
    if (paymentKeyElement && orderIdElement && amountElement) {
        // 주문 ID, 결제 금액, 결제 키를 화면에 표시합니다.
        orderIdElement.textContent = orderId;
        amountElement.textContent = amount + '원';
        paymentKeyElement.textContent = paymentKey;
    } else {
        // 만약 요소가 없다면 오류 메시지를 콘솔에 출력합니다.
        console.error("One or more elements are missing");
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const openModalBtn = document.getElementById("open-modal-btn");
    const closeModalBtn = document.getElementById("close-modal-btn");
    const modalPaymentBtn = document.getElementById("modal-payment-button");
    const modal = document.getElementById("reservation-modal");

    // 모달 열기
    openModalBtn.addEventListener('click', function() {
        modal.style.display = 'block';
        initTossPayments();
    });

    // 모달 닫기
    closeModalBtn.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    async function initTossPayments() {
        const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm"; // Toss Payments Client Key
        const tossPayments = TossPayments(clientKey);
        const customerKey = "3hAXYvqhkTWwG5abB0FWy"; // Customer Key
        const widgets = tossPayments.widgets({
            customerKey,
        });

        const reservationIdElement = document.getElementById("reservationId");
        if (!reservationIdElement) {
            console.error("reservationId 요소를 찾을 수 없습니다.");
            return;
        }
        const reservationId = reservationIdElement.value;

        if (!reservationId) {
            console.error("유효하지 않은 reservationId");
            return;
        }

        // 서버로부터 orderId 가져오기
        let orderId;
        try {
            const response = await fetch('/generate-order-id', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ reservationId: reservationId })
            });

            if (!response.ok) {
                throw new Error(`서버 응답 오류: ${response.status}`);
            }
            const data = await response.json();
            orderId = data.orderId;
            console.log("Generated Order ID:", orderId);
        } catch (error) {
            console.error("orderId 생성 중 오류 발생:", error);
            return;
        }

        try {
            // 서버로부터 결제 금액 가져오기
            const response = await fetch(`/getAmount?reservationId=${reservationId}`);
            if (!response.ok) {
                throw new Error(`서버 응답 오류: ${response.status}`);
            }
            const data = await response.json();
            const amount = data.amount;

            if (!amount || isNaN(amount)) {
                throw new Error("결제 금액이 올바르게 설정되지 않았습니다.");
            }

            // 결제 금액 설정
            widgets.setAmount({
                currency: "KRW",
                value: amount,
            });

            // 결제 방법과 약관 렌더링
            await Promise.all([
                widgets.renderPaymentMethods({
                    selector: "#payment-method",
                    variantKey: "DEFAULT",
                }),
                widgets.renderAgreement({
                    selector: "#agreement",
                    variantKey: "AGREEMENT"
                }),
            ]);

            // 결제 요청 처리
            modalPaymentBtn.addEventListener("click", async function() {
                try {
                    // 결제 방법 선택
                    const paymentMethodElement = document.querySelector('input[name="paymentMethod"]:checked');
                    const paymentMethod = paymentMethodElement ? paymentMethodElement.value : 'CARD';

                    await widgets.requestPayment({
                        orderId: orderId,
                        orderName: "세인트존스 호텔 숙박 예약",
                        successUrl: `${window.location.origin}/success?reservationId=${reservationId}&paymentMethod=${paymentMethod}`,
                        failUrl: `${window.location.origin}/fail?reservationId=${reservationId}&paymentMethod=${paymentMethod}`,
                        customerEmail: "customer123@gmail.com",
                        customerName: "김토스",
                        customerMobilePhone: "01012341234",
                    });

                    // 결제 요청 후 모달 닫기
                    modal.style.display = 'none';
                } catch (error) {
                    console.error("결제 요청 처리 중 오류 발생:", error);
                }
            });
        } catch (error) {
            console.error("결제 금액 설정에 오류가 발생했습니다:", error);
        }
    }
});

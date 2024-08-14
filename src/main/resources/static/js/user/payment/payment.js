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

            // TossPayments 초기화 및 결제 수단 렌더링
            function initTossPayments() {
                const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
                const tossPayments = TossPayments(clientKey);
                const customerKey = "3hAXYvqhkTWwG5abB0FWy";
                const widgets = tossPayments.widgets({
                    customerKey,
                });

                widgets.setAmount({
                    currency: "KRW",
                    value: 1,
                });

                Promise.all([
                    widgets.renderPaymentMethods({
                        selector: "#payment-method",
                        variantKey: "DEFAULT",
                    }),
                    widgets.renderAgreement({ selector: "#agreement", variantKey: "AGREEMENT" }),
                ]);

                // '결제하기' 버튼 누르면 결제창 띄우기
                modalPaymentBtn.addEventListener("click", async function() {
                    await widgets.requestPayment({
                        orderId: "qqhJnr8d__V0uhLkTPkyg",
                        orderName: "세인트존스 호텔 숙박 예약",
                        successUrl: window.location.origin + "/success",
                        failUrl: window.location.origin + "/fail",
                        customerEmail: "customer123@gmail.com",
                        customerName: "김토스",
                        customerMobilePhone: "01012341234",
                    });
                });
            }
        });
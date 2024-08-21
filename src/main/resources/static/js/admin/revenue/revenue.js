document.addEventListener('DOMContentLoaded', function() {
    const exportButton = document.querySelector('.export-button');

    exportButton.addEventListener('click', function() {
        const startDate = document.getElementById('start-date').value;
        const endDate = document.getElementById('end-date').value;
        const userEmail = document.getElementById('partner-email').value;

        const url = `/admin/revenue/export?startDate=${startDate}&endDate=${endDate}&userEmail=${userEmail}`;

        fetch(url, {
            method: 'GET',
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = 'revenue_data.xlsx';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('엑셀 파일 다운로드에 실패했습니다.');
        });
    });
});
$(document).ready(function(){
	  const el = document.getElementById('chart-area');
	  
	  
	 const data = {
        categories: [
          '1월',
          '2월',
          '3월',
          '4월',
          '5월',
          '6월',
          '7월',
          '8월',
          '9월',
          '10월',
          '11월',
          '12월',
        ],
        series: [
          {
            name: '신규직원',
            data: [20, 40, 25, 50, 15, 45, 33, 34, 20, 30, 22, 13],
          },
          {
            name: '신규파트너',
            data: [5, 30, 21, 18, 59, 50, 28, 33, 7, 20, 10, 30],
          },
          {
            name: '신규유저',
            data: [30, 5, 18, 21, 33, 41, 29, 15, 30, 10, 33, 5],
          },
        ],
      };
      const options = {
        chart: {  width: 1100, height: 380 },
        xAxis: { pointOnColumn: false, title: { text: 'Month' } },
        yAxis: { title: '가입률' },
        series: { zoomable: true },
      };

      const chart = toastui.Chart.areaChart({ el, data, options });
	

});
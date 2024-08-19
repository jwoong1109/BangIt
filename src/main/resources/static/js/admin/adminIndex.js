$(document).ready(function() {
	
	/*총 구성 비율*/
	Highcharts.chart('container2', {
    chart: {
        type: 'pie',
        custom: {},
        events: {
            render() {
                const chart = this,
                    series = chart.series[0];
                let customLabel = chart.options.chart.custom.label;

                if (!customLabel) {
                    customLabel = chart.options.chart.custom.label =
                        chart.renderer.label(
                            '총 이용자<br/>' +
                            '<strong>384</strong>'
                        )
                            .css({
                                color: '#000',
                                textAnchor: 'middle'
                            })
                            .add();
                }

                const x = series.center[0] + chart.plotLeft,
                    y = series.center[1] + chart.plotTop -
                    (customLabel.attr('height') / 2);

                customLabel.attr({
                    x,
                    y
                });
                // Set font size based on chart diameter
                customLabel.css({
                    fontSize: `${series.center[2] / 12}px`
                });
            }
        }
    },
    accessibility: {
        point: {
            valueSuffix: '%'
        }
    },
    tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.0f}%</b>'
    },
    legend: {
        enabled: false
    },
    plotOptions: {
        series: {
            allowPointSelect: true,
            cursor: 'pointer',
            borderRadius: 8,
            dataLabels: [{
                enabled: true,
                distance: 20,
                format: '{point.name}'
            }, {
                enabled: true,
                distance: -15,
                format: '{point.percentage:.0f}%',
                style: {
                    fontSize: '0.9em'
                }
            }],
            showInLegend: true
        }
    },
    series: [{
        name: 'Registrations',
        colorByPoint: true,
        innerSize: '75%',
        data: [{
            name: '직원',
            y: 10
        }, {
            name: '판매자',
            y: 30,
            color: '#FE5B5C'
        }, {
            name: '사용자',
            y: 60,
            color: '#fbd546'
        }]
    }]
});

	
	
	
	

	/*월별 가입률 */
	Highcharts.chart('container', {
		chart: {
			type: 'column'
		},
		subtitle: {
			text:
				'Source: <a target="_blank" ' +
				'href="https://www.indexmundi.com/agriculture/?commodity=corn">indexmundi</a>',
			align: 'left'
		},
		xAxis: {
			categories: ['3월', '4월', '5월', '6월', '7월', '8월'],
			crosshair: true,
			accessibility: {
				description: 'Month'
			}
		},
		yAxis: {
			min: 0,
			title: {
				text: '가입자수'
			},
			labels: {
				formatter: function() {
					return this.value >= 10000 ? this.value / 10000 + '명' : this.value;
				}
			}
		},

		plotOptions: {
			column: {
				pointPadding: 0.2,
				borderWidth: 0
			}
		},
		series: [
			{
				name: '판매자',
				data: [20, 40, 50, 100, 30, 150],
				color: '#FE5B5C'
			},
			{
				name: '사용자',
				data: [100, 200, 50, 100, 75, 25],
				color: '#fbd546'
			}
		]
	});
	
	/*도시별 예약*/
	(async () => {

    const mapData = await fetch(
        'https://code.highcharts.com/mapdata/countries/kr/kr-all-all.geo.json'
    ).then(response => response.json());

    const data = [
        ['kr-se', 100], // 예약 데이터 (서울)
        ['kr-gg', 50],  // 예약 데이터 (경기)
        ['kr-cb', 30],  // 예약 데이터 (충북)
        ['kr-cn', 20],  // 예약 데이터 (충남)
        ['kr-gn', 10],  // 예약 데이터 (경남)
        ['kr-gb', 5],   // 예약 데이터 (경북)
    ];

    document.getElementById('container3').innerHTML = 'Rendering map...';

    setTimeout(function () {
        Highcharts.mapChart('container3', {
            chart: {
                map: mapData,
                height: '80%'
            },
            title: {
                text: '한국 지역별 예약 현황',
                align: 'left'
            },
            series: [{
                data: data,
                name: '예약 수',
                joinBy: ['hc-key', 'code'],
                tooltip: {
                    valueSuffix: '건'
                },
                borderWidth: 1,
                borderColor: 'gray',
                shadow: true
            }]
        });
    }, 0);

})();

});
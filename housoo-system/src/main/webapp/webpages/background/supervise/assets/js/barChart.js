/*双柱状折线图*/
function barChart(id, legend, data) {
    var myChart = echarts.init(document.getElementById(id));
    var barwidth;
    if (id == "barChart2") {
        barwidth = 35;
    } else {
        barwidth = 20;
    }
    var option = {
        tooltip: {
            trigger: 'axis'
        },
        color: ['#f8b551', '#ec6941'],
        grid: {
            left: '2%',
            right: '2%',
            top: '15%',
            bottom: '10%',
            containLabel: true,
            borderWidth: 1,
            borderColor: '#f0e7e6'
        },
        legend: {
            data: legend,
            right: 20,
            top: 10,
            textStyle: {
                color: '#333'
            }
        },
        xAxis: [{
            type: 'category',
            data: data.datax,
            axisLabel: {
                textStyle: {
                    color: '#796567',
                    fontSize: 14
                }
            },
            axisLine: {
                lineStyle: {
                    color: '#f0e7e6'
                }
            },
            splitLine: false,
            axisTick: false
        }],
        yAxis: [{
            type: 'value',
            min: 0,
            splitLine: {
                lineStyle: {
                    color: '#f0e7e6'
                }
            },
            axisLabel: {
                textStyle: {
                    color: '#796567',
                    fontSize: 14
                }
            },
            axisLine: {
                lineStyle: {
                    color: '#f0e7e6'
                }
            },
            axisTick: false
        }],
        series: [{
            name: legend[0],
            type: 'bar',
            barWidth: barwidth,
            data: data.datay.datay1,
        },
            {
                name: legend[1],
                type: 'bar',
                barWidth: barwidth,
                data: data.datay.datay2,
            }
        ]
    };
    myChart.setOption(option);
}
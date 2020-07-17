/*折线面积图*/
function lineChart(id, data) {
    /*data = {datax:["部门一","部门二","部门三","部门四","部门五","部门六","部门七","部门八","部门九","部门十"],
     datay:[66,40,60,40,46,66,40,60,40,46]
     };*/
    var myChart = echarts.init(document.getElementById(id));
    var screen = window.screen.width;
    if(screen==1366){
        var leftval = '7%';
    }else if(screen==1440){
        var leftval = '7%';
    }else{
        var leftval = '5%';
    }
    option = {
        title: {
            text: '',
            left: '20',
            textStyle: {
                color: '#573e41',
                fontWeight: 'normal'
            }
        },
        grid: {
            left: leftval,
            right: '5%',
            top: '10%',
            bottom: '8%',
            containLabel: true,
            borderWidth: 1,
            borderColor: '#f0e7e6'
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: data.datax,
            splitLine: {show: false},
            axisTick: {show: false},
            axisLine: {
                lineStyle: {
                    color: '#f0e7e6'
                }
            },
            axisLabel: {
                textStyle: {
                    color: '#796567',
                    fontSize: 14
                },
                rotate: 45
            }
        },
        yAxis: {
            type: 'value',
            axisTick: {show: false},
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
            splitLine: {
                lineStyle: {
                    color: '#f0e7e6'
                }
            },
        },
        series: [
            {
                type: 'line',
                symbol: 'circle',
                itemStyle: {
                    color: '#f4a013'
                },
                areaStyle: {
                    color: 'rgba(243,152,0,0.2)',
                },
                data: data.datay
            }
        ]
    };
    myChart.setOption(option);
}
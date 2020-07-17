/*圆环图*/
function pieChart(id, color, data) {
    var myChart = echarts.init(document.getElementById(id));
    var option = {
        title: {
            text: data + "%",
            x: 'center',
            y: 'center',
            textStyle: {
                fontWeight: 'normal',
                color: '#333333',
                fontSize: 16
            }
        },
        series: [{
            type: 'pie',
            radius: ['50%', '65%'],
            itemStyle: {
                normal: {
                    label: {
                        show: false
                    },
                    labelLine: {
                        show: false
                    }
                }
            },
            hoverAnimation: false,
            data: [{
                name: '完成',
                value: data,
                itemStyle: {
                    normal: {
                        color: color,
                        label: {
                            show: false
                        },
                        labelLine: {
                            show: false
                        }
                    }
                }
            }, {
                name: 'other',
                value: Number(100 - data),
                itemStyle: {
                    color: '#f0e7e6'
                }
            }]
        }]
    }
    myChart.setOption(option);
}
function pieChartsNew(id, data, sum) {
    var name = "";
    var myChart = echarts.init(document.getElementById(id));
    var option = {
        title: {
            text: sum,
            subtext: name,
            x: 'center',
            y: 'center',
            textVerticalAlign: 'auto',
            itemGap: 0,
            padding: 0,
            textStyle: {
                color: '#ec6941',
                fontSize: 20
            },
            subtextStyle: {
                color: '#333',
                fontSize: 16
            }
        },
        color: ['#ec6941', '#5eadff', '#998ef0', '#1bb899', '#f8ac59'],
        tooltip: {
            trigger: 'item',
            formatter: "{a}({b}):<br/>{c} ({d}%)"
        },
        series: [
            {
                name: name,
                type: 'pie',
                radius: [25, 40],
                /*center: ['50%', '65%'],*/
                roseType: "",
                data: data,
                label: {
                    show: false,
                    color: '#333333',
                    fontSize: '10',
                    formatter: "{b} \n {d}%"
                },
                labelLine: {
                    length: 2,
                }
            }
        ]
    };
    myChart.setOption(option);
}

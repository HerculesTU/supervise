function pieCharts(id, data, sum) {
    /*data = [
     {value:20, name:'办结反馈'},
     {value:35, name:'落实反馈'},
     {value:30, name:'办理反馈'},
     {value:40, name:'督办反馈'}
     ];*/
    var myChart = echarts.init(document.getElementById(id));
    var screen = window.screen.width;
    var radius = [];
    if(screen>=1366&&screen<=1440){
        radius = [30, 60];
    }else if(screen>1440){
        radius = [50, 90];
    }
    var option = {
        title: {
            text: sum,
            subtext: '逾期次数',
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
        color: ['#ec6941', '#5eadff', '#1bb899', '#f8ac59'],
        tooltip: {
            trigger: 'item',
            formatter: "{a}({b}):<br/>{c} ({d}%)"
        },
        series: [
            {
                name: '逾期次数',
                type: 'pie',
                radius: radius,
                roseType: 'area',
                data: data,
                label: {
                    color: '#333333',
                    fontSize: '16',
                    formatter: "{b} \n {d}%"
                },
                labelLine:{
                    length: 5,
                }
            }
        ]
    };
    myChart.setOption(option);
}

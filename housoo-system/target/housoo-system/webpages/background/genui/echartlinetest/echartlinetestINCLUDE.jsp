<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<div class="titlePanel" style="display: none;">
</div>
<div style="height:400px;" id="402848a55d405c1c015d405d07bc0002">                    	
</div>
<script type="text/javascript">
$(function() {
	PlatUtil.loadEchartPic("402848a55d405c1c015d405d07bc0002",{
		title : {
	        text: "天气预报",
	        
	        x:"left"
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    toolbox: {
	        show: false,
	        feature: {
	            dataZoom: {
	                yAxisIndex: 'none'
	            },
	            dataView: {readOnly: false},
	            magicType: {type: ['line', 'bar']},
	            restore: {},
	            saveAsImage: {}
	        }
	    },
	    xAxis:  {
	        type: 'category',
	        boundaryGap: false
	    },
	    yAxis: {
	        type: 'value',
	        axisLabel: {
	            formatter: '{value}'
	        }
	    },
	    series: [
             {
                 name:'最高气温',
                 type:'line',
                 markPoint: {
                     data: [
                         {type: 'max', name: '最大值'},
                         {type: 'min', name: '最小值'}
                     ]
                 },
                 markLine: {
                     data: [
                         {type: 'average', name: '平均值'}
                     ]
                 }
             },
             {
                 name:'最低气温',
                 type:'line',
                 markPoint: {
                     data: [
                         {name: '周最低', value: -2, xAxis: 1, yAxis: -1.5}
                     ]
                 },
                 markLine: {
                     data: [
                         {type: 'average', name: '平均值'},
                         [{
                             symbol: 'none',
                             x: '90%',
                             yAxis: 'max'
                         }, {
                             symbol: 'circle',
                             label: {
                                 normal: {
                                     position: 'start',
                                     formatter: '最大值'
                                 }
                             },
                             type: 'max',
                             name: '最高点'
                         }]
                     ]
                 }
             }
         ]
	});
});
</script>



<script type="text/javascript">

</script>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="plat-directlayout" style="height:100%" platundragable="true"
	compcode="direct_layout">
	<div class="ui-layout-center" platundragable="true"
		compcode="direct_layout" style="height:100%">
		<div style="padding: 5px;" id="reviewItem_search">
			<form action="javascript:void(0);" class="form-horizontal"
				id="portalTopSource">
				<div class="form-group">
					<div class="col-sm-6">
						从<input class="wicon form-control" name="startvalue" istime="false" format="YYYY-MM-DD" value="${preDate}" max_date="${curDate}" auth_type="" readonly="readonly" posttimefmt="" placeholder="" style="width: 40%;"  end_name="endvalue" type="text">
						至<input class="wicon form-control" name="endvalue" istime="false" format="YYYY-MM-DD" value="${curDate}" max_date="${curDate}" auth_type="" readonly="readonly" posttimefmt="" placeholder="" style="width: 40%;"  start_name="startvalue" type="text">
					</div>
					<div class="col-sm-3" >
							<select class="form-control select2" name="type" istree="false" auth_type="write" placeholder="请选择查看类型"  tabindex="-1" aria-hidden="true">
								<option value="1" >浏览次数(PV)</option>
								<option value="2" >独立访客(UV)</option>
								<option value="3" >IP</option>
							</select>
						</div>
					<div class="col-sm-3">
						<a onclick="searchTopTime();" class="btn btn-primary"
							href="javascript:void(0);"> 查 询</a>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
			</form>
		</div>
		<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
		<div id="portalTopmain" style="width: 100%;height: calc(100% - 50px);"></div>
	</div>
</div>
<script type="text/javascript">
//基于准备好的dom，初始化echarts实例
var portalTopmain = echarts.init(document.getElementById('portalTopmain'));

// 指定图表的配置项和数据
var portalTopoption = {
	    title: {
	      
	    },
	    tooltip: {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    legend: {
	    	show: true
	    },
	    toolbox: {
	        show: false
	    },
	    xAxis:  {
	        type: 'category',
	        axisLabel:{
	        	interval:0,
	        	rotate:10
	        },
	        data: ['']
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: [
	    ]
	};

// 使用刚指定的配置项和数据显示图表。
portalTopmain.setOption(portalTopoption); 


function searchTopTime(){
	var startDay = $("#portalTopSource input[name='startvalue']").val();
	var endDay = $("#portalTopSource input[name='endvalue']").val();
	var type = $("#portalTopSource select[name='type']").val();
	if(type==""||type==null){
		PlatUtil.changeSelect2Val("#portalTopSource select[name='type']","1");
		type="1";
	}
	if(startDay!=""&&endDay!=""){
		PlatUtil.ajaxProgress({
			url : "webstatistics/DayTrendController.do?getTopSourceData",
			params :{"startDay":startDay,"endDay":endDay,"type":type},
			callback : function(resultJson) {
				portalTopmain.setOption({
					legend: {
	        	    	show: true,
	        	    	data:[resultJson.name]
	        	    },
	            	  xAxis : [
	                           {
	                              data:resultJson.title
	                           }
	                    ],
		              series: [{
		                  name: resultJson.name,
		                  type: 'bar',
		                  data: resultJson.data,
		                  barWidth:40,
		                  itemStyle : { normal: {label : {show: true,position:'top'},color:'#0DCC6C'}}
		              }]
	              });
			}
		});
	}else{
		if(startDay==""){
			layer.alert("请选择开始日期!",{icon: 2,resize:false});
		}else if(endDay==""){
			layer.alert("请选择结束日期!",{icon: 2,resize:false});
		}
	}
	
}

$(function(){
	//初始化下拉框控件
	PlatUtil.initSelect2();
	//初始化日期时间控件
	PlatUtil.initDateTime();
	searchTopTime();
})
</script>




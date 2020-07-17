<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="plat-directlayout" style="height:100%" platundragable="true"
	compcode="direct_layout">
	<div class="ui-layout-center" platundragable="true"
		compcode="direct_layout" style="height:100%">
		<div style="padding: 5px;" id="reviewItem_search">
			<form action="javascript:void(0);" class="form-horizontal"
				id="portalMap">
				<div class="form-group">
					<div class="col-sm-6">
						从<input class="wicon form-control" name="startvalue" istime="false" format="YYYY-MM-DD" value="${preDate}" max_date="${curDate}" auth_type="" readonly="readonly" posttimefmt="" placeholder="" style="width: 40%;"  end_name="endvalue" type="text">
						至<input class="wicon form-control" name="endvalue" istime="false" format="YYYY-MM-DD" value="${curDate}" max_date="${curDate}" auth_type="" readonly="readonly" posttimefmt="" placeholder="" style="width: 40%;"  start_name="startvalue" type="text">
					</div>
					<div class="col-sm-3">
						<a onclick="searchMapTime();" class="btn btn-primary"
							href="javascript:void(0);"> 查 询</a>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
			</form>
		</div>
		<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
		<div id="portalMapmain" style="width: 100%;height: calc(100% - 50px);"></div>
	</div>
</div>
<script src="plug-in/echart-3.6/china.js"></script>
<script type="text/javascript">
var portalMapChart = echarts.init(document.getElementById('portalMapmain'));

// 指定图表的配置项和数据
var portalMapoption = {
	    title: {
	      
	    },
	    tooltip: {
	        trigger: 'item'
	    },
	    legend: {
	        data:['浏览次数(PV)','独立访客(UV)','IP','新增用户数']
	    },
	    /* visualMap: {
	        min: 0,
	        max: 2500,
	        left: 'left',
	        top: 'bottom',
	        text: ['高','低'],           // 文本，默认为数值文本
	        calculable: true
	    }, */
	    series : [
			
	    ]
	};

// 使用刚指定的配置项和数据显示图表。
portalMapChart.setOption(portalMapoption); 


function searchMapTime(){
	var startDay = $("#portalMap input[name='startvalue']").val();
	var endDay = $("#portalMap input[name='endvalue']").val();
	if(startDay!=""&&endDay!=""){
		PlatUtil.ajaxProgress({
			url : "webstatistics/MapDataController.do?getMapData",
			params :{"startDay":startDay,"endDay":endDay},
			callback : function(resultJson) {
				portalMapChart.setOption({
					visualMap: {
				        min: 0,
				        max: resultJson.maxnum,
				        left: 'left',
				        top: 'bottom',
				        text: ['高','低'],           // 文本，默认为数值文本
				        calculable: true
				      },
		              series: [{
		                  name: '浏览次数(PV)',
		                  type: 'map',
		                  mapType: 'china',
		                  roam: false,
		                  label: {
		                      normal: {
		                          show: true
		                      },
		                      emphasis: {
		                          show: true
		                      }
		                  },
		                  data:resultJson.pvdata
		              },{
		                  name: '独立访客(UV)',
		                  type: 'map',
		                  mapType: 'china',
		                  roam: false,
		                  label: {
		                      normal: {
		                          show: true
		                      },
		                      emphasis: {
		                          show: true
		                      }
		                  },
		                  data:resultJson.uvdata
		              },{
		                  name: 'IP',
		                  type: 'map',
		                  mapType: 'china',
		                  roam: false,
		                  label: {
		                      normal: {
		                          show: true
		                      },
		                      emphasis: {
		                          show: true
		                      }
		                  },
		                  data:resultJson.ipdata
		              },{
		                  name: '新增用户数',
		                  type: 'map',
		                  mapType: 'china',
		                  roam: false,
		                  label: {
		                      normal: {
		                          show: true
		                      },
		                      emphasis: {
		                          show: true
		                      }
		                  },
		                  data:resultJson.newuvdata
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
	//初始化日期时间控件
	PlatUtil.initDateTime();
	searchMapTime();
})
</script>




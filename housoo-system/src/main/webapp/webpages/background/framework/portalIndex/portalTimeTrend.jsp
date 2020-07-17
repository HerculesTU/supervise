<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="plat-directlayout" style="height:100%" platundragable="true"
	compcode="direct_layout">
	<div class="ui-layout-center" platundragable="true"
		compcode="direct_layout" style="height:100%">
		<div style="padding: 5px;" id="reviewItem_search">
			<form action="javascript:void(0);" class="form-horizontal"
				id="portalTime">
				<div class="form-group">
					<div class="col-sm-6">
						<input class="wicon form-control" name="searchDay"
							value="${curDate}" max_date="${curDate}" istime="false"
							format="YYYY-MM-DD" auth_type="" readonly="readonly"
							posttimefmt="" placeholder="" type="text">
					</div>
					<div class="col-sm-6">
						<a onclick="searchportalTime();" class="btn btn-primary"
							href="javascript:void(0);"> 查 询</a>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
			</form>
		</div>
		<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
		<div id="portalmain" style="width: 100%;height: calc(100% - 50px);"></div>
	</div>
</div>
<script type="text/javascript">
	// 基于准备好的dom，初始化echarts实例
	var portalmainChart = echarts.init(document.getElementById('portalmain'));

	// 指定图表的配置项和数据
	var portalmainoption = {
		title : {

		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			show : true,
			data : [ '浏览次数(PV)', '独立访客(UV)', 'IP' ]
		},
		toolbox : {
			show : false
		},
		xAxis : {
			type : 'category',
			boundaryGap : false,
			data : [ '' ]
		},
		yAxis : {
			type : 'value'
		},
		series : []
	};

	// 使用刚指定的配置项和数据显示图表。
	portalmainChart.setOption(portalmainoption);

	function searchportalTime() {
		var searchDay = $("#portalTime input[name='searchDay']").val();
		if (searchDay != "") {
			PlatUtil.ajaxProgress({
				url : "webstatistics/DayTrendController.do?getTimeTrendData",
				params : {
					"searchDay" : searchDay
				},
				callback : function(resultJson) {
					portalmainChart.setOption({
						xAxis : [ {
							type : 'category',
							data : resultJson.title
						} ],
						series : [ {
							name : '浏览次数(PV)',
							type : 'line',
							data : resultJson.pvData,
							itemStyle : {
								normal : {
									label : {
										show : true,
										textStyle : {
											color : '#333333'
										}
									}
								}
							}
						}, {
							name : '独立访客(UV)',
							type : 'line',
							data : resultJson.uvData,
							itemStyle : {
								normal : {
									label : {
										show : true,
										textStyle : {
											color : '#333333'
										}
									}
								}
							}
						}, {
							name : 'IP',
							type : 'line',
							data : resultJson.ipData,
							itemStyle : {
								normal : {
									label : {
										show : true,
										textStyle : {
											color : '#333333'
										}
									}
								}
							}
						} ]
					});
				}
			});
		} else {
			layer.alert("请选择查看日期!", {
				icon : 2,
				resize : false
			});
		}

	}

	$(function() {
		searchportalTime();
		PlatUtil.initDateTime();
	})
</script>




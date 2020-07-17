<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>中网软件快速开发平台 开源流程引擎 JBPM|Activiti 可视化流程设计器 智能表单 JAVA开发平台</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,echart"></plattag:resources>
  </head>
  
  <body>
  	<script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
  	 <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   		<div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="height:100%;overflow-y:auto; " >
  			
  			<div class="panel-Title">
				<h5>监视节点</h5>
			</div>
			<div class="container-fluid" style="padding-top: 10px; padding-left: 10px;">
 					 <plattag:select name="DEPLOYLOG_NODES" allowblank="true" auth_type="write" value="" istree="false" onlyselectleaf="false" label_value="节点" placeholder="请选择节点" comp_col_num="4" label_col_num="1" dyna_interface="deployNodeService.findList" dyna_param="无">
 					</plattag:select>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="panel-Title">
				<h5>资源监视</h5>
			</div>
			<div style="padding: 5px;"  >
				<div class="container-fluid">
					<div class="col-sm-3">
						<div id="cpuChart" style="width: 100%;height: 300px;"></div>
					</div>
					<div class="col-sm-3">
						<div id="memoryChart" style="width: 100%;height: 300px;"></div>
					</div>
					<div class="col-sm-3">
						<div id="jvmMemoryChart" style="width: 100%;height: 300px;"></div>
					</div>
					<div class="col-sm-3">
						<div id="diskChart" style="width: 100%;height: 300px;"></div>
					</div>
				</div>
			</div>
			
			<div class="panel-Title">
				<h5>检测信息</h5>
			</div>
			<div class="gridPanel">
				<table class="table table-bordered table-hover platedittable">
					<thead>
						<tr class="active">
							<th style="width: 5%;">序号</th>
							<th style="width: 10%;">检测名称</th>
							<th style="width: 10%;">检查项</th>
							<th style="width: 10%;">检查结果</th>
							<th style="width: 10%;">仪表盘</th>
							<th style="width: 10%;">定时刷新</th>
							<th style="width: 45%;">检测信息</th>
						</tr>
						<tr>
							<td>1</td>
							<td>操作系统</td>
							<td>OS</td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td></td>
							<td></td>
							<td id="OS_TD"><p>名称：${osInfo.osName}，架构：${osInfo.osArch}，版本：${osInfo.osVersion}；</p></td>
						</tr>
						<tr>
							<td>2</td>
							<td>处理器</td>
							<td>CPU</td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td id="CPU_TD"><p>型号：${cpuInfo.cpuModel}；</p>
								<p>数量：${cpuSize}，内核：${cpuInfo.cpuCoresPerSocket}，逻辑处理器：${cpuInfo.cpuTotalSockets}；</p></td>
						</tr>
						<tr>
							<td>3</td>
							<td>物理内存</td>
							<td>Memory</td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td id="MEMORY_TD"><p>总量：${physicalMemory.TotalMemory}GB，空闲：${physicalMemory.FreeMemory}GB，已用：${physicalMemory.UsedMemory}GB；</p></td>
						</tr>
						<tr>
							<td>4</td>
							<td>JVM内存</td>
							<td>JVM Memory</td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td id="JVM_TD"><p>最大：${jvmInfo.totalMemory}MB，分配：${jvmInfo.totalMemory}MB（空闲：${jvmInfo.freeMemory}MB，已用：${jvmInfo.usedMemory}MB），可用：${jvmInfo.freeMemory}MB；</p></td>
						</tr>
						<tr>
							<td>5</td>
							<td>磁盘空间</td>
							<td>Disk</td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td><i class="fa fa-check" aria-hidden="true" style="color: #0DCC6C;"></i></td>
							<td id="DISK_TD">
								<c:forEach items="${fileInfoList}" var="fileInfo">
								<p>分区：${fileInfo.devName}（容量：${fileInfo.usageTotal}GB，已用：${fileInfo.usageUsed}GB，可用：${fileInfo.usageFree}GB，已用%：${fileInfo.usageUsePercent}%）；</p>
								</c:forEach>
							</td>
						</tr>
					</thead>
					
				</table>
			
			</div>
    	</div>
    </div>
    <script type="text/javascript">
    var cpuChart = echarts.init(document.getElementById('cpuChart'));
    var memoryChart = echarts.init(document.getElementById('memoryChart'));
    var jvmMemoryChart = echarts.init(document.getElementById('jvmMemoryChart'));
    var diskChart = echarts.init(document.getElementById('diskChart'));
    
    var cpuChartOption = {
    	    tooltip : {
    	        formatter: "{b} : {c}%"
    	    },
    	    toolbox: {
    	    },
    	    series: [
    	        {
    	            name: 'CPU使用率',
    	            type: 'gauge',
    	            radius: '90%',
    	            axisLine: {            // 坐标轴线
    	                lineStyle: {       // 属性lineStyle控制线条样式
    	                    width: 15
    	                }
    	            },
    	            detail: {formatter:'{value}%',
    	            	textStyle:{fontSize:18,fontFamily:'Microsoft YaHei'}},
    	            data: [{value: 0, name: 'CPU使用率'}]
    	        }
    	    ]
    	};
		
	    var memoryChartOption = {
	    	    tooltip : {
	    	        formatter: "{b} : {c}%"
	    	    },
	    	    toolbox: {
	    	    },
	    	    series: [
	    	        {
	    	            name: 'Memory使用率',
	    	            type: 'gauge',
	    	            radius: '90%',
	    	            axisLine: {            // 坐标轴线
	    	                lineStyle: {       // 属性lineStyle控制线条样式
	    	                    width: 15
	    	                }
	    	            },
	    	            detail: {formatter:'{value}%',
	    	            	textStyle:{fontSize:18,fontFamily:'Microsoft YaHei'}},
	    	            data: [{value: 0, name: 'Memory使用率'}]
	    	        }
	    	    ]
	    	};
	    var jvmMemoryChartOption = {
	    	    tooltip : {
	    	        formatter: "{b} : {c}%"
	    	    },
	    	    toolbox: {
	    	    },
	    	    series: [
	    	        {
	    	            name: 'JVMMemory使用率',
	    	            type: 'gauge',
	    	            radius: '90%',
	    	            axisLine: {            // 坐标轴线
	    	                lineStyle: {       // 属性lineStyle控制线条样式
	    	                    width: 15
	    	                }
	    	            },
	    	            detail: {formatter:'{value}%',
	    	            	textStyle:{fontSize:18,fontFamily:'Microsoft YaHei'}},
	    	            data: [{value: 0, name: 'JVMMemory使用率'}]
	    	        }
	    	    ]
	    	};
	    var diskChartOption = {
	    	    tooltip : {
	    	        formatter: "{b} : {c}%"
	    	    },
	    	    toolbox: {
	    	    },
	    	    series: [
	    	        {
	    	            name: 'Disk使用率',
	    	            type: 'gauge',
	    	            radius: '90%',
	    	            axisLine: {            // 坐标轴线
	    	                lineStyle: {       // 属性lineStyle控制线条样式
	    	                    width: 15
	    	                }
	    	            },
	    	            detail: {formatter:'{value}%',
	    	            	textStyle:{fontSize:18,fontFamily:'Microsoft YaHei'}},
	    	            data: [{value: 0, name: 'Disk使用率'}]
	    	        }
	    	    ]
	    	};
    	setInterval(function () {
    		ajaxIntervalOption();
    		
    	},2000);
	
    	function ajaxIntervalOption(){
    		var DEPLOYLOG_NODES = $("select[name='DEPLOYLOG_NODES']").val();
    		PlatUtil.ajaxProgress({
    			url:"appmodel/ServerStatusController/getIntervalInfo.do",
    			async:"-1",
    			showProgress:"-1",
    			params :{"DEPLOYLOG_NODES":DEPLOYLOG_NODES},
    			callback : function(resultJson) {
    				if (resultJson.success) {
    					cpuChartOption.series[0].data[0].value = resultJson.cpuPercCombined;
    		    		cpuChart.setOption(cpuChartOption, true);
    		    		memoryChartOption.series[0].data[0].value = resultJson.UsedMemoryPre;
    		    		memoryChart.setOption(memoryChartOption, true);
    		    		jvmMemoryChartOption.series[0].data[0].value = resultJson.jvmUsedMemoryPre;
    		    		jvmMemoryChart.setOption(jvmMemoryChartOption, true);
    				} 
    				
    			}
    		});
    		
    	}
    	
    	
    	
    	$(function(){
    		cpuChart.setOption(cpuChartOption, true);
    		memoryChart.setOption(memoryChartOption, true);
    		jvmMemoryChart.setOption(jvmMemoryChartOption, true);
    		diskChartOption.series[0].data[0].value = "${usePercent}";
    		diskChart.setOption(diskChartOption, true);
    		
    		$("select[name='DEPLOYLOG_NODES']").change(function(){
    			changeSelectServerNode();
    		});
    	});
    	
    	
    	function changeSelectServerNode(){
    		var DEPLOYLOG_NODES = $("select[name='DEPLOYLOG_NODES']").val();
    		PlatUtil.ajaxProgress({
    			url:"appmodel/ServerStatusController/getServerStatusInfo.do",
    			async:"-1",
    			showProgress:"1",
    			progressMsg:"正在获取节点信息。。。",
    			params :{"DEPLOYLOG_NODES":DEPLOYLOG_NODES},
    			callback : function(resultJson) {
    				if (resultJson.success) {
    					diskChartOption.series[0].data[0].value = resultJson.usePercent;
    		    		diskChart.setOption(diskChartOption, true);
    		    		var OS_TD_HTML = "<p>名称："+resultJson.osInfo.osName+"，架构："+resultJson.osInfo.osArch+"，版本："+resultJson.osInfo.osVersion+"；</p>";
    					$("#OS_TD").html(OS_TD_HTML);
    					var CPU_TD_HTML = "<p>型号："+resultJson.cpuInfo.cpuModel+"；</p>";
    					CPU_TD_HTML += "<p>数量："+resultJson.cpuSize+"，内核："+resultJson.cpuInfo.cpuCoresPerSocket+"，逻辑处理器："+resultJson.cpuInfo.cpuTotalSockets+"；</p>";
    					$("#CPU_TD").html(CPU_TD_HTML);
    					var MEMORY_TD_HTML="<p>总量："+resultJson.physicalMemory.TotalMemory+"GB，空闲："+resultJson.physicalMemory.FreeMemory+"GB，已用："+resultJson.physicalMemory.UsedMemory+"GB；</p>";
    					$("#MEMORY_TD").html(MEMORY_TD_HTML);
    					var JVM_TD_HTML = "<p>最大："+resultJson.jvmInfo.totalMemory+"MB，分配："+resultJson.jvmInfo.totalMemory
    								+"MB（空闲："+resultJson.jvmInfo.freeMemory+"MB，已用："+resultJson.jvmInfo.usedMemory+"MB），可用："
    								+resultJson.jvmInfo.freeMemory+"MB；</p>";
    					$("#JVM_TD").html(JVM_TD_HTML);
    					if(resultJson.fileInfoList){
    						var DISK_TD_HTML = "";
    						for(i=0;i<resultJson.fileInfoList.length;i++){
    							var fileInfo = resultJson.fileInfoList[i];
    							DISK_TD_HTML += "<p>分区："+fileInfo.devName+"（容量："+fileInfo.usageTotal+"GB，已用："+fileInfo.usageUsed+"GB，可用："+fileInfo.usageFree+"GB，已用%："+fileInfo.usageUsePercent+"%）；</p>";
    						}
    						$("#DISK_TD").html(DISK_TD_HTML);
    					}
    					
    				} 
    			}
    		});
    	}
    </script>
    
  </body>
</html>



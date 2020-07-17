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
 					 <plattag:select name="DEPLOYLOG_NODES" allowblank="true" auth_type="write" value="" istree="false" onlyselectleaf="false" label_value="节点" placeholder="请选择节点" comp_col_num="4" label_col_num="2" dyna_interface="deployNodeService.findList" dyna_param="无">
 					</plattag:select>
			</div>
			<div id="diskChart" style="width: 100%;height: calc(100% - 102px);"></div>
			
    	</div>
    </div>
    <script type="text/javascript">
    
    var diskChart = echarts.init(document.getElementById('diskChart'));
    
    
	    var diskChartOption = {
	    	    tooltip : {
	    	        formatter: "{b} : {c}%"
	    	    },
	    	    toolbox: {
	    	    },
	    	    series: [
	    	        {
	    	            name: '磁盘使用率',
	    	            type: 'gauge',
	    	            radius: '90%',
	    	            axisLine: {            // 坐标轴线
	    	                lineStyle: {       // 属性lineStyle控制线条样式
	    	                    width: 15,
	    	                    color: [["${warnLineNumber}", '#91c7ae'], [1, '#c23531']]
	    	                }
	    	            },
	    	            detail: {formatter:'{value}%',
	    	            	textStyle:{fontSize:18,fontFamily:'Microsoft YaHei'}},
	    	            data: [{value: 0, name: '磁盘使用率'}]
	    	        }
	    	    ]
	    	};
    	
    	
    	
    	
    	$(function(){
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
    		    		
    					
    				} 
    			}
    		});
    	}
    </script>
    
  </body>
</html>



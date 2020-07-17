<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<div class="titlePanel">
	<div class="title-search form-horizontal" id="402848a55ca572e2015ca57b0e53001dsearch" formcontrolid="402848a55ca572e2015ca57b0e53001d">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 300px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="USER_NAME" auth_type="write" label_value="标题" maxlength="100" allowblank="true" placeholder="" label_col_num="4" comp_col_num="8"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('402848a55ca572e2015ca57b0e53001dsearch');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.loadEchartPic('402848a55ca572e2015ca57b0e53001d');" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>
<div style="height:180px;" id="402848a55ca572e2015ca57b0e53001d">                    	
</div>
<script type="text/javascript">
$(function() {
	PlatUtil.loadEchartPic("402848a55ca572e2015ca57b0e53001d",{
		title : {
	        text: "用户访问来源",
	        
	        x:"center"
	    },
	    tooltip : {
	        trigger: "item",
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient: "vertical",
	        left: "left"
	    },
	    series : [
	        {
	            name: "访问来源",
	            type: "pie",
	            radius : "55%",
	            center: ["50%", "60%"],
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]
	});
});
</script>



<script type="text/javascript">

</script>

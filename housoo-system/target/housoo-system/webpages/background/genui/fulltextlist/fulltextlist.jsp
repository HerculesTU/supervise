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
    <title>全文检索列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="fulltext_datatablepaneltitle">索引列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="fulltext_search" formcontrolid="402881e65cfb7977015cfbf3b50e040d">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
								    <jsp:include page="/webpages/common/plattagtpl/rangetime_tag.jsp">
									    <jsp:param name="label_col_num" value="3"></jsp:param>
									    <jsp:param name="format" value="YYYY-MM-DD"></jsp:param>
									    <jsp:param name="label_value" value="入库时间"></jsp:param>
									    <jsp:param name="comp_col_num" value="9"></jsp:param>
									    <jsp:param value="auth_type" name="write"></jsp:param>
									    <jsp:param name="istime" value="false"></jsp:param>
									    <jsp:param name="allowblank" value="false"></jsp:param>
									    <jsp:param name="start_name" value="Q_T.FULLTEXT_CREATETIME_GE"></jsp:param>
									    <jsp:param name="end_name" value="Q_T.FULLTEXT_CREATETIME_LE"></jsp:param>
									</jsp:include>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.FULLTEXT_INDEXTITLE_LIKE" auth_type="write" label_value="索引标题" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
								    <plattag:select placeholder="" istree="false" label_col_num="3" label_value="信息类别" style="width:100%;" allowblank="true" comp_col_num="9" auth_type="write" static_values="" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'fullinfotype',ORDER_TYPE:'ASC'}" name="Q_T.FULLTEXT_TYPE_EQ">
								    </plattag:select>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('fulltext_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('fulltext_datatable','fulltext_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="delJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		  </button>
		
	      <button type="button" onclick="rebuildIndex();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-refresh"></i>&nbsp;重建索引
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="fulltext_datatable"></table>
</div>
<div id="fulltext_datatable_pager"></div>
<script type="text/javascript">
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"fulltext_datatable",
		selectColName:"FULLTEXT_ID",
		url:"system/FullTextController.do?multiDel",
		callback:function(resultJson){
			$("#fulltext_datatable").trigger("reloadGrid"); 
		}
	});
}
function rebuildIndex(){
  parent.layer.confirm("重建索引会带来较大性能开销,您确定继续?", {
		icon: 7,
		resize :false
	}, function(){
		PlatUtil.ajaxProgress({
			url:"system/FullTextController.do?rebuild",
			callback : function(resultJson) {
				if (resultJson.success) {
					if(resultJson.msg){
						parent.layer.alert(resultJson.msg,{icon: 1,resize:false});
					}else{
						parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false});
					}
					$("#fulltext_datatable").trigger("reloadGrid"); 
				} else {
					if(resultJson.msg){
						parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
					}else{
						parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
					}
					
				}
			}
		});
	}, function(){
		
	});	
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"fulltext_datatable",
		  searchPanelId:"fulltext_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402881e65cfb7977015cfbf3b50e040d",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "FULLTEXT_ID",label:"信息ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "DIC_NAME",label:"信息类别",
		         width: 150,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "FULLTEXT_INDEXTITLE",label:"索引标题",
		         width: 400,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "FULLTEXT_CREATETIME",label:"入库时间",
		         width: 150,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "FULLTEXT_TABLENAME",label:"业务表",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "FULLTEXT_RECORDID",label:"业务表记录ID",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         }
           ]
	 });
});
</script>
</div>
</div>
  </body>
</html>

<script type="text/javascript">

</script>

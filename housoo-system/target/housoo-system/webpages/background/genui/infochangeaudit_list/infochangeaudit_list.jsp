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
    <title>数据变更审核列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,slimscroll"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,slimscroll"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout" layoutsize="&quot;west__size&quot;:200">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="baselist_datatablepaneltitle">数据变更申请列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="baselist_search" formcontrolid="297ecf12635d502401635d94a1f10048">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_T.SECAUDIT_STATUS_EQ" value="">
                			  <input type="hidden" name="Q_T.SECAUDIT_ABLEIDS_LIKE" value="${sessionScope.__BACKPLATUSER.SYSUSER_ID}">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
								    <jsp:include page="/webpages/common/plattagtpl/rangetime_tag.jsp">
									    <jsp:param name="label_col_num" value="3"></jsp:param>
									    <jsp:param name="format" value="YYYY-MM-DD"></jsp:param>
									    <jsp:param name="label_value" value="申请时间"></jsp:param>
									    <jsp:param name="comp_col_num" value="9"></jsp:param>
									    <jsp:param value="auth_type" name="write"></jsp:param>
									    <jsp:param name="posttimefmt" value="2"></jsp:param>
									    <jsp:param name="istime" value="false"></jsp:param>
									    <jsp:param name="allowblank" value="false"></jsp:param>
									    <jsp:param name="start_name" value="Q_T.SECAUDIT_TIME_GE"></jsp:param>
									    <jsp:param name="end_name" value="Q_T.SECAUDIT_TIME_LE"></jsp:param>
									</jsp:include>
								</div>
								<div class="form-group">
								    <plattag:select placeholder="" istree="false" label_col_num="3" label_value="变更操作类型" style="width:100%;" allowblank="true" comp_col_num="9" auth_type="write" static_values="" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'secbustype',ORDER_TYPE:'ASC'}" name="Q_T.SECAUDIT_BUSTYPE_EQ">
								    </plattag:select>
								</div>
								<div class="form-group">
								    <plattag:select placeholder="" istree="false" label_col_num="3" label_value="变更对象" style="width:100%;" allowblank="true" comp_col_num="9" auth_type="write" static_values="" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'secbusobjtype',ORDER_TYPE:'ASC'}" name="Q_T.SECAUDIT_OBJTYPE_EQ">
								    </plattag:select>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('baselist_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('baselist_datatable','baselist_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="editJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-edit"></i>&nbsp;审批数据变更申请
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="baselist_datatable"></table>
</div>
<div id="baselist_datatable_pager"></div>
<script type="text/javascript">
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("baselist_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.SECAUDIT_ID);
	}
}

function addJqGridRecordInfo(SECAUDIT_ID){
	var url = "system/SecAuditController.do?goAuditForm";
	var title = "新增信息变更信息";
	if(SECAUDIT_ID){
		url+=("&SECAUDIT_ID="+SECAUDIT_ID);
		title = "审批数据变更申请";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["90%","80%"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#baselist_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"baselist_datatable",
		  searchPanelId:"baselist_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=297ecf12635d502401635d94a1f10048",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "SECAUDIT_ID",label:"审核记录ID",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "APPLY_NAME",label:"申请人",
		         width: 100,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "SECAUDIT_STATUS",label:"审核结果",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="0"){
       return "<span class=\"label label-warning\">未审核</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">审核通过</span>";      
    }else if(cellvalue=="-1"){
       return "<span class=\"label label-danger\">审核未通过</span>";      
    }
    
},

		         
		         sortable:false
		         },
		         {name: "CHANGE_DESC",label:"变更描述",
		         width: 300,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
  return "对【"+rowObject.OBJTYPE+"】进行【"+rowObject.BUSTYPE+"】操作";
},

		         
		         sortable:false
		         },
		         {name: "SECAUDIT_TIME",label:"申请时间",
		         width: 150,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "BUSTYPE",label:"BUSTYPE",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "OBJTYPE",label:"OBJTYPE",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "SYSUSER_NAME",label:"审核人",
		         width: 100,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "SECAUDIT_AUTIME",label:"审核时间",
		         width: 150,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "SECAUDIT_AUOPINION",label:"审核意见",
		         width: 100,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "SECAUDIT_STATUS",label:"审核结果",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         }
           ]
	 });
});
</script>
</div>
   <div class="ui-layout-west" platundragable="true" compcode="direct_layout">
     <plattag:listgroup onclickfn="reloadDatatable" grouptitle="审批结果列表" dyna_interface="secAuditService.findStatusList" dyna_param="无"></plattag:listgroup>
<script type="text/javascript">
  function reloadDatatable(value){
	if(value!="-2"){
		$("input[name='Q_T.SECAUDIT_STATUS_EQ']").val(value);
	}else{
		$("input[name='Q_T.SECAUDIT_STATUS_EQ']").val("");
	}
	PlatUtil.tableDoSearch("baselist_datatable","baselist_search");
}
</script>
</div>
</div>
  </body>
</html>

<script type="text/javascript">

</script>

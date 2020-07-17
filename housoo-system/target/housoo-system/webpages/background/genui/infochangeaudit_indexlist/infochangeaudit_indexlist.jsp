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
    <title>数据变更首页待审核列表</title>
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
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="baseauditlist_datatablepaneltitle">数据变更申请待审核列表</h5>
</div>
<div class="titlePanel" style="display: none;">
	<div class="title-search form-horizontal" id="baseauditlist_search" formcontrolid="402881e5636e7e2c01636e8e23cb00f2">
		<table style="display: none;">
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_T.SECAUDIT_STATUS_EQ" value="0">
                			  <input type="hidden" name="Q_T.SECAUDIT_ABLEIDS_LIKE" value="${sessionScope.__BACKPLATUSER.SYSUSER_ID}">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('baseauditlist_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('baseauditlist_datatable','baseauditlist_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>

<div class="gridPanel">
	<table id="baseauditlist_datatable"></table>
</div>
<div id="baseauditlist_datatable_pager"></div>
<script type="text/javascript">

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"baseauditlist_datatable",
		  searchPanelId:"baseauditlist_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402881e5636e7e2c01636e8e23cb00f2",
		  nopager:true,
		  rowNum : -1,
		  height:340,
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
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
  return '<a onclick="addJqGridRecordInfo(\''+rowObject.SECAUDIT_ID+'\')" href="javascript:void(0);"><span style="text-decoration:underline;color:green;">'+rowObject.APPLY_NAME+'</span></a>';
},

		         
		         sortable:false
		         },
		         {name: "SECAUDIT_STATUS",label:"审核结果",
		         width: 100,align:"left",
		         hidden:true,
		         formatter:function(cellvalue, options, rowObject){
	
    
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
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "SECAUDIT_AUTIME",label:"审核时间",
		         width: 150,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "SECAUDIT_AUOPINION",label:"审核意见",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
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
</div>
  </body>
</html>

<script type="text/javascript">
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
          $("#baseauditlist_datatable").trigger("reloadGrid"); 
            
		  }
	  }
	});
}
</script>

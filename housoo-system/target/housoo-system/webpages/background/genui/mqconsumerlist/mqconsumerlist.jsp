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
    <title>MQ消费者列表</title>
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
	<h5 id="mqconsumer_datatablepaneltitle">消费者列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="mqconsumer_search" formcontrolid="402882a1606c68e301606c6e56a9000f">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
								    <plattag:radio name="Q_T.MQCONSUMER_TYPE_EQ" auth_type="write" label_col_num="3" label_value="消费者类型" static_values="所有:,队列:1,订阅:2" dyna_interface="" dyna_param="" select_first="true" allowblank="true" is_horizontal="true" comp_col_num="9">
								    </plattag:radio>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.MQCONSUMER_CODE_LIKE" auth_type="write" label_value="消息编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('mqconsumer_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('mqconsumer_datatable','mqconsumer_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="addJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		  </button>
		
	      <button type="button" onclick="editJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		  </button>
		
	      <button type="button" onclick="delJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="mqconsumer_datatable"></table>
</div>
<div id="mqconsumer_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(MQCONSUMER_ID){
	var url = "appmodel/MqConsumerController.do?goForm&UI_DESIGNCODE=mqconsumerform";
	var title = "新增MQ消费者信息";
	if(MQCONSUMER_ID){
		url+=("&MQCONSUMER_ID="+MQCONSUMER_ID);
		title = "编辑MQ消费者信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["800px","300px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#mqconsumer_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("mqconsumer_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.MQCONSUMER_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"mqconsumer_datatable",
		selectColName:"MQCONSUMER_ID",
		url:"appmodel/MqConsumerController.do?multiDel",
		callback:function(resultJson){
			$("#mqconsumer_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"mqconsumer_datatable",
		  searchPanelId:"mqconsumer_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402882a1606c68e301606c6e56a9000f",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "MQCONSUMER_ID",label:"消费者ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "MQCONSUMER_TYPE",label:"消费者类型",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="2"){
       return "<span class=\"label label-danger\">订阅</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">队列</span>";      
    }
    
},
		         
		         sortable:false
		         },
		         {name: "MQCONSUMER_CODE",label:"消息编码",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "MQCONSUMER_JAVA",label:"调用接口",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "MQCONSUMER_CREATETIME",label:"创建时间",
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

</script>

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
    <title>定时器列表</title>
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
        <h5 id="schedule_datatablepaneltitle">定时器列表</h5>
    </div>
<div class="titlePanel">
    <div class="title-search form-horizontal" id="schedule_search" formcontrolid="402848a55bae53cd015bae99e3b90022">
        <table>
        <tr>
            <td>查询条件</td>
            <td style="padding-left: 5px;">
                <div class="table-filter">
                    <input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
                    <div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
                                <div class="form-group">
                                        <plattag:input name="Q_T.SCHEDULE_NAME_LIKE" auth_type="write" label_value="定时器名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
                                </div>
                                <div class="form-group">
                                        <plattag:input name="Q_T.SCHEDULE_CODE_LIKE" auth_type="write" label_value="定时器编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
                                </div>
                                <div class="form-group">
                                        <plattag:radio name="Q_T.SCHEDULE_STATUS_EQ" auth_type="write" label_col_num="3" label_value="定时器状态" static_values="所有状态:,启用:1,停用:-1" dyna_interface="" dyna_param="" select_first="true" allowblank="true" is_horizontal="true" comp_col_num="9">
                                        </plattag:radio>
                                </div>
                        <div class="table-filter-list-bottom">
                            <a onclick="PlatUtil.tableSearchReset('schedule_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a>
                            <a onclick="PlatUtil.tableDoSearch('schedule_datatable','schedule_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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

                <button type="button" onclick="startSchedule();" platreskey="" class="btn btn-outline btn-primary btn-sm">
                <i class="fa fa-check"></i>&nbsp;启用定时器
                </button>

                <button type="button" onclick="stopSchedule();" platreskey="" class="btn btn-outline btn-danger btn-sm">
                <i class="fa fa-ban"></i>&nbsp;停用定时器
                </button>

                <button type="button" onclick="goExeLog();" platreskey="" class="btn btn-outline btn-info btn-sm">
                <i class="fa fa-search"></i>&nbsp;查看执行日志
                </button>

</div>
</div>

<div class="gridPanel">
    <table id="schedule_datatable"></table>
</div>
<div id="schedule_datatable_pager"></div>
<script type="text/javascript">
    function addJqGridRecordInfo(SCHEDULE_ID){
	var url = "system/ScheduleController.do?goForm&UI_DESIGNCODE=scheduleform";
	var title = "新增定时器信息";
	if(SCHEDULE_ID){
		url+=("&SCHEDULE_ID="+SCHEDULE_ID);
		title = "编辑定时器信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["70%","90%"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#schedule_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
    function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("schedule_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.SCHEDULE_ID);
	}
}
    function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"schedule_datatable",
		selectColName:"SCHEDULE_ID",
		url:"system/ScheduleController.do?multiDel",
		callback:function(resultJson){
			$("#schedule_datatable").trigger("reloadGrid"); 
		}
	});
}
    function startSchedule(){
	PlatUtil.operMulRecordForTable({
		tableId:"schedule_datatable",
		selectColName:"SCHEDULE_ID",
		tipMsg:"您确定要启用所选定时器吗?",
		url:"system/ScheduleController.do?updateStatus&status=1",
		callback:function(resultJson){
			$("#schedule_datatable").trigger("reloadGrid"); 
		}
	});
}
    function stopSchedule(){
	PlatUtil.operMulRecordForTable({
		tableId:"schedule_datatable",
		selectColName:"SCHEDULE_ID",
		tipMsg:"您确定要停用所选定时器吗?",
		url:"system/ScheduleController.do?updateStatus&status=-1",
		callback:function(resultJson){
			$("#schedule_datatable").trigger("reloadGrid"); 
		}
	});
}
    function goExeLog(){
	var rowData = PlatUtil.getTableOperSingleRecord("schedule_datatable");
	if(rowData){
        PlatUtil.openWindow({
          title:"查看执行日志",
          area: ["60%","70%"],
          content: "framework/ViewController.do?view&UI_DESIGNCODE=joblog_list&SCHEDULE_ID="+rowData.SCHEDULE_ID,
          end:function(){
              
          }
        });
	}
}

    $(function () {
        PlatUtil.initJqGrid({
                tableId: "schedule_datatable",
                searchPanelId: "schedule_search",
                url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55bae53cd015bae99e3b90022",
            ondblClickRow:function (rowid, iRow, iCol, e) {
                
            },
            colModel: [
            {
                name: "SCHEDULE_ID", label: "定时ID",
                width: 100, align: "left",
             hidden:
            true,

                        sortable:false
            }
            ,
            {
                name: "SCHEDULE_CODE", label: "定时器编码",
                width: 150, align: "left",
            
                        sortable:false
            }
            ,
            {
                name: "SCHEDULE_NAME", label: "定时器名称",
                width: 200, align: "left",
            
                        sortable:false
            }
            ,
            {
                name: "SCHEDULE_STATUS", label: "状态",
                width: 100, align: "left",
             formatter
            :function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="-1"){
       return "<span class=\"label label-danger\">停用</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">启用</span>";      
    }
    
},
                        sortable:false
            }
            ,
            {
                name: "SCHEDULE_CRON", label: "CRON表达式",
                width: 200, align: "left",
            
                        sortable:false
            }
            ,
            {
                name: "SCHEDULE_CLASSNAME", label: "定时器处理类",
                width: 300, align: "left",
            
                        sortable:false
            }
            ,
            {
                name: "SCHEDULE_BINDIP", label: "绑定IP地址",
                width: 100, align: "left",
            
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

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
    <title>redis缓存列表</title>
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
	<h5 id="rediskey_datatablepaneltitle">Redis缓存列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="rediskey_search" formcontrolid="402881f15e324594015e3248934d000a">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="keyName" auth_type="write" label_value="KEY名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('rediskey_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('rediskey_datatable','rediskey_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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
		
	      <button type="button" onclick="setRedisTimeLimit();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-clock-o"></i>&nbsp;设置有效时间
		  </button>
		
	      <button type="button" onclick="setPersist();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-database"></i>&nbsp;设置永久有效
		  </button>
		
	      <button type="button" onclick="editJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-edit"></i>&nbsp;编辑
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="rediskey_datatable"></table>
</div>
<div id="rediskey_datatable_pager"></div>
<script type="text/javascript">
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"rediskey_datatable",
		selectColName:"KEY_NAME",
		url:"redis/RedisController.do?multiDel",
		callback:function(resultJson){
			$("#rediskey_datatable").trigger("reloadGrid"); 
		}
	});
}
function setRedisTimeLimit(){
	var selectDatas = PlatUtil.getTableOperMulRecord("rediskey_datatable");
   if(selectDatas){
     var keyNames = "";
     $.each(selectDatas,function(index,obj){
       if(index>0){
         keyNames+=",";
       }
       keyNames+= obj.KEY_NAME;
     });
     PlatUtil.openWindow({
          title:"有效时间设置",
          area: ["800px","400px"],
          content: "redis/RedisController.do?goSetTimeLimit&keyNames="+keyNames,
          end:function(){
            if(PlatUtil.isSubmitSuccess()){
                $("#rediskey_datatable").trigger("reloadGrid"); 
            }
          }
      });
   }
}
function setPersist(){
	PlatUtil.operMulRecordForTable({
		tableId:"rediskey_datatable",
		selectColName:"KEY_NAME",
		url:"redis/RedisController.do?persist",
      tipMsg:"您确定要将所选缓存设置成永久有效?",
		callback:function(resultJson){
			$("#rediskey_datatable").trigger("reloadGrid"); 
		}
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("rediskey_datatable");
	if(rowData){
      var KEY_NAME = rowData.KEY_NAME;
      var url = "redis/RedisController.do?goForm&keyName="+KEY_NAME;
	   var title = "编辑缓存信息";
      PlatUtil.openWindow({
        title:title,
        area: ["90%","90%"],
        content: url,
        end:function(){
            if(PlatUtil.isSubmitSuccess()){
                //弹出框提交成功后,需要回调的代码
                $("#rediskey_datatable").trigger("reloadGrid"); 
            }
        }
      });
	}
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"rediskey_datatable",
		  searchPanelId:"rediskey_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402881f15e324594015e3248934d000a",
		  nopager:true,
		  rowNum : -1,
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "KEY_NAME",label:"KEY名称",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "KEY_TYPE",label:"数据类型",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "KEY_TIMELIMIT",label:"有效时间",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="-1"){
       return "<span class=\"label label-primary\">永久有效</span>";
    }else{
       return "<b>"+cellvalue+"</b>";      
    }
    
},
		         
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

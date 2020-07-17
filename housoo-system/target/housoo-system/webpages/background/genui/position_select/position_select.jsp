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
    <title>岗位选择器</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,tipsy,autocomplete,pinyin,nicevalid,slimscroll"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,tipsy,autocomplete,pinyin,nicevalid,slimscroll"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout" layoutsize="&quot;west__size&quot;:280,&quot;east__size&quot;:280">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="baselist_datatablepaneltitle">岗位列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="baselist_search" formcontrolid="297ecf126353898401635393854e0043">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_T.POSITION_CID_EQ" value="">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.POSITION_NAME_LIKE" auth_type="write" label_value="岗位名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
								    <plattag:select placeholder="" istree="false" label_col_num="3" label_value="岗位等级" style="width:100%;" allowblank="true" comp_col_num="9" auth_type="write" static_values="" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'positionlevel',ORDER_TYPE:'ASC'}" name="Q_T.POSITION_LEVEL_EQ">
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
	      <button type="button" onclick="addJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-check"></i>&nbsp;选择记录
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="baselist_datatable"></table>
</div>
<div id="baselist_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(DATASER_ID){
   var selectDatas = PlatUtil.getTableOperMulRecord("baselist_datatable");
   if(selectDatas){
     var selectRecordIds = "";
     $.each(selectDatas,function(index,obj){
       if(index>0){
         selectRecordIds+=",";
       }
       selectRecordIds+=obj.POSITION_ID;
     });
     PlatUtil.loadSelectedTable({
       tableid:"SELECTEDTABLE",
       selectedRecordIds:selectRecordIds
     });
   }
  
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"baselist_datatable",
		  searchPanelId:"baselist_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=297ecf126353898401635393854e0043",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "POSITION_ID",label:"岗位ID",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "COMPANY_NAME",label:"所属单位",
		         width: 250,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "POSITION_NAME",label:"岗位名称",
		         width: 250,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "DIC_NAME",label:"岗位等级",
		         width: 200,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "POSITION_DESC",label:"岗位描述",
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
   <plattag:treepanel id="basetree" opertip="false" panel_title="单位树面板">
</plattag:treepanel>
<script type="text/javascript">
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
			$("input[name='Q_T.POSITION_CID_EQ']").val("");
		}else{
			$("input[name='Q_T.POSITION_CID_EQ']").val(treeNode.id);
		}
		//刷新右侧列表
		PlatUtil.tableDoSearch("baselist_datatable","baselist_search");
	}
}  
  $(function(){
	  PlatUtil.initZtree({
		  treeId:"basetree",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoCompleteType:"1",
		  edit : {
				enable : false,
				showRemoveBtn : false,
				showRenameBtn : false
		  },
		  callback: {
			  onRightClick: PlatUtil.onZtreeRightClick,
			  onClick:onTreePanelClick,
			  onDrop: PlatUtil.onZtreeNodeDrop
		  },
		  async : {
			url:"common/baseController.do?tree",
			otherParam : {
				//树形表名称
				"tableName" : "PLAT_SYSTEM_COMPANY",
				//键和值的列名称
				"idAndNameCol" : "COMPANY_ID,COMPANY_NAME",
				//查询其它部门列名称
				"targetCols" : "COMPANY_CODE,COMPANY_PARENTID,COMPANY_LEVEL",
				//最先读取的根节点的值
				"loadRootId" : "0",
				//需要回填的值
				"needCheckIds":"",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//根据节点名称
				"treeTitle":"单位树"
			}
		  }
	  });
	  
});
</script>

</div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;确定
		</button>
		<button type="button" onclick="closeWindow();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
   var selectorConfig = PlatUtil.getData(PlatUtil.WIN_SELECTOR_CONFIG);
   var maxselect = selectorConfig.maxselect;
	var minselect = selectorConfig.minselect;
   var checkRecords = PlatUtil.getSelectedTableRecord("SELECTEDTABLE");
   var checkIds = checkRecords.checkIds;
   var checkNames = checkRecords.checkNames;
   if(maxselect!=0){
		if(checkRecords.length>maxselect){
			parent.layer.msg("最多只能选择"+maxselect+"条记录!", {icon: 2});
			return;
		}
	}
	if(checkRecords.length<minselect){
		parent.layer.msg("至少需要选择"+minselect+"条记录!", {icon: 2});
		return;
	}
	PlatUtil.removeData(PlatUtil.WIN_SELECTOR_CONFIG);
	PlatUtil.setData(PlatUtil.WIN_SELECTOR_RECORDS,{
		selectSuccess:true,
		checkIds:checkIds,
		checkNames:checkNames
	});
	PlatUtil.closeWindow();
}
function closeWindow(){
  PlatUtil.closeWindow();
}

</script>

</div>
   <div class="ui-layout-east" platundragable="true" compcode="direct_layout">
   <jsp:include page="/webpages/common/plattagtpl/selectedtable_tag.jsp">
    <jsp:param value="SELECTEDTABLE" name="tableid"></jsp:param>
    <jsp:param value="岗位名称" name="showcolnames"></jsp:param>
    <jsp:param value="POSITION_NAME" name="showcolcodes"></jsp:param>
    <jsp:param value="POSITION_ID" name="checkvaluecol"></jsp:param>
    <jsp:param value="POSITION_NAME" name="checklabelcol"></jsp:param>
    <jsp:param value="positionService.findSelected" name="dynainterface"></jsp:param>
    <jsp:param value="${selectedRecordIds}" name="selectedRecordIds"></jsp:param>
</jsp:include>
</div>
</div>
  </body>
</html>

<script type="text/javascript">

</script>

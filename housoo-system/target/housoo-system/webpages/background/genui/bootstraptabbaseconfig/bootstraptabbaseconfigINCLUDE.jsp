<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" compcode="formcontainer" action="" id="BaseConfigForm" style="">

<div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		基本配置
	</span>
</div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONTROL_ID" allowblank="false" auth_type="write" value="${fieldInfo.CONTROL_ID}" datarule="required;" label_value="TAB的ID" placeholder="请输入TAB的ID" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		页签配置
	</span>
</div><div class="titlePanel">
  <div class="title-search form-horizontal" id="tabConfig_search">
    <table style="display: none;">
      <tr>
        <td>查询条件</td>
        <td style="padding-left: 5px;">
          <div class="table-filter">
                        <input type="hidden" name="FORMCONTROL_ID" value="${formControl.FORMCONTROL_ID}">
            <input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
            <div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
              <div class="table-filter-list-bottom">
                <a onclick="PlatUtil.tableSearchReset('tabConfig_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
                <a onclick="PlatUtil.tableDoSearch('tabConfig_datatable','tabConfig_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
              </div>
            </div>
          </div>
        </td>
      </tr>
    </table>
  </div>
  <div class="toolbar">
    <button type="button" onclick="addEditTableRow();" id="" class="btn btn-outline btn-info btn-sm">
      <i class="fa fa-plus"></i>&nbsp;新增
    </button>
    <button type="button" onclick="delEditTableRow();" id="" class="btn btn-outline btn-danger btn-sm">
      <i class="fa fa-trash"></i>&nbsp;删除
    </button>
  </div>
</div>

<div class="gridPanel">
  <plattag:edittable dyna_interface="commonUIService.findTabConfigList" tr_tplpath="background/genui/bootstraptabbaseconfig_sub/tabConfig" dragable="true" id="tabConfig" searchpanel_id="tabConfig_search" col_style="[10%,页签ID],[25%,页签名称],[15%,点击重绘高度],[25%,页签点击事件名称],[25%,页签点击函数实现]">
  </plattag:edittable>
</div>

<script type="text/javascript">
function addEditTableRow(){
	PlatUtil.addEditTableRow({
		tableId:"tabConfig"
	});
}
function delEditTableRow(){
  PlatUtil.removeEditTableRows('tabConfig');
}

function clickButtonFn(columnName){
	var expcodePath = "edittable/buttonexpjs.js";
	var oldConfigData = $("input[name='"+columnName+"']").val();
	if(oldConfigData&&oldConfigData!=""){
		PlatUtil.setData(columnName,oldConfigData);
	}
	var url = "appmodel/FormControlController.do?goExpCodeView&allowedit=true&expcodePath="+expcodePath;
	url+="&keyName="+columnName;
	PlatUtil.openWindow({
		title:"点击函数实现",
		area: ["80%","70%"],
		content: url,
		end:function(){
		    if(PlatUtil.isSubmitSuccess()){
		    	var configCode = PlatUtil.getData(columnName);
		    	$("input[name='"+columnName+"']").val(configCode);
		    	PlatUtil.removeData(columnName);
		    }
		}
	});
}
</script>
             </form>

<script type="text/javascript">

</script>

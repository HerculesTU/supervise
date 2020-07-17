<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" compcode="formcontainer" action="" id="BaseConfigForm" style="">

<div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		基本信息
	</span>
</div><div class="form-group" compcode="formgroup">
   <plattag:select name="ASSOCIAL_FIELDNAME" allowblank="true" auth_type="write" value="${fieldInfo.ASSOCIAL_FIELDNAME}" istree="true" onlyselectleaf="true" label_value="关联字段" placeholder="请选择关联字段" comp_col_num="3" label_col_num="1" style="width:100%;" dyna_interface="designService.findSelectFields" dyna_param="${formControl.FORMCONTROL_DESIGN_ID}">
   </plattag:select>
   <plattag:input name="CONTROL_NAME" allowblank="false" auth_type="write" value="${fieldInfo.CONTROL_NAME}" datarule="required;" label_value="控件命名" placeholder="请输入控件命名" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:radio name="ALLOW_BLANK" allowblank="false" auth_type="write" value="${fieldInfo.ALLOW_BLANK}" select_first="true" is_horizontal="true" label_value="允许为空" comp_col_num="3" label_col_num="1" static_values="允许:true,不允许:false">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:radio name="CONTROL_AUTH" allowblank="false" auth_type="write" value="${fieldInfo.CONTROL_AUTH}" select_first="true" is_horizontal="true" label_value="权限" comp_col_num="3" label_col_num="1" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'COMP_AUTH',ORDER_TYPE:'ASC'}">
   </plattag:radio>
   <plattag:input name="CONTROL_VALUE" allowblank="true" auth_type="write" value="${fieldInfo.CONTROL_VALUE}" label_value="控件值" placeholder="请输入控件值" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="PLAT_COMPNAME" allowblank="false" auth_type="write" value="${fieldInfo.PLAT_COMPNAME}" datarule="required;" label_value="中文标识" placeholder="请输入中文标识" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONTROL_LABEL" allowblank="true" auth_type="write" value="${fieldInfo.CONTROL_LABEL}" label_value="控件标签" placeholder="请输入控件标签" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="MAX_LENGTH" allowblank="true" auth_type="write" value="${fieldInfo.MAX_LENGTH}" label_value="输入最大值" placeholder="请输入允许输入最大值" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="MIN_LENGTH" allowblank="true" auth_type="write" value="${fieldInfo.MIN_LENGTH}" label_value="输入最小值" placeholder="请输入至少输入长度" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="COMP_PLACEHOLDER" allowblank="true" auth_type="write" value="${fieldInfo.COMP_PLACEHOLDER}" label_value="输入提示" placeholder="请输入输入提示" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="COMP_COL_NUM" allowblank="true" auth_type="write" value="${fieldInfo.COMP_COL_NUM}" label_value="控件栅格列数" placeholder="请输入控件栅格列数" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="LABEL_COL_NUM" allowblank="true" auth_type="write" value="${fieldInfo.LABEL_COL_NUM}" label_value="标签栅格列数" placeholder="请输入标签栅格列数" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONTROL_ID" allowblank="true" auth_type="write" value="${fieldInfo.CONTROL_ID}" label_value="控件ID" placeholder="请输入控件ID" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="CONTROL_STYLE" allowblank="true" auth_type="write" value="${fieldInfo.CONTROL_STYLE}" label_value="控件STYLE" placeholder="请输入控件STYLE" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="CONTROL_ATTACH" allowblank="true" auth_type="write" value="${fieldInfo.CONTROL_ATTACH}" label_value="附加属性" placeholder="请输入附加属性" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div></form>

<script type="text/javascript">

</script>

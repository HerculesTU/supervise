<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" compcode="formcontainer" action="" id="BaseConfigForm" style="">

<div class="form-group" compcode="formgroup">
   <plattag:input name="CONTROL_ID" allowblank="false" auth_type="write" value="${fieldInfo.CONTROL_ID}" datarule="required;" label_value="控件ID" placeholder="请输入控件ID" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:number name="HEIGHT" allowblank="false" auth_type="write" value="${fieldInfo.HEIGHT}" step="10" decimals="0" postfix="PX" label_value="控件高度" placeholder="请输入控件高度" comp_col_num="4" label_col_num="2" max="10000">
   </plattag:number>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="DYNA_INTERFACE" allowblank="false" auth_type="write" value="${fieldInfo.DYNA_INTERFACE}" datarule="required;" label_value="数据源接口" placeholder="请输入数据源接口" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="DYNA_PARAM" allowblank="false" auth_type="write" value="${fieldInfo.DYNA_PARAM}" datarule="required;" label_value="动态参数" placeholder="请输入动态参数" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CLICKFN_NAME" allowblank="false" auth_type="write" value="${fieldInfo.CLICKFN_NAME}" datarule="required;" label_value="点击函数名称" placeholder="请输入点击函数名称" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div>   <plattag:textarea name="CLICKFN_CONTENT" allowblank="true" auth_type="write" value="${fieldInfo.CLICKFN_CONTENT}" codemirror="codemirror" label_value="点击函数实现" comp_col_num="10" label_col_num="2" id="CLICKFN_CONTENT">
   </plattag:textarea>

<script type="text/javascript">

</script>
</form>

<script type="text/javascript">

</script>

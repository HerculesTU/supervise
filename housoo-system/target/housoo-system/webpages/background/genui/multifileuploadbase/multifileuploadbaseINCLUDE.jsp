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
   <plattag:input name="BIND_ID" allowblank="false" auth_type="write" value="${fieldInfo.BIND_ID}" datarule="required;" label_value="绑定控件ID" placeholder="请输入绑定ID" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="UPLOAD_ROOTFOLDER" allowblank="false" auth_type="write" value="${fieldInfo.UPLOAD_ROOTFOLDER}" datarule="required;letters;" label_value="上传目录名称" placeholder="请输入上传目录名称" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="PLAT_COMPNAME" allowblank="false" auth_type="write" value="${fieldInfo.PLAT_COMPNAME}" datarule="required;" label_value="中文标识" placeholder="请输入中文标识" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="FILESIGNLE_LIMIT" allowblank="false" auth_type="write" value="${fieldInfo.FILESIGNLE_LIMIT}" datarule="required;positiveInteger;" label_value="大小限制" placeholder="请输入大小限制,字节是单位" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="ALLOW_FILEEXTS" allowblank="false" auth_type="write" value="${fieldInfo.ALLOW_FILEEXTS}" datarule="required;" label_value="限制文件类型" placeholder="限制文件类型用英文逗号分割" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:radio name="IS_INIT" allowblank="false" auth_type="write" value="${fieldInfo.IS_INIT}" select_first="true" is_horizontal="true" label_value="初始化" comp_col_num="3" label_col_num="1" static_values="是:1,否:-1">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="BUS_TABLENAME" allowblank="false" auth_type="write" value="${fieldInfo.BUS_TABLENAME}" datarule="required;" label_value="关联表名" placeholder="请输入关联表名" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="BUS_RECORDID" allowblank="false" auth_type="write" value="${fieldInfo.BUS_RECORDID}" datarule="required;" label_value="关联记录ID" placeholder="请输入关联记录ID" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="FILE_TYPEKEY" allowblank="true" auth_type="write" value="${fieldInfo.FILE_TYPEKEY}" label_value="附件类别KEY" placeholder="请输入附件类别KEY" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="FILE_RIGHTS" allowblank="true" auth_type="write" value="${fieldInfo.FILE_RIGHTS}" label_value="权限值" placeholder="请输入upload,del或者none" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:radio name="FILE_UPSERVER" allowblank="true" auth_type="write" value="${fieldInfo.FILE_UPSERVER}" select_first="true" is_horizontal="true" label_value="上传服务器" comp_col_num="3" label_col_num="1" static_values="应用服务器:1,文件服务器:2">
   </plattag:radio>
   <plattag:input name="FILE_UPURL" allowblank="true" auth_type="write" value="${fieldInfo.FILE_UPURL}" label_value="自定义上传地址" placeholder="请输入自定义上传地址" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div></form>

<script type="text/javascript">

</script>

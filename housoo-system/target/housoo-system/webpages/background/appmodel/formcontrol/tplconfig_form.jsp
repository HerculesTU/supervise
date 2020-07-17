<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" id="TemplateConfigForm">
	<div class="form-group">
		<plattag:textarea name="FORMCONTROL_TPLCODE" auth_type="write"
		    style="height:200px;"
			label_col_num="2" label_value="模版源代码" id="FORMCONTROL_TPLCODE"
			value="${formControl.FORMCONTROL_TPLCODE}" allowblank="false"
			placeholder="请输入模版源代码" comp_col_num="10">
		</plattag:textarea>
	</div>
</form>

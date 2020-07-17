<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<title>服务申请审批单</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<plattag:resources restype="css"
	loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
<plattag:resources restype="js"
	loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
</head>

<body>
	<script type="text/javascript">
		$(function() {
			PlatUtil.initUIComp();
		});
	</script>
	<div class="plat-directlayout" style="height:100%"
		platundragable="true" compcode="direct_layout">
		<div class="ui-layout-center" platundragable="true"
			compcode="direct_layout" style="overflow-y:auto;">
			<form method="post" class="form-horizontal" compcode="formcontainer"
				action="resledger/SerApplyController.do?updateAuditResult"
				id="serapplyform" style="">

				<input type="hidden" name="SERAPPLY_ID"
					value="${serApply.SERAPPLY_ID}">
				<div class="form-group plat-form-title" compcode="formtitle" id="">
					<span class="plat-current"> 基本信息 </span>
				</div>
				<div class="form-group" compcode="formgroup">
					<plattag:input name="SERAPPLY_PRJNAME" allowblank="false"
						auth_type="readonly" value="${serApply.SERAPPLY_PRJNAME}"
						datarule="required;" maxlength="30" label_value="项目名称"
						placeholder="请输入项目名称" comp_col_num="10" label_col_num="2">
					</plattag:input>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group" compcode="formgroup">
					<plattag:input name="SERAPPLY_LINKMAN" allowblank="false"
						auth_type="readonly" value="${serApply.SERAPPLY_LINKMAN}"
						datarule="required;" maxlength="6" label_value="联系人"
						placeholder="请输入联系人" comp_col_num="4" label_col_num="2">
					</plattag:input>
					<plattag:input name="SERAPPLY_LINKPHONE" allowblank="false"
						auth_type="readonly" value="${serApply.SERAPPLY_LINKPHONE}"
						datarule="required;mobile;" maxlength="14" label_value="联系手机号"
						placeholder="请输入联系手机号" comp_col_num="4" label_col_num="2">
					</plattag:input>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group" compcode="formgroup">
					<plattag:input name="SERAPPLY_EMAIL" allowblank="true"
						auth_type="readonly" value="${serApply.SERAPPLY_EMAIL}"
						datarule="email;" maxlength="30" label_value="电子邮箱"
						placeholder="请输入电子邮箱" comp_col_num="4" label_col_num="2">
					</plattag:input>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group" compcode="formgroup">
					<plattag:textarea name="SERAPPLY_REASON" allowblank="false"
						auth_type="readonly" value="${serApply.SERAPPLY_REASON}"
						maxlength="1022" label_value="申请理由" placeholder="请输入申请理由"
						comp_col_num="10" label_col_num="2">
					</plattag:textarea>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group plat-form-title" compcode="formtitle" id="">
					<span class="plat-current"> 申请服务列表 </span>
				</div>
				<table class="table table-bordered table-hover" >
				    <thead>
						<tr class="active">
						    <th style="width:30%;">服务编码</th>
							<th style="width:70%;">服务名称</th>
						</tr>
					</thead>
					<tbody>
					    <c:forEach items="${serviceList}" var="service">
					    <tr>
						    <td>${service.REQSER_CODE}</td>
						    <td>${service.REQSER_NAME}</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="form-group plat-form-title" compcode="formtitle" id="">
					<span class="plat-current"> 申请IP地址 </span>
				</div>
				<table class="table table-bordered table-hover" >
				    <thead>
						<tr class="active">
						    <th style="width:100%;text-align: center;">IP地址</th>
						</tr>
					</thead>
					<tbody>
					    <c:forEach items="${ipList}" var="ipInfo">
					    <tr>
						    <td style="text-align: center;">${ipInfo.IP_ADDRESS}</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="form-group" compcode="formgroup">
					<plattag:radio name="SERAPPLY_RESULT" allowblank="false"
						auth_type="write" value="${serApply.SERAPPLY_RESULT}"
						select_first="true" is_horizontal="true" label_value="审批结果"
						comp_col_num="4" label_col_num="2" static_values="通过:1,不通过:-1">
					</plattag:radio>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group" compcode="formgroup">
					<plattag:textarea name="SERAPPLY_OPINON" allowblank="true"
						auth_type="write" value="${serApply.SERAPPLY_OPINON}"
						maxlength="510" label_value="审批意见" placeholder="请输入审批意见"
						comp_col_num="10" label_col_num="2">
					</plattag:textarea>
				</div>
				<div class="hr-line-dashed"></div>
			</form>
		</div>
		<div class="ui-layout-south" platundragable="true"
			compcode="direct_layout">
			<div class="row"
				style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;"
				platundragable="true" compcode="buttontoolbar">
				<div class="col-sm-12 text-right">
					<button type="button" onclick="submitBusForm();" platreskey=""
						class="btn btn-outline btn-primary btn-sm">
						<i class="fa fa-check"></i>&nbsp;提交
					</button>
					<button type="button" onclick="closeWindow();" platreskey=""
						class="btn btn-outline btn-danger btn-sm">
						<i class="fa fa-times"></i>&nbsp;关闭
					</button>
				</div>
			</div>

			<script type="text/javascript">
				function submitBusForm() {
					if (PlatUtil.isFormValid("#serapplyform")) {
						var url = $("#serapplyform").attr("action");
						var formData = PlatUtil.getFormEleData("serapplyform");
						PlatUtil.ajaxProgress({
							url : url,
							params : formData,
							callback : function(resultJson) {
								if (resultJson.success) {
									parent.layer.msg(PlatUtil.SUCCESS_MSG, {
										icon : 1
									});
									PlatUtil.setData("submitSuccess", true);
									PlatUtil.closeWindow();
								} else {
									parent.layer.alert(PlatUtil.FAIL_MSG, {
										icon : 2,
										resize : false
									});
								}
							}
						});
					}
				}
				function closeWindow() {
					PlatUtil.closeWindow();
				}
			</script>

		</div>
	</div>
</body>
</html>

<script type="text/javascript">

</script>

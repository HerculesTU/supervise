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
    
    <title>督办环节配置表单-在线设计开发</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,plat-ui,tipsy,autocomplete,layer,nicevalid,codemirror,webuploader,fancybox,flowdesign,touchspin,ratingstar,ueditor">
	</plattag:resources>
	<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin,layer,nicevalid,codemirror,slimscroll,webuploader,fancybox,flowdesign,touchspin,echart,ratingstar,superslide,ueditor">
	</plattag:resources>
	<style type="text/css">
	  /* .plat-designborder{position:relative;border:2px dashed #d9534f !important; background:#F6F6F6 !important;}
	  .plat-designtoolbar{position: absolute;z-index: 100000;right: 0;top: 0;}
	  .platdesigncomp{padding: 22px;} */
	  .plat-designhighlightborder{position:relative;border:2px dashed #C81522 !important;}
	  .plat-designhighlighttoolbar{position: absolute;z-index: 100000;right: 0;top: 0;}
	  .plat-designhighlightclear{ clear:both} 
	</style>
  </head>
  
  <body platcomid="0" class="platdesigncomp" platcompname="body容器" uibtnsrights="add" compcontrolid="0">
      <script type="text/javascript">
      $(function(){
    		PlatUtil.initUIComp();
      });
      </script>
      <input type="hidden" id="__BACK_PLAT_USER_JSON" value="${sessionScope.__BACK_PLAT_USER_JSON}">
      <input type="hidden" id="ONLINE_DESIGNID" value="4028819e7172a0d00171730f35d300ec">
      <div class="plat-directlayout platdesigncomp" style="height:100%" platcompname="东西南北中布局" platcomid="4028819e7172a0d0017173217079019b" uibtnsrights="edit,del" platundragable="true" compcontrolid="4028819e7172a0d0017173217079019b" compcode="direct_layout">
   <div class="ui-layout-center platdesigncomp" platcompname="中央布局" platcomid="4028819e7172a0d0017173217079019b_0" uibtnsrights="add" platundragable="true" compcontrolid="4028819e7172a0d0017173217079019b" compcode="direct_layout">
   <form method="post" class="form-horizontal platdesigncomp" platcompname="表单容器" compcode="formcontainer" compcontrolid="4028819e7172a0d001717321b18c01a5" platcomid="4028819e7172a0d001717321b18c01a5" uibtnsrights="add,edit,del" action="supervise/SuperviseApproveController.do?saveOrUpdate" id="superviseapproveform" style="">

<div class="form-group platdesigncomp" platcompname="主键ID" compcode="hiddencomp" compcontrolid="4028819e7172a0d001717322686201a9" platcomid="4028819e7172a0d001717322686201a9" uibtnsrights="edit,del">
    <plattag:input name="HIDDEN_NAME" auth_type="readonly" allowblank="true" placeholder="隐藏域控件:主键ID" comp_col_num="12"></plattag:input>
</div>
<div class="form-group platdesigncomp" compcode="formgroup" platcompname="表单组" compcontrolid="4028819e7172a0d001717322b00901af" platcomid="4028819e7172a0d001717322b00901af" uibtnsrights="add,edit,del">
<div class="platdesigncomp" platcompname="节点ID" compcode="inputcomp" compcontrolid="4028819e717626e5017176282b390011" platcomid="4028819e717626e5017176282b390011" uibtnsrights="edit,del">
   <plattag:input name="NODE_ID" allowblank="false" auth_type="write" value="${superviseApprove.NODE_ID}" datarule="required;" label_value="节点ID" placeholder="请输入节点ID" comp_col_num="6" label_col_num="3">
   </plattag:input>
</div>
</div>
<div class="hr-line-dashed"></div><div class="form-group platdesigncomp" compcode="formgroup" platcompname="表单组" compcontrolid="4028819e71807231017180e7bf6a01e6" platcomid="4028819e71807231017180e7bf6a01e6" uibtnsrights="add,edit,del">
<div class="platdesigncomp" platcompname="节点名称" compcode="inputcomp" compcontrolid="4028819e717626e5017176288168001e" platcomid="4028819e717626e5017176288168001e" uibtnsrights="edit,del">
   <plattag:input name="NODE_NAME" allowblank="false" auth_type="write" value="${superviseApprove.NODE_NAME}" datarule="required;" maxlength="100" label_value="节点名称" placeholder="请输入节点名称" comp_col_num="6" label_col_num="3">
   </plattag:input>
</div>
</div>
<div class="hr-line-dashed"></div><div class="form-group platdesigncomp" compcode="formgroup" platcompname="表单组" compcontrolid="4028819e719aa39f01719ace230c0043" platcomid="4028819e719aa39f01719ace230c0043" uibtnsrights="add,edit,del">
<div class="platdesigncomp" platcompname="节点简称" compcode="inputcomp" compcontrolid="4028819e719aa39f01719ace50f00045" platcomid="4028819e719aa39f01719ace50f00045" uibtnsrights="edit,del">
   <plattag:input name="SHORT_NAME" allowblank="false" auth_type="write" value="${superviseApprove.SHORT_NAME}" datarule="required;chinese;" maxlength="2" label_value="节点简称" placeholder="请输入节点简称" comp_col_num="6" label_col_num="3">
   </plattag:input>
</div>
</div>
<div class="hr-line-dashed"></div><div class="form-group platdesigncomp" compcode="formgroup" platcompname="表单组" compcontrolid="4028819e71807231017180e7ffb601e9" platcomid="4028819e71807231017180e7ffb601e9" uibtnsrights="add,edit,del">
<div class="platdesigncomp" platcompname="备注信息" compcode="textareacomp" compcontrolid="4028819e717626e50171762907ad0038" platcomid="4028819e717626e50171762907ad0038" uibtnsrights="edit,del">
   <plattag:textarea name="REMARK" allowblank="true" auth_type="write" value="${superviseApprove.REMARK}" maxlength="125" label_value="备注信息" placeholder="请输入备注信息" comp_col_num="6" label_col_num="3">
   </plattag:textarea>
</div>

<script type="text/javascript">

</script>
</div>
<div class="hr-line-dashed"></div><div class="form-group platdesigncomp" compcode="formgroup" platcompname="表单组" compcontrolid="4028819e717626e501717635719d00d1" platcomid="4028819e717626e501717635719d00d1" uibtnsrights="add,edit,del">
<div class="platdesigncomp" platcompname="是否需要审批" compcode="radiocomp" compcontrolid="4028819e717b48c701717bd750080066" platcomid="4028819e717b48c701717bd750080066" uibtnsrights="edit,del">
   <plattag:radio name="NEED_APPROVE_FLAG" allowblank="false" auth_type="write" value="${superviseApprove.NEED_APPROVE_FLAG}" select_first="true" is_horizontal="true" label_value="是否需要审批" comp_col_num="6" label_col_num="3" static_values="是:1,否:2">
   </plattag:radio>
</div>
</div>
<div class="hr-line-dashed"></div><div class="form-group platdesigncomp" compcode="formgroup" platcompname="表单组" compcontrolid="4028819e71807231017180e7681c01e3" platcomid="4028819e71807231017180e7681c01e3" uibtnsrights="add,edit,del">
<div class="platdesigncomp" platcompname="是否标记删除" compcode="radiocomp" compcontrolid="4028819e717626e50171763595c300d3" platcomid="4028819e717626e50171763595c300d3" uibtnsrights="edit,del">
   <plattag:radio name="DEL_FLAG" allowblank="true" auth_type="write" value="${superviseApprove.DEL_FLAG}" select_first="true" is_horizontal="true" label_value="是否标记删除" comp_col_num="6" label_col_num="3" static_values="正常:1,删除:2">
   </plattag:radio>
</div>
</div>
<div class="hr-line-dashed"></div></form></div>
   <div class="ui-layout-south platdesigncomp" platcompname="南部布局" platcomid="4028819e7172a0d0017173217079019b_1" uibtnsrights="add" platundragable="true" compcontrolid="4028819e7172a0d0017173217079019b" compcode="direct_layout">
   <div class="row platdesigncomp" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar" platcompname="按钮工具栏" compcontrolid="4028819e717626e50171762974500045" platcomid="4028819e717626e50171762974500045" uibtnsrights="edit,del">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" platreskey="" id="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;提交
		</button>
		<button type="button" onclick="closeWindow();" platreskey="" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
	if(PlatUtil.isFormValid("#superviseapproveform")){
		var url = $("#superviseapproveform").attr("action");
		var formData = PlatUtil.getFormEleData("superviseapproveform");
		PlatUtil.ajaxProgress({
			url:url,
			params : formData,
			callback : function(resultJson) {
				if (resultJson.success) {
             if(resultJson.msg){
               parent.layer.msg(resultJson.msg, {icon: 1});
             }else{
               parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
             }
					PlatUtil.setData("submitSuccess",true);
					PlatUtil.closeWindow();
				} else {
             if(resultJson.msg){
               parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
             }else{
               parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
             }
					
				}
			}
		});
	}
}
function closeWindow(){
  PlatUtil.closeWindow();
}

</script>

</div>
</div>
  </body>
</html>

<script type="text/javascript">

$(function(){
  var RECORD_ID = $("input[name='RECORD_ID']").val();
  if(RECORD_ID){
    PlatUtil.changeUICompAuth("readonly","NODE_ID");
  }
})

function highlightBorder(id,name){
	$("[platcomid]").each(function(){
		if($(this).is(".plat-designhighlightborder")){
			$(this).removeClass("plat-designhighlightborder");
			$(this).find(".plat-designhighlighttoolbar").remove();
			$(this).find(".plat-designhighlightclear").remove();
		}
	});
	$("[platcomid='"+id+"']").addClass("plat-designhighlightborder");
	var toolbarHtml = "<div class=\"plat-designhighlighttoolbar\"><b>"+name+"</b></div><div class=\"plat-designhighlightclear\"></div>";
	$("[platcomid='"+id+"']").append(toolbarHtml);
}
</script>

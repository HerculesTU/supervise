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
    
    <title>My JSP 'dbmanager_view.jsp' starting page</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-checkbox,plat-ui,codemirror,nicevalid">
	</plattag:resources>
  </head>
  
  <body>
    <div class="plat-directlayout" style="height: 100%;width: 100%;">
      <div class="ui-layout-center" style="overflow-y:auto; ">
  		    <form method="post" class="form-horizontal" id="TemplateConfigForm">
				<div class="form-group">
					<plattag:textarea name="EXP_CODE" auth_type="write"
					    id="EXP_CODE"
						value="${expCode}" allowblank="false"
						placeholder="请输入模版源代码" comp_col_num="12">
					</plattag:textarea>
				</div>
			</form>
      </div>
      <div class="ui-layout-south">
		   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
                   <div class="col-sm-12 text-right">
                       <c:if test="${allowedit!=null&&allowedit=='true'}">
                       <button class="btn btn-outline btn-primary btn-sm" onclick="saveExpCode();" type="button" ><i class="fa fa-check"></i>提交</button>
                       </c:if>
                       <button class="btn btn-outline btn-danger btn-sm" onclick="PlatUtil.closeWindow();" type="button"><i class="fa fa-times"></i>关闭</button>
                   </div>
           </div>
	  </div>
	 </div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jquery-layout,plat-util,codemirror,nicevalid">
</plattag:resources>
<script type="text/javascript">
var editor = null;
function saveExpCode(){
	var configCode = editor.getValue();
	PlatUtil.setData("${keyName}",configCode);
	PlatUtil.setData("submitSuccess",true);
	PlatUtil.closeWindow();
}

$(function(){
	var configCode = PlatUtil.getData("${keyName}");
	PlatUtil.initUIComp();
	editor = CodeMirror.fromTextArea(document.getElementById("EXP_CODE"), {
		  mode: "javascript",
		  theme:"blackboard",
		  autoCloseTags: true,
		  autoCloseBrackets: true,
		  styleActiveLine: true,
		  lineNumbers: true,
		  lineWrapping: true,
		  matchBrackets: true,
		  extraKeys: {
	        "F11": function(cm) {
	          cm.setOption("fullScreen", !cm.getOption("fullScreen"));
	        },
	        "Esc": function(cm) {
	          if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
	        },
	        "Alt-F": "findPersistent"
	      }
	});
	if(configCode){
		editor.setValue(configCode);
	}
	editor.setSize("auto","500px");
});
</script>

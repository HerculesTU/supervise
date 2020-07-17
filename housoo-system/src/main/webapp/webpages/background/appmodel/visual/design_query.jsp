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
    
    <title>${design.DESIGN_NAME}</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,plat-ui,codemirror,nicevalid">
	</plattag:resources>
  </head>
  
  <body class="plat-directlayout">
      <div class="ui-layout-center" style="overflow-y:auto; ">
  		     <form method="post" class="form-horizontal" action="appmodel/GenCmpTplController.do?saveOrUpdate" id="GenCmpTplForm">
				   <div class="form-group">
				      <plattag:input name="DESIGN_URL" auth_type="readonly" label_col_num="2"
				      label_value="请求路径" value="${design.DESIGN_URL}" 
				      allowblank="false" placeholder="请输入模版说明" comp_col_num="10"></plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
						<plattag:textarea name="DESIGN_CODE" auth_type="write"
						    id="DESIGN_CODE" allowblank="true" label_col_num="2"
						    label_value="生成源码" value="${design.DESIGN_CODE}" 
							placeholder="请输入模版源代码" comp_col_num="10">
						</plattag:textarea>
					</div>
              </form>
      </div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jquery-layout,plat-util,codemirror,nicevalid">
</plattag:resources>
<script type="text/javascript">
var editor = null;

$(function(){
	PlatUtil.initUIComp();
	editor = CodeMirror.fromTextArea(document.getElementById("DESIGN_CODE"), {
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
	editor.setSize("auto","auto");
});

</script>

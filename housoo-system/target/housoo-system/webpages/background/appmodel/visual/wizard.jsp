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
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,plat-ui,nicevalid">
	</plattag:resources>
  </head>
  
  <body>
      <div class="ui-layout-center" style="overflow-y:auto; ">
            <div class="plat-wizard" >
		        <ul class="steps">
		            <li class="active"><span class="step">1</span>开始<span class="chevron"></span></li>
		            <li class=""><span class="step">2</span>步骤二<span class="chevron"></span></li>
		            <li class=""><span class="step">3</span>步骤三<span class="chevron"></span></li>
					<li class=""><span class="step">4</span>步骤四<span class="chevron"></span></li>
					<li class=""><span class="step">5</span>步骤五<span class="chevron"></span></li>
					<li class=""><span class="step">6</span>结束<span class="chevron"></span></li>
		        </ul>
		    </div>
		    <div id="wizard-showdiv">
		    	<div>
		    	   <form method="post" class="form-horizontal" action="appmodel/DesignController.do?saveOrUpdateTable" id="DesignForm">
  		           <input type="hidden" name="DESIGN_ID" value="${design.DESIGN_ID}">
  		           <input type="hidden" name="MODULE_ID" value="${design.MODULE_ID}" >
  		           <input type="hidden" name="VALID_TABLENAME" value="T_APPMODEL_DESIGN">
  		           <input type="hidden" name="VALID_FIELDLABLE" value="设计编码">
  		           <input type="hidden" name="VALID_FIELDNAME" value="DESIGN_CODE">
  		           <div class="form-group plat-form-title">
                        <span class="plat-current">
							基本信息
						</span>
				   </div>
				   <div class="form-group">
				      <plattag:input name="MODULE_NAME" auth_type="readonly" allowblank="false" placeholder="" 
				      comp_col_num="4" label_col_num="2" label_value="所在模块" value="${design.MODULE_NAME}" >
				      </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
               	   <div class="form-group">
               	     <plattag:input name="DICTYPE_CODE" auth_type="${design.DESIGN_ID!=null?'readonly':'write'}" 
               	       allowblank="false" 
				       placeholder="请输入设计编码" comp_col_num="4" value="${design.DESIGN_CODE}"
				       label_col_num="2" label_value="设计编码" 
				       datarule="${design.DESIGN_ID==null?'required;onlyLetterNumberUnderLine;remote[common/baseController.do?checkUnique, VALID_TABLENAME, VALID_FIELDLABLE, VALID_FIELDNAME]':''}"
				       ></plattag:input>
				      <plattag:input name="DESIGN_NAME" auth_type="write" allowblank="false" placeholder="请输入设计名称" 
				      comp_col_num="4" label_col_num="2" maxlength="30" datarule="required;"
				      label_value="设计名称" value="${design.DESIGN_NAME}" >
				      </plattag:input>
                   </div>
                   </form>
		    	</div>
		    	<div style="display: none;">
		    	    2
		    	</div>
		    	<div style="display: none;">3</div>
		    	<div style="display: none;">4</div>
		    	<div style="display: none;">5</div>
		    	<div style="display: none;">6</div>
		    </div>
      </div>
      <div class="ui-layout-south">
		   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
                   <div class="col-sm-12 text-right">
                       <button class="btn btn-outline btn-primary btn-sm" id="WIZARD_PREBTN" onclick="PlatUtil.wizardPre();" type="button" ><i class="fa fa-arrow-left"></i>上一步</button>
                       <button class="btn btn-outline btn-primary btn-sm" id="WIZARD_NEXTBTN" onclick="PlatUtil.wizardNext(myfn);" type="button" ><i class="fa fa-arrow-right"></i>下一步</button>
                       <button class="btn btn-outline btn-primary btn-sm" onclick="submitDesignForm();" type="button" ><i class="fa fa-check"></i>完成</button>
                       <button class="btn btn-outline btn-danger btn-sm" onclick="PlatUtil.closeWindow();" type="button"><i class="fa fa-times"></i>关闭</button>
                   </div>
           </div>
	  </div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,jquery-layout,plat-util,nicevalid">
</plattag:resources>
<script type="text/javascript">
function myfn(){
	var result = false;
	PlatUtil.ajaxProgress({
		url:"common/baseController.do?test",
	    //设置同步请求
		async:"-1",		
		params : {
			
		},
		callback : function(resultJson) {
			if (resultJson.success) {
				result = true;
			} else {
				parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
				result = false;
			}
		}
	});
	return result;
}

$(function(){
	$("body").layout({ resizable:false});
	//初始化UI控件
	PlatUtil.initUIComp();
});
</script>

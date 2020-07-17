<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<!DOCTYPE html>
<html>
  <body>
      <div class="ui-layout-west" >
	        <plattag:listgroup onclickfn="reloadUiCompDatatable" grouptitle="组件类别列表" 
	        dyna_interface="dictionaryService.findUiTypeList" dyna_param="{TYPE_CODE:'CONTROL_TYPE',ORDER_TYPE:'ASC'}"
	        ></plattag:listgroup>
		</div>
  		<div class="ui-layout-center" >
  		</div>
  </body>
</html>

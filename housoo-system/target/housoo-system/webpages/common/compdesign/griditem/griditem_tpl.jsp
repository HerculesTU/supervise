<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<c:forEach items="${EDIT_DATAS}" var="DATA" >
   <div ondblclick="removeGridItem(this);" class="card-box active" data-record="${DATA.DATA_RECORD}" id="${DATA.ITEM_KEY}">
   		<div class="card-box-img">
   			<i class="fa ${DATA.ICONFONT}" style="font-size: 60px;"></i>
   		</div>
   		<div class="card-box-content">
   		    <c:forEach items="${DATA.FIELD_DATAS}" var="FIELD" >
   		    <p title="${FIELD.VALUE}" itemkey="${FIELD.COLKEY}"  
   		       <c:if test="${FIELD.ISHIDE=='1'}">style="display: none;"</c:if>
   		    >${FIELD.LABEL}:${FIELD.VALUE}</p>
   		    </c:forEach>
   		</div>
   		<i></i>
   	</div>

</c:forEach>

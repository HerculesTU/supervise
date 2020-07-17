<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.ResService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
   ResService resService = (ResService)PlatAppUtil.getBean("resService");
   Map<String,Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
   String userId = (String)sysUser.get("SYSUSER_ID");
   List<Map<String,Object>> resList = resService.findGrantResList(userId);
   request.setAttribute("resList", resList);
%>
<ul class="menu">
    <c:forEach items="${resList }" var="res" >
	<li class="dropdown treeview"><a data-id="${res.RES_ID}"
		data-toggle="dropdown" data-submenu="" aria-expanded="false">
		   <c:if test="${res.RES_MENUICON!=null&&res.RES_MENUICON!=''}">
		   <i class="${res.RES_MENUICON}"></i>
		   </c:if>
		   <span>${res.RES_NAME}</span></a>
		<ul class="dropdown-menu">
		    <c:forEach items="${res.childres}" var="secondres" >
		    <c:if test="${fn:length(secondres.childres)==0}">
		        <li>
				   <a data-id="${secondres.RES_ID}" 
				      <c:if test="${secondres.RES_MENUURL!=null&&secondres.RES_MENUURL!=''}">
				      class="menuiframe" href="${secondres.RES_MENUURL}"
				      </c:if>
				    >
						<c:if test="${secondres.RES_MENUICON!=null&&secondres.RES_MENUICON!=''}">
					    <i class="${secondres.RES_MENUICON}"></i>
					   </c:if>
						${secondres.RES_NAME}
					</a>
				</li>
		    </c:if>
		    <c:if test="${fn:length(secondres.childres)!=0}">
		        <li class="dropdown-submenu">
		            <a data-id="${secondres.RES_ID}"
		               <c:if test="${secondres.RES_MENUURL!=null&&secondres.RES_MENUURL!=''&&fn:length(secondres.childres)==0}">
				      class="menuiframe" href="${secondres.RES_MENUURL}"
				      </c:if>
		            >
		              <c:if test="${secondres.RES_MENUICON!=null&&secondres.RES_MENUICON!=''}">
					    <i class="${secondres.RES_MENUICON}"></i>
					   </c:if>
					   ${secondres.RES_NAME}
		            </a>
		            <c:if test="${fn:length(secondres.childres)!=0}">
			        <ul class="dropdown-menu">
	                   	<li class="dropdown-submenu">
	                     	   <a data-id="0">Another action</a>
	                   	</li>
	               	</ul>
	               	</c:if>
               	</li>
		    </c:if>
			
			</c:forEach>
		</ul>
	</li>
   </c:forEach>
</ul>


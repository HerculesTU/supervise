<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil" %>
<%@ page language="java" import="com.housoo.platform.core.service.SysUserService" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="plattag" uri="/plattag" %>


<%
    Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
    List<Map<String, Object>> resList = (List<Map<String, Object>>) sysUser.get(SysUserService.GRANTRESLIST_KEY);
    request.setAttribute("resList", resList);
%>

<div class="plat-LeftNav">
    <div id="jquery-accordion-menu" class="jquery-accordion-menu">
        <ul>
            <c:forEach items="${resList}" var="top" varStatus="topStatus">
                <c:forEach items="${top.childres}" var="res">
                    <li resid="${res.RES_ID}" resname="${res.RES_NAME}" resicon="${res.RES_MENUICON}"
                        resurl="${res.RES_MENUURL}" topmenuid="${top.RES_ID}"
                        <c:if test="${topStatus.index>0}">style="display: none;"</c:if> >
                        <a href="javascript:void(0)"><i class="${res.RES_MENUICON}"></i>${res.RES_NAME}</a>
                        <c:if test="${(res.childres)!= null && fn:length(res.childres) > 0}">
                            <ul class="submenu">
                                <c:forEach items="${res.childres}" var="res1">
                                    <li resid="${res1.RES_ID}" resname="${res1.RES_NAME}" resicon="${res1.RES_MENUICON}"
                                        resurl="${res1.RES_MENUURL}" topmenuid="${top.RES_ID}">
                                        <a href="javascript:void(0)">${res1.RES_NAME}</a>
                                        <c:if test="${(res1.childres)!= null && fn:length(res1.childres) > 0}">
                                            <ul class="submenu">
                                                <c:forEach items="${res1.childres}" var="res2">
                                                    <li resid="${res2.RES_ID}" resname="${res2.RES_NAME}"
                                                        resicon="${res2.RES_MENUICON}" resurl="${res2.RES_MENUURL}"
                                                        topmenuid="${top.RES_ID}">
                                                        <a href="javascript:void(0)">${res2.RES_NAME}</a>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:if>
                    </li>
                </c:forEach>
            </c:forEach>
            <%--
            <li><a href="javascript:void(0)"><i class="fa fa-cog"></i>Services </a>
                <ul class="submenu">
                    <li class="active"><a href="javascript:void(0)">Web Design </a></li>
                    <li><a href="javascript:void(0)">Design </a>
                        <ul class="submenu">
                            <li><a href="javascript:void(0)">Graphics </a></li>
                            <li><a href="javascript:void(0)">Vectors </a></li>
                            <li><a href="javascript:void(0)">Photoshop </a></li>
                            <li><a href="javascript:void(0)">Fonts </a></li>
                        </ul>
                    </li>
                    <li><a href="javascript:void(0)">Consulting </a></li>
                </ul>
            </li>
            --%>

        </ul>
    </div>
</div>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>
<c:forEach items="${uicompList}" var="uicomp">
	<div class="platimage-box">
		<div class="platimage">
			<a href="webpages/common/compdesign/${uicomp.VALUE}/preview.png"
				onerror="this.href='webpages/common/images/404_nophoto.png'"
				class="fancybox"> <span
				class="platcorner"></span>
				<div>
					<img class="img-responsive"  style="cursor: pointer;height: 120px;" 
						onerror="this.src='webpages/common/images/404_nophoto.png'"
						src="webpages/common/compdesign/${uicomp.VALUE}/preview.png">
				</div>
			</a>
			<div class="platimage-name">
				<plattag:radio name="FORMCONTROL_COMPCODE" auth_type="write"
					select_first="false" allowblank="true" value="${formControl.FORMCONTROL_COMPCODE}"
					static_values="${uicomp.LABEL}:${uicomp.VALUE}"
					is_horizontal="true" comp_col_num="0"></plattag:radio>
			</div>
		</div>
	</div>
</c:forEach>


<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<jsp:useBean id="eWebEditor" class="ewebeditor.server.upload_jsp" scope="page"/>

<%
eWebEditor.Load(pageContext);
%>
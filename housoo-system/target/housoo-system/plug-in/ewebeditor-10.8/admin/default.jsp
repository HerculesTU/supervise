<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK" session="true"%>
<%request.setCharacterEncoding("GBK");%>
<jsp:useBean id="eWebEditor" class="ewebeditor.admin.default_jsp" scope="page"/>
<%
eWebEditor.Load(pageContext);
%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7">
<title>eWebEditor���߱༭�� - ��̨����</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script language="JavaScript">
window.self.focus();
</script>
</head>
<frameset cols="150,*" framespacing="0" border="0" frameborder="0">
  <frame name="menu" src="menu.jsp" scrolling="yes">
  <frame name="main" src="main.jsp" scrolling="yes">
  <noframes>
    <body topmargin="0" leftmargin="0">
    <p>����ҳʹ���˿�ܣ��������������֧�ֿ��</p>
    </body>
  </noframes>
</frameset>
</html>

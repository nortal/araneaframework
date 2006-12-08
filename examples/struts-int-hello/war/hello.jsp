<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>Hello World!</title>
</head>
<body>
<html:errors/><p>

<logic:present name="examples.hello" scope="request">
   <h2>
     Hello <bean:write name="examples.hello" property="person" />!<p>
   </h2>
</logic:present>

<html:form action="/HelloWorld.do?action=gotName">

  <bean:message key="hello.jsp.prompt.person"/>
  <html:text property="person" size="16" maxlength="16"/><br>

  <html:submit value="Submit"/>
  <html:reset/>
</html:form>
</body>
</html>
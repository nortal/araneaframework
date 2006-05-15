<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"  %>
<%@ taglib prefix="ui" uri="http://araneaframework.org/tag-library/template" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:root>	
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Aranea&mdash;Java Web Framework Construction and Integration Kit</title>

<jsp:include page="scripts.jsp"/>
</head>

<body id="error">

<div id="cont1">
	<div id="header">
		<div class="box1">
			<a href="#" id="logo"><img src="gfx/logo_aranea_print.gif" alt="" /></a>
		</div>
	</div>
	<div class="stripe1">&nbsp;</div>
	<div id="wholder">
		<div id="content">			
			<!-- start content -->
			<h1>Error</h1>
				<ui:systemForm  id="systemForm" method="GET">
					<input name="errorAction" type="hidden"/>
					<ul>
						<li><ui:basicLinkButton 
							styleClass="errorLink"
							labelId="#Retry" 
							onclick="javascript:systemForm.errorAction.value = 'retry'; uiSystemFormSubmit(systemForm); return false;"/></li>
						<li><ui:basicLinkButton 
							styleClass="errorLink"
							labelId="#Cancel" 
							onclick="javascript:systemForm.errorAction.value = 'cancelFlow'; uiSystemFormSubmit(systemForm); return false;"/></li>
					</ul>
				</ui:systemForm>
			<div class="msg-error">
				<div id="crashinfo" style="width: 100%; height: 300px; overflow: auto;">
					<c:if test="${not empty rootStackTrace}">
						<b>Root cause:</b><br/>
						<pre style="font-size: 10pt"><c:out value="${rootStackTrace}"/></pre>
					</c:if>
					<b>Stack trace:</b><br/>
					<pre style="font-size: 10pt"><c:out value="${fullStackTrace}"/></pre>					
				</div>
			</div>
			<!-- end content -->
		</div>
	</div>
	<div class="clear1">&nbsp;</div>
</div>
<div id="footer">
	<div class="box1">
		&copy; <a href="http://www.webmedia.ee">Webmedia</a> 2006 <i>|</i>
			<a href="mailto:info@araneaframework.org">info@araneaframework.org</a>
	</div>
</div>

</body>
</html>
</ui:root>
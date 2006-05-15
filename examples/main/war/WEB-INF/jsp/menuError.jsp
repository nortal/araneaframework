<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">
	<ui:widgetContext>
	<ul>
		<li><ui:eventLinkButton
			styleClass="errorLink"
			labelId="#Retry" 
			eventId="retry"/></li>
		<li><ui:eventLinkButton
			styleClass="errorLink"
			labelId="#Cancel" 
			eventId="cancel"/></li>
		<li><ui:eventLinkButton
			styleClass="errorLink"
			labelId="#Main page" 
			eventId="mainPage"/></li>		
		<li><ui:eventLinkButton
			styleClass="errorLink"
			labelId="#Logout" 
			eventId="logout"/></li>			
	</ul>
	<div class="msg-error">
		<div id="crashinfo" style="width: 100%; height: 300px; overflow: auto;">
			<c:if test="${not empty contextWidget.data.rootStackTrace}">
				<b>Root cause:</b><br/>
				<pre style="font-size: 10pt"><c:out value="${contextWidget.data.rootStackTrace}"/></pre>
			</c:if>
			<b>Stack trace:</b><br/>
			<pre style="font-size: 10pt"><c:out value="${contextWidget.data.fullStackTrace}"/></pre>					
		</div>
	</div>
	</ui:widgetContext>
</jsp:root>
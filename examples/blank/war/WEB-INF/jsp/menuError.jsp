<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard"
    version="1.2">
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
	</ul>
	<div class="msg-error">
		<div id="crashinfo">
			<c:if test="${not empty viewData.rootStackTrace}">
				<b>Root cause:</b><br/>
				<pre style="font-size: 10pt"><c:out value="${viewData.rootStackTrace}"/></pre>
			</c:if>
			<b>Stack trace:</b><br/>
			<pre style="font-size: 10pt"><c:out value="${viewData.fullStackTrace}"/></pre>					
		</div>
	</div>
	</ui:widgetContext>
</jsp:root>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">
	<ui:widgetContext>
		<ul>
			<li><ui:eventLinkButton styleClass="errorLink" labelId="button.retry" eventId="retry"/></li>
			<li><ui:eventLinkButton styleClass="errorLink" labelId="button.cancel" eventId="cancel"/></li>
			<li><ui:eventLinkButton styleClass="errorLink" labelId="label.mainpage" eventId="mainPage"/></li>
			<li><ui:eventLinkButton styleClass="errorLink" labelId="label.logout" eventId="logout"/></li>
		</ul>
		<div class="msg-error">
			<div id="crashinfo" style="width: 100%; height: 300px; overflow: auto;">
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
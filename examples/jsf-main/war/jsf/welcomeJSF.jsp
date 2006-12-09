<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:ui="http://araneaframework.org/tag-library/standard_rt"
    xmlns:t="http://araneaframework.org/tag-library/template"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:h="http://java.sun.com/jsf/html"
    version="2.0">
    <jsp:directive.page contentType="text/html;charset=UTF-8"/>
	<f:view>
		<h1><h:outputText value="Welcome JavaServer Faces!" /></h1>
		<f:verbatim>This was just a static text rendered with h:outputText!</f:verbatim>

		<f:verbatim>
			<c:if test="${widget.nested}">
				<t:componentActions>
					<p/>
					<ui:eventButton eventId="endFlow" labelId="#Back to caller flow" />
				</t:componentActions>
			</c:if>
		</f:verbatim>
	</f:view>
</jsp:root>

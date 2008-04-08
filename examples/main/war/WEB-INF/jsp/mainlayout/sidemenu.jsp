<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="1.2">
	
	<jsp:directive.page import="org.araneaframework.http.HttpInputData"/>
  <jsp:directive.page import="org.araneaframework.http.util.ServletUtil"/>
  <jsp:directive.page import="org.araneaframework.InputData"/>
  
	<!--  variables -->
	<c:set var="activeStyle" value="active"/>

	<ui:widgetContext id="menu">
	  <jsp:scriptlet>
      InputData input = ServletUtil.getInputData(request);
      request.setAttribute("containerURL", ((HttpInputData) input).getContainerURL());
    </jsp:scriptlet>    
	
		<div id="leftcol">
			<ul id="menu2">
				<c:forEach items="${viewData.menu.subMenu}" var="topMenuItem">
					<c:if test="${topMenuItem.value.selected}">
						<c:forEach items="${topMenuItem.value.subMenu}" var="item">
							<li>
								<c:if test="${item.value.selected}">
	 
								   <ui:eventLinkButton 
								   	eventId="menuSelect" 
								   	eventParam="${topMenuItem.value.label}.${item.value.label}"
								   	labelId="${item.value.label}"
								   	styleClass="${activeStyle}"/>

                  				   <!-- ui:link href="${containerURL}/mount/${widgetId}/${topMenuItem.value.label}.${item.value.label}" styleClass="${activeStyle}"><fmt:message key="${item.value.label}"/></ui:link-->																											
	 
									<c:if test="${item.value.holder}">
									<ul>
										<c:forEach items="${item.value.subMenu}" var="subitem">
											<li>
												<c:if test="${subitem.value.selected}">
													<ui:eventLinkButton 
														eventId="menuSelect"
														eventParam="${topMenuItem.value.label}.${item.value.label}.${subitem.value.label}"
														labelId="${subitem.value.label}"
														styleClass="${activeStyle}"/>
												<!-- ui:link href="${containerURL}/mount/${widgetId}/${topMenuItem.value.label}.${item.value.label}.${subitem.value.label}" styleClass="${activeStyle}"><fmt:message key="${subitem.value.label}"/></ui:link -->
												</c:if>
	
												<c:if test="${not subitem.value.selected}">
													<ui:eventLinkButton 
														eventId="menuSelect"
														eventParam="${topMenuItem.value.label}.${item.value.label}.${subitem.value.label}"
														labelId="${subitem.value.label}"/>

												  <!-- ui:link href="${containerURL}/mount/${widgetId}/${topMenuItem.value.label}.${item.value.label}.${subitem.value.label}"><fmt:message key="${subitem.value.label}"/></ui:link-->															
												</c:if>
											</li>
										</c:forEach>
									</ul>
									</c:if>
	
								</c:if>
	
								<c:if test="${not item.value.selected}">
									<ui:eventLinkButton
										eventId="menuSelect" 
									   	eventParam="${topMenuItem.value.label}.${item.value.label}"
									   	labelId="${item.value.label}"/>
								  <!-- ui:link href="${containerURL}/mount/${widgetId}/${topMenuItem.value.label}.${item.value.label}"><fmt:message key="${item.value.label}"/></ui:link  -->												
								</c:if>
							</li>
						</c:forEach>
					</c:if>
				</c:forEach>
			</ul>
		</div>

	</ui:widgetContext>
</jsp:root>

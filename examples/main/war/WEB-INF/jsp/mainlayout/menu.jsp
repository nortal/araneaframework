<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:fmt="http://java.sun.com/jsp/jstl/fmt" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    xmlns:tui="http://araneaframework.org/tag-library/template" 
    version="2.0">
<jsp:directive.page import="org.araneaframework.http.HttpInputData"/>
<jsp:directive.page import="org.araneaframework.http.util.ServletUtil"/>
<jsp:directive.page import="org.araneaframework.InputData"/>
    <!--  variables -->
    <c:set var="activeStyle" value="active"/>
    
    <jsp:scriptlet>
      InputData input = ServletUtil.getInputData(request);
      request.setAttribute("containerURL", ((HttpInputData) input).getContainerURL());
    </jsp:scriptlet>    

    <!-- WidgetContext id must be set here, because we want to render MenuWidget 
    	not TemplateRootWidget (which includes this JSP, thereby providing its own widget context) here. -->
    <ui:widgetContext id="menu">
        <div id="header">
            <div class="box1">
            	<ui:eventLinkButton id="logo" eventId="mainPage">
                    <tui:image src="gfx/logo_aranea_screen.jpg" />
                </ui:eventLinkButton>

                <div id="menu1">
                    <!-- ${viewData.VariableName} allows us to access data 
                        that was explicitly added to widget view model by the programmer
                        with putViewData() method. -->
                    <c:forEach items="${viewData.menu.subMenu}" var="item">
                        <div class="item">
                            <c:if test="${item.value.selected}">
                                <!-- create a button that 
                                    * has a label
                                    * submits event called "menuSelect"
                                    * submits label id as event parameter 
                                    * CSS class is active, indicating that this menu item is selected currently -->
                                    <ui:eventLinkButton eventId="menuSelect" eventParam="${item.value.label}" labelId="${item.value.label}" styleClass="${activeStyle}"/>
                                 <!-- ui:link href="${containerURL}/mount/${widgetId}/${item.value.label}" styleClass="${activeStyle}"><fmt:message key="${item.value.label}"/></ui:link-->                                
                            </c:if>

                            <c:if test="${not item.value.selected}">
                                <!-- same as the other button, but menu item is not selected -->
                                <!--ui:link href="${containerURL}/mount/${widgetId}/${item.value.label}"><fmt:message key="${item.value.label}"/></ui:link-->
                                <ui:eventLinkButton eventId="menuSelect" eventParam="${item.value.label}" labelId="${item.value.label}"/>
                            </c:if>
                        </div>
                    </c:forEach>
                    
                    <div class="item">
                    	<ui:form id="form">
	                    	<ui:select id="langSelect" localizeDisplayItems="true"/>
                    	</ui:form>
                    </div>
                </div>
            </div>
        </div>

    </ui:widgetContext>
</jsp:root>

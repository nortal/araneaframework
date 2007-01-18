<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="1.2">
    <!--  variables -->
    <c:set var="activeStyle" value="active"/>

    <!-- WidgetContext id must be set here, because we want to render MenuWidget 
    	not TemplateRootWidget (which includes this JSP, thereby providing its own widget context) here. -->
    <ui:widgetContext id="menu">
        <div id="header">
            <div class="box1">
                <a href="#" id="logo">
                    <tui:image src="gfx/logo_aranea_screen.jpg" alt="Aranea fancy logo"/>
                </a>

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
                            </c:if>

                            <c:if test="${not item.value.selected}">
                                <!-- same as the other button, but menu item is not selected -->
                                <ui:eventLinkButton eventId="menuSelect" eventParam="${item.value.label}" labelId="${item.value.label}"/>
                            </c:if>
                        </div>
                    </c:forEach>
                    
                    <div class="item">
                    	<ui:form id="form">
	                    	<ui:select id="langSelect"/>
                    	</ui:form>
                    </div>
                </div>
            </div>
        </div>

    </ui:widgetContext>
</jsp:root>
